#include "sender.h"

int main(int argc, char* argv[]) {
	if (argc < 2) {
		std::cout << "Sender: File name not provided" << std::endl;
		std::cout << "Usage: Sender.exe <filename>" << std::endl;
		return 1;
	}

	std::string fileName = argv[1];
	std::cout << "SENDER\n";
	std::cout << "Sender: Ready to work with file: " << fileName << "\n";

	std::string emptySemName = "emptySem";
	std::string fullSemName = "fullSem";
	std::string mutexName = "mutex";

	std::cout << "Sender: Looking for synchronization objects..." << std::endl;
	std::cout << "  Empty semaphore: " << emptySemName << std::endl;
	std::cout << "  Full semaphore: " << fullSemName << std::endl;
	std::cout << "  Mutex: " << mutexName << std::endl;

	HANDLE hMapFile = OpenFileMapping(FILE_MAP_ALL_ACCESS, FALSE, fileName.c_str());
	if (hMapFile == NULL) {
		std::cout << "Sender: Could not open file mapping. Error: " << GetLastError() << std::endl;
		std::cout << "Make sure Receiver is running first!" << std::endl;
		return 1;
	}

	SharedData* sharedData = (SharedData*)MapViewOfFile(hMapFile, FILE_MAP_ALL_ACCESS, 0, 0, sizeof(SharedData));
	if (sharedData == NULL) {
		std::cout << "Sender: Could not map view of file" << std::endl;
		CloseHandle(hMapFile);
		return 1;
	}

	HANDLE emptySemaphore = OpenSemaphore(SEMAPHORE_ALL_ACCESS, FALSE, emptySemName.c_str());
	if (emptySemaphore == NULL) {
		std::cout << "Sender: Could not open empty semaphore. Error: " << GetLastError() << std::endl;
		UnmapViewOfFile(sharedData);
		CloseHandle(hMapFile);
		return 1;
	}

	HANDLE fullSemaphore = OpenSemaphore(SEMAPHORE_ALL_ACCESS, FALSE, fullSemName.c_str());
	if (fullSemaphore == NULL) {
		std::cout << "Sender: Could not open full semaphore. Error: " << GetLastError() << std::endl;
		CloseHandle(emptySemaphore);
		UnmapViewOfFile(sharedData);
		CloseHandle(hMapFile);
		return 1;
	}

	HANDLE mutex = OpenMutex(MUTEX_ALL_ACCESS, FALSE, mutexName.c_str());
	if (mutex == NULL) {
		std::cout << "Sender: Could not open mutex. Error: " << GetLastError() << std::endl;
		CloseHandle(emptySemaphore);
		CloseHandle(fullSemaphore);
		UnmapViewOfFile(sharedData);
		CloseHandle(hMapFile);
		return 1;
	}

	std::cout << "Sender: Successfully opened all synchronization objects!" << std::endl;
	std::cout << "Sender: Maximum messages: " << sharedData->maxMessages << std::endl;

	bool running = true;
	while (running) {
		std::cout << "\n=== SENDER ===" << std::endl;
		std::cout << "Commands:\n1 - Send message\n2 - Exit" << std::endl;
		std::cout << "Enter choice: ";

		int choice;
		inputNatural(choice, 2);

		switch (choice) {
		case 1: {
			std::cout << "Current messages in buffer: " << sharedData->messageCount << "/" << sharedData->maxMessages << "\n";
			if (sharedData->messageCount == sharedData->maxMessages) {
				std::cout << "There are no space to new message";
				break;
			}

			std::string message;
			std::cout << "Enter message (max " << MAX_MESSAGE_LENGTH << " chars): ";
			std::cin >> message;

			if (message.length() > MAX_MESSAGE_LENGTH) {
				std::cout << "Sender: Message too long" << std::endl;
				break;
			}

			std::cout << "Sender: Waiting for free space..." << std::endl;

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

				std::cout << "Sender: Message sent successfully: '" << message << "'" << std::endl;
			}
			else {
				std::cout << "Sender: Error waiting for free space. Error: " << GetLastError() << std::endl;
			}
			break;
		}
		case 2:
			running = false;
			break;
		default:
			std::cout << "Sender: Invalid choice" << std::endl;
			break;
		}
	}

	std::cout << "Sender: Closing..." << std::endl;

	CloseHandle(emptySemaphore);
	CloseHandle(fullSemaphore);
	CloseHandle(mutex);
	UnmapViewOfFile(sharedData);
	CloseHandle(hMapFile);

	std::cout << "Sender: Finished" << std::endl;
	return 0;
}