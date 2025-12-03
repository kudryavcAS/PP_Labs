#pragma once

#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <windows.h>
#include "../Common/Entities.hpp"

// Глобальная критическая секция для синхронизации вывода в консоль
extern CRITICAL_SECTION g_csConsole;

// Структура параметров для передачи в поток
struct ThreadParam {
	HANDLE hPipe;
	HANDLE hFile;
};

// Прототипы функций
void initializeSync();
void cleanupSync();
void log(const std::string& msg);

void printFileContent(const std::string& filename);
HANDLE createDatabase(std::string& outFilename, int& outClientCount);
void launchClients(int count);

// Функция потока (сигнатура WinAPI)
DWORD WINAPI clientThread(LPVOID lpParam);

void runServer(HANDLE hFile, int clientCount);