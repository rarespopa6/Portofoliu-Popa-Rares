import json
from abc import abstractmethod


class DataRepo:

    def __init__(self, datei):
        self.datei = datei

    @abstractmethod
    def save(self, filename):
        with open(filename, 'w') as file:
            json.dump(self.datei, file, indent=4)

    @abstractmethod
    def load(self, filename):
        try:
            with open(filename, 'r') as file:
                json.load(file)
                # Da load unui file
        except FileNotFoundError:
            print("Could not open file.")

    @abstractmethod
    def read_file(self, filename):
        try:
            with open(filename, 'r') as file:
                self.datei = json.load(file)
                return self.datei
        except FileNotFoundError:
            print("Could not open file.")

    @abstractmethod
    def write(self, filename):
        with open(filename, 'w') as file:
            file.write(self.datei)
        # Scrie un string

    def convert_to_string(self):
        pass

    def convert_from_string(self):
        pass
