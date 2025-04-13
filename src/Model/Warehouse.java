import java.util.*;

// warehouse class
public class Warehouse {
    private List<Item> items;

    public Warehouse() {
        this.items = new ArrayList<>();
    }

    // add item to warehouse
    public void addItem(Item item) {
        if (items.contains(item)) {
            throw new ItemAlreadyExistsException("Item " + item.getName() + " already exists in the warehouse.");
        }
        items.add(item);
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
}    