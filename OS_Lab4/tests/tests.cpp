#include <gtest/gtest.h>
#include "receiver.h"
#include "sender.h"

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

TEST(FindSenderPathTest, ReturnsCorrectPath) {
	EXPECT_EQ("Sender.exe", findSenderPath());
}