#pragma once
#include <vector>
#include <windows.h>

struct ArrayData {
    std::vector<int>* array;
    int maxElement, minElement;
    double average;

    ArrayData(std::vector<int>* _array, int _max, int _min, double _average);
    ArrayData();
};

DWORD WINAPI searchMinMaxElement(LPVOID lpData);
DWORD WINAPI searchAverage(LPVOID lpData);