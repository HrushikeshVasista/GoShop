package com.cs442.Team14;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.cs442.Team14.dummy.CartFragmentStuff;
import com.cs442.Team14.dummy.FavFragmentStuff;
import com.cs442.Team14.dummy.MenuActivityStuff;
import com.cs442.Team14.dummy.OrderHistoryStuff;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Prashanth Molakala on 4/29/2016.
 */
public class DBOperations {

    private Context CTX;
    public DBOperations(Context ctx){
        this.CTX = ctx;
    }

    //To fetch data from the database
    public void getData(String Table){
        new DBData(CTX, Table, false).execute(); //async task for getting data from db
    }

    //to insert data into the database
    public void insertData(String Table, Object values){
        new DBData(CTX, Table, values, true).execute(); //async task for inserting data from db
    }

    //to delete data in the database
    public void deleteData(String Table, Object values){
        new DBData(CTX, Table, values, false).execute(); //async task for deleting data in db
    }

    //to delete all data in the database
    public void clearData(String Table){
        new DBData(CTX, Table, true).execute(); //async task for clearing data in db
    }

    //to update cart data in the database
    public void updateCart(Object value){
        new DBData(CTX, value).execute(); //async task for clearing data in db
    }

    //the async task that fetches data from the database
    public class DBData extends AsyncTask<Void, Void, String> {
        ProgressDialog mProgressDialog;
        Context context;
        private String Table;
        private boolean get = false;
        private Object value;
        private boolean add = true;
        private boolean deleteAll = false;
        CartFragmentStuff.CartItem cartItem;
        FavFragmentStuff.FavItem favItem;
        OrderHistoryStuff.OrderItem orderItem;
        private String resultString = "";
        private boolean update = false;
        private String email_id;
        SharedPreferences preferences;

        //constructor for Getting Data from database
        public DBData(Context context, String Table, boolean deleteAll) {
            this.context = context;
            this.Table = Table;
            this.get = true;
            this.deleteAll = deleteAll;
        }

        public DBData(Context context, String Table, Object values, boolean add) {
            this.context = context;
            this.Table = Table;
            this.value = values;
            this.add = add;
            this.get = false;
        }

        public DBData(Context context, Object values) {
            this.context = context;
            this.Table = GoShopApplicationData.CartTableInfo.TableName;
            this.value = values;
            this.update = true;
            this.add = false;
            this.get = false;
        }


        //This method executes before executing the db query
        protected void onPreExecute() {
            preferences = context.getSharedPreferences(GoShopApplicationData.GO_SHOP_SHARED_PREFERENCES, context.MODE_PRIVATE);
            email_id = preferences.getString(GoShopApplicationData.USER_EMAIL, "mpnaidu26@gmail.com");
            if (Table.equals(GoShopApplicationData.InventoryTableInfo.TableName)) {
                mProgressDialog = ProgressDialog.show(context, "", context.getString(R.string.loginActivityMsg5));
            }
        }

        //This method runs the query for fetching data from db in the background
        protected String doInBackground(Void... params) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                java.sql.Connection con = DriverManager.getConnection(GoShopApplicationData.DB_URL, GoShopApplicationData.USER, GoShopApplicationData.PASS);
                java.sql.Statement st = con.createStatement();

