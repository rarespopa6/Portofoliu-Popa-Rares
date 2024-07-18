package Repo;

import Model.Car;
import Model.Motorcycle;
import Model.Vehicle;

import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Repository {
    private List<Vehicle> vehicles;
    private final String fileName;

    public Repository(String fileName) {
        this.fileName = fileName;
        vehicles = new ArrayList<>();
        readFile();
    }

    private void readFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Vehicle vehicle = convertToVehicle(line);
                if (vehicle != null) {
                    vehicles.add(vehicle);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Vehicle convertToVehicle(String line) {
        String[] parts = line.split(",");
        if (parts.length < 8) {
            System.err.println("Invalid line " + line);
            return null;
        }

        try {
            if (parts[0].equals("Car")) {
                if (parts.length < 8) {
                    System.err.println("Invalid line" + line);
                    return null;
                }
                return new Car(
                        parts[1].trim(), // license
                        parts[2].trim(), // brand
                        parts[3].trim(), // model
                        Double.parseDouble(parts[4].trim()), // price
                        Integer.parseInt(parts[5].trim()), // year
                        parts[6].trim(), // color
                        Integer.parseInt(parts[7].trim()) // numberOfDoors
                );
            } else if (parts[0].equals("Motorcycle")) {
                return new Motorcycle(
                        parts[1].trim(), // license
                        parts[2].trim(), // brand
                        parts[3].trim(), // model
                        Double.parseDouble(parts[4].trim()), // price
                        Integer.parseInt(parts[5].trim()), // year
                        parts[6].trim(), // color
                        Integer.parseInt(parts[7].trim()) // weight
                );
            } else {
                System.err.println("Unknown type. " + line);
                return null;
            }
        } catch (NumberFormatException e) {
            System.err.println("Error at line: " + line);
            return null;
        }
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        writeFile();
    }

    private void writeFile() {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (Vehicle vehicle : vehicles) {
                writer.write(vehicleToString(vehicle) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String vehicleToString(Vehicle vehicle) {
        if (vehicle instanceof Car) {
            Car car = (Car) vehicle;
            return car.toString();
        } else if (vehicle instanceof Motorcycle){
            Motorcycle mt = (Motorcycle) vehicle;
            return mt.toString();
        }
        return "";
    }

    public List<Vehicle> getAllVehicles() {
        return new ArrayList<>(vehicles);
    }

    public Vehicle getVehicleByLicense(String license) {
        //return (Vehicle) vehicles.stream().filter(vehicle -> vehicle.getLicense().equals(license));
        return vehicles.stream()
                .filter(vehicle -> vehicle.getLicense().equals(license))
                .findFirst()
                .orElse(null);
    }

    public void removeVehicleByLicense(String license) {
        vehicles.removeIf(vehicle -> vehicle.getLicense().equals(license));
        writeFile();
    }
}
