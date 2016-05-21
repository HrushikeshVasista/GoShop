package com.cs442.Team14.dummy;

import com.cs442.Team14.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for Menu Activity
 *
 */
public class MenuActivityStuff {

    /**
     * An array of sample (dummy) items.
     */
    public static final ArrayList<DummyItem> ITEMS = new ArrayList<DummyItem>();

    public static final ArrayList<DummyItem> PRODUCE_ITEMS = new ArrayList<DummyItem>();

    public static final ArrayList<DummyItem> BEVERAGE_ITEMS = new ArrayList<DummyItem>();

    public static final ArrayList<DummyItem> BAKERY_ITEMS = new ArrayList<DummyItem>();

    //public static int start_bakery=0,start_beverages=0,start_produce=0;

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<Integer, DummyItem> ITEM_MAP = new HashMap<Integer, DummyItem>();

    public static final Map<String, Integer> IMAGE_MAP = new HashMap<String, Integer>();

    static {
        // Add images data
        if (IMAGE_MAP.size() == 0) {
            IMAGE_MAP.put("broccoli", R.drawable.broccoli);
            IMAGE_MAP.put("lettuce", R.drawable.lettuce);
            IMAGE_MAP.put("tomato", R.drawable.tomato);
            IMAGE_MAP.put("potato", R.drawable.potato);
            IMAGE_MAP.put("spinach", R.drawable.spinach);
            IMAGE_MAP.put("bake1", R.drawable.bake1);
            IMAGE_MAP.put("bake3", R.drawable.bake3);
            IMAGE_MAP.put("bake4", R.drawable.bake4);
            IMAGE_MAP.put("beverage2", R.drawable.beverage2);
            IMAGE_MAP.put("beverage", R.drawable.beverage);
            IMAGE_MAP.put("beverage3", R.drawable.beverage3);
            IMAGE_MAP.put("bake5", R.drawable.bake5);
            IMAGE_MAP.put("bake6", R.drawable.bake6);
            IMAGE_MAP.put("bake7", R.drawable.bake7);
            IMAGE_MAP.put("beverage4", R.drawable.beverage4);
            IMAGE_MAP.put("beverage5", R.drawable.beverage5);
            IMAGE_MAP.put("beverage6", R.drawable.beverage6);
            IMAGE_MAP.put("produce1", R.drawable.produce1);
        }
    }

    public static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void clearData() {
        ITEMS.clear();
        ITEM_MAP.clear();
        PRODUCE_ITEMS.clear();
        BEVERAGE_ITEMS.clear();
        BAKERY_ITEMS.clear();
        //start_bakery=0;start_beverages=0;start_produce=0;
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final int id;
        public final String name;
        public final double price;
        public final String details;
        public final int image;
        public final String category;

        public DummyItem(int id, String name, double price, String details, int image, String category) {
            this.id = id;
            this.name = name;
            this.price =price;
            this.details = details;
            this.image = image;
            this.category = category;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
