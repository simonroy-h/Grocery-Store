public abstract class PerishableItem extends GroceryItem {
    // zero-parameter constructor
    public PerishableItem() {
        super();
    }

    // three-parameter constructor
    public PerishableItem(String itemName, float itemPrice, float itemWeight) {
        super(itemName, itemPrice, itemWeight);
    }

    // toString method
    public String toString() {
        return (super.toString() + " (perishable)");
    }
}
