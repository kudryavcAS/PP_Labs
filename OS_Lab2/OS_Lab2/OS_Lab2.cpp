#include <iostream>
#include <vector>
#include <windows.h>

struct ArrayData {
	std::vector<int>* array;
	int maxElement, minElement;
	double average;

	ArrayData(std::vector<int>* _array, int _max, int _min, double _average) :
		array(_array), maxElement(_max), minElement(_min), average(_average) {
	}

	ArrayData() :array(nullptr), maxElement(0), minElement(0), average(0) {}
};

DWORD WINAPI searchMinMaxElement(LPVOID lpData) {
	ArrayData* arrayData = (ArrayData*)lpData;

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
	ArrayData* arrayData = (ArrayData*)lpData;

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
	std::cout <<"The average value of the array (rounded to an integer): " << arrayData->average << "\n";
	return 0;
}

int main() {

	int n;
	std::cout << "Enter the array size:\n";
	std::cin >> n;

	std::vector<int> array(n);

	for (int i = 0; i < n; i++) {
		std::cin >> array[i];
	}

	ArrayData arrayData(&array, 0, 0, 0);

	HANDLE hMinMax = CreateThread(NULL, 0, searchMinMaxElement, &arrayData, 0, NULL);

	HANDLE hAverage = CreateThread(NULL, 0, searchAverage, &arrayData, 0, NULL);

	if (hMinMax == NULL || hAverage == NULL) {
		std::cout << "Error: failed to create a thread.\n";
		return 1;
	}

	WaitForSingleObject(hMinMax, INFINITE);
	WaitForSingleObject(hAverage, INFINITE);

	std::cout << "The threads have completed their work.\n";

	CloseHandle(hMinMax);
	CloseHandle(hAverage);

	std::cout << "The resulting array:\n";
	for (int i = 0; i < array.size(); i++) {
		if (array[i] == arrayData.maxElement || array[i] == arrayData.minElement) {
			array[i] = arrayData.average;
		}

		std::cout << array[i] << "\t";
	}

	return 0;
}