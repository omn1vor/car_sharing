package carsharing;

import java.util.List;
import java.util.Scanner;

public class CLI {

    static Scanner scanner = new Scanner(System.in);
    private final DB db;
    private final CompanyModel companyModel;
    private final CarModel carModel;
    private final CustomerModel customerModel;

    public CLI(DB db) {
        this.db = db;
        companyModel = new CompanyModel(db);
        carModel = new CarModel(db);
        customerModel = new CustomerModel(db);

        showMainMenu();
    }

    private void quit() {
        db.close();
    }

    private void showMainMenu() {
        Menu menu = new Menu();
        menu.addItem(new MenuItem("Log in as a manager", this::showManagerMenu));
        menu.addItem(new MenuItem("Log in as a customer", this::chooseCustomer));
        menu.addItem(new MenuItem("Create a customer", this::createCustomer));
        menu.addExitAction(new MenuItem("Exit", this::quit));

        menu.show();
    }

    private void showManagerMenu() {
        Menu menu = new Menu();
        menu.addItem(new MenuItem("Company list", this::chooseCompany));
        menu.addItem(new MenuItem("Create a company", this::addCompany));
        menu.addExitAction(new MenuItem("Back", this::showMainMenu));

        menu.show();
    }

    private void showCompanyMenu(Company company) {
        Menu menu = new Menu(String.format("'%s' company", company.getName()));
        menu.addItem(new MenuItem("Car list", () -> printCars(company)));
        menu.addItem(new MenuItem("Create a car", () -> addCar(company)));
        menu.addExitAction(new MenuItem("Back", this::showManagerMenu));

        menu.show();
    }

    private void showCustomerMenu(Customer customer) {
        Menu menu = new Menu();
        menu.addItem(new MenuItem("Rent a car", () -> chooseCompanyForRent(customer)));
        menu.addItem(new MenuItem("Return a rented car", () -> returnCar(customer)));
        menu.addItem(new MenuItem("My rented car", () -> showCar(customer)));
        menu.addExitAction(new MenuItem("Back", this::showMainMenu));

        menu.show();
    }

    private void chooseCustomer() {
        List<Customer> list = customerModel.getAll();
        if (list.isEmpty()) {
            System.out.println("The customer list is empty!");
            System.out.println();
            showMainMenu();
        } else {
            Menu menu = new Menu("The customer list:");
            list.forEach(i -> menu.addItem(new MenuItem(i.getName(), () -> showCustomerMenu(i))));
            menu.addExitAction(new MenuItem("Back", this::showMainMenu));
            menu.show();
        }
    }

    private void createCustomer() {
        System.out.println("Enter the customer name:");
        String name = scanner.nextLine();
        customerModel.add(new Customer(name));
        System.out.println("The customer was added!");
        System.out.println();
        showMainMenu();
    }

    private void chooseCompany() {
        List<Company> list = companyModel.getAll();
        if (list.isEmpty()) {
            System.out.println("The company list is empty!");
            System.out.println();
            showManagerMenu();
        } else {
            Menu menu = new Menu("Choose the company:");
            list.forEach(i -> menu.addItem(new MenuItem(i.getName(), () -> showCompanyMenu(i))));
            menu.addExitAction(new MenuItem("Back", this::showManagerMenu));

            menu.show();
        }
    }

    private void chooseCompanyForRent(Customer customer) {
        if (customer.getCarId() > 0) {
            System.out.println("You've already rented a car!");
            System.out.println();
            showCustomerMenu(customer);
            return;
        }
        List<Company> list = companyModel.getAll();
        if (list.isEmpty()) {
            System.out.println("The company list is empty!");
            System.out.println();
            showCustomerMenu(customer);
            return;
        }
        Menu menu = new Menu("Choose a company:");
        list.forEach(i -> menu.addItem(new MenuItem(i.getName(), () -> chooseCarForRent(customer, i))));
        menu.addExitAction(new MenuItem("Back", () -> showCustomerMenu(customer)));

        menu.show();
    }

    private void chooseCarForRent(Customer customer, Company company) {
        List<Car> list = carModel.getFreeByCompany(company);
        if (list.isEmpty()) {
            System.out.printf("No available cars in the '%s' company%n", company.getName());
            System.out.println();
            chooseCompanyForRent(customer);
            return;
        }

        Menu menu = new Menu("Choose a car:");
        list.forEach(i -> menu.addItem(new MenuItem(i.getName(), () -> rentCar(customer, i))));
        menu.addExitAction(new MenuItem("Back", () -> chooseCompanyForRent(customer)));

        menu.show();
    }

    private void showCar(Customer customer) {
        Car car = carModel.get(customer.getCarId());
        if (car == null) {
            System.out.println("You didn't rent a car!");
        } else {
            System.out.println("Your rented car:");
            System.out.println(car.getName());
            System.out.println("Company:");
            Company company = companyModel.get(car.getCompanyId());
            if (company != null) {
                System.out.println(company.getName());
            }
        }
        System.out.println();
        showCustomerMenu(customer);
    }

    private void rentCar(Customer customer, Car car) {
        customerModel.setCar(customer, car);
        System.out.printf("You rented '%s'%n", car.getName());
        System.out.println();
        showCustomerMenu(customer);
    }

    private void returnCar(Customer customer) {
        Car car = carModel.get(customer.getCarId());
        if (car == null) {
            System.out.println("You didn't rent a car!");
        } else {
            customerModel.clearCar(customer);
            System.out.println("You've returned a rented car!");
        }
        System.out.println();
        showCustomerMenu(customer);
    }

    private void addCompany() {
        System.out.println("Enter the company name:");
        String name = scanner.nextLine();
        companyModel.add(new Company(name));
        System.out.println("The company was created!");
        System.out.println();
        showManagerMenu();
    }

    private void printCars(Company company) {
        List<Car> list = carModel.getByCompany(company);
        if (list.isEmpty()) {
            System.out.println("The car list is empty!");
            System.out.println();
        } else {
            System.out.println("Car list:");
            for (int i = 1; i <= list.size(); i++) {
                System.out.printf("%d. %s%n", i, list.get(i - 1).getName());
            }
        }
        System.out.println();
        showCompanyMenu(company);
    }

    private void addCar(Company company) {
        System.out.println("Enter the car name:");
        String name = scanner.nextLine();
        carModel.add(new Car(name, company.getId()));
        System.out.println("The car was added!");
        System.out.println();
        showCompanyMenu(company);
    }

}
