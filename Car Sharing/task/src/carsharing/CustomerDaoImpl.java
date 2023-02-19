package carsharing;

import carsharing.BasicClasses.Car;
import carsharing.BasicClasses.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerDaoImpl implements ICustomerDao {

    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:./src/carsharing/db/carsharing";
    static final String USER = "";
    static final String PASS = "";

    private Connection conn;

    public CustomerDaoImpl() {
        conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(true);

            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS customer " +
                    "(id INT PRIMARY KEY AUTO_INCREMENT, " +
                    " name VARCHAR UNIQUE NOT NULL," +
                    " rented_car_id INT, " +
                    " FOREIGN KEY (rented_car_id) REFERENCES car(id));";
            stmt.executeUpdate(sql);

            stmt.close();
        } catch (Exception se) {
            se.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public HashMap<Integer, Customer> getAllCustomers() {
        HashMap<Integer, Customer> customers = new HashMap<>();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM customer ORDER BY id");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                customers.put(rs.getInt(1), new Customer(rs.getInt(1), rs.getString(2),
                        rs.getInt(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }


    @Override
    public void createCustomer(String name) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO customer (name, rented_car_id) VALUES (?, NULL)");
            ps.setString(1, name);
            //ps.setInt(2, null);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rentCar(String nameCustomer, int rented_car_id) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE customer " +
                    " SET rented_car_id = ? " +
                    " WHERE name = ?");
            ps.setInt(1, rented_car_id);
            ps.setString(2, nameCustomer);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unrentCar(String nameCustomer) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE customer " +
                    " SET rented_car_id = NULL " +
                    " WHERE name = ?");
            ps.setString(1, nameCustomer);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
