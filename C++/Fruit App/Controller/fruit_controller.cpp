#include <iostream>
#include <string>
#include "../Domain/fruit.h"
#include "../Repository/fruit_repo.h"
#include "fruit_controller.h"
#include <cstring>

using namespace std;

FruitController::FruitController(FruitRepo *fruitRepo) : fruitRepo(fruitRepo) {
}

void FruitController::add(std::string name, std::string type, int price, std::string expiry_date, int quantity) {
    Fruit new_fruit(name, type, price, expiry_date, quantity);
    this->fruitRepo->add(new_fruit);
}

void FruitController::remove(std::string name, std::string type) {
    Fruit fruit_to_remove(name, type);
    this->fruitRepo->remove(fruit_to_remove);
}

void FruitController::print(list<Fruit> fruitList) {
    for (const Fruit& fruit : fruitList)
        cout << "Name: " << fruit.get_name() << ", Type: " << fruit.get_type() << ", Price: " << fruit.get_price() << ", Expiry Date: " << fruit.get_expiry_date() << ", Quantity: " << fruit.get_quantity() << "\n";

}

list<Fruit> FruitController::find_type(std::string type) {
    if(type == "")
        return this->fruitRepo->get_all();
    list<Fruit> new_list;
    list<Fruit>& fruitList = this->fruitRepo->get_all();
    for (auto it = fruitList.begin(); it != fruitList.end(); ++it)
        if(it->get_type() == type){
            new_list.push_back(*it);
        }
    return new_list;
}

list<Fruit> FruitController::find_low_quantity(int low_quantity) {
    list<Fruit> new_list;
    list<Fruit>& fruitList = this->fruitRepo->get_all();
    for (auto it = fruitList.begin(); it != fruitList.end(); ++it)
        if(it->get_quantity() <= low_quantity){
            new_list.push_back(*it);
        }
    return new_list;
}

list<Fruit> FruitController::find_name(std::string name) {
    if (name == "'"){
        list<Fruit>& fruitList = this->fruitRepo->get_all();
        fruitList.sort();
        return fruitList;
    }

    list<Fruit> new_list;
    list<Fruit>& fruitList = this->fruitRepo->get_all();
    for (auto it = fruitList.begin(); it != fruitList.end(); ++it){
        string fruitName = it->get_name();
        if (fruitName.find(name) != std::string::npos) {
            new_list.push_back(*it);
        }
    }

    new_list.sort();
    return new_list;
}

bool FruitController::compareByExpirationDate(const Fruit &fruit1, const Fruit &fruit2) {
    string date1 = fruit1.get_expiry_date();
    int day1 = stoi(date1.substr(0, 2)); /// Day
    int month1 = stoi(date1.substr(3, 2)); /// Month
    int year1 = stoi(date1.substr(6, 4)); /// Year

    string date2 = fruit2.get_expiry_date();
    int day2 = stoi(date2.substr(0, 2)); /// Day
    int month2 = stoi(date2.substr(3, 2)); /// Month
    int year2 = stoi(date2.substr(6, 4)); /// Year

    return (year1 < year2) || (year1 == year2 && month1 < month2) || (year1 == year2 && month1 == month2 && day1 < day2);
}

list<Fruit> FruitController::sorted_expiry_date() {
    list<Fruit> fruitList = this->fruitRepo->get_all();

    fruitList.sort(FruitController::compareByExpirationDate);
    return fruitList;
}

void FruitController::save() {
    this->fruitRepo->write_fruits_to_file("fruits.txt");
}