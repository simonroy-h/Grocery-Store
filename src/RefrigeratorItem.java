public class RefrigeratorItem extends PerishableItem {
    // zero-parameter constructor
    public RefrigeratorItem() {
        super();
    }

    // three-parameter constructor
    public RefrigeratorItem(String itemName, float itemPrice, float itemWeight) {
        super(itemName, itemPrice, itemWeight);
    }

    // toString method
    public String toString() {
        return (super.toString() + " [keep refrigerated]");
    }
}
