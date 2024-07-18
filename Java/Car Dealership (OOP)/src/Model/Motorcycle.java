package Model;

public class Motorcycle extends Vehicle{
    private final int weight;

    public Motorcycle(String license, String brand, String model, double price, int year, String color, int weight){
        super(license, brand, model, price, year, color);
        this.weight = weight;
    }

    public int getWeight(){
        return this.weight;
    }

    @Override
    public String toString() {
        return "Motorcycle," + this.getLicense() + "," + this.getBrand() + "," + this.getModel() + "," +
                this.getPrice() + "," + this.getYear() + "," + this.getColor() + "," + this.getWeight();
    }
}
