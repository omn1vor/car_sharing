package carsharing;

public class Customer {
    private final int id;
    private final String name;
    private int carId;

    public Customer(String name, int id, int carId) {
        this.name = name;
        this.id = id;
        this.carId = carId;
    }

    public Customer(String name) {
        this(name, 0, 0);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }
}
