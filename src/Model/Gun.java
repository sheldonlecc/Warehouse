// Gun class extends Weapon class
public class Gun extends Weapon {
    private int bulletCount;

    public Gun(String name, double weight, int bulletCount) {
        super(name, weight);
        this.bulletCount = bulletCount;
    }

    // fire gun method
    @Override
    public void use() {
        if (bulletCount > 0) {
            System.out.println("Bang");
            bulletCount--;
        } else {
            throw new NoBulletsException("Cannot use " + getName() + " as it has no bullets.");
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", bulletCount=" + bulletCount;
    }
}