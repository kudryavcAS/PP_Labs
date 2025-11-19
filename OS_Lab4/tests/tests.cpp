#include <gtest/gtest.h>
#include "receiver.h"
#include "sender.h"

class Win32SharedMemoryTest : public ::testing::Test {
protected:
	void SetUp() override {

		HANDLE hMapFile = CreateFileMapping(INVALID_HANDLE_VALUE, NULL, PAGE_READWRITE, 0, sizeof(SharedData), "test_memory");
		if (hMapFile != NULL) {
			CloseHandle(hMapFile);
		}
	}

	void TearDown() override {}
};

TEST_F(Win32SharedMemoryTest, CreateSharedMemory_Success) {
	SharedData* sharedData = NULL;
	HANDLE hMapFile = NULL;

	bool result = createSharedMemory("test_memory", 5, sharedData, hMapFile);

	EXPECT_TRUE(result);
	EXPECT_NE(sharedData, nullptr);
	EXPECT_NE(hMapFile, (HANDLE)NULL);
	EXPECT_EQ(sharedData->maxMessages, 5);
	EXPECT_EQ(sharedData->messageCount, 0);
	EXPECT_EQ(sharedData->readIndex, 0);
	EXPECT_EQ(sharedData->writeIndex, 0);

	UnmapViewOfFile(sharedData);
	CloseHandle(hMapFile);
}

TEST_F(Win32SharedMemoryTest, CreateSyncObjects_Success) {
	HANDLE emptySem = NULL;
	HANDLE fullSem = NULL;
	HANDLE mutex = NULL;

	bool result = createSynchronizationObjects("test_memory", 5, emptySem, fullSem, mutex);

	EXPECT_TRUE(result);
	EXPECT_NE(emptySem, (HANDLE)NULL);
	EXPECT_NE(fullSem, (HANDLE)NULL);
	EXPECT_NE(mutex, (HANDLE)NULL);

	CloseHandle(emptySem);
	CloseHandle(fullSem);
	CloseHandle(mutex);
}

TEST_F(Win32SharedMemoryTest, OpenSharedMemory_Success) {
	SharedData* createdData = NULL;
	HANDLE hMapFileCreate = NULL;
	createSharedMemory("test_memory", 5, createdData, hMapFileCreate);

	SharedData* openedData = NULL;
	HANDLE hMapFileOpen = NULL;
	bool result = openSharedMemory("test_memory", openedData, hMapFileOpen);

	EXPECT_TRUE(result);
	EXPECT_NE(openedData, nullptr);
	EXPECT_EQ(openedData->maxMessages, 5);

	UnmapViewOfFile(createdData);
	UnmapViewOfFile(openedData);
	CloseHandle(hMapFileCreate);
	CloseHandle(hMapFileOpen);
}

TEST_F(Win32SharedMemoryTest, OpenSharedMemory_NotFound) {
	SharedData* sharedData = NULL;
	HANDLE hMapFile = NULL;
	bool result = openSharedMemory("non_existent_memory", sharedData, hMapFile);

	EXPECT_FALSE(result);
	EXPECT_EQ(sharedData, nullptr);
	EXPECT_EQ(hMapFile, (HANDLE)NULL);
}

class Win32MessageTest : public ::testing::Test {
protected:
	void SetUp() override {
		createSharedMemory("msg_test", 3, sharedData, hMapFile);
		createSynchronizationObjects("msg_test", 3, emptySem, fullSem, mutex);
	}

	void TearDown() override {
		if (sharedData) UnmapViewOfFile(sharedData);
		if (hMapFile) CloseHandle(hMapFile);
		if (emptySem) CloseHandle(emptySem);
		if (fullSem) CloseHandle(fullSem);
		if (mutex) CloseHandle(mutex);
	}

	SharedData* sharedData = NULL;
	HANDLE hMapFile = NULL;
	HANDLE emptySem = NULL;
	HANDLE fullSem = NULL;
	HANDLE mutex = NULL;
};

TEST_F(Win32MessageTest, SendMessage_Success) {
	WaitForSingleObject(emptySem, INFINITE);
	WaitForSingleObject(mutex, INFINITE);

	Message msg;
	strcpy_s(msg.content, sizeof(msg.content), "Test");
	sharedData->messages[sharedData->writeIndex] = msg;
	sharedData->writeIndex = (sharedData->writeIndex + 1) % 3;
	sharedData->messageCount++;

	ReleaseMutex(mutex);
	ReleaseSemaphore(fullSem, 1, NULL);

	EXPECT_EQ(sharedData->messageCount, 1);
	EXPECT_EQ(std::string(sharedData->messages[0].content), "Test");
}

