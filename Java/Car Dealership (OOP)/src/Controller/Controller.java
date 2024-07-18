package Controller;

import Model.Car;
import Model.Motorcycle;
import Model.Vehicle;
import Repo.Repository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {
    private final Repository repository;

    public Controller(Repository repository){
        this.repository = repository;
    }

    public void addVehicle(String type, String license, String brand, String model, double price, int year, String color, int attr) {
        if (this.getVehicleByLicense(license) != null){
            throw new RuntimeException("A vehicle with license plate: " + license + " already exists.");
        }

        Vehicle v = null;
        if (type.equalsIgnoreCase("Car")){
            v = new Car(license, brand, model, price, year, color, attr);
        } else if (type.equalsIgnoreCase("Motorcycle")){
            v = new Motorcycle(license, brand, model, price, year, color, attr);
        } else {
            throw new IllegalArgumentException("Invalid vehicle type: " + type);
        }
        repository.addVehicle(v);
    }

    public void removeVehicle(String license){
        if (this.getVehicleByLicense(license) == null){
            throw new RuntimeException("There is no vehicle with that license plate.");
        }
        repository.removeVehicleByLicense(license);
    }

    public void printAllVehicles(){
        List<Vehicle> vehicles = this.getAllVehicles();
        for (Vehicle vehicle: vehicles){
            System.out.println(vehicle);
        }
    }

    public List<Vehicle> filterVehiclesByYear(int year) {
        return repository.getAllVehicles().stream()
                .filter(vehicle -> vehicle.getYear() == year)
                .collect(Collectors.toList());
    }

    public List<Vehicle> filterVehiclesByPriceRange(double minPrice, double maxPrice) {
        return repository.getAllVehicles().stream()
                .filter(vehicle -> vehicle.getPrice() >= minPrice && vehicle.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    public List<Vehicle> sortVehiclesByPriceAscending() {
        return repository.getAllVehicles().stream()
                .sorted((v1, v2) -> Double.compare(v1.getPrice(), v2.getPrice()))
                .collect(Collectors.toList());
    }

    public List<Vehicle> sortVehiclesByPriceDescending() {
        return repository.getAllVehicles().stream()
                .sorted((v1, v2) -> Double.compare(v2.getPrice(), v1.getPrice()))
                .collect(Collectors.toList());
    }

    public Vehicle getVehicleByLicense(String license) {
        return repository.getVehicleByLicense(license);
    }

    public void removeVehicleByLicense(String license) {
        repository.removeVehicleByLicense(license);
    }

    public List<Vehicle> getAllVehicles() {
        return repository.getAllVehicles();
    }
}