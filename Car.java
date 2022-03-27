package carsharing;

public class Car {
    private String name;
    private final int id;
    private final int companyId;

    public Car(String name, int companyId, int id) {
        this.name = name;
        this.companyId = companyId;
        this.id = id;
    }

    public Car(String name, int companyId) {
        this(name, companyId, 0);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCompanyId() {
        return companyId;
    }
}
