// OS_Lab3.h : включаемый файл для стандартных системных включаемых файлов
// или включаемые файлы для конкретного проекта.

#pragma once

#include <iostream>
#include <Windows.h>
#include <ctime>
#include <vector>

void inputNatural(int& integer, int max = INT_MAX);
void printArray(int* array, int arraySize);

extern CRITICAL_SECTION arrayCS;
// TODO: установите здесь ссылки на дополнительные заголовки, требующиеся для программы.
