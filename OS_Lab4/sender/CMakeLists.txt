# CMakeList.txt: проект CMake для OS_Lab4; включите исходный код и определения,
# укажите здесь логику для конкретного проекта.
#

# Добавьте источник в исполняемый файл этого проекта.
add_executable (sender "sender.cpp" "sender.h")

if (CMAKE_VERSION VERSION_GREATER 3.12)
  set_property(TARGET sender PROPERTY CXX_STANDARD 98)
endif()

if(WIN32)
    target_link_libraries(sender ${SYSTEM_LIBS})
endif()

# Устанавливаем выходной каталог для ВСЕХ исполняемых файлов
set_target_properties(sender PROPERTIES
    RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}
)
# TODO: Добавьте тесты и целевые объекты, если это необходимо.
