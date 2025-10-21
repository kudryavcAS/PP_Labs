// OS_Lab3.cpp: определяет точку входа для приложения.
//C++11

#include "main.h"

int arraySize = 0;
int* array = nullptr;

CRITICAL_SECTION arrayCS;

HANDLE threadStartEvent;


DWORD WINAPI markerThread(LPVOID lpParam) {
	int number = (int)lpParam, markedCount = 0;

	WaitForSingleObject(threadStartEvent, INFINITE);

	srand(number);

	while (true) {
		int randomValue = rand();
		int i = randomValue % arraySize;

		EnterCriticalSection(&arrayCS);
		if (array[i] == 0) {
			Sleep(5);
			
			array[i] = number;
			markedCount++;

			LeaveCriticalSection(&arrayCS);

			Sleep(5);
		}
		else {
			std::cout << "Number of thread: " << number
				<< "\nCount of marked elements: " << markedCount
				<< "\nIndex of ubmarked elements: " << i;
		}

	}

}

void inputNatural(int& integer) {
	while (true) {
		std::cin >> integer;

		if (std::cin.fail()) {
			std::cin.clear();
			std::cin.ignore(INT_MAX, '\n');
			std::cout << "Invalid input. Enter an integer n > 0 and < " << INT_MAX << "\n";
			continue;
		}
		if (integer <= 0) {
			std::cout << "Invalid input. Enter n > 0\n";
			continue;
		}
		break;
	}
}

int main()
{
	int size, count;

	std::cout << "Enter the array size:\n";
	inputNatural(size);
	std::cout << size;

	array = new int[size];
	for (int i = 0; i < size; i++) {
		array[i] = 0;
	}

	std::cout << "\nEnter the number of marker threads:\n";
	inputNatural(count);
	std::cout << count;



	return 0;
}
