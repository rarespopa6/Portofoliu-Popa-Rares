#include <iostream>
#include "Domain/fruit.h"
#include "Controller/fruit_controller.h"
#include "Repository/fruit_repo.h"
#include "UI/fruit_ui.h"
#include "Tests/tests.h"
#include <memory>

using namespace std;

int main() {
    run_tests();
    unique_ptr<FruitRepo> fruitRepo = make_unique<FruitRepo>();
    unique_ptr<FruitController> fruitController = make_unique<FruitController>(fruitRepo.get());
    shared_ptr<FruitUI> fruitUi = make_shared<FruitUI>(fruitController.get());

    fruitUi->run_app();
    return 0;
}