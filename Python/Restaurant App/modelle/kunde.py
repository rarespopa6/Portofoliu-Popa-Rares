from modelle.identifizierbar import Identifizierbar


class Kunde(Identifizierbar):

    def __init__(self, id, name, adresse):
        Identifizierbar.__init__(self, id)
        self.name = name
        self.adresse = adresse
