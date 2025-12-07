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
	std::cout << "[SERVER Thread " << GetCurrentThreadId() << "]: " << msg << "\n";
	LeaveCriticalSection(&g_csConsole);
}

void printFileContent(const std::string& filename) {
	HANDLE hFile = CreateFile(filename.c_str(), 
		GENERIC_READ, 
		FILE_SHARE_READ | FILE_SHARE_WRITE,
		NULL, 
		OPEN_EXISTING, 
		FILE_ATTRIBUTE_NORMAL, 
		NULL);

	if (hFile == INVALID_HANDLE_VALUE) return;

	Employee e;
	DWORD bytesRead;

	EnterCriticalSection(&g_csConsole);
	std::cout << "\n--- File Content ---\n";
	std::cout << std::left << std::setw(10) << "ID" << std::setw(35) << "Name" << std::setw(10) << "Hours" << "\n";
	std::cout << std::string(55, '-') << "\n";

	while (ReadFile(hFile, &e, sizeof(Employee), &bytesRead, NULL) && bytesRead > 0) {
		std::cout << std::left << std::setw(10) << e.num
			<< std::setw(35) << e.name << std::setw(10) << e.hours << "\n";
	}
	std::cout << std::string(55, '-') << "\n";
	LeaveCriticalSection(&g_csConsole);
	CloseHandle(hFile);
}

long long findRecordOffset(HANDLE hFile, int id) {
	LARGE_INTEGER fileSize;
	if (!GetFileSizeEx(hFile, &fileSize)) return -1;

	long long currentPos = 0;
	Employee emp;
	DWORD bytesRead;

	while (currentPos < fileSize.QuadPart) {
		OVERLAPPED ov = { 0 };
		ov.Offset = static_cast<DWORD>(currentPos);
		ov.OffsetHigh = static_cast<DWORD>(currentPos >> 32);

		// Попытка 1: Прочитать "по-хорошему"
		bool readSuccess = ReadFile(hFile, &emp, sizeof(Employee), &bytesRead, &ov);

		// Если не вышло
		if (!readSuccess) {
			DWORD err = GetLastError();

			if (err != ERROR_LOCK_VIOLATION) break;
			if (!LockFileEx(hFile, 0, 0, sizeof(Employee), 0, &ov)) break;

			UnlockFileEx(hFile, 0, sizeof(Employee), 0, &ov);

			if (!ReadFile(hFile, &emp, sizeof(Employee), &bytesRead, &ov) || bytesRead == 0) {
				break;
			}
		}
		else if (bytesRead == 0) {
			break; 
		}
		if (emp.num == id) {
			return currentPos;
		}

		currentPos += sizeof(Employee);
	}

	return -1;
}

DWORD WINAPI clientThread(LPVOID lpParam) {
	std::unique_ptr<ThreadParam> params(static_cast<ThreadParam*>(lpParam));
	HANDLE hPipe = params->hPipe;

	HANDLE hLocalFile = CreateFile(
		params->dbFileName.c_str(),
		GENERIC_READ | GENERIC_WRITE,
		FILE_SHARE_READ | FILE_SHARE_WRITE,
		NULL,
		OPEN_EXISTING,
		FILE_ATTRIBUTE_NORMAL,
		NULL
	);

	if (hLocalFile == INVALID_HANDLE_VALUE) {
		log("Error: Could not open database file in thread.");
		DisconnectNamedPipe(hPipe);
		CloseHandle(hPipe);
		return 1;
	}

	DWORD bytesRead, bytesWritten;
	Request req;

	while (ReadFile(hPipe, &req, sizeof(Request), &bytesRead, NULL)) {
		if (bytesRead == 0) break;

		Response resp;

		long long offset = findRecordOffset(hLocalFile, req.employeeNum);

		if (offset == -1) {
			resp.found = false;
			strncpy_s(resp.message, "Employee not found.", MESSAGE_SIZE);
			WriteFile(hPipe, &resp, sizeof(Response), &bytesWritten, NULL);
			continue;
		}

		DWORD lockFlags = 0;
		if (req.type == RequestType::MODIFY) {
			lockFlags = LOCKFILE_EXCLUSIVE_LOCK;
		}

		OVERLAPPED ovLock = { 0 };
		ovLock.Offset = static_cast<DWORD>(offset);
		ovLock.OffsetHigh = static_cast<DWORD>(offset >> 32);

		log("Waiting lock for ID " + std::to_string(req.employeeNum) +
			(req.type == RequestType::MODIFY ? " (EXCLUSIVE)" : " (SHARED)"));

		if (!LockFileEx(hLocalFile, lockFlags, 0, sizeof(Employee), 0, &ovLock)) {
			resp.found = false;
			strncpy_s(resp.message, "Server lock error.", MESSAGE_SIZE);
			WriteFile(hPipe, &resp, sizeof(Response), &bytesWritten, NULL);
			continue;
		}

		log("Acquired lock for ID " + std::to_string(req.employeeNum));

		if (!ReadFile(hLocalFile, &resp.record, sizeof(Employee), &bytesRead, &ovLock)) {
			UnlockFileEx(hLocalFile, 0, sizeof(Employee), 0, &ovLock);
			resp.found = false;
			strncpy_s(resp.message, "Server read error.", MESSAGE_SIZE);
			WriteFile(hPipe, &resp, sizeof(Response), &bytesWritten, NULL);
			continue;
		}

		resp.found = true;
		strncpy_s(resp.message, "OK", MESSAGE_SIZE);
		WriteFile(hPipe, &resp, sizeof(Response), &bytesWritten, NULL);

		EndAction action;
		if (ReadFile(hPipe, &action, sizeof(EndAction), &bytesRead, NULL)) {
			if (req.type == RequestType::MODIFY && action == EndAction::SAVE) {
				Employee modifiedEmp;
				if (ReadFile(hPipe, &modifiedEmp, sizeof(Employee), &bytesRead, NULL)) {
					WriteFile(hLocalFile, &modifiedEmp, sizeof(Employee), &bytesWritten, &ovLock);
					FlushFileBuffers(hLocalFile);
					log("Updated ID " + std::to_string(modifiedEmp.num));
				}
			}
		}

		UnlockFileEx(hLocalFile, 0, sizeof(Employee), 0, &ovLock);
		log("Released lock for ID " + std::to_string(req.employeeNum));
	}

	CloseHandle(hLocalFile);
	FlushFileBuffers(hPipe);
	DisconnectNamedPipe(hPipe);
	CloseHandle(hPipe);
	log("Client disconnected.");

	return 0;
}

