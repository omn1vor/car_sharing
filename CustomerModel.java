package carsharing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerModel {
    private final DB db;

    public CustomerModel(DB db){
        this.db = db;
    }

    public List<Customer> getAll() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT ID, NAME, RENTED_CAR_ID\n" +
                "FROM CUSTOMER\n" +
                "ORDER BY ID";
        try (Statement stmt = db.getConn().createStatement()) {
            ResultSet res = stmt.executeQuery(sql);
            while (res.next()) {
                list.add(new Customer(res.getString("NAME"),
                        res.getInt("ID"),
                        res.getInt("RENTED_CAR_ID")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void add(Customer customer) {
        String sql = String.format("INSERT INTO CUSTOMER (NAME) \n" +
                "VALUES ('%s')", customer.getName());
        db.execute(sql);
    }

    public void setCar(Customer customer, Car car) {
        String sql = String.format("UPDATE CUSTOMER\n" +
                "SET RENTED_CAR_ID = %d\n" +
                "WHERE ID = %d", car.getId(), customer.getId());
        db.execute(sql);
        customer.setCarId(car.getId());
    }

    public void clearCar(Customer customer) {
        String sql = String.format("UPDATE CUSTOMER\n" +
                "SET RENTED_CAR_ID = NULL\n" +
                "WHERE ID = %d", customer.getId());
        db.execute(sql);
        customer.setCarId(0);
    }

}
