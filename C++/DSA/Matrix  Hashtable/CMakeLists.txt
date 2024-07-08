cmake_minimum_required(VERSION 3.27)
project(Lab_4_Matrix)

set(CMAKE_CXX_STANDARD 14)

include_directories(.)

add_executable(Lab_4_Matrix
        App.cpp
        ExtendedTest.cpp
        ExtendedTest.h
        Matrix.cpp
        Matrix.h
        ShortTest.cpp
        ShortTest.h
)
