package Model;

import java.util.Objects;

public abstract class Vehicle {
    private final String license;
    private final String brand;
    private final String model;
    private double price;
    private final int year;
    private String color;

    public Vehicle(String license, String brand, String model, double price, int year, String color){
        this.license = license;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.year = year;
        this.color = color;
    }

    public String getLicense(){
        return this.license;
    }

    public String getBrand(){
        return this.brand;
    }

    public String getModel(){
        return this.model;
    }

    public double getPrice(){
        return this.price;
    }

    public int getYear(){
        return this.year;
    }

    public String getColor(){
        return this.color;
    }

    public void setPrice(double newPrice){
        this.price = newPrice;
    }

    public void setColor(String newColor){
        this.color = newColor;
    }

    abstract public String toString();

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Vehicle vehicle = (Vehicle) obj;
        return Objects.equals(license, vehicle.license);
    }

    @Override
    public int hashCode() {
        return Objects.hash(license);
    }
}
