from modelle.gericht import Gericht


class GekochterGericht(Gericht):

    def __init__(self, preis, portions_grosse, zubereitungszeit):
        Gericht.__init__(self, preis, portions_grosse)
        self.zubereitungszeit = zubereitungszeit

    def __str__(self):
        return f"{self.id} {self.preis} {self.portionsgrosse} {self.zubereitungszeit}"

    def __repr__(self):
        return f"{self.id} {self.preis} {self.portionsgrosse} {self.zubereitungszeit}"

