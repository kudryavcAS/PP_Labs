#include "receiver.h"

const int MAX_MESSAGE_LENGTH = 20;
const int MAX_MESSAGES = 100;

struct Message {
	char content[MAX_MESSAGE_LENGTH + 1];
	bool active;
};

struct SharedData {
	Message messages[MAX_MESSAGES];
	int readIndex;
	int writeIndex;
	int messageCount;
	int maxMessages;
	HANDLE semaphoreEmpty;
	HANDLE semaphoreFull;
	HANDLE mutex;
};

void inputNatural(int& integer, int max = INT_MAX) {
	while (true) {
		std::cin >> integer;

		if (std::cin.fail()) {
			std::cin.clear();
			std::cin.ignore(INT_MAX, '\n');
			std::cout << "Invalid input. Enter an integer 0 < " << max << "\n";

			continue;
		}
		if (integer <= 0 || integer > max) {
			std::cout << "Invalid input. Enter an integer 0 < " << max << "\n";

			continue;
		}
		break;
	}
}

int main() {
	std::string fileName;
	int maxMessages;
	int senderCount;

	// Ввод имени файла и количества сообщений
	std::cout << "Receiver: Enter binary file name: ";
	std::cin >> fileName;
	std::cout << "Receiver: Enter max number of messages: ";
	inputNatural(maxMessages);

	// Создание файла отображения в память
	HANDLE hMapFile = CreateFileMapping(
		INVALID_HANDLE_VALUE,
		NULL,
		PAGE_READWRITE,
		0,
		sizeof(SharedData),
		fileName.c_str()
	);

	if (hMapFile == NULL) {
		std::cout << "Receiver: Could not create file mapping" << std::endl;
		return 1;
	}

	SharedData* sharedData = (SharedData*)MapViewOfFile(
		hMapFile,
		FILE_MAP_ALL_ACCESS,
		0,
		0,
		sizeof(SharedData)
	);

	if (sharedData == NULL) {
		std::cout << "Receiver: Could not map view of file" << std::endl;
		CloseHandle(hMapFile);
		return 1;
	}

	// Инициализация разделяемых данных
	sharedData->readIndex = 0;
	sharedData->writeIndex = 0;
	sharedData->messageCount = 0;
	sharedData->maxMessages = maxMessages;

	// Создание семафоров и мьютекса
	sharedData->semaphoreEmpty = CreateSemaphore(NULL, maxMessages, maxMessages, NULL);
	sharedData->semaphoreFull = CreateSemaphore(NULL, 0, maxMessages, NULL);
	sharedData->mutex = CreateMutex(NULL, FALSE, NULL);

	if (sharedData->semaphoreEmpty == NULL ||
		sharedData->semaphoreFull == NULL ||
		sharedData->mutex == NULL) {
		std::cout << "Receiver: Could not create synchronization objects" << std::endl;
		CloseHandle(hMapFile);
		return 1;
	}

	// Ввод количества процессов Sender
	std::cout << "Receiver: Enter number of Sender processes: ";
	std::cin >> senderCount;

	// Запуск процессов Sender
	std::vector<PROCESS_INFORMATION> senderProcesses;

	for (int i = 0; i < senderCount; i++) {
		STARTUPINFO startupInfo;
		PROCESS_INFORMATION processInfo;

		ZeroMemory(&startupInfo, sizeof(startupInfo));
		startupInfo.cb = sizeof(startupInfo);
		ZeroMemory(&processInfo, sizeof(processInfo));

		// Пробуем разные варианты запуска
		std::string commandLine = "Sender.exe " + fileName;

		BOOL success = FALSE;

		// Вариант 1: Запуск с полным путем
		char exePath[MAX_PATH];
		GetModuleFileName(NULL, exePath, MAX_PATH);
		std::string fullPath = exePath;
		size_t pos = fullPath.find_last_of("\\");
		if (pos != std::string::npos) {
			fullPath = fullPath.substr(0, pos + 1) + "Sender.exe";
			success = CreateProcess(
				fullPath.c_str(),
				(LPSTR)commandLine.c_str(),
				NULL,
				NULL,
				FALSE,
				0,
				NULL,
				NULL,
				&startupInfo,
				&processInfo
			);
		}

		// Вариант 2: Если не сработало, пробуем просто по имени
		if (!success) {
			success = CreateProcess(
				NULL,
				(LPSTR)commandLine.c_str(),
				NULL,
				NULL,
				FALSE,
				0,
				NULL,
				NULL,
				&startupInfo,
				&processInfo
			);
		}

		if (success) {
			senderProcesses.push_back(processInfo);
			std::cout << "Receiver: Started Sender process " << i + 1 << std::endl;
			std::cout << "Receiver: Sender process ID: " << processInfo.dwProcessId << std::endl;
		}
		else {
			DWORD error = GetLastError();
			std::cout << "Receiver: Failed to start Sender process " << i + 1 << std::endl;
			std::cout << "Receiver: Error code: " << error << std::endl;

			// Расшифровка ошибок
			if (error == 2) {
				std::cout << "Receiver: ERROR_FILE_NOT_FOUND - Sender.exe not found" << std::endl;
			}
			else if (error == 3) {
				std::cout << "Receiver: ERROR_PATH_NOT_FOUND - Path not found" << std::endl;
			}
			else if (error == 5) {
				std::cout << "Receiver: ERROR_ACCESS_DENIED - Access denied" << std::endl;
			}
		}
	}

	// Ожидание готовности всех Sender процессов
	std::cout << "Receiver: Waiting for all Senders to be ready..." << std::endl;
	Sleep(3000);

	// Основной цикл Receiver
	bool running = true;
	while (running) {
		std::cout << "\nReceiver commands:\n";
		std::cout << "1 - Read message\n";
		std::cout << "2 - Exit\n";
		std::cout << "Enter choice: ";

		int choice;
		std::cin >> choice;

		switch (choice) {
		case 1: {
			// Чтение сообщения
			WaitForSingleObject(sharedData->semaphoreFull, INFINITE);
			WaitForSingleObject(sharedData->mutex, INFINITE);

			Message msg = sharedData->messages[sharedData->readIndex];
			sharedData->readIndex = (sharedData->readIndex + 1) % maxMessages;
			sharedData->messageCount--;

			ReleaseMutex(sharedData->mutex);
			ReleaseSemaphore(sharedData->semaphoreEmpty, 1, NULL);

			std::cout << "Receiver: Received message: " << msg.content << std::endl;
			break;
		}
		case 2:
			running = false;
			break;
		default:
			std::cout << "Receiver: Invalid choice" << std::endl;
			break;
		}
	}

	// Завершение работы
	std::cout << "Receiver: Shutting down..." << std::endl;

	// Закрытие handles процессов Sender (НЕ закрываем их раньше!)
	for (int i = 0; i < (int)senderProcesses.size(); i++) {
		TerminateProcess(senderProcesses[i].hProcess, 0);
		CloseHandle(senderProcesses[i].hProcess);
		CloseHandle(senderProcesses[i].hThread);
	}

	// Закрытие synchronization objects
	CloseHandle(sharedData->semaphoreEmpty);
	CloseHandle(sharedData->semaphoreFull);
	CloseHandle(sharedData->mutex);

	UnmapViewOfFile(sharedData);
	CloseHandle(hMapFile);

	std::cout << "Receiver: Finished" << std::endl;
	return 0;
}