package Model;

// weapon class extends item class
public abstract class Weapon extends Item {
    public Weapon(String name, double weight) {
        super(name, weight);
    }

    // abstract method
    public abstract void use();
}    