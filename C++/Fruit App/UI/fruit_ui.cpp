#include <iostream>
#include <string>
#include "../Controller/fruit_controller.h"
#include "fruit_ui.h"

using namespace std;

FruitUI::FruitUI(FruitController *fruitController) : fruitController(fruitController) {
}

void FruitUI::run_app() {
    int initial_option = 1;
    cout << "Welcome back!" << "\n";

    while(initial_option) {
        cout << "Options:" << "\n";
        cout << "Manage Fruits    " << "  Find Fruits  " << "         Close" <<"\n";
        cout << "      1                 2                 0 " << "\n";
        cout << "Input: ";
        cin >> initial_option;

        if (initial_option == 1) {
            int option1 = 1;

            while(option1) {
                cout << "See all Fruits    " << "  Add Fruit  " << "         Delete Fruit" << "         Back""\n";
                cout << "      1                  2                   3                 0 " << "\n";
                cout << "Input: ";
                cin >> option1;

                if(option1 == 1) {
                    list<Fruit> fruits = this->fruitController->find_type("");
                    cout << "List of Fruits:\n";
                    this->fruitController->print(fruits);
                }

                else if(option1 == 2){
                    string name, type, expiry_date;
                    int price, quantity;
                    cout << "You have to enter the details of the fruit.\n";
                    cout << "Name: ", cin >> name;
                    cout << "Type: ", cin >> type;
                    cout << "Price: ", cin >> price;
                    cout << "Expiry Date(DD.MM.YYYY): ", cin >> expiry_date;
                    cout << "Quantity: ", cin >> quantity;

                    this->fruitController->add(name, type, price, expiry_date, quantity);
                    cout << "Fruit was added successfully!\n\n";
                }

                else if(option1 == 3){
                    string name, type;
                    cout << "Enter the name and the type of the Fruit you want to delete" << "\n";
                    cout << "Name: ", cin >> name;
                    cout << "Type: ", cin >> type;
                    this->fruitController->remove(name, type);
                }
            }

        } else if (initial_option == 2) {
           int option2 = 1;
           while(option2){
               cout << "Find by type    " << "  Find by low Quantity  " << "         Find by Name" << "         Sort by expiry date               Back""\n";
               cout << "      1                    2                           3                       4                           0 " << "\n";
               cout << "Input: ";
               cin >> option2;

               if(option2 == 1){
                   string type;
                   cout << "Enter the Type you want to search by: ", cin >> type;
                   list<Fruit> fruits_t = this->fruitController->find_type(type);
                   this->fruitController->print(fruits_t);
               }
               else if(option2 == 2){
                   int low_quantity;
                   cout << "Enter the Quantity: ", cin >> low_quantity;
                   list<Fruit> fruits_q = this->fruitController->find_low_quantity(low_quantity);
                   this->fruitController->print(fruits_q);
                }
               else if(option2 == 3){
                    string partial_name;
                    cout << "Enter the name: ", cin >> partial_name;
                    list<Fruit> fruits_n = this->fruitController->find_name(partial_name);
                    if (!fruits_n.size())
                        cout << "There are no Fruits that contain '" << partial_name << "' in their name.\n";
                    else
                        this->fruitController->print(fruits_n);
               }
               else if(option2 == 4){
                   list<Fruit> fruits_e = this->fruitController->sorted_expiry_date();
                   this->fruitController->print(fruits_e);
               }
           }
        }
        else{
            this->fruitController->save();
            cout << "App closed.";
        }
    }
}