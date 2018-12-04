package VendingMachineProject;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

/**
 This program simulates a vending machine.
 */
public class VendingMachineSimulation extends Application {
    private Stage window;
    private ListView<Product> listView = new ListView<>();
    private static VendingMachine machineGUI = new VendingMachine();
    private Coin[] coins = machineGUI.getCoinTypes();
    private boolean authorize = false;

    public static void main(String[] args) throws IOException {
        VendingMachineMenu menu = new VendingMachineMenu();
        VendingMachine machine = new VendingMachine();

        machine.loadFromFile();
        machineGUI.loadFromFile();

        Scanner in = new Scanner(System.in);

        System.out.println("==============================");
        System.out.println("Would you like CLI or GUI?");
        System.out.println("1)CLI\n2)GUI");
        String input = in.nextLine();

        try {
            if (Integer.parseInt(input) == 2) {
                launch(args);
            } else if (Integer.parseInt(input) == 1){
                menu.run(machine);
                machine.writeToFile();
            } else {
                new VendingException("Invalid Number! Enter 1 or 2.");
            }
        } catch (NumberFormatException e) {
            System.out.println(e.getStackTrace());
        }
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        window.setOnCloseRequest(e -> {
            machineGUI.writeToFile();
        });

        if (listView.getItems().size() == 0) {
            listView.getItems().addAll(machineGUI.getProducts());
        }

        Button showProducts = new Button("Show Products");
        Button insertCoin = new Button("Insert Coin");
        Button buyProduct = new Button("Buy Product");
        Button addProduct = new Button("Add Product");
        Button removeCoins = new Button("Remove Coins");
        Button quit = new Button("Quit");

        //showProducts
        showProducts.setOnAction(e -> showProducts());
        insertCoin.setOnAction(e -> insertCoin());
        buyProduct.setOnAction(e -> buyProduct());
        addProduct.setOnAction(e -> confirmBox());
        removeCoins.setOnAction(e -> removeCoins());

        quit.setOnAction(e -> {
            machineGUI.writeToFile();
            System.exit(0);
        });

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.getChildren().addAll(showProducts, insertCoin, buyProduct, addProduct, removeCoins, quit);

        hbox.setMaxWidth(Double.MAX_VALUE);
        hbox.setMaxWidth(Double.MAX_VALUE);

        Scene scene = new Scene(hbox, 700, 200);
        window.setScene(scene);
        window.setTitle("Vending Machine");
        window.centerOnScreen();
        window.show();
    }

    private boolean confirmBox() {
        Stage operatorStage = new Stage();
        operatorStage.setTitle("Remove Coins");

        TextField inputFromUser = new TextField("Enter Operator Code.");

        Button confirm = new Button("Submit");
        confirm.setOnAction(e -> {
            if (checkUserGUI(inputFromUser.getText())) {
                addProduct();
            } else {
                AlertBox.display("Incorrect", "Unauthorized User");
                authorize = false;
            }
        });

        Button back = new Button("Back");
        back.setAlignment(Pos.BASELINE_RIGHT);
        back.cancelButtonProperty();

        back.setOnAction(e -> {
            start(operatorStage);
            Stage stage = (Stage) back.getScene().getWindow();
            stage.close();
        });

        HBox hBox = new HBox(10);

        hBox.setPadding(new Insets(20, 20, 20, 20));
        hBox.getChildren().addAll(inputFromUser, confirm, back);

        Scene scene = new Scene(hBox, 350, 200);
        window.setScene(scene);
        window.centerOnScreen();
        window.show();
        return authorize;
    }

