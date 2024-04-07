import json
from json import JSONDecodeError
import pandas
import random
from modelle.kunde import Kunde
ID = random.randint(0, 99)


class Kundencontroller:

    def __init__(self):
        self.data = {}

    # Erzeugt ein neuer Kunde
    def create_kunde(self):

        global ID

        name = input("Numele clientului: ")
        adresse = input("Adresa clientului: ")
        kunde = Kunde(ID, name, adresse)

        return kunde

    # Speichert ein Kunde in der JSON File "kunden.json"
    def kunde_dict_to_file(self, kunde):

        kunde_dict = {
            kunde.name: {
                "id": kunde.id,
                "adresse": kunde.adresse
            }
        }

        try:
            with open("kunden.json", 'r') as file:
                self.data = json.load(file)

        except JSONDecodeError:
            with open("kunden.json", 'w') as file:
                json.dump(kunde_dict, file, indent=4)
                print(f"Clientul {kunde.name} a fost adaugat cu succes.")
        else:
            self.data.update(kunde_dict)
            with open("kunden.json", 'w') as file:
                json.dump(self.data, file, indent=4)
                print(f"Clientul {kunde.name} cu ID-ul {kunde.id} a fost adaugat cu succes.")

    # Returns alle Kunden als Pandas DataFrame
    def get_clienti_DF(self):

        try:
            with open("kunden.json", 'r') as file:
                self.data = json.load(file)
                return pandas.DataFrame(self.data)

        except FileNotFoundError:
            print("File not found.")
            return 0

    # Returns alle Kunden
    def get_clienti(self):
        try:
            with open("kunden.json", 'r') as file:
                self.data = json.load(file)
                return self.data

        except FileNotFoundError:
            print("File not found.")
            return 0

    # Verandert ein Kunde
    def modifica_client(self, nume, modificare_efectuata, modificare):

        self.data = self.get_clienti()
        if nume not in self.data:
            return 0

        valid_inputs = ["id", "adresa"]

        if modificare not in valid_inputs:
            return 0
        else:

            try:
                if modificare == 'id':
                    self.data[nume]["id"] = modificare_efectuata
                elif modificare == "adresa":
                    self.data[nume]["adresse"] = modificare_efectuata

                self.data.update()

                with open("kunden.json", 'w') as file:
                    json.dump(self.data, file, indent=4)

                return 1
            except KeyError:
                print("Eroare.")
                return 0

    # Loscht ein Kunde
    def sterge_client(self, nume: str):

        self.data = self.get_clienti()
        del self.data[nume]
        self.data.update()

        with open("kunden.json", 'w') as file:
            json.dump(self.data, file, indent=4)
            print(f"Clientul {nume} a fost sters din lista clientilor.")

    # Returns alle Kunden als Pandas DataFrame
    def afisare_clienti(self):

        try:
            with open("kunden.json", 'r') as file:
                self.data = json.load(file)
                return pandas.DataFrame(self.data)

        except FileNotFoundError:
            print("File not found.")
            return 0

    # Sucht ein Kunde nach der Name
    def search_kunde_name(self, kunde_name):

        self.data = self.afisare_clienti()
        if kunde_name not in self.data:
            kunde_name = self.partial_search_name(kunde_name)
            if not kunde_name:
                return 0
            else:
                print(f"Name: {kunde_name}\nID: {self.data[kunde_name].id}\nAdresse: {self.data[kunde_name].adresse}")
        else:
            print(f"Name: {kunde_name}\nID: {self.data[kunde_name].id}\nAdresse: {self.data[kunde_name].adresse}")

    # Sucht ein Kunde nach der Adresse
    def search_kunde_adresse(self, kunde_adresse):

        self.data = self.afisare_clienti()
        if kunde_adresse not in self.data:
            kunde_adresse = self.partial_search_adresse(kunde_adresse)
            if not kunde_adresse:
                return 0
            else:
                for name in self.data:
                    if self.data[name].adresse == kunde_adresse:
                        na = name
                        break
                print(f"Name: {na}\nID: {self.data[na].id}\nAdresse: {kunde_adresse}")

    # Sucht ein Kunde nach der Name - partial search
    def partial_search_name(self, pname):

        self.data = self.afisare_clienti()
        for name in self.data:
            if pname == name[0:len(pname)]:
                return name
        return 0

    # Sucht ein Kunde nach der Adresse - partial search
    def partial_search_adresse(self, padr):

        self.data = self.afisare_clienti()
        for adresse in self.data.loc["adresse"]:

            if padr == adresse[0:len(padr)]:
                return adresse

        return 0

    # Sucht ein Kunde nach der Name und gebt es zuruch, wenn dieser gefunden wurde, andernfalls 0
    def search_kunde_name_b(self, kunde_name):

        self.data = self.afisare_clienti()
        if kunde_name not in self.data:
            kunde_name = self.partial_search_name(kunde_name)
            if not kunde_name:
                return 0
            else:
                return kunde_name
        else:
            return kunde_name

    # Sucht ein Kunde nach der Adresse und gebt es zuruch, wenn dieser gefunden wurde, andernfalls 0
    def search_kunde_adresse_b(self, kunde_adresse):

        self.data = self.afisare_clienti()
        if kunde_adresse not in self.data:
            kunde_adresse = self.partial_search_adresse(kunde_adresse)
            if not kunde_adresse:
                return 0
            else:
                for name in self.data:
                    if self.data[name].adresse == kunde_adresse:
                        return name

    def afisare_gestionare_clienti(self):
        return """
                Afisare clienti  Cautare clienti  Modificare client  Creare client nou  Sterge client  Inchidere
                       1               2                  3                  4                5            0
        """