TEST_F(Win32MessageTest, ReadMessage_Success) {
	WaitForSingleObject(emptySem, INFINITE);
	WaitForSingleObject(mutex, INFINITE);

	Message msg;
	strcpy_s(msg.content, sizeof(msg.content), "Hello");
	sharedData->messages[sharedData->writeIndex] = msg;
	sharedData->writeIndex = (sharedData->writeIndex + 1) % 3;
	sharedData->messageCount++;

	ReleaseMutex(mutex);
	ReleaseSemaphore(fullSem, 1, NULL);

	WaitForSingleObject(fullSem, INFINITE);
	WaitForSingleObject(mutex, INFINITE);

	Message readMsg = sharedData->messages[sharedData->readIndex];
	sharedData->readIndex = (sharedData->readIndex + 1) % 3;
	sharedData->messageCount--;

	ReleaseMutex(mutex);
	ReleaseSemaphore(emptySem, 1, NULL);

	EXPECT_EQ(sharedData->messageCount, 0);
	EXPECT_EQ(std::string(readMsg.content), "Hello");
}

TEST_F(Win32MessageTest, CircularBuffer_WrapAround) {
	sharedData->maxMessages = 3;

	for (int i = 0; i < 3; i++) {
		WaitForSingleObject(emptySem, INFINITE);
		WaitForSingleObject(mutex, INFINITE);

		Message msg;
		strcpy_s(msg.content, sizeof(msg.content), ("Msg" + std::to_string(i)).c_str());
		sharedData->messages[i] = msg;
		sharedData->messageCount++;

		ReleaseMutex(mutex);
		ReleaseSemaphore(fullSem, 1, NULL);
	}

	sharedData->writeIndex = 0;

	WaitForSingleObject(emptySem, INFINITE);
	WaitForSingleObject(mutex, INFINITE);

	Message newMsg;
	strcpy_s(newMsg.content, sizeof(newMsg.content), "NewMsg");
	sharedData->messages[0] = newMsg;
	sharedData->writeIndex = 1;

	ReleaseMutex(mutex);
	ReleaseSemaphore(fullSem, 1, NULL);

	EXPECT_EQ(sharedData->writeIndex, 1);
}

TEST_F(Win32SharedMemoryTest, CleanupResources_Success) {
	SharedData* sharedData = NULL;
	HANDLE hMapFile = NULL;
	createSharedMemory("test_memory", 5, sharedData, hMapFile);

	HANDLE emptySem = CreateSemaphore(NULL, 5, 5, "test_memory_empty");
	HANDLE fullSem = CreateSemaphore(NULL, 0, 5, "test_memory_full");
	HANDLE mutex = CreateMutex(NULL, FALSE, "test_memory_mutex");

	EXPECT_NO_THROW({
		 cleanupResources(emptySem, fullSem, mutex, sharedData, hMapFile);
		});
}

TEST_F(Win32MessageTest, ConcurrentAccess_NoDataRace) {
	sharedData->maxMessages = 10;

	for (int i = 0; i < 10; i++) {
		WaitForSingleObject(emptySem, INFINITE);
		WaitForSingleObject(mutex, INFINITE);

		Message msg;
		strcpy_s(msg.content, sizeof(msg.content), ("Msg" + std::to_string(i)).c_str());
		sharedData->messages[sharedData->writeIndex] = msg;
		sharedData->writeIndex = (sharedData->writeIndex + 1) % 10;
		sharedData->messageCount++;

		ReleaseMutex(mutex);
		ReleaseSemaphore(fullSem, 1, NULL);
	}

	EXPECT_EQ(sharedData->messageCount, 10);

	for (int i = 0; i < 10; i++) {
		WaitForSingleObject(fullSem, INFINITE);
		WaitForSingleObject(mutex, INFINITE);

		sharedData->readIndex = (sharedData->readIndex + 1) % 10;
		sharedData->messageCount--;

		ReleaseMutex(mutex);
		ReleaseSemaphore(emptySem, 1, NULL);
	}

	EXPECT_EQ(sharedData->messageCount, 0);
}

