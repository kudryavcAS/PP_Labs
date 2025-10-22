
# Добавьте источник в исполняемый файл этого проекта.
add_executable (tests "test.cpp")

if (CMAKE_VERSION VERSION_GREATER 3.12)
  set_property(TARGET tests PROPERTY CXX_STANDARD 11)
endif()

# TODO: Добавьте тесты и целевые объекты, если это необходимо.
target_sources(tests PRIVATE ../OS_Lab3/main.cpp)
target_include_directories(tests PRIVATE ../OS_Lab3)

enable_testing()
add_test(Test_0, "${CMAKE_SOURCE_DIR}/bin/${PROJECT_NAME}" "0")