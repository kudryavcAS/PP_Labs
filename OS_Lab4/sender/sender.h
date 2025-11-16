#ifndef SENDER_H
#define SENDER_H

#include <windows.h>
#include <iostream>
#include <string>

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
bool openSharedMemory(const std::string& fileName, SharedData*& sharedData, HANDLE& hMapFile);
bool openSynchronizationObjects(const std::string& fileName,
	HANDLE& emptySemaphore, HANDLE& fullSemaphore, HANDLE& mutex);
void senderLoop(SharedData* sharedData, HANDLE emptySemaphore, HANDLE fullSemaphore, HANDLE mutex);
void cleanupSenderResources(HANDLE emptySemaphore, HANDLE fullSemaphore, HANDLE mutex,
	SharedData* sharedData, HANDLE hMapFile);
bool sendMessage(SharedData* sharedData, HANDLE emptySemaphore, HANDLE fullSemaphore, HANDLE mutex);

#endif