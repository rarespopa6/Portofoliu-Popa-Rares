package UI;

import Controller.Controller;
import Model.Car;
import Model.Motorcycle;
import Model.Vehicle;
import Repo.Repository;

import java.util.List;
import java.util.Scanner;

public class UI {
    private final Controller controller;
    private final Scanner scanner;

    public UI(Controller controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\tMenu:");
            System.out.println("1. Show all vehicles");
            System.out.println("2. Filter vehicles by year");
            System.out.println("3. Filter vehicles by price range.");
            System.out.println("4. Sort vehicles by price (ascending).");
            System.out.println("5. Sort vehicles by price (descending).");
            System.out.println("6. Add a new vehicle");
            System.out.println("7. Delete vehicle");
            System.out.println("8. Search a vehicle");
            System.out.println("9. Quit");
            System.out.print("Enter your option: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    displayAllVehicles();
                    break;
                case 2:
                    filterVehiclesByYear();
                    break;
                case 3:
                    filterVehiclesByPriceRange();
                    break;
                case 4:
                    sortVehiclesByPriceAscending();
                    break;
                case 5:
                    sortVehiclesByPriceDescending();
                    break;
                case 6:
                    addVehicle();
                    break;
                case 7:
                    removeVehicleByLicense();
                    break;
                case 8:
                    searchVehicleByLicense();
                    break;
                case 9:
                    System.out.println("Bye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void displayAllVehicles() {
        List<Vehicle> vehicles = controller.getAllVehicles();
        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle);
        }
    }

    private void filterVehiclesByYear() {
        System.out.print("Enter the year: ");
        int year = scanner.nextInt();
        scanner.nextLine();
        List<Vehicle> vehicles = controller.filterVehiclesByYear(year);
        if (vehicles.isEmpty()) {
            System.out.println("There are no cars from year " + year);
        } else {
            for (Vehicle vehicle : vehicles) {
                System.out.println(vehicle);
            }
        }
    }

    private void filterVehiclesByPriceRange() {
        System.out.print("Min price: ");
        double minPrice = scanner.nextDouble();
        System.out.print("Max price: ");
        double maxPrice = scanner.nextDouble();
        scanner.nextLine();
        List<Vehicle> vehicles = controller.filterVehiclesByPriceRange(minPrice, maxPrice);
        if (vehicles.isEmpty()) {
            System.out.println("There are no cars that match the criteria.");
        } else {
            for (Vehicle vehicle : vehicles) {
                System.out.println(vehicle);
            }
        }
    }

    private void sortVehiclesByPriceAscending() {
        List<Vehicle> vehicles = controller.sortVehiclesByPriceAscending();
        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle);
        }
    }

    private void sortVehiclesByPriceDescending() {
        List<Vehicle> vehicles = controller.sortVehiclesByPriceDescending();
        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle);
        }
    }

    private void addVehicle() {
        System.out.print("Vehicle type (Car/Motorcycle): ");
        String type = scanner.nextLine();

        System.out.print("License plate: ");
        String license = scanner.nextLine();
        System.out.print("Brand: ");
        String brand = scanner.nextLine();
        System.out.print("Model: ");
        String model = scanner.nextLine();
        System.out.print("Price: ");
        double price = scanner.nextDouble();
        System.out.print("Year: ");
        int year = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Color: ");
        String color = scanner.nextLine();

        try {
            if (type.equalsIgnoreCase("Car")) {
                System.out.print("Number of doors: ");
                int numberOfDoors = scanner.nextInt();
                scanner.nextLine();
                controller.addVehicle("Car", license, brand, model, price, year, color, numberOfDoors);
            } else if (type.equalsIgnoreCase("Motorcycle")) {
                System.out.print("Weight: ");
                int weight = scanner.nextInt();
                scanner.nextLine();
                controller.addVehicle("Motorcycle", license, brand, model, price, year, color, weight);
            } else {
                System.out.println("Invalid option.");
            }
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
    }

    private void removeVehicleByLicense() {
        System.out.print("Enter the license plate: ");
        String license = scanner.nextLine();
        try {
            controller.removeVehicleByLicense(license);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void searchVehicleByLicense() {
        System.out.print("Enter the license plate: ");
        String license = scanner.nextLine();
        Vehicle vehicle = controller.getVehicleByLicense(license);
        if (vehicle != null) {
            System.out.println(vehicle);
        } else {
            System.out.println("There is no car with the license plate " + license);
        }
    }
}