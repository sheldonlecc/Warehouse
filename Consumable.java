import java.time.LocalDate;

// consumable class extends item class
public class Consumable extends Item {
    private LocalDate expirationDate;

    public Consumable(String name, double weight, LocalDate expirationDate) {
        super(name, weight);
        this.expirationDate = expirationDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    // 判断物品是否过期
    public boolean isExpired() {
        return LocalDate.now().isAfter(expirationDate);
    }

    @Override
    public String toString() {
        return super.toString() + ", expirationDate=" + expirationDate + ", isExpired=" + isExpired();
    }
}    