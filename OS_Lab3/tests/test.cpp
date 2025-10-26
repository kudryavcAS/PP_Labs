#include <gtest/gtest.h>
#include "winapi.h"

class PrintArrayTest : public ::testing::Test {
protected:
	void SetUp() override {
		InitializeCriticalSection(&arrayCS);
	}
	void TearDown() override {
		DeleteCriticalSection(&arrayCS);
	}

	std::string captureOutput(int* array, int size) {
		std::stringstream buffer;
		std::streambuf* old = std::cout.rdbuf(buffer.rdbuf());

		printArray(array, size);

		std::cout.rdbuf(old);
		return buffer.str();
	}
};

TEST_F(PrintArrayTest, EmptyArray) {
	int emptyArray[] = { 0, 0, 0 };
	std::string output = captureOutput(emptyArray, 3);

	EXPECT_TRUE(output.find("Array:") != std::string::npos);
	EXPECT_TRUE(output.find("0") != std::string::npos);
}

TEST_F(PrintArrayTest, PositiveNumbers) {
	int positiveArray[] = { 1, 2, 3, 4, 5 };
	std::string output = captureOutput(positiveArray, 5);

	EXPECT_TRUE(output.find("Array:") != std::string::npos);
	EXPECT_TRUE(output.find("1") != std::string::npos);
	EXPECT_TRUE(output.find("2") != std::string::npos);
	EXPECT_TRUE(output.find("3") != std::string::npos);
	EXPECT_TRUE(output.find("4") != std::string::npos);
	EXPECT_TRUE(output.find("5") != std::string::npos);
}

TEST_F(PrintArrayTest, NegativeNumbers) {
	int negativeArray[] = { -1, -2, -3 };
	std::string output = captureOutput(negativeArray, 3);

	EXPECT_TRUE(output.find("Array:") != std::string::npos);
	EXPECT_TRUE(output.find("-1") != std::string::npos);
	EXPECT_TRUE(output.find("-2") != std::string::npos);
	EXPECT_TRUE(output.find("-3") != std::string::npos);
}

TEST_F(PrintArrayTest, SingleElement) {
	int singleArray[] = { 42 };
	std::string output = captureOutput(singleArray, 1);

	EXPECT_TRUE(output.find("Array:") != std::string::npos);
	EXPECT_TRUE(output.find("42") != std::string::npos);
}

TEST_F(PrintArrayTest, LargeArray) {
	const int SIZE = 100;
	int largeArray[SIZE];
	for (int i = 0; i < SIZE; i++) {
		largeArray[i] = i * 10;
	}

	// Просто проверяем что не падает
	EXPECT_NO_THROW(captureOutput(largeArray, SIZE));
}

TEST_F(PrintArrayTest, MixedNumbers) {
	int mixedArray[] = { 0, -5, 10, -3, 7 };
	std::string output = captureOutput(mixedArray, 5);

	EXPECT_TRUE(output.find("Array:") != std::string::npos);
	EXPECT_TRUE(output.find("-5") != std::string::npos);
	EXPECT_TRUE(output.find("10") != std::string::npos);
	EXPECT_TRUE(output.find("-3") != std::string::npos);
	EXPECT_TRUE(output.find("7") != std::string::npos);
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
	std::streambuf* oldCinBuf;
	std::streambuf* oldCoutBuf;
};

TEST_F(InputNaturalTest, ValidInput_ReturnsCorrectValue) {
	simulateInput("5");

	int result;
	std::string output = captureOutput([&]() {
		inputNatural(result, 10);
		});

	EXPECT_EQ(result, 5);
	EXPECT_TRUE(output.empty());
}

TEST_F(InputNaturalTest, StringThenValidInput_HandlesError) {
	simulateInput("abc\n7");

	int result;
	std::string output = captureOutput([&]() {
		inputNatural(result, 10);
		});

	EXPECT_EQ(result, 7);
	EXPECT_TRUE(output.find("Invalid input") != std::string::npos);
}

TEST_F(InputNaturalTest, OutOfRangeThenValid_HandlesError) {
	simulateInput("15\n3");

	int result;
	std::string output = captureOutput([&]() {
		inputNatural(result, 10);
		});

	EXPECT_EQ(result, 3);
	EXPECT_TRUE(output.find("Invalid input") != std::string::npos);
}

TEST_F(InputNaturalTest, MaxBoundaryValue_Accepted) {
	simulateInput("10");

	int result;
	std::string output = captureOutput([&]() {
		inputNatural(result, 10);
		});

	EXPECT_EQ(result, 10);
	EXPECT_TRUE(output.empty());
}