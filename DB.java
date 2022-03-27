package carsharing;

import java.sql.*;

public class DB {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL_TEMPLATE = "jdbc:h2:./src/carsharing/db/%s";

    private final String dbUrl;
    private Connection conn;

    public DB(String name) {
        dbUrl = String.format(DB_URL_TEMPLATE, name);
        init();
        if (conn == null) {
            throw new IllegalArgumentException("Could not initialise DB with name: " + name);
        }
        createTables();
    }

    public Connection getConn() {
        return conn;
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void execute(String sql) {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(dbUrl);
            conn.setAutoCommit(true);
        } catch (SQLException se) {
            System.out.println("H2 error:");
            se.printStackTrace();
        } catch (Exception e) {
            System.out.println("DB initialisation error:");
            e.printStackTrace();
        }
    }

    private void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS COMPANY (\n" +
                "\tID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                "\tNAME VARCHAR(255) UNIQUE NOT NULL\n" +
                ");\n" +
                "CREATE TABLE IF NOT EXISTS CAR (\n" +
                "\tID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                "\tNAME VARCHAR(255) UNIQUE NOT NULL,\n" +
                "\tCOMPANY_ID INT NOT NULL,\n" +
                "\tCONSTRAINT FK_COMPANY FOREIGN KEY (COMPANY_ID)\n" +
                "\tREFERENCES COMPANY(ID)\n" +
                ");\n" +
                "CREATE TABLE IF NOT EXISTS CUSTOMER (\n" +
                "\tID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                "\tNAME VARCHAR(255) UNIQUE NOT NULL,\n" +
                "\tRENTED_CAR_ID INT,\n" +
                "\tCONSTRAINT FK_CAR FOREIGN KEY (RENTED_CAR_ID)\n" +
                "\tREFERENCES CAR(ID)\n" +
                ")";
        execute(sql);
    }

}