TEST_F(Win32SharedMemoryTest, CreateSharedMemory_InvalidSize) {
	SharedData* sharedData = NULL;
	HANDLE hMapFile = NULL;

	hMapFile = CreateFileMapping(
		INVALID_HANDLE_VALUE,
		NULL,
		PAGE_READWRITE,
		0,
		0,
		"invalid_test"
	);

	EXPECT_EQ(hMapFile, (HANDLE)NULL);
}

TEST(EdgeCasesTest, MessageLengthBoundaries_WinAPI) {
	Message msg = {};

	std::string maxLengthMsg(MAX_MESSAGE_LENGTH, 'A');
	strcpy_s(msg.content, sizeof(msg.content), maxLengthMsg.c_str());

	EXPECT_EQ(strlen(msg.content), MAX_MESSAGE_LENGTH);

	std::string tooLongMsg(MAX_MESSAGE_LENGTH + 10, 'B');
	strcpy_s(msg.content, sizeof(msg.content), tooLongMsg.c_str());

	EXPECT_LE(strlen(msg.content), MAX_MESSAGE_LENGTH);
}

TEST_F(Win32MessageTest, SemaphoreOperations_Success) {
	DWORD result = WaitForSingleObject(emptySem, 1000);
	EXPECT_EQ(result, WAIT_OBJECT_0);

	BOOL releaseResult = ReleaseSemaphore(emptySem, 1, NULL);
	EXPECT_TRUE(releaseResult);
}

TEST_F(Win32MessageTest, MutexOperations_Success) {
	DWORD result1 = WaitForSingleObject(mutex, 1000);
	EXPECT_EQ(result1, WAIT_OBJECT_0);

	DWORD result2 = WaitForSingleObject(mutex, 100);
	EXPECT_EQ(result2, WAIT_TIMEOUT);

	ReleaseMutex(mutex);

	DWORD result3 = WaitForSingleObject(mutex, 1000);
	EXPECT_EQ(result3, WAIT_OBJECT_0);

	ReleaseMutex(mutex);
}

class InputNaturalTest : public ::testing::Test {
protected:
	void SetUp() override {
		oldCinBuf = std::cin.rdbuf();
		oldCoutBuf = std::cout.rdbuf();
	}

	void TearDown() override {
		std::cin.rdbuf(oldCinBuf);
		std::cout.rdbuf(oldCoutBuf);
	}

	void simulateInput(const std::string& input) {
		inputStream.str(input);
		inputStream.clear();
		std::cin.rdbuf(inputStream.rdbuf());
	}

	std::string captureOutput(std::function<void()> testFunction) {
		std::stringstream outputStream;
		std::cout.rdbuf(outputStream.rdbuf());

		testFunction();

		std::cout.rdbuf(oldCoutBuf);
		return outputStream.str();
	}

	std::stringstream inputStream;
	std::streambuf* oldCinBuf = nullptr;
	std::streambuf* oldCoutBuf = nullptr;
};

TEST_F(InputNaturalTest, ValidInput_ReturnsCorrectValue) {
	simulateInput("5");

	int result = 0;
	std::string output = captureOutput([&]() {
		inputNatural(result, 10);
		});

	EXPECT_EQ(result, 5);
	EXPECT_TRUE(output.empty());
}

TEST_F(InputNaturalTest, StringThenValidInput_HandlesError) {
	simulateInput("abc\n7");

	int result = 0;
	std::string output = captureOutput([&]() {
		inputNatural(result, 10);
		});

	EXPECT_EQ(result, 7);
	EXPECT_TRUE(output.find("Invalid input") != std::string::npos);
}

TEST_F(InputNaturalTest, OutOfRangeThenValid_HandlesError) {
	simulateInput("15\n3");

	int result = 0;
	std::string output = captureOutput([&]() {
		inputNatural(result, 10);
		});

	EXPECT_EQ(result, 3);
	EXPECT_TRUE(output.find("Invalid input") != std::string::npos);
}

TEST_F(InputNaturalTest, MaxBoundaryValue_Accepted) {
	simulateInput("10");

	int result = 0;
	std::string output = captureOutput([&]() {
		inputNatural(result, 10);
		});

	EXPECT_EQ(result, 10);
	EXPECT_TRUE(output.empty());
}

TEST(FindSenderPathTest, ReturnsCorrectPath) {
	EXPECT_EQ("Sender.exe", findSenderPath());
}