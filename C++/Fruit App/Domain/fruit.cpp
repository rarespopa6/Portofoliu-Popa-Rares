#include <iostream>
#include <string>
#include "fruit.h"

using namespace std;

Fruit::Fruit(std::string name, std::string type, int price, std::string expiry_date, int quantity) {
    this->name = name;
    this->type = type;
    this->price = price;
    this->expiry_date = expiry_date;
    this->quantity = quantity;
}

Fruit::Fruit(std::string name, std::string type) {
    this->name = name;
    this->type = type;
    this->price = 0;
    this->expiry_date = "0";
    this->quantity = 0;
}

string Fruit::get_name() const {
    return this->name;
}

string Fruit::get_type() const {
    return this->type;
}

int Fruit::get_price() const {
    return this->price;
}

void Fruit::set_price(int new_price) {
    this->price = new_price;
}

string Fruit::get_expiry_date() const {
    return this->expiry_date;
}

int Fruit::get_quantity() const{
    return this->quantity;
}

bool Fruit::operator==(const Fruit &other) const {
    return this->name == other.name && this->type == other.type;
}

void Fruit::set_quantity(int new_quantity) {
    this->quantity = new_quantity;
}

bool Fruit::operator<(const Fruit &other) const {
    return this->name < other.name;
}