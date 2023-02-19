package carsharing;

import carsharing.BasicClasses.Car;
import carsharing.BasicClasses.Company;
import carsharing.BasicClasses.Customer;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final ICompanyDao companyDao;
    private final Scanner scanner;
    private final ICarDao carDao;
    private final ICustomerDao customerDao;
    private int companyId;
    private int customerId;
    private String companyName;


    public Menu() {
        scanner = new Scanner(System.in);
        companyDao = new CompanyDaoImpl();
        carDao = new CarDaoImpl();
        customerDao = new CustomerDaoImpl();
    }

    public void mainMenu() {
        System.out.println("1. Log in as a manager");
        System.out.println("2. Log in as a customer");
        System.out.println("3. Create a customer");
        System.out.println("0. Exit");
        int choice = scanner.nextInt();
        if (choice == 1) {
            managerMenu();
        } else if (choice == 2) {
            customerChoice();
        } else if (choice == 3) {
            createCustomer();
        } else if (choice == 0) {
            companyDao.exit();
        } else {
            mainMenu();
        }
    }

    private void customerChoice() {
        HashMap<Integer, Customer> customers = customerDao.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
            mainMenu();
        } else {
            System.out.println("Choose a customer:");
            int n = 0;
            for (int i = 1; i <= customers.size(); i++) {
                System.out.println(++n + ". " + customers.get(i).getName());
            }
            System.out.println("0. Back");
            int choice = scanner.nextInt();
            if (choice == 0) {
                mainMenu();
            } else {
                customerId = customers.values().stream().map(Customer::getId).filter(c -> c == choice).toList().get(0);
                customerMenu();
            }
        }
    }

    private void customerMenu() {
        System.out.println("1. Rent a car");
        System.out.println("2. Return a rented car");
        System.out.println("3. My rented car");
        System.out.println("0. Back");
        int choice = scanner.nextInt();
        if (choice == 1) {
            rentCar();
        } else if (choice == 2) {
            returnCar();
        } else if (choice == 3) {
            rentedCar();
        } else if (choice == 0) {
            mainMenu();
        }
    }

    private void rentCar() {
        if (customerDao.getAllCustomers().get(customerId).getRented_car_id() != 0) {
            System.out.println("You've already rented a car!");
            customerMenu();
        } else {
            companyList();
            carMenu();
            String nameCustomer = customerDao.getAllCustomers().get(customerId).getName();
            int n = 0;
            int carId;
            //String[] cars = new String[]{"0 0", "0 0", "0 0"};
            HashMap<Integer, Car> avCars = carDao.availableCars();
            HashMap<Integer, Company> companies = companyDao.getAllCompanies();
            if (avCars.isEmpty()) {
                System.out.println("No available cars in the '" +
                        companies.get(companies.values().stream().map(Company::getName).toList().indexOf(companyName)).getName() + "' company.");
            } else {
                System.out.println("Choose a car:");
                String nCars[] = new String[]{"0 0", "0 0", "0 0"};
                for (int i = 1; i <= avCars.values().size(); i++) {
                    if (avCars.get(i) != null) {
                        if (avCars.get(i).getCompanyId() == companyId) {
                            System.out.println(++n + ". " + avCars.get(i).getName());
                            nCars[n] = n + " " + avCars.get(i).getId();

                        }
                    }
                }
                System.out.println("0. Back");
                carId = scanner.nextInt();
                carId = Integer.parseInt(nCars[carId].split(" ")[1]);
                if (carId == 0) {
                    customerMenu();
                } else {
                    customerDao.rentCar(nameCustomer, carId);
                    customerDao.getAllCustomers().get(customerId).setRented_car_id(carId);
                    System.out.println("You rented '" + carDao.getAllCars().get(carId).getName() + "'");
                }
            }
        }
        customerMenu();
    }

    private void returnCar() {
        if (customerDao.getAllCustomers().get(customerId).getRented_car_id() == 0) {
            System.out.println("You didn't rent a car!");
        } else {
            String nameCustomer = customerDao.getAllCustomers().get(customerId).getName();
            customerDao.unrentCar(nameCustomer);
            System.out.println("You've returned a rented car!");
        }
        customerMenu();
    }
    private void rentedCar() {
        System.out.println();
        if (companyDao.getAllCompanies().isEmpty() || carDao.getAllCars().isEmpty()) {
            System.out.println("You didn't rent a car!");
        } else if (customerDao.getAllCustomers().get(customerId).getRented_car_id() == 0) {
            System.out.println("You didn't rent a car!");
        } else {
            HashMap<Integer, Car> hashCars = carDao.getAllCars();
            for (int i = 1; i <= hashCars.size(); i++) {
                 if (hashCars.get(i).getId() == customerDao.getAllCustomers().get(customerId).getRented_car_id()) {
                    System.out.println("Your rented car:");
                    System.out.println(hashCars.get(i).getId() + ". " + hashCars.get(i).getName());
                    System.out.println("Company:");
                    int companyId1 = hashCars.get(i).getCompanyId();
                    System.out.println(companyDao.getAllCompanies().get(companyId1).getName());
                 }
            }
        }
        customerMenu();
    }

    private void createCustomer() {

        System.out.println();
        System.out.println("Enter the customer name:");
        String name = scanner.nextLine();
        if (name.isEmpty()) {
            name = scanner.nextLine();
        }
        customerDao.createCustomer(name);
        System.out.println("The customer was created!");
        mainMenu();

    }

    private void managerMenu() {
        System.out.println();
        System.out.println("1. Company list");
        System.out.println("2. Create a company");
        System.out.println("0. Back");
        int choice = scanner.nextInt();
        if (choice == 1) {
            companyList();
            carMenu();
            carChoice();
        } else if (choice == 2) {
            createCompany();
        } else if (choice == 0) {
            mainMenu();
        } else {
            managerMenu();
        }
    }

    private void carMenu() {
        HashMap<Integer, Company> companies = companyDao.getAllCompanies();
        int choice = scanner.nextInt();
        if (choice == 0) {
            managerMenu();
        }
        for (Company comps : companies.values()) {
            if (comps.getId() == choice) {
                companyId = companies.get(choice).getId();
            }
        }

        companyName = companies.get(companyId).getName();



    }

    private void carChoice() {
        System.out.println("1. Car list");
        System.out.println("2. Create a car");
        System.out.println("0. Back");
        int secChoice = scanner.nextInt();
        if (secChoice == 1) {
            carList();
        } else if (secChoice == 2) {
            createCar();
        } else if (secChoice == 0) {
            managerMenu();
        } else {
            companyList();
            carMenu();
            System.out.println("'" + companyName + "'" + " company:");
        }
    }


    private void companyList() {
        System.out.println();
        HashMap<Integer, Company> companies = companyDao.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
            managerMenu();
        } else {
        System.out.println("Choose a company:");
        for (Company company : companies.values()) {
            System.out.println(company.getId() + ". " + company.getName());

        }
        System.out.println("0. Back");
        }
    }

    private void carList() {
        System.out.println();
        HashMap<Integer, Car> cars = carDao.getAllCars();
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
            carChoice();
        } else {
            int n = 0;
            for (int i = 1; i <= cars.size(); i++ ) {
                if (cars.get(i).getCompanyId() == companyId) {
                    System.out.println(++n  + ". " + cars.get(i).getName());
                }
            }
            if (n == 0) { System.out.println("The car list is empty!"); }
            carChoice();
        }
    }


    private void createCompany() {
        System.out.println();
        System.out.println("Enter the company name:");
        String name = scanner.nextLine();
        if (name.isEmpty()) {
            name = scanner.nextLine();
        }
        companyDao.createCompany(name);
        System.out.println("The company was created!");
        managerMenu();
    }

    private void createCar() {
        System.out.println();
        System.out.println("Enter the car name:");
        String name = scanner.nextLine();
        if (name.isEmpty()) {
            name = scanner.nextLine();
        }

        carDao.createCar(name, companyId);

        System.out.println("The car was created!");
        carChoice();
    }
}