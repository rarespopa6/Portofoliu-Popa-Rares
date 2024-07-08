cmake_minimum_required(VERSION 3.27)
project(Lab_5_SortedMap)

set(CMAKE_CXX_STANDARD 14)

include_directories(.)

add_executable(Lab_5_SortedMap
        App.cpp
        ExtendedTest.cpp
        ExtendedTest.h
        ShortTest.cpp
        ShortTest.h
        SMIterator.cpp
        SMIterator.h
        SortedMap.cpp
        SortedMap.h)
