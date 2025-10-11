// OS_Lab3.cpp: определяет точку входа для приложения.
//

#include "main.h"

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

	std::unique_ptr<int[]> array(new int[size]);
	for (int i = 0; i < size; i++) {
		array[i] = 0;
	}

	std::cout << "\nЗапросить количество потоков marker, которые требуется запустить.";
	inputNatural(count);
	std::cout << count;



	return 0;
}
