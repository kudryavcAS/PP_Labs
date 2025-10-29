#include "winapi.h"

CRITICAL_SECTION arrayCS;
CRITICAL_SECTION consoleCS;

void inputNatural(int& integer, int max) {
	while (true) {
		std::cin >> integer;

		if (std::cin.fail()) {
			std::cin.clear();
			std::cin.ignore(INT_MAX, '\n');
			std::cout << "Invalid input. Enter an integer 0 < " << max << "\n";

			continue;
		}
		if (integer <= 0 || integer > max) {
			std::cout << "Invalid input. Enter an integer 0 < " << max << "\n";

			continue;
		}
		break;
	}
}

void printArray(int* array, int arraySize) {
	std::cout.setf(std::ios::unitbuf);

	EnterCriticalSection(&arrayCS);

	std::cout << "Array: ";
	for (int i = 0; i < arraySize; i++) {
		std::cout << array[i] << "\t";
	}
	std::cout << "\n";

	LeaveCriticalSection(&arrayCS);
}