HANDLE createDatabase(std::string& outFilename, int& outClientCount) {
	std::cout << "Enter filename for database: ";
	std::cin >> outFilename;

	HANDLE hFile = CreateFile(outFilename.c_str(),
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
		std::cout << "Name (max " << NAME_SIZE << " chars): ";
		std::string tempName;
		std::getline(std::cin, tempName);
		strncpy_s(emp.name, tempName.c_str(), NAME_SIZE);
		std::cout << "Hours: ";
		inputDouble(emp.hours);
		DWORD written;
		WriteFile(hFile, &emp, sizeof(Employee), &written, NULL);
	}

	FlushFileBuffers(hFile);
	printFileContent(outFilename);

	std::cout << "\nEnter number of clients to launch: ";
	inputNatural(outClientCount);

	return hFile;
}

void launchClients(int count) {
	for (int i = 0; i < count; ++i) {
		STARTUPINFO si;
		PROCESS_INFORMATION pi;
		ZeroMemory(&si, sizeof(si));
		si.cb = sizeof(si);
		ZeroMemory(&pi, sizeof(pi));

		std::string cmdLine = "Client.exe " + std::to_string(i + 1);
		std::vector<char> cmdBuf(cmdLine.begin(), cmdLine.end());
		cmdBuf.push_back(0);

		if (CreateProcess(NULL, cmdBuf.data(), NULL, NULL, FALSE,
			CREATE_NEW_CONSOLE, NULL, NULL, &si, &pi)) {
			CloseHandle(pi.hProcess);
			CloseHandle(pi.hThread);
		}
	}
}

void runServer(const std::string& dbFileName, int clientCount) {
	std::vector<HANDLE> hThreads;
	hThreads.reserve(clientCount);

	for (int i = 0; i < clientCount; ++i) {
		HANDLE hPipe = CreateNamedPipe(
			PIPE_NAME.c_str(),
			PIPE_ACCESS_DUPLEX,
			PIPE_TYPE_MESSAGE | PIPE_READMODE_MESSAGE | PIPE_WAIT,
			PIPE_UNLIMITED_INSTANCES,
			1024, 1024, 0, NULL
		);

		if (hPipe == INVALID_HANDLE_VALUE) continue;

		if (ConnectNamedPipe(hPipe, NULL) || GetLastError() == ERROR_PIPE_CONNECTED) {
			log("Client " + std::to_string(i + 1) + " connected.");

			auto params = std::make_unique<ThreadParam>();
			params->hPipe = hPipe;
			params->dbFileName = dbFileName;

			HANDLE hThread = CreateThread(NULL, 0, clientThread, params.release(), 0, NULL);
			if (hThread) hThreads.push_back(hThread);
		}
		else {
			CloseHandle(hPipe);
		}
	}

	if (!hThreads.empty()) {
		WaitForMultipleObjects(static_cast<DWORD>(hThreads.size()), hThreads.data(), TRUE, INFINITE);
	}
	for (HANDLE h : hThreads) if (h) CloseHandle(h);
}