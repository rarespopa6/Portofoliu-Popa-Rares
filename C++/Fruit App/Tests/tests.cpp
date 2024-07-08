#include <iostream>
#include "tests.h"
#include "../Domain/fruit.h"
#include "../Controller/fruit_controller.h"
#include "../Repository/fruit_repo.h"
#include <string>
#include <list>
#include <memory>
#include <assert.h>

using namespace std;

void run_tests(){
    unique_ptr<FruitRepo> fr = make_unique<FruitRepo>();
    unique_ptr<FruitController> fc = make_unique<FruitController>(fr.get());

    int count = fr->get_all().size();
    assert(fr->get_all().size() == count);
    Fruit f1("Banana", "YHAA5", 12, "12.05.2024", 50);
    fr->add(f1);
    Fruit f2("Grapes", "FASA5", 15, "22.05.2024", 80);
    fc->add("Melon", "FASAF5", 17, "19.05.2024", 90);
    assert(fr->get_all().size() == count+2);
    fr->add(f2);
    assert(fr->search(f1) == true);
    assert(fr->search(f2) == true);
    Fruit f3("Grapes", "FFFFF", 15, "22.05.2024", 80);
    assert(fr->search(f3) == false);
}