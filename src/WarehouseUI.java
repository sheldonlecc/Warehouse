import java.time.LocalDate;
import java.util.Scanner;

// User interaction class, providing command-line interface for user interaction
public class WarehouseUI {
    private Warehouse warehouse;
    private Scanner scanner;

    public WarehouseUI() {
        this.warehouse = new Warehouse();
        this.scanner = new Scanner(System.in);
    }

    // Display menu options
    public void showMenu() {
        System.out.println("1. Add Item");
        System.out.println("2. Sort Items");
        System.out.println("3. Print Items");
        System.out.println("4. Use Item");
        System.out.println("5. Exit");
    }

    // Handle user input
    public void handleUserInput() {
        while (true) {
            showMenu();
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addItem();
                    break;
                case 2:
                    sortItems();
                    break;
                case 3:
                    warehouse.printItems();
                    break;
                case 4:
                    useItem();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Add item to the warehouse
    private void addItem() {
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

    // Sort items in the warehouse
    private void sortItems() {
        System.out.print("Enter sorting criteria (weight, name, type): ");
        String criteria = scanner.nextLine();
        System.out.print("Enter sorting order (ascending/descending): ");
        String order = scanner.nextLine();
        boolean ascending = order.equalsIgnoreCase("ascending");
        warehouse.sortItems(criteria, ascending);
    }

    // Use an item from the warehouse
    private void useItem() {
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
        System.out.println("Item not found.");
    }
}
