package com.cs442.Team14.dummy;

import java.util.ArrayList;

/**
 * Created by Prashanth Molakala on 5/1/2016.
 */
public class OrderHistoryStuff {
    public static ArrayList<OrderItem> ITEMS = new ArrayList<>();


    /**
     * A dummy item representing a piece of content.
     */
    public static class OrderItem {
        public final int oid;
        public final String items;
        public final double total;
        public final String date;

        public OrderItem(int oid, String items, double price, String date) {
            this.oid = oid;
            this.items = items;
            this.total =price;
            this.date = date;
        }

        @Override
        public String toString() {
            return oid+"";
        }
    }
}
