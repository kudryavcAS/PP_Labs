// OS_Lab3.cpp: определяет точку входа для приложения.
// C++11

#include "winapi.h"

int arraySize = 0;
std::unique_ptr<int[]>array;

HANDLE threadStartEvent;
std::unique_ptr<HANDLE[]> threadContinueEvents;
std::unique_ptr<HANDLE[]> threadStopEvents;
std::unique_ptr<HANDLE[]> threadCannotContinueEvents;
std::unique_ptr<bool[]> threadTerminated;

DWORD WINAPI markerThread(LPVOID lpParam) {
	int number = (int)lpParam, markedCount = 0;
	srand(number);

	WaitForSingleObject(threadStartEvent, INFINITE);

	while (true) {
		int randomValue = rand();
		int i = randomValue % arraySize;

		EnterCriticalSection(&arrayCS);
		EnterCriticalSection(&consoleCS);
		if (array[i] == 0) {
			Sleep(5);

			array[i] = number;
			markedCount++;

			LeaveCriticalSection(&arrayCS);

			Sleep(5);
		}
		else {

			std::cout << "Number of thread: " << number
				<< ". Count of marked elements: " << markedCount
				<< ". Index of unmarked elements: " << i << "\n";
			LeaveCriticalSection(&consoleCS);
			LeaveCriticalSection(&arrayCS);

			SetEvent(threadCannotContinueEvents[number - 1]);

			HANDLE waitEvents[2] = {
				 threadContinueEvents[number - 1],
				 threadStopEvents[number - 1]
			};

			DWORD result = WaitForMultipleObjects(2, waitEvents, FALSE, INFINITE);

			if (result == WAIT_OBJECT_0 + 1) {
				EnterCriticalSection(&arrayCS);
				EnterCriticalSection(&consoleCS);

				for (int i = 0; i < arraySize; i++) {
					if (array[i] == number) {
						array[i] = 0;
					}

				}

				LeaveCriticalSection(&arrayCS);
				LeaveCriticalSection(&consoleCS);

				std::cout << "Thread " << number << " terminated.\n";
				threadTerminated[number - 1] = true;
				return 0;
			}
		}
	}

	return 0;
}


int main()
{
	std::cout.setf(std::ios::unitbuf);

	int count;

	std::cout << "Enter the array size:\n";
	inputNatural(arraySize);

	array = std::make_unique<int[]>(arraySize);
	for (int i = 0; i < arraySize; i++) {
		array[i] = 0;
	}

	std::cout << "Enter the number of marker threads:\n";
	inputNatural(count, MAXIMUM_WAIT_OBJECTS);

	InitializeCriticalSection(&arrayCS);
	InitializeCriticalSection(&consoleCS);

	threadStartEvent = CreateEvent(NULL, TRUE, FALSE, NULL);

	threadContinueEvents = std::make_unique<HANDLE[]>(count);
	threadStopEvents = std::make_unique<HANDLE[]>(count);
	threadCannotContinueEvents = std::make_unique<HANDLE[]>(count);
	threadTerminated = std::make_unique<bool[]>(count);
	auto threadHandles = std::make_unique<HANDLE[]>(count);


	for (int i = 0; i < count; i++) {
		threadContinueEvents[i] = CreateEvent(NULL, FALSE, FALSE, NULL);
		threadStopEvents[i] = CreateEvent(NULL, FALSE, FALSE, NULL);
		threadCannotContinueEvents[i] = CreateEvent(NULL, FALSE, FALSE, NULL);
		threadTerminated[i] = false;

		threadHandles[i] = CreateThread(NULL, 0, markerThread, (LPVOID)(i + 1), 0, NULL);

		if (threadHandles[i] == NULL) {
			std::cout << "Error creating thread " << i + 1 << std::endl;
			return 1;
		}
	}
	SetEvent(threadStartEvent);
	int activeThreads = count;

	while (activeThreads > 0) {
		std::vector<HANDLE> activeEvents;
		for (int i = 0; i < count; i++) {
			if (!threadTerminated[i]) {
				activeEvents.push_back(threadCannotContinueEvents[i]);
			}
		}

		if (activeEvents.empty()) break;

		WaitForMultipleObjects(activeEvents.size(), activeEvents.data(), TRUE, INFINITE);

		printArray(array.get(), arraySize);

		int threadToTerminate;

		std::cout << "Enter thread number to terminate (1-" << count << "): ";
		inputNatural(threadToTerminate, count);

		SetEvent(threadStopEvents[threadToTerminate - 1]);
		WaitForSingleObject(threadHandles[threadToTerminate - 1], INFINITE);
		threadTerminated[threadToTerminate - 1] = true;

		printArray(array.get(), arraySize);

		for (int i = 0; i < count; i++) {
			if (!threadTerminated[i]) {
				SetEvent(threadContinueEvents[i]);
			}
		}
		activeThreads--;

	}
	CloseHandle(threadStartEvent);

	for (int i = 0; i < count; i++) {
		CloseHandle(threadContinueEvents[i]);
		CloseHandle(threadStopEvents[i]);
		CloseHandle(threadCannotContinueEvents[i]);
		CloseHandle(threadHandles[i]);
	}

	std::cout << "All threads completed. Program finished." << std::endl;

	DeleteCriticalSection(&consoleCS);
	DeleteCriticalSection(&arrayCS);

	return 0;
}