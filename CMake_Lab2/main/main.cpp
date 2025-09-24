#include "ArrayFunctions.h"
#include <iostream>

int main() {

	int n;
	try {
		std::cout << "Enter the array size (n < 10000):\n";
		std::cin >> n;

		if (std::cin.fail()) {
			std::cin.clear();
			std::cin.ignore(10000, '\n');
			throw std::invalid_argument("Invalid input. Enter an integer.");
		}
		if (n <= 0 || n >= 10000) {
			throw std::invalid_argument("Invalid input. Enter n > 0 and < 10000");
		}
	}
	catch (const std::exception& ecx) {
		std::cout << "Error: " << ecx.what() << "\n";
		main();
	}

	std::vector<int> array(n);
	while (true)
		try {

		std::cout << "Enter the array elements separated by a space\n";
		for (int i = 0; i < n; i++) {
			std::cin >> array[i];
		}
		if (std::cin.fail()) {
			std::cin.clear();
			std::cin.ignore(10000, '\n');
			throw std::invalid_argument("Invalid input. Enter an integer.");
		}
		break;
	}
	catch (const std::exception& ecx) {
		std::cout << "Error: " << ecx.what() << "\n";
		continue;
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