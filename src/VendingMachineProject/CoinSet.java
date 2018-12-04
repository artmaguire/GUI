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

    boolean hasCoin(Coin hc) {
        for (Coin c : coins) {
            if (c.equals(hc))
                return true;
        }
        return false;
    }

    void removeCoin(Coin rc) {
        coins.remove(rc);
    }

    void addOneCoin(Coin rc) {
        for (Coin c : coins) {
            if (c.equals(rc)) {
                c.increaseQuantity();
                return;
            }
        }
    }

    void removeOneCoin(Coin rc) {
        for (Coin c : coins) {
            if (c.equals(rc)) {
                c.reduceQuantity();
                return;
            }
        }
    }

    @Override
    public String toString() {
        return "CoinSet{" +
                "Total = " + getTotal() +
                '}';
    }
}