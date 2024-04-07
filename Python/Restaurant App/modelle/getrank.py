from modelle.gericht import Gericht


class Getrank(Gericht):

    def __init__(self, id, preis, portions_grosse, alkohol):
        Gericht.__init__(id, preis, portions_grosse)
        self.alkohol = alkohol
        

