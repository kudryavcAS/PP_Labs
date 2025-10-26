# Включаем Google Test
include(FetchContent)
FetchContent_Declare(
    googletest
    URL https://github.com/google/googletest/archive/03597a01ee50ed33e9dfd640b249b4be3799d395.zip
)
set(gtest_force_shared_crt ON CACHE BOOL "" FORCE)
FetchContent_MakeAvailable(googletest)

# Создаем тестовый исполняемый файл
add_executable(tests test.cpp)

# Подключаем исходники с функциями
target_sources(tests PRIVATE ../OS_Lab3/winapi.cpp)

# Настройки компиляции
set_property(TARGET tests PROPERTY CXX_STANDARD 11)
target_include_directories(tests PRIVATE ../OS_Lab3)

# Линкуем с Google Test
target_link_libraries(tests PRIVATE gtest_main)

# Добавляем тест
enable_testing()
add_test(NAME tests COMMAND tests)