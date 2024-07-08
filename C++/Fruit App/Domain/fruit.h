#pragma once

#include <iostream>
#include <string>

using namespace std;

class Fruit{
private:
    string name;
    string type;
    int price;
    string expiry_date;
    int quantity;

public:
    Fruit(string name, string type, int price, string expiry_date, int quantity);
    Fruit(string name, string type);
    string get_name() const;
    string get_type() const;
    int get_price() const;
    void set_price(int new_price);
    string get_expiry_date() const;
    int get_quantity() const;
    void set_quantity(int new_quantity);
    bool operator==(const Fruit& other) const;
    bool operator<(const Fruit& other) const;
};
