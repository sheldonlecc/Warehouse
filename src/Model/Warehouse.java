package Model;

import Exception.ItemAlreadyExistsException;
import Exception.WarehouseCapacityExceededException;

import java.util.*;

// warehouse class
public class Warehouse {
    private List<Item> items;
    private double maxCapacity;
    private double currentCapacity;

    public Warehouse(double maxCapacity) {
        this.items = new ArrayList<>();
        this.maxCapacity = maxCapacity;
        this.currentCapacity = 0.0;
    }

    // add item to warehouse
    public void addItem(Item item) {
        if (items.contains(item)) {
            throw new ItemAlreadyExistsException("Item " + item.getName() + " already exists in the warehouse.");
        }

        // Check if adding this item would exceed capacity
        if (currentCapacity + item.getWeight() > maxCapacity) {
            throw new WarehouseCapacityExceededException(
                    "Cannot add item. Current capacity: " + currentCapacity +
                            ", Max capacity: " + maxCapacity +
                            ", Item weight: " + item.getWeight()
            );
        }

        items.add(item);
        currentCapacity += item.getWeight();
    }

    // sort items with given criteria
    public void sortItems(String criteria, boolean ascending) {
        Comparator<Item> comparator;
        switch (criteria) {
            case "weight":
                comparator = Comparator.comparingDouble(Item::getWeight);
                break;
            case "name":
                comparator = Comparator.comparing(Item::getName);
                break;
            case "type":
                comparator = Comparator.comparing(item -> {
                    if (item instanceof Food) {
                        return 1;
                    } else if (item instanceof Drink) {
                        return 2;
                    } else if (item instanceof Weapon) {
                        return 3;
                    }
                    return 4;
                });
                break;
            default:
                throw new IllegalArgumentException("Invalid sorting criteria: " + criteria);
        }
        if (!ascending) {
            comparator = comparator.reversed();
        }
        items.sort(comparator);
    }

    // print items
    public void printItems() {
        for (Item item : items) {
            System.out.println(item);
        }
    }

    public List<Item> getItems() {
        return items;
    }

    // remove item from warehouse
    public void removeItem(String itemName) {
        Item itemToRemove = items.stream()
                .filter(item -> item.getName().equals(itemName))
                .findFirst()
                .orElse(null);

        if (itemToRemove != null) {
            items.remove(itemToRemove);
            currentCapacity -= itemToRemove.getWeight();
        }
    }

    // Get current capacity
    public double getCurrentCapacity() {
        return currentCapacity;
    }

    // Get max capacity
    public double getMaxCapacity() {
        return maxCapacity;
    }

    // Get capacity percentage
    public double getCapacityPercentage() {
        return (currentCapacity / maxCapacity) * 100;
    }
}