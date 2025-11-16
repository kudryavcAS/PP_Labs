#include "receiver.h"

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