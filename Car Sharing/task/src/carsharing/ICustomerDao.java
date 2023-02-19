package carsharing;

import carsharing.BasicClasses.Car;
import carsharing.BasicClasses.Customer;

import java.util.HashMap;
import java.util.List;

public interface ICustomerDao {
    HashMap<Integer, Customer> getAllCustomers();
    void createCustomer(String name);
    void rentCar(String nameCustomer, int rented_car_id);
    void unrentCar(String nameCustomer);
}
