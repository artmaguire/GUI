package VendingMachineProject;

import java.util.ArrayList;
import java.util.List;

/**
 A set of coins.
 */
public class CoinSet {
    private List<Coin> coins;

    /**
     Constructs a CoinSet object.
     */
    CoinSet() {
        coins = new ArrayList<>();
    }

    void addCoin(Coin choice) {
        coins.add(choice);
    }

    double getTotal() {
        double total = 0;
        for (Coin c : coins) {
            total += c.getValue();
        }
        return total;
    }

    double removeCoinSet() {
        double result = getTotal();
        coins.clear();
        return result;
    }

    List<Coin> getCoins() {
        return coins;
    }

    @Override
    public String toString() {
        return "CoinSet{" +
                "Total = " + getTotal() +
                '}';
    }
}