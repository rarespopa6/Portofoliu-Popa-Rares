import functools
from modelle.identifizierbar import Identifizierbar


class Bestellung(Identifizierbar):

    def __init__(self, kunde_id, gerichte_id, getranke_id, gesamtkosten):
        Identifizierbar.__init__(self, kunde_id)
        self.gerichte_id = gerichte_id
        self.getranke_id = getranke_id
        self.gesamtkosten = gesamtkosten
