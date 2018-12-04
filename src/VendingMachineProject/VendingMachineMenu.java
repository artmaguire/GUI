package VendingMachineProject;

import java.util.Scanner;
import java.io.IOException;

/**
A menu from the vending machine.
*/
class VendingMachineMenu {
    private Scanner in;

    /**
     * Constructs a VendingMachineMenu object
     */
    VendingMachineMenu() {
        in = new Scanner(System.in);
    }

    /**
     * Runs the vending machine system.
     *
     * @param machine the vending machine
     */
    void run(VendingMachine machine) throws IOException {
        boolean more = true;
        Coin[] coins = machine.getCoinTypes();

        while (more) {
            System.out.println("S)how products  I)nsert coin  B)uy  A)dd product  R)emove coins  Q)uit");
            String command = in.nextLine().toUpperCase();

            switch (command) {
                case "S":   /*
           getProductTypes() returns an array of products that doesn't contain duplicates
           */
                    for (Product p : machine.getProductTypes())
                        System.out.println(p.getDescription());
                    break;
                case "I": //allows one coin be inserted at a time
                    machine.addCoin((Coin) getChoice(coins));
                    break;
                case "R":
                    boolean operator = machine.checkUser();
                    if (operator) {
                        System.out.println("Removed Coins: " + machine.removeMoney());
                    } else {
                        new VendingException("Unauthorized User.");
                    }
                    break;
                case "B":
                    try {
                        Product p = (Product) getChoice(machine.getProductTypes());
                        if (machine.buyProduct(p))
                            System.out.println("Purchased: " + p);
                        else
                            new VendingException("Insufficient Funds!");
                    } catch (VendingException ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;
                case "A":
                    boolean checkOperator = machine.checkUser();
                    if (checkOperator) {
                        Scanner input = new Scanner(System.in);
                        System.out.println("Description:");
                        String description = input.nextLine();
                        System.out.println("Price:");
                        double price = input.nextDouble();
                        System.out.println("Quantity:");
                        int quantity = input.nextInt();
                        input.nextLine(); // read the new-line character

                        machine.addProduct(new Product(description, price, quantity));
                    } else {
                        break;
                    }
                    break;
                case "Q":
                    more = false;
                    System.exit(0);
                    break;
            }
        }
    }

    private Object getChoice(Object[] choices) {
        while (true) {
            char c = 'A';
            for (Object choice : choices) {
                System.out.println(c + ") " + choice);
                c++;
            }
            String input = in.nextLine();
            int n = input.toUpperCase().charAt(0) - 'A';
            if (0 <= n && n < choices.length)
                return choices[n];
        }
    }
}