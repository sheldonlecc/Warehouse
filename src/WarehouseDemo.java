import java.time.LocalDate;

public class WarehouseDemo {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();

        //add 2 food( one expired)
        Food expiredFood = new Food("ExpiredBread", 0.3, LocalDate.now().minusDays(10));
        Food freshFood = new Food("FreshApple", 0.2, LocalDate.now().plusDays(10));
        warehouse.addItem(expiredFood);
        warehouse.addItem(freshFood);

        // add 2 drinks( one expired)
        Drink expiredDrink = new Drink("ExpiredMilk", 0.5, LocalDate.now().minusDays(5));
        Drink freshDrink = new Drink("FreshJuice", 0.4, LocalDate.now().plusDays(15));
        warehouse.addItem(expiredDrink);
        warehouse.addItem(freshDrink);

        // add 1 bomb
        Bomb bomb = new Bomb("BigBomb", 1.0);
        warehouse.addItem(bomb);

        // add 2 guns (one with zero bullet, one with 5 bullets)
        Gun zeroBulletGun = new Gun("EmptyGun", 2.0, 0);
        Gun hasBulletGun = new Gun("LoadedGun", 2.5, 5);
        warehouse.addItem(zeroBulletGun);
        warehouse.addItem(hasBulletGun);

        // for each item, try to eat, drink, or use
        for (Item item : warehouse.getItems()) {
            System.out.println("===== Item Attributes =====");
            System.out.println(item);
            if (item instanceof Food) {
                try {
                    ((Food) item).eat();
                } catch (ExpiredItemException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else if (item instanceof Drink) {
                try {
                    ((Drink) item).drink();
                } catch (ExpiredItemException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else if (item instanceof Weapon) {
                try {
                    ((Weapon) item).use();
                } catch (NoBulletsException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }
}