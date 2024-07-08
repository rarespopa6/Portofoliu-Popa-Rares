#pragma once

#include <iostream>
#include <string>
#include "../Domain/fruit.h"
#include <list>

using namespace std;

class FruitRepo{
private:
    list<Fruit> fruit_list;
public:
    FruitRepo();

    void read_fruits_from_file(string filename);

    void write_fruits_to_file(string filename);

    void add(Fruit& fruit);

    void remove(Fruit& fruit);

    list<Fruit>& get_all();

    bool search(Fruit& fruit);
};