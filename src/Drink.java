import java.time.LocalDate;

// Drink class, inheriting from Consumable class
public class Drink extends Consumable {
    private boolean isDrunk;

    public Drink(String name, double weight, LocalDate expirationDate) {
        super(name, weight, expirationDate);
        this.isDrunk = false;
    }

    // Method to drink the drink
    public void drink() {
        if (isDrunk) {
            System.out.println(getName() + " has already been drunk.");
        } else if (isExpired()) {
            throw new ExpiredItemException("Cannot drink " + getName() + " as it is expired.");
        } else {
            System.out.println("Drinking " + getName());
            isDrunk = true;
        }
    }
}
