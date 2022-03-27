package carsharing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CarModel {
    private final DB db;

    public CarModel(DB db){
        this.db = db;
    }

    public List<Car> getByCompany(Company company) {
        String filter = String.format("WHERE COMPANY_ID = %d%n", company.getId());
        return getCars(filter);
    }

    public List<Car> getFreeByCompany(Company company) {
        String filter = String.format("LEFT JOIN CUSTOMER\n" +
                "\tON CAR.ID = CUSTOMER.RENTED_CAR_ID\n" +
                "WHERE COMPANY_ID = %d\n" +
                "\tAND CUSTOMER.ID IS NULL%n", company.getId());
        return getCars(filter);
    }

    public Car get(int id) {
        String sql = String.format("SELECT ID, NAME, COMPANY_ID \n" +
                "FROM CAR \n" +
                "WHERE ID = %d", id);
        try (Statement stmt = db.getConn().createStatement()) {
            ResultSet res = stmt.executeQuery(sql);
            if (res.next()) {
                return new Car(res.getString("NAME"),
                        res.getInt("COMPANY_ID"),
                        res.getInt("ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void add(Car car) {
        String sql = String.format("INSERT INTO CAR (NAME, COMPANY_ID) \n" +
                        "VALUES ('%s', %d)", car.getName(), car.getCompanyId());
        db.execute(sql);
    }

    private List<Car> getCars(String filter) {
        List<Car> list = new ArrayList<>();

        String sql = "SELECT CAR.ID, CAR.NAME, CAR.COMPANY_ID \n" +
                "FROM CAR \n" +
                filter +
                "ORDER BY CAR.ID";
        try (Statement stmt = db.getConn().createStatement()) {
            ResultSet res = stmt.executeQuery(sql);
            while (res.next()) {
                list.add(new Car(res.getString("NAME"),
                        res.getInt("COMPANY_ID"),
                        res.getInt("ID")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
