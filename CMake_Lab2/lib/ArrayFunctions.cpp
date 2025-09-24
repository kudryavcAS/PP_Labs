#include "ArrayFunctions.h"
#include <iostream>
#include <windows.h>

ArrayData::ArrayData(std::vector<int>* _array, int _max, int _min, double _average)
	: array(_array), maxElement(_max), minElement(_min), average(_average) {
}

ArrayData::ArrayData() : array(nullptr), maxElement(0), minElement(0), average(0){}

DWORD WINAPI searchMinMaxElement(LPVOID lpData) {
	ArrayData* arrayData = static_cast<ArrayData*>(lpData);

	if (arrayData->array->empty()) {
		return 0;
	}
	std::vector<int>& arr = *arrayData->array;
	arrayData->minElement = arr[0];
	arrayData->maxElement = arr[0];

	for (int i = 0; i < arr.size(); i++) {

		if (arrayData->maxElement < arr[i]) {
			arrayData->maxElement = arr[i];
		}
		if (arrayData->minElement > arr[i]) {
			arrayData->minElement = arr[i];
		}

		Sleep(7);
	}

	std::cout << "Minimum element of the array: " << arrayData->minElement
		<< "\nMaximum element of the array: " << arrayData->maxElement << "\n";

	return 0;
}

DWORD WINAPI searchAverage(LPVOID lpData) {
	ArrayData* arrayData = static_cast<ArrayData*>(lpData);

	if (arrayData->array->empty()) {
		return 0;
	}
	std::vector<int>& arr = *arrayData->array;

	int sum = 0;
	for (int i = 0; i < arr.size(); i++) {
		sum += arr[i];
		Sleep(12);
	}
	arrayData->average = static_cast<double>(sum) / arr.size();
	//!!!!!!!!!!!!!
	std::cout << "The average value of the array (rounded to an integer): " << arrayData->average << "\n";
	return 0;
}