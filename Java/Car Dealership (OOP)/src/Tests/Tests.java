package Tests;

import Controller.Controller;
import Model.Car;
import Model.Vehicle;
import Repo.Repository;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class Tests {
    private Repository repository;
    private Controller controller;

    @Before
    public void setUp() {
        repository = new Repository("test.csv");
        controller = new Controller(repository);

        repository.removeVehicleByLicense("ABC123");
        repository.removeVehicleByLicense("XYZ789");
    }

    @Test
    public void testAddAndGetVehicle() {
        Car car = new Car("ABC123", "Toyota", "Corolla", 15000, 2020, "Red", 4);
        controller.addVehicle("Car", "ABC123", "Toyota", "Corolla", 15000, 2020, "Red", 4);

        Vehicle retrievedVehicle = controller.getVehicleByLicense("ABC123");
        assertNotNull(retrievedVehicle);
        assertEquals(car, retrievedVehicle);
    }

    @Test
    public void testRemoveVehicle() {
        controller.addVehicle("Car", "ABC123", "Toyota", "Corolla", 15000, 2020, "Red", 4);

        controller.removeVehicleByLicense("ABC123");
        Vehicle retrievedVehicle = controller.getVehicleByLicense("ABC123");
        assertNull(retrievedVehicle);
    }

    @Test
    public void testFilterVehiclesByYear() {
        Car car1 = new Car("ABC123", "Toyota", "Corolla", 15000, 2020, "Red", 4);
        Car car2 = new Car("XYZ789", "Honda", "Civic", 18000, 2021, "Blue", 4);
        controller.addVehicle("Car", "ABC123", "Toyota", "Corolla", 15000, 2020, "Red", 4);
        controller.addVehicle("Car", "XYZ789", "Honda", "Civic", 18000, 2021, "Blue", 4);

        List<Vehicle> vehicles2020 = controller.filterVehiclesByYear(2020);
        assertEquals(1, vehicles2020.size());
        assertEquals(car1, vehicles2020.get(0));
    }

    @Test
    public void testFilterVehiclesByPriceRange() {
        Car car1 = new Car("ABC123", "Toyota", "Corolla", 15000, 2020, "Red", 4);
        Car car2 = new Car("XYZ789", "Honda", "Civic", 18000, 2021, "Blue", 4);
        controller.addVehicle("Car", "ABC123", "Toyota", "Corolla", 15000, 2020, "Red", 4);
        controller.addVehicle("Car", "XYZ789", "Honda", "Civic", 18000, 2021, "Blue", 4);

        List<Vehicle> vehiclesInRange = controller.filterVehiclesByPriceRange(14000, 16000);
        assertEquals(1, vehiclesInRange.size());
        assertEquals(car1, vehiclesInRange.get(0));
    }

    @Test
    public void testSortVehiclesByPriceAscending() {
        Car car1 = new Car("ABC123", "Toyota", "Corolla", 15000, 2020, "Red", 4);
        Car car2 = new Car("XYZ789", "Honda", "Civic", 18000, 2021, "Blue", 4);
        controller.addVehicle("Car", "ABC123", "Toyota", "Corolla", 15000, 2020, "Red", 4);
        controller.addVehicle("Car", "XYZ789", "Honda", "Civic", 18000, 2021, "Blue", 4);

        List<Vehicle> sortedVehicles = controller.sortVehiclesByPriceAscending();
        assertEquals(2, sortedVehicles.size());
        assertEquals(car1, sortedVehicles.get(0));
        assertEquals(car2, sortedVehicles.get(1));
    }

    @Test
    public void testSortVehiclesByPriceDescending() {
        Car car1 = new Car("ABC123", "Toyota", "Corolla", 15000, 2020, "Red", 4);
        Car car2 = new Car("XYZ789", "Honda", "Civic", 18000, 2021, "Blue", 4);
        controller.addVehicle("Car", "ABC123", "Toyota", "Corolla", 15000, 2020, "Red", 4);
        controller.addVehicle("Car", "XYZ789", "Honda", "Civic", 18000, 2021, "Blue", 4);

        List<Vehicle> sortedVehicles = controller.sortVehiclesByPriceDescending();
        assertEquals(2, sortedVehicles.size());
        assertEquals(car2, sortedVehicles.get(0));
        assertEquals(car1, sortedVehicles.get(1));
    }
}
