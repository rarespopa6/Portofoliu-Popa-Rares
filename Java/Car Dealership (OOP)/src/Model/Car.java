package Model;

public class Car extends Vehicle{
    private final int numberOfDoors;

    public Car(String license, String brand, String model, double price, int year, String color, int numberOfDoors){
        super(license, brand, model, price, year, color);
        this.numberOfDoors = numberOfDoors;
    }

    public int getNumberOfDoors(){
        return this.numberOfDoors;
    }

    @Override
    public String toString() {
        return "Car," + this.getLicense() + "," + this.getBrand() + "," + this.getModel() + "," +
                this.getPrice() + "," + this.getYear() + "," + this.getColor() + "," + this.getNumberOfDoors();
    }
}
