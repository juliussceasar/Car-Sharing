package carsharing;

import carsharing.BasicClasses.Car;

import java.util.HashMap;
import java.util.List;

public interface ICarDao {
    HashMap<Integer, Car> getAllCars();
    void createCar(String name, int companyId);
    HashMap<Integer, Car> availableCars();
}
