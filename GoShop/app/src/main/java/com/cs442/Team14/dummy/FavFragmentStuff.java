package com.cs442.Team14.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles the favorites items
 * @author Prashanth Molakala
 */
public class FavFragmentStuff {

    /**
     * An array of sample (dummy) items.
     */
    public static ArrayList<FavItem> ITEMS = new ArrayList<FavItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, FavItem> ITEM_MAP = new HashMap<String, FavItem>();

    public FavFragmentStuff(){
        ITEMS.clear();
        ITEM_MAP.clear();
    }

    public static void addItem(FavItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.name,item);
    }

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
    public static class FavItem {
        public String name;
        public double price;
        public int image;
        public int pageState;

        public FavItem(){}

        public FavItem(FavItem item){
            this.name = item.name;
            this.price = item.price;
            this.image = item.image;
            this.pageState = 0;
        }

        public FavItem(String name, double price, int image) {
            this.name = name;
            this.price = price;
            this.image = image;
            this.pageState = 0;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
