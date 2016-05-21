package com.cs442.Team14.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles the cart items
 * @author Hrushikesh Vasista
 */
public class CartFragmentStuff {

    /**
     * An array of sample (dummy) items.
     */
    public static ArrayList<CartItem> ITEMS = new ArrayList<CartItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Integer> ITEM_MAP = new HashMap<String, Integer>();

    public CartFragmentStuff(){

        ITEMS.clear();
        // Add some sample items.
        /*for (int i = 1; i <= COUNT; i++) {
            addItem(createCartItem(i), -1);
        }*/
    }

    private static double cartTotalAmount;

    public static double getCartTotalAmount() {
        return (double)Math.round(cartTotalAmount*100)/100;
    }

    /**
     *
     * @param nextVal value to be updated with #cartTotalAmount
     * @param add true = add, false = subtract
     */
    public static void updateCartTotalAmount(double nextVal, boolean add){
        if (add) {
            cartTotalAmount = (double)Math.round((cartTotalAmount+nextVal)*100)/100;
        } else{
            cartTotalAmount = (double)Math.round((cartTotalAmount-nextVal)*100)/100;
        }


    }

    public static void resetCartTotalAmount() {
        CartFragmentStuff.cartTotalAmount = 0;
    }

    public static void addItem(CartItem item) {
            ITEMS.add(item);
            ITEM_MAP.put(item.name, item.qty);
            updateCartTotalAmount(item.subtotal, true);
    }

    /*private static CartItem createDummyItem(int position) {
        //return new CartItem(String.valueOf(position), "Item " + position, makeDetails(position));
        return new CartItem(String.valueOf(position), "Item " + position, "0");
    }*/

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class CartItem {
        public int qty;
        public String name;
        public double subtotal;
        public int pageState;
        public int image;

        public CartItem(CartItem item){
            this.qty = item.qty;
            this.name = item.name;
            this.subtotal = item.subtotal;
            this.image = item.image;
            this.pageState = 0;
        }

        public CartItem(int qty, String itemName, double Subtotal, int image) {
            this.qty = qty;
            this.name = itemName;
            this.subtotal = Subtotal;
            this.pageState = 0;
            this.image = image;
        }

        @Override
        public String toString() {
            return name;
        }

        public double getSubtotal()
        {
            return this.subtotal;
        }
    }
}
