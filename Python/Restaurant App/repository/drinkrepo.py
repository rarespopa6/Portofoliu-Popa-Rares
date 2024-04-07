from modelle.getrank import Getrank
from repository.datarepo import DataRepo
import pandas


class DrinkRepo(DataRepo):

    def __init__(self, datei):
        DataRepo.__init__(self, datei)

    def convert_to_string(self):
        return " ".join(map(str, self.datei))

    def convert_from_string(self):
        return list(map(str, self.datei))


