cmake_minimum_required(VERSION 3.10)
project("Lab 5")

set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

include_directories(Common)

add_executable(Server Server/main.cpp Server/ServerFunctions.cpp)

add_executable(Client Client/main.cpp Client/ClientFunctions.cpp)

if(WIN32)
    target_link_libraries(Server kernel32 user32)
    target_link_libraries(Client kernel32 user32)
endif()