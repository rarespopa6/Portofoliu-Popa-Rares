cmake_minimum_required(VERSION 3.27)
project(Lab2)

set(CMAKE_CXX_STANDARD 14)

include_directories(.)

add_executable(Lab2
        App.cpp
        ExtendedTest.cpp
        ExtendedTest.h
        IteratedList.cpp
        IteratedList.h
        ListIterator.cpp
        ListIterator.h
        ShortTest.cpp
        ShortTest.h)
