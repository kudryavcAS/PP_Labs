#ifndef SENDER_H
#define SENDER_H

#include <windows.h>
#include <iostream>
#include <string>
#include "receiver.h"

bool openSharedMemory(const std::string& fileName, SharedData*& sharedData, HANDLE& hMapFile);
bool openSynchronizationObjects(const std::string& fileName,
	HANDLE& emptySemaphore, HANDLE& fullSemaphore, HANDLE& mutex);
void senderLoop(SharedData* sharedData, HANDLE emptySemaphore, HANDLE fullSemaphore, HANDLE mutex);
void cleanupSenderResources(HANDLE emptySemaphore, HANDLE fullSemaphore, HANDLE mutex,
	SharedData* sharedData, HANDLE hMapFile);
bool sendMessage(SharedData* sharedData, HANDLE emptySemaphore, HANDLE fullSemaphore, HANDLE mutex);

#endif