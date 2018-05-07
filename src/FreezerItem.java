public class FreezerItem extends PerishableItem {
    // zero-parameter constructor
    public FreezerItem() {
        super();
    }

    // three-parameter constructor
    public FreezerItem(String itemName, float itemPrice, float itemWeight) {
        super(itemName, itemPrice, itemWeight);
    }

    // toString method
    public String toString() {
        return (super.toString() + " [keep frozen]");
    }
}
