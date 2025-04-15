package Model;// bomb class extends Model.Weapon

public class Bomb extends Weapon {
    private boolean isUsed;

    public Bomb(String name, double weight) {
        super(name, weight);
        this.isUsed = false;
    }

    // use method overrides Model.Weapon's use method
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