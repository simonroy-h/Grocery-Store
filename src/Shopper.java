public class Shopper {
    public static final int  MAX_CART_ITEMS = 100;

    private Carryable[]     cart;
    private int             numItems;

    // get methods
    public Carryable[] getCart() { return cart; }
    public int getNumItems() { return numItems; }

    // zero-parameter constructor
    public Shopper() {
        cart = new Carryable[MAX_CART_ITEMS];
        numItems = 0;
    }

    // toString method
    public String toString() {
        return ("Shopper with shopping cart containing " + numItems + " items");
    }

    // method that adds a carryable item to the cart
    public void addItem(Carryable item) {
        if (numItems < MAX_CART_ITEMS)
            cart[numItems++] = item;
    }

    // method that removes a carryable item from the cart
    public void removeItem(Carryable item) {
        for (int i = 0; i < numItems; i++) {
            if (cart[i] == item) {
                cart[i] = cart[numItems - 1];              // rearrange contents of the bag
                numItems--;
                return;                                  // remove only one instance of the item
            }
        }
    }

    // method that packs the cart contents into bags
    public void packBags() {
        GroceryBag[] maxBags = new GroceryBag[numItems]; // 'maximum' possible bags
        GroceryBag bag = new GroceryBag();
        int numOfHeavyItems = 0;

        for (int i = 0; i < numItems; i++) {
            GroceryItem item = (GroceryItem) cart[i];
            if (item.getWeight() > GroceryBag.MAX_WEIGHT)
                numOfHeavyItems++;
        }

        int count = 0;
        // sort as many items as possible into a bag
        while (numItems != numOfHeavyItems) {
            for (int i = 0; i < numItems; i++) {
                GroceryItem item = (GroceryItem) cart[i];
                if ((bag.getWeight() + item.getWeight()) <= GroceryBag.MAX_WEIGHT) {
                    bag.addItem(item);
                    removeItem(item);
                    i--;
                }
            }
            maxBags[count++] = bag;                     // add current bag to 'maximum' possible bags
            bag = new GroceryBag();                     // then "grab" a new bag
        }

        for (int i = 0; i < maxBags.length; i++) {
            if (maxBags[i] != null)
                addItem(maxBags[i]);                    // add all packed GroceryBags to the cart
        }
    }

    // method that displays the cart contents
    public void displayCartContents() {
        for (int i = 0; i < numItems; i++) {
            System.out.println(cart[i].getDescription());
            System.out.print(cart[i].getContents());
        }
    }

    // method that returns an array of perishable items in the cart (and bags)
    public PerishableItem[] removePerishables() {
        PerishableItem[] perishables = new PerishableItem[MAX_CART_ITEMS];
        PerishableItem[] bagPerishables;

        int count = 0;
        for (int i = 0; i < numItems; i++) {
            if (cart[i] instanceof PerishableItem) {
                perishables[count++] = (PerishableItem) cart[i];
                removeItem(cart[i]);
                i--;
            }
            else if (cart[i] instanceof GroceryBag) {
                GroceryBag currentBag = (GroceryBag) cart[i];
                bagPerishables = currentBag.unpackPerishables();
                for (int j = 0; j < bagPerishables.length; j++)
                    perishables[count++] = bagPerishables[j];
            }
        }

        int numOfPerishables = 0;
        for (int i = 0; i < perishables.length; i++)
            if (perishables[i] != null)
                numOfPerishables++;                   // count number of perishables items

        PerishableItem[] removedPerishables = new PerishableItem[numOfPerishables];
        count = 0;

        for (int i = 0; i < perishables.length; i++)
            if (perishables[i] != null)
                removedPerishables[count++] = perishables[i];

        return removedPerishables;
    }

    // method that returns the total price of all FreezerItems in the cart
    public float computeFreezerItemCost() {
        float totalPrice = 0;

        for (int i = 0; i < numItems; i++) {
            if (cart[i] instanceof FreezerItem) {
                totalPrice += cart[i].getPrice();
            }
            else if (cart[i] instanceof GroceryBag) {
                GroceryBag currentBag = (GroceryBag) cart[i];
                for (int j = 0; j < currentBag.getItems().length; j++)
                    if (currentBag.getItems()[j] instanceof FreezerItem)
                        totalPrice += currentBag.getItems()[j].getPrice();
            }
        }

        return totalPrice;
    }

    // method that returns the total price of all items in the cart
    public float computeTotalCost() {
        float totalPrice = 0;

        for (int i = 0; i < numItems; i++)
            totalPrice += cart[i].getPrice();

        return totalPrice;
    }
}
