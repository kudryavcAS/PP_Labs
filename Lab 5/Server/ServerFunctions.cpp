#include "Server.hpp"

CRITICAL_SECTION g_csConsole;

void initializeSync() {
	InitializeCriticalSection(&g_csConsole);
}

void cleanupSync() {
	DeleteCriticalSection(&g_csConsole);
}

void log(const std::string& msg) {
	EnterCriticalSection(&g_csConsole);
	std::cout << "[SERVER]: " << msg << "\n";
	LeaveCriticalSection(&g_csConsole);
}

void printFileContent(const std::string& filename) {
	// Используем std::ifstream для простоты вывода всего файла подряд,
	// так как это операция "администратора", когда клиенты еще не работают или закончили.
	std::ifstream file(filename, std::ios::binary);
	if (!file.is_open()) return;

	Employee e;
	EnterCriticalSection(&g_csConsole);
	std::cout << "\n--- File Content ---\n";
	std::cout << "ID\tName\tHours\n";
	while (file.read(reinterpret_cast<char*>(&e), sizeof(Employee))) {
		std::cout << e.num << "\t" << e.name << "\t" << e.hours << "\n";
	}
	std::cout << "--------------------\n";
	LeaveCriticalSection(&g_csConsole);
	file.close();
}

long long findRecordOffset(HANDLE hFile, int id) {
	LARGE_INTEGER fileSize;
	if (!GetFileSizeEx(hFile, &fileSize)) return -1;

	long long currentPos = 0;
	Employee emp;
	DWORD bytesRead;

	// Читаем последовательно, чтобы найти смещение
	// Для чистоты эксперимента в многопоточной среде лучше использовать pread (ReadFile с OVERLAPPED),
	// чтобы не двигать глобальный курсор файла.

	while (currentPos < fileSize.QuadPart) {
		OVERLAPPED ov = { 0 };
		ov.Offset = static_cast<DWORD>(currentPos);
		ov.OffsetHigh = static_cast<DWORD>(currentPos >> 32);

		if (!ReadFile(hFile, &emp, sizeof(Employee), &bytesRead, &ov) || bytesRead == 0) {
			break;
		}

		if (emp.num == id) {
			return currentPos;
		}
		currentPos += sizeof(Employee);
	}
	return -1;
}

// Функция, которая будет работать в отдельном потоке (WinAPI style)
DWORD WINAPI clientThread(LPVOID lpParam) {
	// Распаковываем параметры и освобождаем память, выделенную в main
	ThreadParam* params = static_cast<ThreadParam*>(lpParam);
	HANDLE hPipe = params->hPipe;
	HANDLE hFile = params->hFile;
	delete params;

	DWORD bytesRead, bytesWritten;
	Request req;

	while (ReadFile(hPipe, &req, sizeof(Request), &bytesRead, NULL)) {
		if (bytesRead == 0) break; // Клиент отключился

		Response resp;
		long long offset = findRecordOffset(hFile, req.employeeNum);

		if (offset == -1) {
			resp.found = false;
			strncpy_s(resp.message, "Employee not found.", 255);
			WriteFile(hPipe, &resp, sizeof(Response), &bytesWritten, NULL);
			continue;
		}

		// --- БЛОКИРОВКА ЗАПИСЕЙ ---
		// Если клиент хочет ЧИТАТЬ -> ставим Shared Lock (0). Другие тоже могут читать.
		// Если клиент хочет МЕНЯТЬ -> ставим Exclusive Lock. Никто не может ни читать, ни писать.

		DWORD lockFlags = 0; // Shared lock по умолчанию
		if (req.type == RequestType::MODIFY) {
			lockFlags = LOCKFILE_EXCLUSIVE_LOCK;
		}

		OVERLAPPED ovLock = { 0 };
		ovLock.Offset = static_cast<DWORD>(offset);
		ovLock.OffsetHigh = static_cast<DWORD>(offset >> 32);

		log("Waiting lock for ID " + std::to_string(req.employeeNum));

		// LockFileEx - это нативная WinAPI функция для Reader-Writer lock на уровне файла
		if (!LockFileEx(hFile, lockFlags, 0, sizeof(Employee), 0, &ovLock)) {
			resp.found = false;
			strncpy_s(resp.message, "Server lock error.", 255);
			WriteFile(hPipe, &resp, sizeof(Response), &bytesWritten, NULL);
			continue;
		}

		// Читаем данные из заблокированной области
		if (!ReadFile(hFile, &resp.record, sizeof(Employee), &bytesRead, &ovLock)) {
			UnlockFileEx(hFile, 0, sizeof(Employee), 0, &ovLock);
			resp.found = false;
			strncpy_s(resp.message, "Server read error.", 255);
			WriteFile(hPipe, &resp, sizeof(Response), &bytesWritten, NULL);
			continue;
		}

		resp.found = true;
		strncpy_s(resp.message, "OK", 255);
		WriteFile(hPipe, &resp, sizeof(Response), &bytesWritten, NULL);

		// Ждем решения клиента (изменить или просто закончить)
		EndAction action;
		Employee modifiedEmp;

		if (ReadFile(hPipe, &action, sizeof(EndAction), &bytesRead, NULL)) {
			if (req.type == RequestType::MODIFY && action == EndAction::SAVE) {
				// Если клиент сохраняет, читаем новую структуру
				if (ReadFile(hPipe, &modifiedEmp, sizeof(Employee), &bytesRead, NULL)) {
					WriteFile(hFile, &modifiedEmp, sizeof(Employee), &bytesWritten, &ovLock);
					log("Updated ID " + std::to_string(modifiedEmp.num));
				}
			}
		}

		// Снимаем блокировку
		UnlockFileEx(hFile, 0, sizeof(Employee), 0, &ovLock);
		log("Unlocked ID " + std::to_string(req.employeeNum));
	}

	FlushFileBuffers(hPipe);
	DisconnectNamedPipe(hPipe);
	CloseHandle(hPipe);
	log("Client disconnected.");

	return 0;
}

