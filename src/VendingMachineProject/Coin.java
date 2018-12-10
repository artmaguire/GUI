package VendingMachineProject;

/**
 A coin with a monetary value.
 */
public class Coin {
    private double value;
    private String name;
    private int quantity;

    Coin(String name, double value, int quantity) {
        this.name = name;
        this.value = value;
        this.quantity = quantity;
    }

    double getValue() {
        return value;
    }

    String getName() {
        return name;
    }

    String getCSV() {
        return name + "," + value + "," + quantity;
    }

    void increaseQuantity() {
        quantity++;
    }

    void reduceQuantity() {
        quantity--;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coin coin = (Coin) o;
        return Double.compare(coin.value, value) == 0;
    }
}