#include<gtest/gtest.h>
#include "ArrayFunctions.h"

extern DWORD WINAPI searchMinMaxElement(LPVOID lpData);
extern DWORD WINAPI searchAverage(LPVOID lpData);

TEST(ThreadFunctions, MinMaxFindsCorrectValues) {
   std::vector<int> arr = { 5, 2, 8, 1, 9, 3 };
   ArrayData data(&arr, 0, 0, 0.0);

   EXPECT_EQ(searchMinMaxElement(&data), 0);
   EXPECT_EQ(data.minElement, 1);
   EXPECT_EQ(data.maxElement, 9);
}