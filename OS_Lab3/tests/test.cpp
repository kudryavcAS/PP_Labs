#include "main.h"

#include <sstream>
#include <cassert>

CRITICAL_SECTION arrayCS;

void test_inputNatural_ValidInput() {
   std::istringstream input("5");
   std::cin.rdbuf(input.rdbuf());

   int result;
   inputNatural(result, 10);

   assert(result == 5);
   std::cout << "test_inputNatural_ValidInput: PASSED\n";
}