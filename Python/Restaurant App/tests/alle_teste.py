from ui.main import *
from repository.cookeddishrepo import CookedDishRepo


def test_neue_bestellung():
    # Sucht die Kunden
    data = bestellungen.get_bestellungen()
    assert ("Johann" in data) == True
    assert ("Denis" in data) == True
    assert ("Fred" in data) == False
    assert ("Lukas" in data) == True
    assert ("Henrich" in data) == False


def test_search_kunde_name_b():
    # Die Funktion sucht der partielle Name der Kunden und gibt zuruck der vollstandige Name
    assert kunden_controller.search_kunde_name_b("ja") == 0
    assert kunden_controller.search_kunde_name_b("Den") == "Denis"
    assert kunden_controller.search_kunde_name_b("Jo") == "Johann"
    assert kunden_controller.search_kunde_name_b("alfr") == 0


def test_search_kunde_adresse_b():
    # Die Funktion sucht die partielle Adresse und gibt zuruck der Name der Kunde, der dort wohnt
    assert kunden_controller.search_kunde_adresse_b("Her") == "Jack"
    assert kunden_controller.search_kunde_adresse_b("Zentra") == "Lukas"
    assert kunden_controller.search_kunde_adresse_b("Frankf") == "Hofmann"
    assert kunden_controller.search_kunde_adresse_b("Af") == 0
    assert kunden_controller.search_kunde_adresse_b("Jaso") == 0


def test_modifica_client():
    status1 = kunden_controller.modifica_client("Denis", "Hoff 2", "adresa")
    assert status1 == 1

    status2 = kunden_controller.modifica_client("Hofmann", 34, "id")
    assert status2 == 1


def test_nota_de_plata():
    # Die Funktion bekommt als Parameter eine Liste mit der ID-s aus der Menu und berechnet die Rechnung
    # Beispiel:
    # ID: 0 - Schnitzel, Preis: 25
    # ID: 2 - Ton, Preis: 50
    # ID: 105 - Gin, Preis: 25 => Rechnung = 100 und wird als String zuruckgegeben

    assert(bestellungen.nota_de_plata([0, 2, 105])) == "100"
    assert(bestellungen.nota_de_plata([0, 3, 107, 105, 1])) == "154"
    assert(bestellungen.nota_de_plata([0, 0, 0])) == "75"
    assert(bestellungen.nota_de_plata([4, 0, 0, 1, 105])) == "122"
    assert(bestellungen.nota_de_plata([106])) == "20"


def test_convert_to_string():
    # ID-s aus der Menu
    cd = CookedDishRepo([1, 105, 108, 0, 5])
    assert cd.convert_to_string() == "1 105 108 0 5"


def test_convert_from_string():
    # ID-s aus der Menu
    cd = CookedDishRepo("4 105 107 2 105 7")
    assert cd.convert_from_string() == ['4', '105', '107', '2', '105', '7']


# Die Methode, die alle Teste ruft
def run_tests():
    test_neue_bestellung()
    test_search_kunde_name_b()
    test_search_kunde_adresse_b()
    test_nota_de_plata()
    test_modifica_client()
    test_convert_to_string()
    test_convert_from_string()
