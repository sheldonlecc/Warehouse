package Controller;

import Exception.*;
import Model.*;
import Util.JsonUtil;
import View.WarehouseView;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.List;

public class WarehouseController {
    private Warehouse warehouse;
    private WarehouseView view;
    private Scanner scanner;
    private static final double DEFAULT_MAX_CAPACITY = 100.0; // 默认最大容量为100

    public WarehouseController() {
        this.warehouse = new Warehouse(DEFAULT_MAX_CAPACITY);
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
        System.out.print("Enter item type (Food, Drink, Bomb, Gun): ");
        String type = scanner.nextLine();
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();
        System.out.print("Enter item weight: ");
        double weight = scanner.nextDouble();
        scanner.nextLine();

        switch (type) {
            case "Food":
                System.out.print("Enter expiration date (yyyy-MM-dd): ");
                LocalDate foodExpiration = LocalDate.parse(scanner.nextLine());
                warehouse.addItem(new Food(name, weight, foodExpiration));
                break;
            case "Drink":
                System.out.print("Enter expiration date (yyyy-MM-dd): ");
                LocalDate drinkExpiration = LocalDate.parse(scanner.nextLine());
                warehouse.addItem(new Drink(name, weight, drinkExpiration));
                break;
            case "Bomb":
                warehouse.addItem(new Bomb(name, weight));
                break;
            case "Gun":
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
        System.out.printf("Current capacity: %.1f/%.1f (%.1f%%)\n",
                warehouse.getCurrentCapacity(),
                warehouse.getMaxCapacity(),
                warehouse.getCapacityPercentage());
    }

    public void useItem() {
        System.out.print("Enter item name to use: ");
        String itemName = scanner.nextLine();
        for (Item item : warehouse.getItems()) {
            if (item.getName().equals(itemName)) {
                if (item instanceof Food) {
                    ((Food) item).eat();
                } else if (item instanceof Drink) {
                    ((Drink) item).drink();
                } else if (item instanceof Weapon) {
                    ((Weapon) item).use();
                }
                return;
            }
        }
        System.out.println("Item not found.");
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
                if (item instanceof Food) {
                    ((Food) item).eat();
                } else if (item instanceof Drink) {
                    ((Drink) item).drink();
                } else if (item instanceof Weapon) {
                    ((Weapon) item).use();
                }
                return;
            }
        }
        throw new ItemNotFoundException("Item not found: " + itemName);
    }

    public void removeItem(String itemName) {
        warehouse.removeItem(itemName);
    }

    public List<Item> getItems() {
        return warehouse.getItems();
    }

    public double getCurrentCapacity() {
        return warehouse.getCurrentCapacity();
    }

    public double getMaxCapacity() {
        return warehouse.getMaxCapacity();
    }

    public double getCapacityPercentage() {
        return warehouse.getCapacityPercentage();
    }
    
    // 保存仓库数据到文件
    public void saveWarehouseData(String filePath) throws IOException {
        JsonUtil.saveWarehouse(warehouse, filePath);
    }
    
    // 从文件加载仓库数据
    public void loadWarehouseData(String filePath) throws IOException {
        this.warehouse = JsonUtil.loadWarehouse(filePath);
    }
}