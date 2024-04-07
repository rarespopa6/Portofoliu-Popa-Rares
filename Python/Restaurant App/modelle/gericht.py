from modelle.identifizierbar import Identifizierbar


class Gericht(Identifizierbar):

    def __init__(self, id, preis, portions_grosse='350g'):
        Identifizierbar.__init__(self, id)
        self.preis = preis
        self.portionsgrosse = portions_grosse

