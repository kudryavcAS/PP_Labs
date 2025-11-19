#include "receiver.h"

int main() {

#ifdef _WIN32
	SetConsoleTitleA("Receiver");
#endif

	std::string memoryName;
	int maxMessages;
	int senderCount;

	SharedData* sharedData = NULL;
	HANDLE hMapFile = NULL;
	HANDLE emptySemaphore = NULL;
	HANDLE fullSemaphore = NULL;
	HANDLE mutex = NULL;

	std::cout << "Enter shared memory name: ";
	std::cin >> memoryName;
	std::cout << "Enter max number of messages: ";
	inputNatural(maxMessages, MAX_MESSAGES);

	if (!createSharedMemory(memoryName, maxMessages, sharedData, hMapFile)) {
		return 1;
	}

	if (!createSynchronizationObjects(memoryName, maxMessages, emptySemaphore, fullSemaphore, mutex)) {
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

	if (!startSenderProcesses(senderCount, senderPath, memoryName)) {
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