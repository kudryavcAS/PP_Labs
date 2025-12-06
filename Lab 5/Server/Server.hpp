#pragma once

#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <windows.h>
#include <memory>
#include "../Common/Entities.hpp"

extern CRITICAL_SECTION g_csConsole;

struct ThreadParam {
	HANDLE hPipe;
	HANDLE hFile;
};

void initializeSync();
void cleanupSync();
void log(const std::string& msg);

void printFileContent(const std::string& filename);
HANDLE createDatabase(std::string& outFilename, int& outClientCount);
void launchClients(int count);

DWORD WINAPI clientThread(LPVOID lpParam);

void runServer(HANDLE hFile, int clientCount);