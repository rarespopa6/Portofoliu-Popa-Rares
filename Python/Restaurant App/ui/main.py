from controller.menu_CRUD import Menu
from controller.bestellungen_CRUD import Bestellungen
from controller.kunden_CRUD import Kundencontroller
import pprint

# Wir bilden die Objekte der drei Controller(Menu, Bestellungen, Kunden)
menu = Menu()
bestellungen = Bestellungen()
kunden_controller = Kundencontroller()


def run():
    # Zeigt dem User welche Wahle er treffen kann
    print(menu.afisare_meniu_comanda())
    input_initial = int(input("Introduceti numarul comenzii dorite: "))

    # Menu
    if input_initial == 1:

        input_meniu = 99

        # Man wird ein while-loop benutzt, um mehrere Entscheidungen auf einmal treffen zu konnen
        while input_meniu:

            # Zeigt dem User welche Entscheidungen er treffen kann
            print(menu.actiune_meniu_comanda())
            input_meniu = int(input("Introduceti numarul comenzii dorite: "))

            # Der Menu wird angezeigt
            if input_meniu == 1:
                menu.afisare_meniu()

            # Etwas Neues ins Menu einfugen
            elif input_meniu == 2:
                menu.add_to_menu()

            # Etwas aus der Menu rausholen
            elif input_meniu == 3:
                # Der User muss der ID der Reihe eingeben
                id_sters = int(input("Introduceti ID-ul randului pe care vreti sa-l stergeti: "))
                menu.delete_from_menu(id_sters)

            # Etwas aus der Menu verandern
            elif input_meniu == 4:
                # 1. Der User muss der ID der Reihe eingeben
                # 2. Der User muss wahlen, was er verandern mochte: (cod, articol, pret, cantitate, timp_p)
                # 3. Er soll die Anderung eingeben
                id_rand = int(input("Introduceti ID-ul randului pe care vreti sa-l modificati: "))
                atribut_modificat = input("Alegeti atributul: (cod, articol, pret, cantitate, timp_p): ")
                modificare_efectuata = input("Modificare: ")

                menu.modify_menu(id_rand, atribut_modificat, modificare_efectuata)

            else:
                print("Program incheiat.")
                break

    # Bestellungen
    elif input_initial == 2:
        bestellungen_inp = 99

        # Man wird ein while-loop benutzt, um mehrere Entscheidungen auf einmal treffen zu konnen
        while bestellungen_inp:
            # Zeigt dem User welche Entscheidungen er treffen kann
            print(bestellungen.actiuni_comenzi())
            bestellungen_inp = int(input("Introduceti numarul comenzii dorite: "))

            # Zeigt alle Bestellungen aus der JSON File "bestellungen.json"
            if bestellungen_inp == 1:
                # pprint sieht schoner aus fur JSON Files
                pprint.pprint(bestellungen.get_bestellungen(), compact=True)

            # Einfugen/verandern einer Bestellung
            elif bestellungen_inp == 2:
                # Der User wird gefragt, ob er eine neue Bestellung einfugen mochte
                # oder ob er eine schon existierende verandern mochte
                print(bestellungen.intrebare_client())
                client_nou_sau_existent = int(input("Introduceti numarul comenzii dorite: "))

                # Neuer Kunde
                if client_nou_sau_existent == 1:
                    bestellungen.neue_bestellung()

                # Existierender Kunde -> verandern seiner Bestellung (mehrere Artikeln aus der Menu werden
                # hinzugefugt)
                elif client_nou_sau_existent == 2:

                    # Der User kann auch nur der partielle Name oder die partielle Adresse hinzufugen
                    nume_adr = input("Introduceti numele sau adresa clientului: ").title()
                    nume_cautat = kunden_controller.search_kunde_name_b(nume_adr)
                    pret = int(bestellungen.get_pret(nume_cautat))
                    if not nume_cautat:
                        nume_cautat = kunden_controller.search_kunde_adresse_b(nume_adr)
                        if not nume_cautat:
                            print("Clientul nu a fost gasit.")
                        else:
                            # Der User soll weitere Artikeln aus der Menu in der Kunde Bestellung hinzufugen
                            data_essen = bestellungen.get_comanda_essen_existenta(nume_cautat)
                            data_getrank = bestellungen.get_comanda_getranke_existenta(nume_cautat)
                            timp = bestellungen.get_timp(nume_cautat)
                            bestellungen.modificare_bestellung(nume_cautat, data_essen, data_getrank, pret, timp)
                            print(f"Modificare in comanda lui {nume_cautat} efectuata cu succes!")
                    else:
                        data_essen = bestellungen.get_comanda_essen_existenta(nume_cautat)
                        data_getrank = bestellungen.get_comanda_getranke_existenta(nume_cautat)
                        timp = bestellungen.get_timp(nume_cautat)
                        bestellungen.modificare_bestellung(nume_cautat, data_essen, data_getrank, pret, timp)
                        print(f"Modificare in comanda lui {nume_cautat} efectuata cu succes!")

            # Loschen einer Bestellung
            elif bestellungen_inp == 3:
                name = input("Introduceti numele clientului caruia doriti sa ii stergeti comanda: ").title()
                try:
                    bestellungen.remove_bestellung(name)
                    print(f"Comanda lui {name} a fost stearsa cu succes.")
                except KeyError:
                    print("Eroare. Introduceti un nume valid.")

            else:
                print("Program incheiat.")
                break

    # Kunden
    elif input_initial == 3:
        kunden_input = 99

        # Man wird ein while-loop benutzt, um mehrere Entscheidungen auf einmal treffen zu konnen
        while kunden_input:

            # Zeigt alle Bestellungen aus der JSON File bestellungen.json
            print(kunden_controller.afisare_gestionare_clienti())
            kunden_input = int(input("Introduceti numarul comenzii dorite: "))

            # Zeigt alle Kunden als Pandas DataFrame
            if kunden_input == 1:
                print(kunden_controller.afisare_clienti())

            # Suchen einer Kunde nach Name oder Adresse
            elif kunden_input == 2:
                nume_adr = input("Introduceti numele sau adresa clientului: ").title()
                a = kunden_controller.search_kunde_name(nume_adr)
                if a == 0:
                    b = kunden_controller.search_kunde_adresse(nume_adr)
                    if b == 0:
                        print("Clientul nu a fost gasit.")

            # Ein Kunde andern
            elif kunden_input == 3:
                # 1. Der User muss der vollstandige Name der Kunde eingeben
                # 2. Der User muss zuerst die Anderung eingeben
                # 3. Er soll wahlen, welche die Anderung ist (id, adresa)
                nume = input("Introduceti numele clientului caruia doriti sa ii faceti o modificare: ").title()
                modificare = input("Modificare: ")
                id_sau_adr = input("Ce modificare doriti sa faceti (id, adresa): ")
                status_modificare = kunden_controller.modifica_client(nume, modificare, id_sau_adr)
                if status_modificare == 0:
                    print("Eroare. Introduceti un input valid.")
                else:
                    print("Modificare efectuata cu succes!")

            # Einfugen einer neuen Kunde und diesen in der JSON File "kunden.json" speichern
            elif kunden_input == 4:
                kunde = kunden_controller.create_kunde()
                kunden_controller.kunde_dict_to_file(kunde)

            # Loschen einer Kunde
            elif kunden_input == 5:
                # Der User soll der vollstandige Name der Kunde eingeben
                name = input("Introduceti numele clientului pe care doriti sa-l stergeti: ").title()

                try:
                    kunden_controller.sterge_client(name)
                except KeyError:
                    print("Eroare. Introduceti un nume valid.")

            else:
                print("Program incheiat.")
                break
    else:
        # Der Programm endet
        print("Program incheiat.")
