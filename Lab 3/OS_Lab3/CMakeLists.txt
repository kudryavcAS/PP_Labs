# CMakeList.txt: проект CMake для OS_Lab3; включите исходный код и определения,
# укажите здесь логику для конкретного проекта.
#

# Добавьте источник в исполняемый файл этого проекта.
add_executable (OS_Lab3 "main.cpp" "winapi.h" "winapi.cpp")

if (CMAKE_VERSION VERSION_GREATER 3.12)
  set_property(TARGET OS_Lab3 PROPERTY CXX_STANDARD 11)
endif()

# TODO: Добавьте тесты и целевые объекты, если это необходимо.
#t arget_include_directories(OS_Lab3 PRIVATE ${CMAKE_SOURCE_DIR}/lib)