#ifndef RECEIVER_H
#define RECEIVER_H

#include <windows.h>
#include <iostream>
#include <string>
#include <vector>

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
bool startProcess(const std::string& processPath, const std::string& arguments, const std::string& windowTitle = "");

#endif