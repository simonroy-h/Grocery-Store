import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class ShoppingListView extends Pane {
    public boolean buttonState = true;         // reflects 'true' for "checkout"

    public final GroceryItem[] PRODUCTS = {
            new FreezerItem("Smart-Ones Frozen Entrees", 1.99f, 0.311f),
            new GroceryItem("SnackPack Pudding", 0.99f, 0.396f),
            new FreezerItem("Breyers Chocolate Icecream",2.99f,2.27f),
            new GroceryItem("Nabob Coffee", 3.99f, 0.326f),
            new GroceryItem("Gold Seal Salmon", 1.99f, 0.213f),
            new GroceryItem("Ocean Spray Cranberry Cocktail",2.99f,2.26f),
            new GroceryItem("Heinz Beans Original", 0.79f, 0.477f),
            new RefrigeratorItem("Lean Ground Beef", 4.94f, 0.75f),
            new FreezerItem("5-Alive Frozen Juice",0.75f,0.426f),
            new GroceryItem("Coca-Cola 12-pack", 3.49f, 5.112f),
            new GroceryItem("Toilet Paper - 48 pack", 40.96f, 10.89f),
            new RefrigeratorItem("2L Sealtest Milk",2.99f,2.06f),
            new RefrigeratorItem("Extra-Large Eggs",1.79f,0.77f),
            new RefrigeratorItem("Yoplait Yogurt 6-pack",4.74f,1.02f),
            new FreezerItem("Mega-Sized Chocolate Icecream",67.93f,15.03f)
    };

    // model to which this view is attached
    private Shopper	                model;

    // user interface components needed by the controller
    private ListView<GroceryItem>   productList, contentsList;
    private ListView<String>        cartList;
    private Button                  buyButton, returnButton, checkoutButton;
    private TextField               priceField;

    // public methods to allow access to components
    public ListView<GroceryItem> getProductList() { return productList; }
    public ListView<String> getCartList() { return cartList; }
    public Button getBuyButton() { return buyButton; }
    public Button getReturnButton() { return returnButton; }
    public Button getCheckoutButton() { return checkoutButton; }

    public ShoppingListView(Shopper m) {
        // store the model for access later
        model = m;

        // create font
        Font font = new Font("Arial", 12.0);

        // add the labels
        Label label = new Label("Products");
        label.relocate(10, 10);
        label.setPrefSize(200, 35);
        label.setFont(font);
        label.setAlignment(Pos.TOP_LEFT);
        getChildren().add(label);

        label = new Label("Shopping Cart");
        label.relocate(220, 10);
        label.setPrefSize(200, 35);
        label.setFont(font);
        label.setAlignment(Pos.TOP_LEFT);
        getChildren().add(label);

        label = new Label("Contents");
        label.relocate(430, 10);
        label.setPrefSize(120, 35);
        label.setFont(font);
        label.setAlignment(Pos.TOP_LEFT);
        getChildren().add(label);

        label = new Label("Total Price:");
        label.relocate(565, 355);
        label.setPrefSize(65, 25);
        label.setFont(font);
        label.setAlignment(Pos.TOP_LEFT);
        getChildren().add(label);

        // add the listviews
        productList = new ListView<GroceryItem>();
        productList.relocate(10, 45);
        productList.setPrefSize(200, 300);

        cartList = new ListView<String>();
        cartList.relocate(220, 45);
        cartList.setPrefSize(200, 300);

        contentsList = new ListView<GroceryItem>();
        contentsList.relocate(430, 45);
        contentsList.setPrefSize(300, 300);

        // add the buttons
        buyButton = new Button("Buy");
        buyButton.relocate(10, 355);
        buyButton.setPrefSize(200, 25);
        buyButton.setFont(font);

        returnButton = new Button("Return");
        returnButton.relocate(220, 355);
        returnButton.setPrefSize(200, 25);
        returnButton.setFont(font);

        checkoutButton = new Button("Checkout");
        checkoutButton.relocate(430, 355);
        checkoutButton.setPrefSize(120, 25);
        checkoutButton.setFont(font);

        // add the textfield
        priceField = new TextField();
        priceField.relocate(630, 355);
        priceField.setPrefSize(100, 25);
        priceField.setFont(font);
        priceField.setAlignment(Pos.CENTER_RIGHT);
        priceField.setEditable(false);

        // add remaining components to the window
        getChildren().addAll(productList, cartList, contentsList, buyButton, returnButton, checkoutButton, priceField);

        // call update() to ensure model contents are shown
        update();
    }

    public void update() {
        // populate productList with products
        int selectedIndex = productList.getSelectionModel().getSelectedIndex();
        productList.setItems(FXCollections.observableArrayList(PRODUCTS));
        productList.getSelectionModel().select(selectedIndex);

        // enable/disable the buyButton accordingly
        buyButton.setDisable(selectedIndex < 0);

        // populate cartList with cart items
        String[] exactList = new String[model.getNumItems()];
        for (int i = 0; i < model.getNumItems(); i++)
            exactList[i] = model.getCart()[i].getDescription();
        selectedIndex = cartList.getSelectionModel().getSelectedIndex();
        cartList.setItems(FXCollections.observableArrayList(exactList));
        cartList.getSelectionModel().select(selectedIndex);

        // enable/disable the returnButton accordingly
        returnButton.setDisable(selectedIndex < 0);

        // populate contentsList with bag items
        if (selectedIndex >= 0) {
            if (model.getCart()[selectedIndex] instanceof GroceryBag) {
                GroceryBag bag = (GroceryBag) model.getCart()[selectedIndex];
                GroceryItem[] items = new GroceryItem[bag.getNumItems()];
                for (int i = 0; i < bag.getNumItems(); i++)
                    items[i] = bag.getItems()[i];
                contentsList.setItems(FXCollections.observableArrayList(items));
            }
            else {
                contentsList.setItems(null);
            }
        }

        // update priceField to display total cost
        priceField.setText(String.format("$%1.2f", model.computeTotalCost()));

        // enable/disable the checkoutButton accordingly
        // and handle checkout / restart shopping views
        if (!buttonState) {
            buyButton.setDisable(true);
            returnButton.setDisable(true);
            productList.setDisable(true);
            checkoutButton.setDisable(false);
            checkoutButton.setText("Restart Shopping");
        }
        else {
            productList.setDisable(false);
            checkoutButton.setDisable(model.getNumItems() == 0);
            checkoutButton.setText("Checkout");
        }
    }

    public void restart() {
        for (int i = 0; i < model.getCart().length; i++) {
            model.removeItem(model.getCart()[i]);
        }

        cartList.setItems(null);
        contentsList.setItems(null);
        priceField.setText(String.format("$%1.2f", model.computeTotalCost()));
    }
}
