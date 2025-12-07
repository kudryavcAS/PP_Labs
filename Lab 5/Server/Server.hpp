#pragma once

#include <iostream>
#include <vector>
#include <string>
#include <windows.h>
#include <memory>
#include <iomanip>
#include <conio.h>
#include "Entities.hpp"

extern CRITICAL_SECTION g_csConsole;

struct ThreadParam {
	HANDLE hPipe = nullptr;
	std::string dbFileName;
};

void initializeSync();
void cleanupSync();
void log(const std::string& msg);
long long findRecordOffset(HANDLE hFile, int id);
void printFileContent(const std::string& filename);
HANDLE createDatabase(std::string& outFilename, int& outClientCount);
void launchClients(int count);

DWORD WINAPI clientThread(LPVOID lpParam);

void runServer(const std::string& dbFileName, int clientCount);