#include "ArrayFunctions.h"
#include <iostream>
#include <string>

int main() {

	const int SIZE = 10000;
	int n;

	std::cout << "Enter the array size (n <" << SIZE << "):\n";
	while (true) {
		std::cin >> n;

		if (std::cin.fail()) {
			std::cin.clear();
			std::cin.ignore(10000, '\n');
			std::cout << "Invalid input. Enter an integer n > 0 and < 10000\n";
			continue;
		}
		if (n <= 0 || n >= 10000) {
			std::cout << "Invalid input. Enter n > 0 and < 10000\n";
			continue;
		}
		break;
	}

	std::vector<int> array(n);

	std::cout << "Enter the array elements (< 2,147,483,647) separated by a space\n";
	while (true) {

		for (int i = 0; i < n; i++) {
			std::cin >> array[i];
		}
		if (std::cin.fail()) {
			std::cin.clear();
			std::cin.ignore(10000, '\n');
			std::cout << "Invalid input. Enter an integer n < 2,147,483,647\n";
			continue;
		}
		break;
	}

	ArrayData arrayData(&array, 0, 0, 0);
	HANDLE hMinMax = NULL;
	HANDLE hAverage = NULL;
	try {
		
		HANDLE hMinMax = CreateThread(NULL, 0, searchMinMaxElement, &arrayData, 0, NULL);
	   
		HANDLE hAverage = CreateThread(NULL, 0, searchAverage, &arrayData, 0, NULL);

		if (hMinMax == NULL || hAverage == NULL) {
			DWORD error = GetLastError();
			throw std::runtime_error(
				"Failed to create threads.\nError code: " + std::to_string(error) +
				".\nhMinMax: " + std::string(hMinMax ? "OK" : "FAILED") +
				".\nhAverage: " + std::string(hAverage ? "OK" : "FAILED")
			);
		}

		WaitForSingleObject(hMinMax, INFINITE);
		WaitForSingleObject(hAverage, INFINITE);

		std::cout << "The threads have completed their work.\n";

		CloseHandle(hMinMax);
		CloseHandle(hAverage);

	}
	catch (const std::exception& e) {
		if (hMinMax != NULL) {
			CloseHandle(hMinMax);
		}
		if (hAverage != NULL) {
			CloseHandle(hAverage);
		}
		std::cout << "Thread error: " << e.what() << std::endl;
		return 1;
	}

	std::cout << "The resulting array:\n";
	for (int i = 0; i < array.size(); i++) {
		if (array[i] == arrayData.maxElement || array[i] == arrayData.minElement) {
			array[i] = arrayData.average;
		}

		std::cout << array[i] << "\t";
	}

	return 0;
}