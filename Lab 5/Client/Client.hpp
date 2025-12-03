#pragma once

#include <iostream>
#include <windows.h>
#include <string>
#include <conio.h>
#include "../Common/Entities.hpp"

// Прототипы
void printEmployee(const Employee& e);
HANDLE connectToServer();
void processSession(HANDLE hPipe);