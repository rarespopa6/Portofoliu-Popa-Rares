#pragma once

#include <iostream>
#include <string>
#include "../Controller/fruit_controller.h"

using namespace std;

class FruitUI{
private:
    FruitController *fruitController;

public:
    FruitUI(FruitController *fruitController);

    void run_app();
};