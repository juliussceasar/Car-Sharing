package carsharing;

import carsharing.BasicClasses.Car;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CarDaoImpl implements ICarDao{
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:./src/carsharing/db/carsharing";
    static final String USER = "";
    static final String PASS = "";
    private Connection conn;

    public CarDaoImpl() {
        conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(true);

            stmt = conn.createStatement();
            String sql =  "CREATE TABLE IF NOT EXISTS car" +
                    "(id INT PRIMARY KEY AUTO_INCREMENT, " +
                    " name VARCHAR UNIQUE NOT NULL, " +
                    " company_id INT NOT NULL, " +
                    " FOREIGN KEY (company_id) REFERENCES company(id));";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch(Exception se) {
            se.printStackTrace();
        } finally {
            try{
                if(stmt!=null) stmt.close();
            } catch(SQLException ignored) {
            }
        }
    }

    @Override
    public HashMap<Integer, Car> getAllCars() {
        HashMap<Integer, Car> cars = new HashMap<>();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM car ORDER BY id");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cars.put(rs.getInt(1), new Car(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    @Override
    public HashMap<Integer, Car> availableCars() {
        HashMap<Integer, Car> cars = new HashMap<>();
        try {
            PreparedStatement ps = conn.prepareStatement(" SELECT car.id, car.name, car.company_id \n" +
                    "                    FROM car LEFT JOIN customer \n" +
                    "                    ON car.id = customer.rented_car_id \n" +
                    "                    WHERE customer.name IS NULL;");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cars.put(rs.getInt(1), new Car(rs.getInt(1), rs.getString(2),
                        rs.getInt(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }




    @Override
    public void createCar(String name, int companyId) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO car (name, company_id) VALUES (?, ?)");
            ps.setString(1, name);
            ps.setString(2, String.valueOf(companyId));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
