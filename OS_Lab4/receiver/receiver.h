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

void inputNatural(int& integer, int max = INT_MAX);
bool createSharedMemory(const std::string& fileName, int maxMessages, SharedData*& sharedData, HANDLE& hMapFile);

void cleanupResources(HANDLE emptySemaphore, HANDLE fullSemaphore, HANDLE mutex,
	SharedData* sharedData, HANDLE hMapFile);

bool createSynchronizationObjects(const std::string& fileName, int maxMessages,
	HANDLE& emptySemaphore, HANDLE& fullSemaphore, HANDLE& mutex);

std::string findSenderPath();

bool startSenderProcesses(int senderCount, const std::string& senderPath, const std::string& fileName);

bool startProcess(const std::string& processPath, const std::string& arguments, const std::string& windowTitle);

void receiverLoop(SharedData* sharedData, int maxMessages, HANDLE emptySemaphore, HANDLE fullSemaphore, HANDLE mutex);

#endif