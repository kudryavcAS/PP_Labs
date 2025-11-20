#ifndef RECEIVER_H
#define RECEIVER_H

#include <windows.h>
#include <iostream>
#include <string>
#include <vector>
#include <climits>

const int MAX_MESSAGE_LENGTH = 20;
const int MAX_MESSAGES = 100;

struct Message {
	char content[MAX_MESSAGE_LENGTH + 1];
};

struct SharedData {
	Message messages[MAX_MESSAGES];
	int readIndex;
	int writeIndex;
	int messageCount;
	int maxMessages;
};


inline void inputNatural(int& integer, int max = INT_MAX) {
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
bool createSharedMemory(const std::string& fileName, int maxMessages, SharedData*& sharedData, HANDLE& hMapFile);

void cleanupResources(HANDLE emptySemaphore, HANDLE fullSemaphore, HANDLE mutex,
	SharedData* sharedData, HANDLE hMapFile);

bool createSynchronizationObjects(const std::string& fileName, int maxMessages,
	HANDLE& emptySemaphore, HANDLE& fullSemaphore, HANDLE& mutex);

std::string findSenderPath();

bool startSenderProcesses(int senderCount, const std::string& senderPath, const std::string& fileName);

bool startProcess(const std::string& processPath, const std::string& arguments, const std::string& windowTitle);

void receiverLoop(SharedData* sharedData, int maxMessages, HANDLE emptySemaphore, HANDLE fullSemaphore, HANDLE mutex);

bool readMessage(SharedData* sharedData, int maxMessages, HANDLE emptySemaphore,
	HANDLE fullSemaphore, HANDLE mutex, Message& receivedMessage);
#endif