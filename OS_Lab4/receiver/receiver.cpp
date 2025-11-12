#include "receiver.h"

void inputNatural(int& integer, int max) {
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

bool startProcess(const std::string& processPath, const std::string& arguments, const std::string& windowTitle) {
	STARTUPINFO startupInfo;
	PROCESS_INFORMATION processInfo;

	ZeroMemory(&startupInfo, sizeof(startupInfo));
	startupInfo.cb = sizeof(startupInfo);
	ZeroMemory(&processInfo, sizeof(processInfo));

	std::string commandLine;
	if (windowTitle.empty()) {
		commandLine = "\"" + processPath + "\" " + arguments;
	}
	else {
		commandLine = "cmd.exe /c start \"" + windowTitle + "\" \"" + processPath + "\" " + arguments;
	}

	std::cout << "Starting: " << commandLine << std::endl;

	if (!CreateProcess(NULL, (LPSTR)commandLine.c_str(), NULL, NULL, FALSE, 0, NULL, NULL, &startupInfo, &processInfo)) {
		std::cout << "Failed to start process. Error: " << GetLastError() << std::endl;
		return false;
	}
	CloseHandle(processInfo.hProcess);
	CloseHandle(processInfo.hThread);
	return true;
}

std::string findSenderPath() {
	return "Sender.exe";
}

bool createSharedMemory(const std::string& fileName, int maxMessages, SharedData*& sharedData, HANDLE& hMapFile) {
	// Создание файла отображения в память
	hMapFile = CreateFileMapping(
		INVALID_HANDLE_VALUE,
		NULL,
		PAGE_READWRITE,
		0,
		sizeof(SharedData),
		fileName.c_str()
	);

	if (hMapFile == NULL) {
		std::cout << "Receiver: Could not create file mapping" << std::endl;
		return false;
	}

	// Получение указателя на разделяемую память
	sharedData = (SharedData*)MapViewOfFile(
		hMapFile,
		FILE_MAP_ALL_ACCESS,
		0,
		0,
		sizeof(SharedData)
	);

	if (sharedData == NULL) {
		std::cout << "Receiver: Could not map view of file" << std::endl;
		CloseHandle(hMapFile);
		return false;
	}

	// Инициализация разделяемых данных
	sharedData->readIndex = 0;
	sharedData->writeIndex = 0;
	sharedData->messageCount = 0;
	sharedData->maxMessages = maxMessages;

	return true;
}

bool createSynchronizationObjects(const std::string& fileName, int maxMessages,
	HANDLE& emptySemaphore, HANDLE& fullSemaphore, HANDLE& mutex) {
	// Создание имен для объектов синхронизации
	std::string emptySemName = "Global\\" + fileName + "_empty";
	std::string fullSemName = "Global\\" + fileName + "_full";
	std::string mutexName = "Global\\" + fileName + "_mutex";

	std::cout << "Receiver: Creating synchronization objects..." << std::endl;
	std::cout << "  Empty semaphore: " << emptySemName << std::endl;
	std::cout << "  Full semaphore: " << fullSemName << std::endl;
	std::cout << "  Mutex: " << mutexName << std::endl;

	// Создание семафоров и мьютекса
	emptySemaphore = CreateSemaphore(NULL, maxMessages, maxMessages, emptySemName.c_str());
	fullSemaphore = CreateSemaphore(NULL, 0, maxMessages, fullSemName.c_str());
	mutex = CreateMutex(NULL, FALSE, mutexName.c_str());

	if (emptySemaphore == NULL || fullSemaphore == NULL || mutex == NULL) {
		std::cout << "Receiver: Could not create synchronization objects" << std::endl;
		return false;
	}

	std::cout << "Receiver: Synchronization objects created successfully!" << std::endl;
	return true;
}

bool startSenderProcesses(int senderCount, const std::string& senderPath, const std::string& fileName) {
	for (int i = 0; i < senderCount; i++) {
		std::string windowTitle = "Sender " + std::to_string(i + 1);
		if (!startProcess(senderPath, fileName, windowTitle)) {
			std::cout << "Receiver: Failed to start Sender process " << i + 1 << std::endl;
			return false;
		}
		std::cout << "Receiver: Started Sender process " << i + 1 << std::endl;
	}
	return true;
}

void receiverLoop(SharedData* sharedData, int maxMessages, HANDLE emptySemaphore, HANDLE fullSemaphore, HANDLE mutex) {
	bool running = true;
	while (running) {
		std::cout << "\n=== RECEIVER ===" << std::endl;
		std::cout << "Messages in buffer: " << sharedData->messageCount << "/" << maxMessages << std::endl;
		std::cout << "Commands: 1 - Read message, 2 - Exit" << std::endl;
		std::cout << "Enter choice: ";

		int choice;
		std::cin >> choice;

		switch (choice) {
		case 1: {
			std::cout << "Receiver: Waiting for message..." << std::endl;

			// Ожидаем сообщение
			if (WaitForSingleObject(fullSemaphore, INFINITE) == WAIT_OBJECT_0) {
				WaitForSingleObject(mutex, INFINITE);

				// Читаем сообщение из буфера
				Message msg = sharedData->messages[sharedData->readIndex];
				sharedData->readIndex = (sharedData->readIndex + 1) % maxMessages;
				sharedData->messageCount--;

				ReleaseMutex(mutex);
				ReleaseSemaphore(emptySemaphore, 1, NULL);

				std::cout << "Receiver: Received message: '" << msg.content << "'" << std::endl;
			}
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
}

void cleanupResources(HANDLE emptySemaphore, HANDLE fullSemaphore, HANDLE mutex,
	SharedData* sharedData, HANDLE hMapFile) {
	CloseHandle(emptySemaphore);
	CloseHandle(fullSemaphore);
	CloseHandle(mutex);

	if (sharedData != NULL) {
		UnmapViewOfFile(sharedData);
	}

	if (hMapFile != NULL) {
		CloseHandle(hMapFile);
	}
}

int main() {
	std::string fileName;
	int maxMessages;
	int senderCount;

	// Указатели на ресурсы (для корректного освобождения)
	SharedData* sharedData = NULL;
	HANDLE hMapFile = NULL;
	HANDLE emptySemaphore = NULL;
	HANDLE fullSemaphore = NULL;
	HANDLE mutex = NULL;

	// Ввод параметров
	std::cout << "Receiver: Enter binary file name: ";
	std::cin >> fileName;
	std::cout << "Receiver: Enter max number of messages: ";
	inputNatural(maxMessages, MAX_MESSAGES);

	// Создание разделяемой памяти
	if (!createSharedMemory(fileName, maxMessages, sharedData, hMapFile)) {
		return 1;
	}

	// Создание объектов синхронизации
	if (!createSynchronizationObjects(fileName, maxMessages, emptySemaphore, fullSemaphore, mutex)) {
		cleanupResources(emptySemaphore, fullSemaphore, mutex, sharedData, hMapFile);
		return 1;
	}

	// Ввод количества процессов Sender
	std::cout << "Receiver: Enter number of Sender processes: ";
	inputNatural(senderCount);

	// Поиск и запуск Sender процессов
	std::string senderPath = findSenderPath();
	if (senderPath.empty()) {
		std::cout << "Receiver: Sender.exe not found!" << std::endl;
		cleanupResources(emptySemaphore, fullSemaphore, mutex, sharedData, hMapFile);
		return 1;
	}

	if (!startSenderProcesses(senderCount, senderPath, fileName)) {
		cleanupResources(emptySemaphore, fullSemaphore, mutex, sharedData, hMapFile);
		return 1;
	}

	// Ожидание готовности Sender процессов
	std::cout << "Receiver: Waiting for all Senders to be ready..." << std::endl;
	Sleep(5000);

	// Основной цикл Receiver
	receiverLoop(sharedData, maxMessages, emptySemaphore, fullSemaphore, mutex);

	// Завершение работы
	std::cout << "Receiver: Shutting down..." << std::endl;
	std::cout << "Receiver: Please close Sender windows manually" << std::endl;

	cleanupResources(emptySemaphore, fullSemaphore, mutex, sharedData, hMapFile);

	std::cout << "Receiver: Finished" << std::endl;
	return 0;
}