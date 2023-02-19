package carsharing;

import carsharing.BasicClasses.Car;
import carsharing.BasicClasses.Company;

import java.util.HashMap;
import java.util.List;

public interface ICompanyDao {
    HashMap<Integer, Company> getAllCompanies();
    void createCompany(String name);
    void exit();
}

