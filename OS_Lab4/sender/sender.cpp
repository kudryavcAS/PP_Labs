#include "sender.h"

int main(int argc, char* argv[]) {
	if (argc < 2) {
		std::cout << "File name not provided" << "\n";
		std::cout << "Usage: Sender.exe <filename>" << "\n";
		return 1;
	}

	std::string fileName = argv[1];
	std::cout << "SENDER:\n" << "\n";
	std::cout << "Ready to work with file: " << fileName << "\n";

	SharedData* sharedData = NULL;
	HANDLE hMapFile = NULL;
	HANDLE emptySemaphore = NULL;
	HANDLE fullSemaphore = NULL;
	HANDLE mutex = NULL;

	if (!openSharedMemory(fileName, sharedData, hMapFile)) {
		return 1;
	}

	if (!openSynchronizationObjects(fileName, emptySemaphore, fullSemaphore, mutex)) {
		cleanupSenderResources(emptySemaphore, fullSemaphore, mutex, sharedData, hMapFile);
		return 1;
	}

	std::cout << "Maximum messages: " << sharedData->maxMessages << "\n";

	senderLoop(sharedData, emptySemaphore, fullSemaphore, mutex);

	cleanupSenderResources(emptySemaphore, fullSemaphore, mutex, sharedData, hMapFile);
	std::cout << "Finished" << "\n";

	return 0;
}