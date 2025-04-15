package Model;

import Exception.ExpiredItemException;

import java.time.LocalDate;

// Model.Food class that extends Model.Consumable
public class Food extends Consumable {
    private boolean isEaten;

    public Food(String name, double weight, LocalDate expirationDate) {
        super(name, weight, expirationDate);
        this.isEaten = false;
    }

    // eat method
    public void eat() {
        if (isEaten) {
            System.out.println(getName() + " has already been eaten.");
        } else if (isExpired()) {
            throw new ExpiredItemException("Cannot eat " + getName() + " as it is expired.");
        } else {
            System.out.println("Eating " + getName());
            isEaten = true;
        }
    }
}    