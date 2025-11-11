#include "sender.h"

const int MAX_MESSAGE_LENGTH = 20;
const int MAX_MESSAGES = 100;

struct Message {
	char content[MAX_MESSAGE_LENGTH + 1];
	bool active;
};

struct SharedData {
	Message messages[MAX_MESSAGES];
	int readIndex;
	int writeIndex;
	int messageCount;
	int maxMessages;
	HANDLE semaphoreEmpty;
	HANDLE semaphoreFull;
	HANDLE mutex;
};

int main(int argc, char* argv[]) {
	if (argc < 2) {
		std::cout << "Sender: File name not provided" << std::endl;
		return 1;
	}

	std::string fileName = argv[1];

	// Открытие файла отображения в память
	HANDLE hMapFile = OpenFileMapping(
		FILE_MAP_ALL_ACCESS,
		FALSE,
		fileName.c_str()
	);

	if (hMapFile == NULL) {
		std::cout << "Sender: Could not open file mapping" << std::endl;
		return 1;
	}

	SharedData* sharedData = (SharedData*)MapViewOfFile(
		hMapFile,
		FILE_MAP_ALL_ACCESS,
		0,
		0,
		sizeof(SharedData)
	);

	if (sharedData == NULL) {
		std::cout << "Sender: Could not map view of file" << std::endl;
		CloseHandle(hMapFile);
		return 1;
	}

	// Открытие synchronization objects
	HANDLE semaphoreEmpty = sharedData->semaphoreEmpty;
	HANDLE semaphoreFull = sharedData->semaphoreFull;
	HANDLE mutex = sharedData->mutex;

	std::cout << "Sender: Ready to work" << std::endl;

	// Основной цикл Sender
	bool running = true;
	while (running) {
		std::cout << "\nSender commands:\n";
		std::cout << "1 - Send message\n";
		std::cout << "2 - Exit\n";
		std::cout << "Enter choice: ";

		int choice;
		std::cin >> choice;

		switch (choice) {
		case 1: {
			// Отправка сообщения
			std::string message;
			std::cout << "Enter message (max " << MAX_MESSAGE_LENGTH << " chars): ";
			std::cin >> message;

			if (message.length() > MAX_MESSAGE_LENGTH) {
				std::cout << "Sender: Message too long" << std::endl;
				break;
			}

			WaitForSingleObject(semaphoreEmpty, INFINITE);
			WaitForSingleObject(mutex, INFINITE);

			Message msg;
			std::strcpy(msg.content, message.c_str());
			sharedData->messages[sharedData->writeIndex] = msg;
			sharedData->writeIndex = (sharedData->writeIndex + 1) % sharedData->maxMessages;
			sharedData->messageCount++;

			ReleaseMutex(mutex);
			ReleaseSemaphore(semaphoreFull, 1, NULL);

			std::cout << "Sender: Message sent: " << message << std::endl;
			break;
		}
		case 2:
			running = false;
			break;
		default:
			std::cout << "Sender: Invalid choice" << std::endl;
			break;
		}
	}

	// Завершение работы
	UnmapViewOfFile(sharedData);
	CloseHandle(hMapFile);

	std::cout << "Sender: Finished" << std::endl;
	return 0;
}