HANDLE createDatabase(std::string& filename, int& clientCount) {
	std::cout << "Enter filename for database: ";
	std::cin >> filename;

	HANDLE hFile = CreateFile(filename.c_str(),
		GENERIC_READ | GENERIC_WRITE,
		FILE_SHARE_READ | FILE_SHARE_WRITE,
		NULL, CREATE_ALWAYS, FILE_ATTRIBUTE_NORMAL, NULL);

	if (hFile == INVALID_HANDLE_VALUE) {
		std::cerr << "CreateFile failed: " << GetLastError() << "\n";
		exit(1);
	}

	int empCount;
	std::cout << "Enter number of employees: ";
	inputNatural(empCount);

	for (int i = 0; i < empCount; ++i) {
		Employee emp;
		std::cout << "Employee " << (i + 1) << "\nID: ";
		inputNatural(emp.num);

		std::cout << "Name (max 30 chars): ";
		std::string tempName;
		std::getline(std::cin, tempName);

		strncpy_s(emp.name, tempName.c_str(), 30);

		std::cout << "Hours: ";
		inputDouble(emp.hours);

		DWORD written;
		WriteFile(hFile, &emp, sizeof(Employee), &written, NULL);
	}

	printFileContent(filename);

	std::cout << "\nEnter number of clients to launch: ";
	inputNatural(clientCount);

	return hFile;
}
void launchClients(int count) {
	for (int i = 0; i < count; ++i) {
		STARTUPINFO si;
		PROCESS_INFORMATION pi;
		ZeroMemory(&si, sizeof(si));
		si.cb = sizeof(si);
		ZeroMemory(&pi, sizeof(pi));

		std::string cmdLine = "Client.exe";
		std::vector<char> cmdBuf(cmdLine.begin(), cmdLine.end());
		cmdBuf.push_back(0);

		if (CreateProcess(NULL, cmdBuf.data(), NULL, NULL, FALSE,
			CREATE_NEW_CONSOLE, NULL, NULL, &si, &pi)) {
			CloseHandle(pi.hProcess);
			CloseHandle(pi.hThread);
		}
		else {
			std::cerr << "Failed to start Client.exe. Error: " << GetLastError() << "\n";
		}
	}
}

void runServer(HANDLE hFile, int clientCount) {
	// Массив дескрипторов потоков
	HANDLE* hThreads = new HANDLE[clientCount];

	for (int i = 0; i < clientCount; ++i) {
		HANDLE hPipe = CreateNamedPipe(
			PIPE_NAME.c_str(),
			PIPE_ACCESS_DUPLEX,
			PIPE_TYPE_MESSAGE | PIPE_READMODE_MESSAGE | PIPE_WAIT,
			PIPE_UNLIMITED_INSTANCES,
			1024, 1024, 0, NULL
		);

		if (hPipe == INVALID_HANDLE_VALUE) {
			std::cerr << "Pipe creation failed." << "\n";
			hThreads[i] = NULL;
			continue;
		}

		// Ждем подключения клиента
		if (ConnectNamedPipe(hPipe, NULL) || GetLastError() == ERROR_PIPE_CONNECTED) {
			log("Client connected.");

			// Выделяем память под параметры потока (чтобы избежать гонки данных при передаче адреса)
			ThreadParam* params = new ThreadParam;
			params->hPipe = hPipe;
			params->hFile = hFile;

			// Создаем поток через WinAPI
			hThreads[i] = CreateThread(
				NULL,           // Default security
				0,              // Default stack size
				clientThread,   // Функция потока
				params,         // Параметр
				0,              // Default creation flags (run immediately)
				NULL            // Thread ID (не нужен)
			);
		}
		else {
			CloseHandle(hPipe);
			hThreads[i] = NULL;
		}
	}

	// Ждем завершения всех потоков (аналог join)
	WaitForMultipleObjects(clientCount, hThreads, TRUE, INFINITE);

	// Закрываем дескрипторы потоков
	for (int i = 0; i < clientCount; ++i) {
		if (hThreads[i] != NULL) CloseHandle(hThreads[i]);
	}
	delete[] hThreads;
}