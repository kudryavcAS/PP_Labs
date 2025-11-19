#include "sender.h"

bool openSharedMemory(const std::string& memoryName, SharedData*& sharedData, HANDLE& hMapFile) {
	hMapFile = OpenFileMapping(FILE_MAP_ALL_ACCESS, FALSE, memoryName.c_str());
	if (hMapFile == NULL) {
		std::cout << "Could not open file mapping. Error: " << GetLastError() << "\n";
		std::cout << "Make sure Receiver is running first!" << "\n";
		return false;
	}

	sharedData = (SharedData*)MapViewOfFile(hMapFile, FILE_MAP_ALL_ACCESS, 0, 0, sizeof(SharedData));
	if (sharedData == NULL) {
		std::cout << "Could not map view of file" << "\n";
		CloseHandle(hMapFile);
		return false;
	}

	return true;
}

bool openSynchronizationObjects(const std::string& memoryName,
	HANDLE& emptySemaphore, HANDLE& fullSemaphore, HANDLE& mutex) {
	std::string emptySemName = memoryName + "_empty";
	std::string fullSemName = memoryName + "_full";
	std::string mutexName = memoryName + "_mutex";

	emptySemaphore = OpenSemaphore(SEMAPHORE_ALL_ACCESS, FALSE, emptySemName.c_str());
	fullSemaphore = OpenSemaphore(SEMAPHORE_ALL_ACCESS, FALSE, fullSemName.c_str());
	mutex = OpenMutex(MUTEX_ALL_ACCESS, FALSE, mutexName.c_str());

	if (emptySemaphore == NULL || fullSemaphore == NULL || mutex == NULL) {
		std::cout << "Could not open synchronization objects" << "\n";
		return false;
	}

	return true;
}

void cleanupSenderResources(HANDLE emptySemaphore, HANDLE fullSemaphore, HANDLE mutex,
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

bool sendMessage(SharedData* sharedData, HANDLE emptySemaphore, HANDLE fullSemaphore, HANDLE mutex) {
	std::cout << "Current messages in buffer: " << sharedData->messageCount << "/" << sharedData->maxMessages << "\n";

	if (sharedData->messageCount == sharedData->maxMessages) {
		std::cout << "There are no space for new message" << "\n";
		return false;
	}

	std::string message;
	std::cout << "Enter message (max " << MAX_MESSAGE_LENGTH << " chars): ";
	std::cin >> message;

	if (message.length() > MAX_MESSAGE_LENGTH) {
		std::cout << "Message too long" << "\n";
		return false;
	}

	DWORD result = WaitForSingleObject(emptySemaphore, INFINITE);
	if (result == WAIT_OBJECT_0) {
		WaitForSingleObject(mutex, INFINITE);

		Message msg;
		std::strcpy(msg.content, message.c_str());
		sharedData->messages[sharedData->writeIndex] = msg;
		sharedData->writeIndex = (sharedData->writeIndex + 1) % sharedData->maxMessages;
		sharedData->messageCount++;

		ReleaseMutex(mutex);
		ReleaseSemaphore(fullSemaphore, 1, NULL);

		std::cout << "Message sent successfully.\n";
		return true;
	}
	else {
		std::cout << "Error waiting for free space. Error: " << GetLastError() << "\n";
		return false;
	}
}

void senderLoop(SharedData* sharedData, HANDLE emptySemaphore, HANDLE fullSemaphore, HANDLE mutex) {
	bool running = true;
	while (running) {
		std::cout << "\nSENDER:\n";
		std::cout << "Commands:\n1 - Send message\n2 - Exit" << "\n";
		std::cout << "Enter choice: ";

		int choice;
		inputNatural(choice, 2);

		switch (choice) {
		case 1:
			sendMessage(sharedData, emptySemaphore, fullSemaphore, mutex);
			break;
		case 2:
			running = false;
			break;
		default:
			std::cout << "Invalid choice" << "\n";
			break;
		}
	}
}
