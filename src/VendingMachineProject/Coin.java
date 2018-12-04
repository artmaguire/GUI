package VendingMachineProject;

/**
 A coin with a monetary value.
 */
public class Coin {
    private double value;
    private String name;
    private int quantity;

    /**
     Constructs a coin.
     @param aValue the monetary value of the coin.
     @param aName the name of the coin
     */
    Coin(double aValue, String aName) {
        value = aValue;
        name = aName;
    }

    Coin(String name, double value, int quantity) {
        this.name = name;
        this.value = value;
        this.quantity = quantity;
    }

    double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getCSV() {
        return name + "," + value + "," + quantity;
    }

    @Override
    public String toString() {
        return name;
    }
}