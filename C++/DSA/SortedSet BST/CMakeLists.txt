cmake_minimum_required(VERSION 3.22)
project(SortedSet_L5)

set(CMAKE_CXX_STANDARD 14)

include_directories(.)

add_executable(SortedSet_L5
        App.cpp
        ExtendedTest.cpp
        ExtendedTest.h
        ShortTest.cpp
        ShortTest.h
        SortedSet.cpp
        SortedSet.h
        SortedSetIterator.cpp
        SortedSetIterator.h)
