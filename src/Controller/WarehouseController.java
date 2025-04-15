package Controller;

import Exception.*;
import Model.*;
import View.WarehouseView;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.List;


public class WarehouseController {
    private Warehouse warehouse;
    private WarehouseView view;
    private Scanner scanner;

    public WarehouseController() {
        this.warehouse = new Warehouse();
        this.view = new WarehouseView();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            view.showMenu();
            int choice = view.getUserChoice();

            switch (choice) {
                case 1:
                    addItem();
                    break;
                case 2:
                    sortItems();
                    break;
                case 3:
                    printItems();
                    break;
                case 4:
                    useItem();
                    break;
                case 5:
                    view.displayMessage("Exiting...");
                    return;
                default:
                    view.displayMessage("Invalid choice. Please try again.");
            }
        }
    }

    public void addItem() {
        System.out.print("Enter item type (Model.Food, Model.Drink, Model.Bomb, Model.Gun): ");
        String type = scanner.nextLine();
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();
        System.out.print("Enter item weight: ");
        double weight = scanner.nextDouble();
        scanner.nextLine();

        switch (type) {
            case "Model.Food":
                System.out.print("Enter expiration date (yyyy-MM-dd): ");
                LocalDate foodExpiration = LocalDate.parse(scanner.nextLine());
                warehouse.addItem(new Food(name, weight, foodExpiration));
                break;
            case "Model.Drink":
                System.out.print("Enter expiration date (yyyy-MM-dd): ");
                LocalDate drinkExpiration = LocalDate.parse(scanner.nextLine());
                warehouse.addItem(new Drink(name, weight, drinkExpiration));
                break;
            case "Model.Bomb":
                warehouse.addItem(new Bomb(name, weight));
                break;
            case "Model.Gun":
                System.out.print("Enter bullet count: ");
                int bulletCount = scanner.nextInt();
                scanner.nextLine();
                warehouse.addItem(new Gun(name, weight, bulletCount));
                break;
            default:
                System.out.println("Invalid item type.");
        }
    }

    public void sortItems() {
        System.out.print("Enter sorting criteria (weight, name, type): ");
        String criteria = scanner.nextLine();
        System.out.print("Enter sorting order (ascending/descending): ");
        String order = scanner.nextLine();
        boolean ascending = order.equalsIgnoreCase("ascending");
        warehouse.sortItems(criteria, ascending);
    }

    public void printItems() {
        warehouse.printItems();
    }

    public void useItem() {
        System.out.print("Enter item name to use: ");
        String itemName = scanner.nextLine();
        for (Item item : warehouse.getItems()) {
            if (item.getName().equals(itemName)) {
                try {
                    if (item instanceof Food) {
                        ((Food) item).eat();
                    } else if (item instanceof Drink) {
                        ((Drink) item).drink();
                    } else if (item instanceof Weapon) {
                        ((Weapon) item).use();
                    }
                } catch (ExpiredItemException | NoBulletsException e) {
                    System.out.println(e.getMessage());
                }
                return;
            }
        }
        System.out.println("Model.Item not found.");
    }

    public void addItem(Item item) {
        warehouse.addItem(item);
    }

    public void sortItems(String criteria, boolean ascending) {
        warehouse.sortItems(criteria, ascending);
    }

    public void useItem(String itemName) {
        for (Item item : warehouse.getItems()) {
            if (item.getName().equals(itemName)) {
                try {
                    if (item instanceof Food) {
                        ((Food) item).eat();
                    } else if (item instanceof Drink) {
                        ((Drink) item).drink();
                    } else if (item instanceof Weapon) {
                        ((Weapon) item).use();
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return;
            }
        }
        System.out.println("Model.Item not found.");
    }

    public void removeItem(String itemName) {
        warehouse.removeItem(itemName);
    }

    public List<Item> getItems() {
        return warehouse.getItems();
    }
} 