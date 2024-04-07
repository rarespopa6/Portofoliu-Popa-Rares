import json
from functools import reduce
from repository.cookeddishrepo import CookedDishRepo
import pandas
from modelle.kunde import Kunde
from controller.kunden_CRUD import Kundencontroller, ID
menu = pandas.read_csv("Menu.csv")

GETRANKE_ALKOHOL = {
    "Gin": 40,
    "Rom": 40,
    "Vodka": 40,
    "Cidru": 5,
    "Bere": 5,
    "Vin": 15,
    "Whiskey": 40,
    "Tuica": 40,
    "Palinca": 40
}


class Bestellungen:

    def __init__(self):
        self.data = {}

    def actiuni_comenzi(self):
        return """
            Afisare comenzi  Adaugare comenzi  Stergere comanda  Inchidere
                  1                 2                  3             0
        """

    def intrebare_client(self):
        return """
            Creare client nou  Client existent  Inchidere
                    1                2              0
        """

    # Returns alle Bestellungen
    def get_bestellungen(self):
        try:
            with open("bestellungen.json", 'r') as file:
                self.data = json.load(file)
                return self.data

        except FileNotFoundError:
            print("File not found")

    # Returns eine Liste mit IDs, die vom User eingegeben wurden
    def bestellung_liste(self):
        inp = input("Introduceti ID-urile comandate din meniu,separate prin cate o virgula: ")
        self.data = [int(id) for id in inp.strip().split(",")]
        return self.data

    # Loscht eine Bestellung
    def remove_bestellung(self, kunden_name):

        self.data = self.get_bestellungen()
        del self.data[kunden_name]

        with open("bestellungen.json", 'w') as file:
            json.dump(self.data, file, indent=4)

    # Verandert eine Bestellung
    def modificare_bestellung(self, name, lista_comandate_essen, lista_comandate_getranke, pret, timp):

        global menu
        nou_comandate = input("Introduceti articolele comandate, separate prin cate o virgula: ")

        pret_nou = int(pret)

        for art in nou_comandate.split(","):
            if art in GETRANKE_ALKOHOL:
                new_dict = {art: {
                    "cod": str(menu[menu.articol == art].iloc[0][0]),
                    "pret": str(menu[menu.articol == art].iloc[0][2]),
                    "cantitate": str(menu[menu.articol == art].iloc[0][3]),
                    "timp_p": str(menu[menu.articol == art].iloc[0][4]),
                    "alcool": str(GETRANKE_ALKOHOL[art])
                    }
                }
                lista_comandate_getranke.append(new_dict)
            else:
                cod = str(menu[menu.articol == art].iloc[0][0])
                pret = str(menu[menu.articol == art].iloc[0][2])
                cant = str(menu[menu.articol == art].iloc[0][3])
                timp_p = str(menu[menu.articol == art].iloc[0][4])
                new_dict = {art: {
                        "cod": cod,
                        "pret": pret,
                        "cantitate": cant,
                        "timp_p": timp_p
                    }
                }
                lista_comandate_essen.append(new_dict)

            pret_art = int(menu[menu.articol == art].iloc[0][2])
            timp_art = int(menu[menu.articol == art].iloc[0][4])

            if timp_art > timp:
                timp = menu[menu.articol == art].iloc[0][4]

            pret_nou = pret_nou + pret_art

        self.data = self.get_bestellungen()
        rechnung = str(pret_nou)

        self.data[name]["essen"] = lista_comandate_essen
        self.data[name]["getranke"] = lista_comandate_getranke
        self.data[name]["zubereitungszeit"] = str(timp)
        self.data[name]["plata"] = rechnung
        self.data.update()
        with open("bestellungen.json", 'w') as file:
            json.dump(self.data, file, indent=4)

    # Returns die Liste mit Essenartikeln aus der Bestellung
    def get_comanda_essen_existenta(self, name):

        self.data = self.get_bestellungen()
        return self.data[name]["essen"]

    # Returns die Liste mit Getrankeartikeln aus der Bestellung
    def get_comanda_getranke_existenta(self, name):

        self.data = self.get_bestellungen()
        return self.data[name]["getranke"]

    # Returns die Summe, die der Kunde bezahlen soll (die Rechnung)
    def get_pret(self, name):

        self.data = self.get_bestellungen()
        return self.data[name]["plata"]

    # Returns der Zubereitungszeit einer Bestellung
    def get_timp(self, name):

        self.data = self.get_bestellungen()
        return int(self.data[name]["zubereitungszeit"])

    # Returns der String mit der Rechnung einer Bestellung
    def nota_de_plata(self, lista_ids):

        lista_noua = []
        for id in lista_ids:
            preis = menu[menu.cod == id].iloc[0][2]
            lista_noua.append(preis)

        return str(reduce(lambda x, y: x+y, lista_noua))

    # Einfuhren einer neuer Bestellung
    def neue_bestellung(self):

        kunden_controller = Kundencontroller()

        name = input("Numele clientului: ").title()
        adresse = input("Adresa clientului: ").title()
        ora_comanda = input("Ora: ")
        kunde = Kunde(ID, name, adresse)

        kunden_controller.kunde_dict_to_file(kunde)

        comanda = input("Introduceti articolele comandate, separate prin cate o virgula: ")
        lista_comandate = [item for item in comanda.strip().split(",")]
        lista_comandate_int = []
        lista_comanda = []
        lista_getranke = []

        timp_preparare_max = 0

        for item in lista_comandate:
            cod = menu[menu.articol == item].iloc[0][0]
            pret = menu[menu.articol == item].iloc[0][2]
            portie = menu[menu.articol == item].iloc[0][3]
            timp_p = menu[menu.articol == item].iloc[0][4]
            nume_articol = menu[menu.articol == item].iloc[0][1]

            lista_comandate_int.append(cod)

            if int(timp_p) > timp_preparare_max:
                timp_preparare_max = int(timp_p)

            if cod >= 100:
                drinks_dict = {nume_articol: {
                            "cod": str(cod),
                            "pret": str(pret),
                            "portie": "350ml",
                            "timp_p": str(timp_p),
                            "alcool": str(GETRANKE_ALKOHOL[nume_articol])
                    }
                }

                lista_getranke.append(drinks_dict)

            else:
                new_dict = {nume_articol: {
                    "cod": str(cod),
                    "pret": str(pret),
                    "cantitate": str(portie),
                    "timp_p": str(timp_p)
                        }
                }

                lista_comanda.append(new_dict)

            rechnung = self.nota_de_plata(lista_comandate_int)

        new_data = {
            kunde.name: {
                "adresse": kunde.adresse,
                "essen": lista_comanda,
                "getranke": lista_getranke,
                "ora": str(ora_comanda),
                "zubereitungszeit": str(timp_preparare_max),
                "plata": rechnung
                }
            }

        # Speichert die Bestellung in file
        cd = CookedDishRepo(new_data)
        data = cd.read_file("bestellungen.json")
        data.update(new_data)
        cd.save("bestellungen.json")
        print("Comanda adaugata cu succes!")
