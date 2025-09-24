add_executable (tests "tests.cpp")


if (CMAKE_VERSION VERSION_GREATER 3.12)
  set_property(TARGET tests PROPERTY CXX_STANDARD 20)
endif()

enable_testing()


target_link_libraries(tests GTest::gtest_main)

include(GoogleTest)
gtest_discover_tests(tests)
if(WIN32)
    target_link_libraries(tests Kernel32.lib)
endif()