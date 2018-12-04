package VendingMachineProject;

import java.io.*;
import java.util.*;

/**
 A vending machine.
 */
class VendingMachine {
    private final String productFileName = "stock.csv";
    private final String operatorFileName = "operators.csv";
    private ArrayList<Product> products;
    private CoinSet coins;
    private CoinSet currentCoins;
    private ArrayList<Operator> operators;

    /**
     Constructs a VendingMachine object.
     */
    VendingMachine() {
        products = new ArrayList<>();
        operators = new ArrayList<>();
        coins = new CoinSet();
        currentCoins = new CoinSet();
    }

    void loadFromFile() {
        loadProducts();
        loadCoins();
        loadOperator();
    }

    private void loadProducts() {
        File f = new File(productFileName);
        if (f.isFile()) {
            Scanner scanner = null;
            try {
                scanner = new Scanner(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                products.add(new Product(values[0], Double.parseDouble(values[1]), Integer.parseInt(values[2])));
            }
            scanner.close();
        } else {
            try {
                f.createNewFile();
                System.out.println("Created new " + productFileName);
            } catch (IOException e) {
                System.out.println("Failed to create: " + productFileName + "\n" + e.getStackTrace());
            }
        }
    }

    private void loadCoins() {
        String moneyFileName = "money.csv";
        File f = new File(moneyFileName);
        Scanner in = null;
        if (f.isFile()) {
            try {
                in = new Scanner(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            while (in.hasNext()) {
                String line = in.nextLine();
                String[] values = line.split(",");
                coins.addCoin(new Coin(values[0], Double.parseDouble(values[1]), Integer.parseInt(values[2])));
            }

            in.close();
        }
    }

    private void loadOperator() {
        File f = new File(operatorFileName);
        if (f.isFile()) {
            Scanner scanner = null;
            try {
                scanner = new Scanner(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                operators.add(new Operator(values[0], Integer.parseInt(values[1])));
            }
            scanner.close();
        } else {
            try {
                f.createNewFile();
                System.out.println("Created new: " + operatorFileName);
            } catch (IOException e) {
                System.out.println("Failed to create: " + operatorFileName + "\n" + e.getStackTrace());
            }
        }
    }

    double removeMoney() {
        return currentCoins.removeCoinSet();
    }

    Product[] getProductTypes() {
        Set<Product> s = new HashSet<>(products);
        Product[] prods = new Product[s.size()];
        return s.toArray(prods);
    }

    Coin[] getCoinTypes() {
        List<Coin> s = coins.getCoins();
        Coin[] c = new Coin[s.size()];
        return s.toArray(c);
    }

    void addCoin(Coin choice) {
        currentCoins.addCoin(choice);
    }

    boolean buyProduct(Product p) {
        if (p.getPrice() <= currentCoins.getTotal()) {
            if (p.decrementQuantity(p) == 0) {
                products.remove(products.lastIndexOf(p));

                //Splits product price into coins and adds them to coin arraylist
                double productPrice = p.getPrice();
                double remainingCoins;
                String price = String.valueOf(productPrice);
                String priceBits [] = price.split(".");

                int euroCount = Integer.parseInt(priceBits[0]);

                while (productPrice > 0) {
                    while (euroCount > 0) {
                        coins.addCoinType(new Coin("Euro", 1, 1));
                        productPrice -= 1;
                        euroCount -= 1;
                    }

                    remainingCoins = Integer.parseInt(priceBits[1]);
                    while ((remainingCoins - .50) > 0) {
                        coins.addCoinType(new Coin("50 cent", .50, 1));
                        productPrice -= 0.50;
                        remainingCoins -= 0.50;
                    }
                    while ((remainingCoins - .10) > 0) {
                        coins.addCoinType(new Coin("10 cent", .10, 1));
                        productPrice -= 0.10;
                        remainingCoins -= 0.10;
                    }
                    while ((remainingCoins - .05) > 0) {
                        coins.addCoinType(new Coin("5 cent", .05, 1));
                        productPrice -= 0.05;
                        remainingCoins -= 0.05;
                    }
                }
            }
            return true;
        } else {
            System.out.println("Insufficient Coins!");
            return false;
        }
    }

    void addProduct(Product product) {
        products.add(product);
    }

    void writeToFile() {
        writeOperators();
        writeProducts();
    }

    private void writeProducts() {
        PrintWriter pw;
        Set<Product> s = new LinkedHashSet<>(products);
        try {
            pw = new PrintWriter(new FileOutputStream(productFileName, false));
            for (Product p : s) {
                pw.println(p.getCSV());
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Failed to create: " + productFileName + "\n" + e.getStackTrace());
        }
    }

    private void writeOperators() {
        PrintWriter pw;
        try {
            pw = new PrintWriter(new FileOutputStream(operatorFileName, false));
            for (Operator o : operators) {
                pw.println(o.getCSV());
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Failed to create: " + operatorFileName + "\n" + e.getStackTrace());
        }
    }

    boolean checkUser() {
        boolean validOperator = false;
        System.out.println("Enter Operator Code: ");

        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        int oCode = Integer.parseInt(input);

        for (Operator o : operators) {
            validOperator = o.getCode() == oCode;
        }

        return validOperator;
    }

    ArrayList<Operator> getOperators() {
        return operators;
    }

    ArrayList<Product> getProducts() {
        return products;
    }
}