                if (get) {
                    if (deleteAll){
                        st.executeUpdate("delete from " + Table + " where emailid='"+email_id+"';");
                    } else {
                        java.sql.ResultSet rs;
                        if (Table.equals(GoShopApplicationData.InventoryTableInfo.TableName)) {
                            st.executeUpdate("SET SQL_SAFE_UPDATES = 0;");
                            rs = st.executeQuery("select * from " + Table + " ORDER BY category;");
                        } else {
                            rs = st.executeQuery("select * from " + Table + " WHERE (emailid='"+ email_id +"');");;
                        }
                        int i = 1;
                        while (rs.next()) {
                            if (Table.equals(GoShopApplicationData.InventoryTableInfo.TableName)) {
                                String name = rs.getString(GoShopApplicationData.InventoryTableInfo.item_name);
                                double price = rs.getDouble(GoShopApplicationData.InventoryTableInfo.price);
                                String description = rs.getString(GoShopApplicationData.InventoryTableInfo.description);
                                String image = rs.getString(GoShopApplicationData.InventoryTableInfo.image);
                                String category = rs.getString(GoShopApplicationData.InventoryTableInfo.category);
                                MenuActivityStuff.DummyItem invItem = new MenuActivityStuff.DummyItem(i, name, price, description, MenuActivityStuff.IMAGE_MAP.get(image), category);
                                MenuActivityStuff.addItem(invItem);
                            } else if (Table.equals(GoShopApplicationData.FavTableInfo.TableName)) {
                                String name = rs.getString(GoShopApplicationData.FavTableInfo.item_name);
                                double price = rs.getDouble(GoShopApplicationData.FavTableInfo.price);
                                int image = rs.getInt(GoShopApplicationData.FavTableInfo.image);
                                FavFragmentStuff.FavItem fItem = new FavFragmentStuff.FavItem(name, price, image);
                                FavFragmentStuff.addItem(fItem);
                            } else if (Table.equals(GoShopApplicationData.CartTableInfo.TableName)) {
                                String name = rs.getString(GoShopApplicationData.CartTableInfo.item_name);
                                double subtotal = rs.getDouble(GoShopApplicationData.CartTableInfo.subtotal);
                                int quantity = rs.getInt(GoShopApplicationData.CartTableInfo.quantity);
                                int image = rs.getInt(GoShopApplicationData.CartTableInfo.image);
                                CartFragmentStuff.CartItem cItem = new CartFragmentStuff.CartItem(quantity, name, subtotal, image);
                                CartFragmentStuff.addItem(cItem);
                            } else if (Table.equals(GoShopApplicationData.OrderHistoryTableInfo.TableName)) {
                                String items = rs.getString(GoShopApplicationData.OrderHistoryTableInfo.items);
                                double total = rs.getDouble(GoShopApplicationData.OrderHistoryTableInfo.total);
                                int oid = rs.getInt(GoShopApplicationData.OrderHistoryTableInfo.oid);
                                String date = rs.getString(GoShopApplicationData.OrderHistoryTableInfo.date);
                                OrderHistoryStuff.OrderItem oItem = new OrderHistoryStuff.OrderItem(oid, items, total, date);
                                OrderHistoryStuff.ITEMS.add(oItem);
                            }
                            i++;
                        }
                    }
                } else {
                    if (add){
                        if (Table.equals(GoShopApplicationData.CartTableInfo.TableName)) {
                            cartItem = (CartFragmentStuff.CartItem) value;
                            st.executeUpdate("INSERT INTO " + Table + " VALUES ('" + cartItem.name + "'," + cartItem.qty + "," + cartItem.subtotal + "," + cartItem.image + ",'"+email_id+"');");
                        } else if (Table.equals(GoShopApplicationData.FavTableInfo.TableName)){
                            favItem = (FavFragmentStuff.FavItem) value;
                            st.executeUpdate("INSERT INTO " + Table + " VALUES ('" + favItem.name + "'," + favItem.price + "," + favItem.image + ",'"+email_id+"');");
                        } else if (Table.equals(GoShopApplicationData.OrderHistoryTableInfo.TableName)){
                            orderItem = (OrderHistoryStuff.OrderItem) value;
                            st.executeUpdate("INSERT INTO " + Table + " VALUES (" + orderItem.oid + ",'" + orderItem.items + "'," + orderItem.total + ",'" + orderItem.date + "','"+email_id+"');");
                        }
                    } else if (update) {
                        if (Table.equals(GoShopApplicationData.CartTableInfo.TableName)) {
                            cartItem = (CartFragmentStuff.CartItem) value;
                            st.executeUpdate("UPDATE " + Table + " SET quantity="+cartItem.qty+",subtotal="+cartItem.subtotal+" WHERE (emailid='" + email_id + "' AND itemname='" + cartItem.name + "');");
                        }
                    } else {
                        if (Table.equals(GoShopApplicationData.CartTableInfo.TableName)) {
                            cartItem = (CartFragmentStuff.CartItem) value;
                            st.executeUpdate("DELETE FROM " + Table + " WHERE (emailid='"+email_id+"' AND itemname='" + cartItem.name + "');");
                        } else if (Table.equals(GoShopApplicationData.FavTableInfo.TableName)){
                            favItem = (FavFragmentStuff.FavItem) value;
                            st.executeUpdate("DELETE FROM " + Table + " WHERE (emailid='"+email_id+"' AND itemname='" + favItem.name + "');");
                        } else if (Table.equals(GoShopApplicationData.OrderHistoryTableInfo.TableName)){
                            orderItem = (OrderHistoryStuff.OrderItem) value;
                            st.executeUpdate("DELETE FROM " + Table + " WHERE (emailid='"+email_id+"' AND orderid='" + orderItem.oid + "');");
                        }
                    }
                }
                if (MenuActivityStuff.ITEMS.size() != 0) {
                    resultString = "DB Connected";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return resultString;
        }

        //this method executes after fetching the data from db
        protected void onPostExecute(String result) {
            if (result.equals("")){
                mProgressDialog = ProgressDialog.show(context, "",
                        context.getString(R.string.loginActivityMsg4));
                //Toast.makeText(context, R.string.loginActivityMsg4, Toast.LENGTH_SHORT).show();
            } else {
                if (Table.equals(GoShopApplicationData.InventoryTableInfo.TableName)) {
                    //Toast.makeText(context, "Data Base connected and data fetched successfully", Toast.LENGTH_SHORT).show();
                    ((ResultListener) context).onDBReadSuccessful(true);
                    mProgressDialog.dismiss();
                }
            }
        }
    }

    //Interface to call from the MenuActivity to fire the HomeFragment
    public interface ResultListener{
        void onDBReadSuccessful(boolean result);
    }
}