    //Asks for operator authentication
    private void removeCoins() {
        Stage operatorStage = new Stage();
        operatorStage.setTitle("Remove Coins");

        TextField inputFromUser = new TextField("Enter Operator Code.");

        Button confirm = new Button("Submit");
        confirm.setOnAction(e -> {
            if (checkUserGUI(inputFromUser.getText())) {
                    AlertBox.display("Remove Coins", "Removed Coins: " + machineGUI.removeMoney());
            } else {
                AlertBox.display("Incorrect", "Unauthorized User");
            }
        });

        Button back = new Button("Back");
        back.setAlignment(Pos.BASELINE_RIGHT);
        back.cancelButtonProperty();

        back.setOnAction(e -> {
            start(operatorStage);
            Stage stage = (Stage) back.getScene().getWindow();
            stage.close();
        });

        HBox hBox = new HBox(10);

        hBox.setPadding(new Insets(20, 20, 20, 20));
        hBox.getChildren().addAll(inputFromUser, confirm, back);

        Scene scene = new Scene(hBox, 350, 200);
        window.setScene(scene);
        window.centerOnScreen();
        window.show();
    }

    //Asks for operator authencation
    //Make all first parameter in tableview Product
    private void addProduct() {
        Stage showAddProduct = new Stage();
        showAddProduct.setTitle("Buy Product");

        Button addButton = new Button("Add");

        Button back = new Button("Back");
        back.setAlignment(Pos.BOTTOM_RIGHT);

        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        addButton.setOnAction(e -> addButtonClicked());

        back.setOnAction(e -> {
            start(showAddProduct);
            Stage stage = (Stage) back.getScene().getWindow();
            stage.close();
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(listView, addButton, back);

        Scene scene = new Scene(layout, 500, 300);
        window.setScene(scene);
        window.centerOnScreen();
        window.show();
    }

    private void addButtonClicked() {
        Stage addProductStage = new Stage();
        addProductStage.setTitle("Buy Product");

        Label label = new Label("Enter Product Details");
        label.setPadding(new Insets(10, 10, 10, 10));
        Button confirm = new Button("Confirm");
        confirm.setAlignment(Pos.BOTTOM_RIGHT);

        //Name input
        TextField nameInput = new TextField();
        nameInput.setPromptText("Name");
        nameInput.setMinWidth(100);

        //Price input
        TextField priceInput = new TextField();
        priceInput.setPromptText("Price");

        //Quantity input
        TextField quantityInput = new TextField();
        quantityInput.setPromptText("Quantity");

        confirm.setOnAction(e -> {
            Product p = new Product(nameInput.getText(), Double.parseDouble(priceInput.getText()), Integer.parseInt(quantityInput.getText()));
            machineGUI.addProduct(p);
            listView.getItems().add(p);

            nameInput.clear();
            priceInput.clear();
            quantityInput.clear();

            AlertBox.display("Successful", p.getDescription() + " added to the list of products!");

            start(addProductStage);
            Stage stage = (Stage) confirm.getScene().getWindow();
            stage.close();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            try {
                addProduct();
                Stage stage = (Stage) cancelButton.getScene().getWindow();
                stage.close();
            } catch (NullPointerException ex) {
            }
        });

        HBox hBox1 = new HBox();
        HBox hBox2 = new HBox();
        BorderPane bp = new BorderPane();

        hBox1.setPadding(new Insets(10,10,10,10));
        hBox1.setSpacing(10);
        hBox1.getChildren().addAll(label, nameInput, priceInput, quantityInput);

        hBox2.setPadding(new Insets(10,10,10,10));
        hBox2.setSpacing(10);
        hBox2.getChildren().addAll(cancelButton, confirm);

        BorderPane.setAlignment(label, Pos.TOP_CENTER);
        bp.setTop(label);
        bp.setCenter(hBox1);
        bp.setBottom(hBox2);

        Scene scene = new Scene(bp, 500, 150);
        window.setScene(scene);
        window.show();
    }

    //ListView
    private void buyProduct() {
        Stage showBuyProduct = new Stage();
        showBuyProduct.setTitle("Buy Product");
        Button submit = new Button("Submit");
        Button back = new Button("Back");
        back.setAlignment(Pos.BOTTOM_RIGHT);

        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        submit.setOnAction(e -> {
            try {
                try {
                    ObservableList<Product> product = listView.getSelectionModel().getSelectedItems();
                    Product p = new Product(product.get(0).getDescription(), product.get(0).getPrice(), product.get(0).getQuantity());
                    if (machineGUI.buyProduct(p))
                        AlertBox.display("", "Purchased " + p.getDescription());
                    else
                        AlertBox.display("Error", "Insufficient Funds!");

                } catch (IndexOutOfBoundsException ex) {
                    System.out.println(ex.getMessage());
                }
            } catch (VendingException ex) {
                AlertBox.display("Error", ex.getMessage());
            }
        });
        back.setOnAction(e -> {
            start(showBuyProduct);
            Stage stage = (Stage) back.getScene().getWindow();
            stage.close();
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(listView, submit, back);

        Scene scene = new Scene(layout, 500, 300);
        window.setScene(scene);
        window.centerOnScreen();
        window.show();
    }

    private void insertCoin() {
        Stage insertCoinsStage = new Stage();
        insertCoinsStage.setTitle("Insert Coin");

        Button fiveButton = new Button("5 Cent");
        Button tenButton = new Button("10 Cent");
        Button fiftyButton = new Button("50 Cent");
        Button euroButton = new Button("1 Euro");
        Button back = new Button("Back");

        fiveButton.setOnAction(e -> {
            //Do Coin stuff
            machineGUI.addCoin(coins[0]);
            AlertBox.display("Add Coin", coins[0].getName()  + " added");
            start(insertCoinsStage);
            Stage stage = (Stage) fiveButton.getScene().getWindow();
            stage.close();
        });

        tenButton.setOnAction(e -> {
            //Do Coin stuff
            machineGUI.addCoin(coins[1]);
            AlertBox.display("Add Coin", coins[1].getName()  + " added");
            start(insertCoinsStage);
            Stage stage = (Stage) tenButton.getScene().getWindow();
            stage.close();
        });

        fiftyButton.setOnAction(e -> {
            //Do Coin stuff
            machineGUI.addCoin(coins[2]);
            AlertBox.display("Add Coin", coins[2].getName()  + " added");
            start(insertCoinsStage);
            Stage stage = (Stage) fiftyButton.getScene().getWindow();
            stage.close();
        });

        euroButton.setOnAction(e -> {
            //Do Coin stuff
            machineGUI.addCoin(coins[3]);
            AlertBox.display("Add Coin", coins[3].getName()  + " added");
            start(insertCoinsStage);
            Stage stage = (Stage) euroButton.getScene().getWindow();
            stage.close();
        });

        back.setOnAction(e -> {
            start(insertCoinsStage);
            Stage stage = (Stage) back.getScene().getWindow();
            stage.close();
        });

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.getChildren().addAll(fiveButton, tenButton, fiftyButton, euroButton, back);


        Scene scene = new Scene(hBox, 700, 200);
        window.setScene(scene);
        window.centerOnScreen();
        window.show();
    }

    private void showProducts() {
        Stage showProduct = new Stage();
        showProduct.setTitle("Product");

        Button buyButton = new Button("Buy");
        Button back = new Button("Back");

        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        back.setOnAction(e -> {
            start(showProduct);
            Stage stage = (Stage) back.getScene().getWindow();
            stage.close();
        });

        BorderPane bp = new BorderPane();
        HBox hBox = null;

        bp.setCenter(listView);
        bp.setBottom(hBox);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(listView, buyButton, back);

        Scene scene = new Scene(layout, 500, 300);
        window.setScene(scene);
        window.centerOnScreen();
        window.show();
    }

    private boolean checkUserGUI(String text) {
    boolean validOperator = false;

    int oCode = Integer.parseInt(text);

    for (Operator o : machineGUI.getOperators()) {
        validOperator = o.getCode() == oCode;
    }

    return validOperator;
    }
}

