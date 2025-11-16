//C++98

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

	std::cout << "  Empty semaphore: " << emptySemName << "\n";
	std::cout << "  Full semaphore: " << fullSemName << "\n";
	std::cout << "  Mutex: " << mutexName << "\n";

	emptySemaphore = CreateSemaphore(NULL, maxMessages, maxMessages, emptySemName.c_str());
	fullSemaphore = CreateSemaphore(NULL, 0, maxMessages, fullSemName.c_str());
	mutex = CreateMutex(NULL, FALSE, mutexName.c_str());

	if (emptySemaphore == NULL || fullSemaphore == NULL || mutex == NULL) {
		std::cout << "Error: Could not create synchronization objects\n";
		return false;
	}

	std::cout << "Synchronization objects created successfully\n";
	return true;
}

std::string findSenderPath() {
	return "Sender.exe";
}

bool startSenderProcesses(int senderCount, const std::string& senderPath, const std::string& fileName) {
	for (int i = 0; i < senderCount; i++) {
		std::string windowTitle = "Sender " + std::to_string(i + 1);
		if (!startProcess(senderPath, fileName, windowTitle)) {
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
			std::cout << "Messages in buffer: " << sharedData->messageCount << "/" << maxMessages << "\n";
			if (!sharedData->messageCount) std::cout << "Waiting for message...";

			if (WaitForSingleObject(fullSemaphore, INFINITE) == WAIT_OBJECT_0) {
				WaitForSingleObject(mutex, INFINITE);

				Message msg = sharedData->messages[sharedData->readIndex];
				sharedData->readIndex = (sharedData->readIndex + 1) % maxMessages;
				sharedData->messageCount--;

				ReleaseMutex(mutex);
				ReleaseSemaphore(emptySemaphore, 1, NULL);

				std::cout << "Received message: '" << msg.content << "'" << "\n";
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

	SharedData* sharedData = NULL;
	HANDLE hMapFile = NULL;
	HANDLE emptySemaphore = NULL;
	HANDLE fullSemaphore = NULL;
	HANDLE mutex = NULL;

	std::cout << "Enter binary file name: ";
	std::cin >> fileName;
	std::cout << "Enter max number of messages: ";
	inputNatural(maxMessages, MAX_MESSAGES);

	if (!createSharedMemory(fileName, maxMessages, sharedData, hMapFile)) {
		return 1;
	}

	if (!createSynchronizationObjects(fileName, maxMessages, emptySemaphore, fullSemaphore, mutex)) {
		cleanupResources(emptySemaphore, fullSemaphore, mutex, sharedData, hMapFile);
		return 1;
	}

	std::cout << "Enter number of Sender processes: ";
	inputNatural(senderCount);

	std::string senderPath = findSenderPath();
	if (senderPath.empty()) {
		std::cout << "Sender.exe not found!\n";
		cleanupResources(emptySemaphore, fullSemaphore, mutex, sharedData, hMapFile);
		return 1;
	}

	if (!startSenderProcesses(senderCount, senderPath, fileName)) {
		cleanupResources(emptySemaphore, fullSemaphore, mutex, sharedData, hMapFile);
		return 1;
	}

	std::cout << "Waiting for all Senders to be ready...\n";
	Sleep(5000);

	receiverLoop(sharedData, maxMessages, emptySemaphore, fullSemaphore, mutex);

	std::cout << "Receiver: Please close Sender windows manually\n";

	cleanupResources(emptySemaphore, fullSemaphore, mutex, sharedData, hMapFile);

	std::cout << "Receiver: Finished\n";
	return 0;
}