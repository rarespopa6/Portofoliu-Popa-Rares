cmake_minimum_required(VERSION 3.27)
project(Lab4)

set(CMAKE_CXX_STANDARD 17)

add_executable(Lab4 main.cpp
        Domain/fruit.h
        Controller/fruit_controller.h
        Repository/fruit_repo.h
        UI/fruit_ui.h
        Domain/fruit.cpp
        Repository/fruit_repo.cpp
        Controller/fruit_controller.cpp
        UI/fruit_ui.cpp
        Tests/tests.h
        Tests/tests.cpp)
