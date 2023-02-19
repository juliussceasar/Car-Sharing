package carsharing;

import carsharing.BasicClasses.Car;
import carsharing.BasicClasses.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CompanyDaoImpl implements ICompanyDao {
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:./src/carsharing/db/carsharing";
    static final String USER = "";
    static final String PASS = "";

    private Connection conn;

    public CompanyDaoImpl() {
        conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(true);

            stmt = conn.createStatement();
            String sql =  "CREATE TABLE IF NOT EXISTS company " +
                    "(id INT PRIMARY KEY AUTO_INCREMENT, " +
                    " name VARCHAR UNIQUE NOT NULL);";
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
    public HashMap<Integer, Company> getAllCompanies() {
        HashMap<Integer, Company> companies = new HashMap();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM company ORDER BY id");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                companies.put(rs.getInt(1), new Company(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return companies;
    }

    @Override
    public void createCompany(String name) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO company (name) VALUES (?)");
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void exit() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}