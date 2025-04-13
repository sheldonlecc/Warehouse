// bomb class extends Weapon
public class Bomb extends Weapon {
    private boolean isUsed;

    public Bomb(String name, double weight) {
        super(name, weight);
        this.isUsed = false;
    }

    // use method overrides Weapon's use method
    @Override
    public void use() {
        if (isUsed) {
            System.out.println(getName() + " has already been used.");
        } else {
            System.out.println("Boom");
            isUsed = true;
        }
    }
}    