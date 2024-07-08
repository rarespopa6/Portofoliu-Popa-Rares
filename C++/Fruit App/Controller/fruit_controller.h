#pragma once

#include <iostream>
#include <string>
#include "../Domain/fruit.h"
#include "../Repository/fruit_repo.h"
#include <list>
#include <memory>

using namespace std;

class FruitController{
private:
    FruitRepo *fruitRepo;
public:
    FruitController(FruitRepo *fruitRepo);

    void add(string name, string type, int price, string expiry_date, int quantity);

    void remove(string name, string origin);

    void print(list<Fruit> fruitList);

    list<Fruit> find_type(string type);

    list<Fruit> find_low_quantity(int low_quantity);

    list<Fruit> find_name(string name);

    static bool compareByExpirationDate(const Fruit& fruit1, const Fruit& fruit2);

    void sortFruitsByExpirationDate(list<Fruit>& fruitList);

    list<Fruit> sorted_expiry_date();

    void save();
};
