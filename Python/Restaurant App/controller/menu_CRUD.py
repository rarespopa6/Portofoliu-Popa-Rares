import pandas


class Menu:

    def __init__(self):
        self.menu = pandas.read_csv("Menu.csv")

    # Zeigt das Menu
    def afisare_meniu(self):
        try:
            print(self.menu.to_string(index=False))
        except FileNotFoundError:
            print("Could not open file.")

    # Loscht ein Artikel aus dem Menu
    def delete_from_menu(self, index):
        try:
            self.menu = self.menu.drop(index)
            self.menu_update()
            print("Articol sters cu succes!")

        except KeyError:
            print("ID invalid.")

    # Returns eine Liste mit allen notwendigen Daten fur add_to_menu
    def add_to_menu_items(self):
        cod_meniu = int(input("Cod: "))
        nume_articol = input("Nume articol: ")
        pret = int(input("Pret: "))
        cantitate = "350g"
        timp_p = int(input("Timp de preparare: "))

        return [cod_meniu, nume_articol, pret, cantitate, timp_p]

    # Fugt ein Artikel in dem Menu ein
    def add_to_menu(self):
        try:
            item_list = self.add_to_menu_items()
            self.menu.loc[len(self.menu)] = item_list
            self.menu_update()
            print("Articol adaugat cu succes!")

        except FileNotFoundError:
            print("File not found")

    # Verandert ein Artikel aus dem Menu
    def modify_menu(self, row, attribute, change):
        valid_attribute = ["cod", "articol", "pret", "cantitate", "timp_p"]

        if attribute not in valid_attribute:
            print("Atribut invalid.")
            return 0

        if row not in self.menu.cod:
            print("ID rand invalid.")
            return 0

        if attribute == "cod" or attribute == "pret" or attribute == "timp_p":
            change = int(change)

        try:
            self.menu.loc[row, attribute] = change
            self.menu_update()
            print("Modificare efectuata cu succes!")

        except FileNotFoundError:
            print("File was not found.")

        except KeyError:
            print("Invalid Key.")

    # Aktualisieren des Menus
    def menu_update(self):
        self.menu.to_csv("Menu.csv", index=False)

    def afisare_meniu_comanda(self):
        return """
                Gestionare Meniu          Gestionare Comenzi          Gestionare Clienti          Inchidere        
                       1                          2                          3                        0
        """

    def actiune_meniu_comanda(self):
        return """
                Afisare Meniu   Adaugare in meniu  Stergere din meniu  Modificare Meniu  Inchidere
                      1                 2                  3                  4              0
        """

    def actiune_comenzi(self):
        return """
                Afisare comenzi  Adaugare comenzi  Stergere comanda  Inchidere
                      1                 2                  3             0
        """

