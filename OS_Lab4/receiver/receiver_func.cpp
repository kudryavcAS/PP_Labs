//C++98

#include "receiver.h"

bool createSharedMemory(const std::string& fileName, int maxMessages, SharedData*& sharedData, HANDLE& hMapFile) {
	hMapFile = CreateFileMapping(
		INVALID_HANDLE_VALUE,
		NULL,
		PAGE_READWRITE,
		0,
		sizeof(SharedData),
		fileName.c_str()
	);

	if (hMapFile == NULL) {
		std::cout << "Could not create file mapping" << "\n";
		return false;
	}

	sharedData = (SharedData*)MapViewOfFile(
		hMapFile,
		FILE_MAP_ALL_ACCESS,
		0,
		0,
		sizeof(SharedData)
	);

	if (sharedData == NULL) {
		std::cout << "Could not map view of file\n";
		CloseHandle(hMapFile);
		return false;
	}

	sharedData->readIndex = 0;
	sharedData->writeIndex = 0;
	sharedData->messageCount = 0;
	sharedData->maxMessages = maxMessages;

	return true;
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

bool createSynchronizationObjects(const std::string& fileName, int maxMessages,
	HANDLE& emptySemaphore, HANDLE& fullSemaphore, HANDLE& mutex) {

	std::string emptySemName = fileName + "_empty";
	std::string fullSemName = fileName + "_full";
	std::string mutexName = fileName + "_mutex";

	emptySemaphore = CreateSemaphore(NULL, maxMessages, maxMessages, emptySemName.c_str());
	fullSemaphore = CreateSemaphore(NULL, 0, maxMessages, fullSemName.c_str());
	mutex = CreateMutex(NULL, FALSE, mutexName.c_str());

	if (emptySemaphore == NULL || fullSemaphore == NULL || mutex == NULL) {
		std::cout << "Error: Could not create synchronization objects\n";
		return false;
	}

	return true;
}

std::string findSenderPath() {
	return "Sender.exe";
}

bool startSenderProcesses(int senderCount, const std::string& senderPath, const std::string& memoryName) {
	for (int i = 0; i < senderCount; i++) {
		std::string windowTitle = "Sender " + std::to_string(i + 1);
		if (!startProcess(senderPath, memoryName, windowTitle)) {
			std::cout << "Receiver: Failed to start Sender process " << i + 1 << "\n";
			return false;
		}
		std::cout << "Receiver: Started Sender process " << i + 1 << "\n";
	}
	return true;
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

	if (!CreateProcess(NULL, (LPSTR)commandLine.c_str(), NULL, NULL, FALSE, 0, NULL, NULL, &startupInfo, &processInfo)) {
		std::cout << "Failed to start process. Error: " << GetLastError() << "\n";
		return false;
	}

	CloseHandle(processInfo.hProcess);
	CloseHandle(processInfo.hThread);
	return true;
}

bool readMessage(SharedData* sharedData, int maxMessages, HANDLE emptySemaphore,
	HANDLE fullSemaphore, HANDLE mutex, Message& receivedMessage) {

	std::cout << "Messages in buffer: " << sharedData->messageCount << "/" << maxMessages << "\n";

	if (!sharedData->messageCount) {
		std::cout << "Buffer is empty! Waiting for message..." << "\n";
	}

	DWORD result = WaitForSingleObject(fullSemaphore, INFINITE);
	if (result == WAIT_OBJECT_0) {
		WaitForSingleObject(mutex, INFINITE);

		if (sharedData->messageCount <= 0) {
			std::cout << "Error: Buffer is still empty after wait\n";
			ReleaseMutex(mutex);
			ReleaseSemaphore(fullSemaphore, 1, NULL);
			return false;
		}

		receivedMessage = sharedData->messages[sharedData->readIndex];

		sharedData->readIndex = (sharedData->readIndex + 1) % maxMessages;
		sharedData->messageCount--;

		ReleaseMutex(mutex);

		ReleaseSemaphore(emptySemaphore, 1, NULL);

		return true;
	}
	else {
		std::cout << "Error waiting for message. Error: " << GetLastError() << "\n";
		return false;
	}
}

void receiverLoop(SharedData* sharedData, int maxMessages, HANDLE emptySemaphore, HANDLE fullSemaphore, HANDLE mutex) {
	bool running = true;
	while (running) {
		std::cout << "\nRECEIVER:\n";
		std::cout << "Commands:\n1 - Read message\n2 - Exit\n";
		std::cout << "Enter choice: ";

		int choice;
		inputNatural(choice, 2);

		switch (choice) {
		case 1: {
			Message receivedMsg;
			if (readMessage(sharedData, maxMessages, emptySemaphore, fullSemaphore, mutex, receivedMsg)) {
				std::cout << "Received message: '" << receivedMsg.content << "'" << "\n";
			}
			else {
				std::cout << "Failed to read message" << "\n";
			}
			break;
		}
		case 2:
			running = false;
			break;
		default:
			std::cout << "Receiver: Invalid choice\n";
			break;
		}
	}
}
