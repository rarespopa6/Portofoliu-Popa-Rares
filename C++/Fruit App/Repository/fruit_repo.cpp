#include <iostream>
#include <string>
#include "../Domain/fruit.h"
#include "fruit_repo.h"
#include <fstream>
#include <list>

using namespace std;

FruitRepo::FruitRepo(){
    this->fruit_list = {};
    this->read_fruits_from_file("fruits.txt");
}

void FruitRepo::read_fruits_from_file(std::string filename) {
    ifstream fin(filename);
    string name, type, expiry_date;
    int price, quantity;

    while(fin >> name >> type >> price >> expiry_date >> quantity){
        Fruit fruit(name, type, price, expiry_date, quantity);
        this->fruit_list.push_back(fruit);
    }
}

void FruitRepo::write_fruits_to_file(std::string filename) {
    ofstream fout(filename);

    for (auto it = this->fruit_list.begin(); it != this->fruit_list.end(); ++it)
        fout << it->get_name() << " " << it->get_type() << " " << it->get_price() << " " << it->get_expiry_date() << " " << it->get_quantity() << "\n";
}

void FruitRepo::add(Fruit& fruit) {
    /// Zuerst wird man uberpruft, ob dieses Fruit schon in der Liste existiert.
    for (auto it = this->fruit_list.begin(); it != this->fruit_list.end(); ++it)
            if(*it == fruit){
                it->set_quantity(fruit.get_quantity());
                return;
        }
        this->fruit_list.push_back(fruit);
    }

void FruitRepo::remove(Fruit& fruit) {
    if (!this->search(fruit)){
        cout << "The Fruit doesn't exist in the list.\n";
        return;
    }

    for (auto it =  this->fruit_list.begin(); it !=  this->fruit_list.end(); ++it) {
        if (*it == fruit) {
            this->fruit_list.erase(it);
            cout << "Fruit " << it->get_name() << " was successfully deleted.\n";
            return;
        }
    }
}

list<Fruit>& FruitRepo::get_all() {
    return this->fruit_list;
}

bool FruitRepo::search(Fruit &fruit) {
    for (auto it =  this->fruit_list.begin(); it !=  this->fruit_list.end(); ++it)
        if (*it == fruit)
            return true;
    return false;
}