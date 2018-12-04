package VendingMachineProject;

/**
 A product in a vending machine.
 */
public class Product {
    private String description;
    private double price;
    private int quantity;

    /**
     Constructs a Product object
     @param aDescription the description of the product
     @param aPrice the price of the product
     */
    Product(String aDescription, double aPrice) {
        description = aDescription;
        price = aPrice;
    }

    Product(String description, double price, int quantity) {
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    String getDescription() {
        return description;
    }

    double getPrice() {
        return price;
    }

    int getQuantity() {
        return quantity;
    }

    String getCSV() {
        return description + "," + price + "," + quantity;
    }

    @Override
    public String toString() {
        return  description  + " "
                + price;
    }

    int decrementQuantity(Product p) {
        return --p.quantity;
    }

    //ADD REMAINING CODE HERE
}
