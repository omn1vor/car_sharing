package carsharing;

public class Company {
    private String name;
    private int id;

    public Company(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public Company(String name) {
        this(name, 0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
