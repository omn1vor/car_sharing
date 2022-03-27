package carsharing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompanyModel {
    private final DB db;

    public CompanyModel(DB db){
        this.db = db;
    }

    public List<Company> getAll() {
        List<Company> list = new ArrayList<>();
        String sql = "SELECT ID, NAME \n" +
                "FROM COMPANY \n" +
                "ORDER BY ID";
        try (Statement stmt = db.getConn().createStatement()) {
            ResultSet res = stmt.executeQuery(sql);
            while (res.next()) {
                list.add(new Company(res.getString("NAME"), res.getInt("ID")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Company get(int id) {
        String sql = String.format("SELECT ID, NAME \n" +
                "FROM COMPANY \n" +
                "WHERE ID = %d", id);
        try (Statement stmt = db.getConn().createStatement()) {
            ResultSet res = stmt.executeQuery(sql);
            if (res.next()) {
                return new Company(res.getString("NAME"), res.getInt("ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void add(Company company) {
        String sql = String.format(
                "INSERT INTO COMPANY (NAME) \n" +
                "VALUES ('%s')",
                company.getName());
        db.execute(sql);
    }
}
