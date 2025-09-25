#include<gtest/gtest.h>
#include "ArrayFunctions.h"

TEST(MinMaxSearch, SimpleArrayCase) {
	std::vector<int> arr = { 5, 2, 8, 1, 9, 3 };
	ArrayData data(&arr, 0, 0, 0);

	EXPECT_EQ(searchMinMaxElement(&data), 0);
	EXPECT_EQ(data.minElement, 1);
	EXPECT_EQ(data.maxElement, 9);
}

TEST(MinMaxSearch, SimilarNumbersCase) {
	std::vector<int> arr = { 2, 2, 2, 2, 2, 2 };
	ArrayData data(&arr, 0, 0, 0);

	EXPECT_EQ(searchMinMaxElement(&data), 0);
	EXPECT_EQ(data.minElement, 2);
	EXPECT_EQ(data.maxElement, 2);
}

TEST(MinMaxSearch, NegativeNumbersCase) {
	std::vector<int> arr = { -2, -3, -4, -5, -10, -1900 };
	ArrayData data(&arr, 0, 0, 0);

	EXPECT_EQ(searchMinMaxElement(&data), 0);
	EXPECT_EQ(data.minElement, -1900);
	EXPECT_EQ(data.maxElement, -2);
}

TEST(MinMaxSearch, SingleNumberCase) {
	std::vector<int> arr = { 5 };
	ArrayData data(&arr, 0, 0, 0);

	EXPECT_EQ(searchMinMaxElement(&data), 0);
	EXPECT_EQ(data.minElement, 5);
	EXPECT_EQ(data.maxElement, 5);
}

TEST(MinMaxSearch, EmptyArrayCase) {
	std::vector<int> arr = {};
	ArrayData data(&arr, 0, 0, 0);

	EXPECT_EQ(searchMinMaxElement(&data), 0);
}

TEST(AverageSearh, EmptyArrayCase) {
	std::vector<int> arr = {};
	ArrayData data(&arr, 0, 0, 0);

	EXPECT_EQ(searchAverage(&data), 0);
}

TEST(AverageSearh, SimpleArrayCase) {
	std::vector<int> arr = { 5, 2, 8, 1, 9, 3 };
	ArrayData data(&arr, 0, 0, 0);

	EXPECT_EQ(searchAverage(&data), 0);
	EXPECT_EQ(data.average, 5);
}

TEST(AverageSearh, SimilarNumbersCase) {
	std::vector<int> arr = { 5, 5, 5, 5, 5, 5 };
	ArrayData data(&arr, 0, 0, 0);

	EXPECT_EQ(searchAverage(&data), 0);
	EXPECT_EQ(data.average, 5);
}

TEST(AverageSearh, NegativeNumbersCase) {
	std::vector<int> arr = { -2, -3, -4, -5, -10, -13 };
	ArrayData data(&arr, 0, 0, 0);

	EXPECT_EQ(searchAverage(&data), 0);
	EXPECT_EQ(data.average, -6);
}

TEST(AverageSearh, SingleNumberCase) {
	std::vector<int> arr = { 6 };
	ArrayData data(&arr, 0, 0, 0);

	EXPECT_EQ(searchAverage(&data), 0);
	EXPECT_EQ(data.average, 6);
}

TEST(AverageSearh, HugeNumbers) {
	std::vector<int> arr = { 1000000000, 1500000000, 2000000000};
	ArrayData data(&arr, 0, 0, 0);

	EXPECT_EQ(searchAverage(&data), 0);
	EXPECT_EQ(data.average, 1500000000);
}
