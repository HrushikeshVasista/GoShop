package com.cs442.Team14;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs442.Team14.dummy.CartFragmentStuff;
import com.cs442.Team14.dummy.FavFragmentStuff;
import com.cs442.Team14.dummy.MenuActivityStuff;
import com.cs442.Team14.dummy.OrderHistoryStuff;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Main activity through which user can navigate to home page, user profile page, cart page,
 * favorites page, order history page, invite friends and perform logout
 *
 * @author Hrushikesh Vasista, Prashanth Molakala
 * @since 13-Mar-2016
 */
public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        ,GoogleApiClient.OnConnectionFailedListener
        ,GoogleApiClient.ConnectionCallbacks
        ,CartFragment.OnCartFragmentInteractionListener
        ,DBOperations.ResultListener {

    public static boolean defaultpicture=true;
    /**
     * Reference to singleton GoShopApplicationData
     */
    private GoShopApplicationData applicationData;

    /**
     * Reference to the common shared preference
     */
    private SharedPreferences preferences;

    /**
     * Reference to the common database operations
     */
    private DBOperations dbOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applicationData = GoShopApplicationData.getInstance();
        preferences = getSharedPreferences(applicationData.GO_SHOP_SHARED_PREFERENCES, MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Creating an instance of DBOperations class
        dbOperations = new DBOperations(this);

        //to fetch the data from database
        if(GoShopApplicationData.ACCESS_DB) {
            GoShopApplicationData.ACCESS_DB = false;
            MenuActivityStuff.clearData();
            CartFragmentStuff.ITEMS.clear();
            CartFragmentStuff.ITEM_MAP.clear();
            FavFragmentStuff.ITEMS.clear();
            FavFragmentStuff.ITEM_MAP.clear();
            OrderHistoryStuff.ITEMS.clear();

            dbOperations.getData(GoShopApplicationData.InventoryTableInfo.TableName);
            dbOperations.getData(GoShopApplicationData.CartTableInfo.TableName);
            dbOperations.getData(GoShopApplicationData.FavTableInfo.TableName);
            dbOperations.getData(GoShopApplicationData.OrderHistoryTableInfo.TableName);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        //Empty the backstack
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //Dont add to backstack here
        transaction
                .replace(R.id.main_activity_fragment, new HomeFragment(), GoShopApplicationData.HOME_FRAGMENT_X)
                //.addToBackStack(GoShopApplicationData.HOME_FRAGMENT_X)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this
                , drawer
                , toolbar
                , R.string.navigation_drawer_open
                , R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displayUserDetails(navigationView.getHeaderView(0));

        GoShopEULA eula = new GoShopEULA(this);
        eula.show();


    }

    //Displays the User details - name, profile picture and email id
    private void displayUserDetails(View view){

        TextView name = (TextView) view.findViewById(R.id.menuActivityUserName);
        TextView email = (TextView) view.findViewById(R.id.menuActivityUserEmail);
        String signinType = preferences.getString(GoShopApplicationData.SIGN_IN_TYPE, "");

        if(signinType.equals(GoShopApplicationData.GOOGLE_SIGNIN_TYPE)){

            //display google account details
            name.setText(preferences.getString(GoShopApplicationData.USER_NAME, ""));
            email.setText(preferences.getString(GoShopApplicationData.USER_EMAIL, ""));

            //if profile pic exists...
            if(!(preferences.getString(GoShopApplicationData.GOOGLE_USER_PHOTO_URL, "").equals(""))){

                String url = preferences.getString(GoShopApplicationData.GOOGLE_USER_PHOTO_URL, "");
                DownloadImage downloadImage = new DownloadImage(url);
                downloadImage.execute();
            }
        }
        else {

            Profile profile = GoShopApplicationData.getInstance().getFacebookProfile();

            //display google account details
            name.setText(preferences.getString(GoShopApplicationData.USER_NAME, ""));
            email.setText(preferences.getString(GoShopApplicationData.USER_EMAIL, ""));

            //if profile pic exists...
            try {
                String url = "https://graph.facebook.com/" + profile.getId() + "/picture?type=large";
                DownloadImage downloadImage = new DownloadImage(url);
                downloadImage.execute();
            } catch (Exception e) {
                //Profile pic could not be found
            }
        }
    }

    //Categorises the list of item into Produce, Bevarages or Bakery
    public void categorizeLists(){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<MenuActivityStuff.ITEMS.size();i++){
                    if (MenuActivityStuff.ITEMS.get(i).category.equals("Produce")){
                        MenuActivityStuff.PRODUCE_ITEMS.add(MenuActivityStuff.ITEMS.get(i));
                        //MenuActivityStuff.start_produce++;
                    } else if (MenuActivityStuff.ITEMS.get(i).category.equals("Beverages")){
                        MenuActivityStuff.BEVERAGE_ITEMS.add(MenuActivityStuff.ITEMS.get(i));
                        //MenuActivityStuff.start_beverages++;
                    } else if (MenuActivityStuff.ITEMS.get(i).category.equals("Bakery")){
                        MenuActivityStuff.BAKERY_ITEMS.add(MenuActivityStuff.ITEMS.get(i));
                        //MenuActivityStuff.start_bakery++;
                    }
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {

            FragmentManager fragmentManager = getSupportFragmentManager();

            if(fragmentManager.getBackStackEntryCount()>1){
                fragmentManager.popBackStack();

            }
            else{
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this,ContactUsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = null;

        //Clear backstack until Home Fragment
        manager.popBackStack(GoShopApplicationData.HOME_FRAGMENT, 0);

        String fragmentTagName = "";

        switch(id){

            case R.id.homePage:

                fragment = new HomeFragment();
                fragmentTagName = GoShopApplicationData.HOME_FRAGMENT;
                FragmentCommit(transaction, fragment, fragmentTagName);
                break;

            case R.id.cartPage:

                fragment = new CartFragment();
                fragmentTagName = GoShopApplicationData.CART_FRAGMENT;
                FragmentCommit(transaction, fragment, fragmentTagName);
                break;

            case R.id.userProfile:

                fragment = new UserProfileFragment();
                fragmentTagName = GoShopApplicationData.USER_PROFILE_FRAGMENT;
                FragmentCommit(transaction, fragment, fragmentTagName);

                break;

            case R.id.favoritesPage:

                fragment = new FavoritesFragment();
                fragmentTagName = GoShopApplicationData.FAVORITES_FRAGMENT;
                FragmentCommit(transaction, fragment, fragmentTagName);
                break;

            case R.id.orderHistory:

                fragment = new OrderHistoryFragment();
                fragmentTagName = GoShopApplicationData.ORDER_HISTORY_FRAGMENT;
                FragmentCommit(transaction, fragment, fragmentTagName);

                break;
            case R.id.inviteFriends:

                fragment = new InviteFriendsFragment();
                fragmentTagName = GoShopApplicationData.INVITE_FRIENDS_FRAGMENT;
                FragmentCommit(transaction, fragment, fragmentTagName);

                break;

            case R.id.logout:

                //clear backstack
                manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                preferences.edit().putBoolean(GoShopApplicationData.USER_LOGGED_IN_STATUS, false).commit();
                if(preferences.getString(GoShopApplicationData.SIGN_IN_TYPE,"").equals(GoShopApplicationData.GOOGLE_SIGNIN_TYPE)){
                    //Signout from Google
                    Auth.GoogleSignInApi.signOut(applicationData.getGoogleApiClient());
                }
                else {
                    //Signout from Facebook
                    LoginManager.getInstance().logOut();
                }

                Toast.makeText(MenuActivity.this, getString(R.string.menuActivityMsg2), Toast.LENGTH_SHORT).show();


                //Show login page
                Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

                break;

            case R.id.aboutGoShop:

                //Display dialog
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_menu_about_goshop)
                        .setTitle(R.string.menuActivityAboutGoShop)
                        .setMessage(R.string.aboutGoShopText)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create();
                alertDialog.show();

                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void FragmentCommit(FragmentTransaction transaction, Fragment fragment, String fragmentTagName){
        transaction
                .replace(R.id.main_activity_fragment, fragment, fragmentTagName)
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if(!(fragment instanceof HomeFragment)){
            transaction.addToBackStack(fragmentTagName);
        }
        transaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(MenuActivity.this, getString(R.string.menuActivityMsg2), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void PaymentSuccessful(final int orderId, final String orderItems, final double orderAmt) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                SharedPreferences preferences;
                preferences = getSharedPreferences(applicationData.GO_SHOP_SHARED_PREFERENCES, MODE_PRIVATE);
                String tosend=preferences.getString(GoShopApplicationData.USER_EMAIL, "");

                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.socketFactory.class",
                        "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "465");

                Session session = Session.getDefaultInstance(props,
                        new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication("goshopappiit@gmail.com","goshop123");//change your password accordingly
                            }
                        });

                //compose message
                try {
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress("goshopappiit@gmail.com"));
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(tosend));
                    message.setSubject("Order Placed -Order ID- " + orderId);
                    message.setText("Thank you for your Purchase your order will be delivered within two hours\n" +
                            "Order Details\n" +
                            "Order Id: " + orderId + "\t Total Price = " + orderAmt +
                            "\n Items Purchased: \n" + orderItems);

                    Transport.send(message);
                } catch (Exception e) {e.printStackTrace();}

            }
        }).start();




        //Clear backstack
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack(GoShopApplicationData.CART_FRAGMENT, 0);

        //Clear the DB for cart items
        //dbOperations.clearData(GoShopApplicationData.CartTableInfo.TableName);

        //Display the default fragment i.e Home fragment
        Fragment fragment = new HomeFragment();
        String fragmentTagName = GoShopApplicationData.HOME_FRAGMENT;
        FragmentCommit(manager.beginTransaction(), fragment, fragmentTagName);

        onBackPressed();
    }

    @Override
    public void onDBReadSuccessful(boolean result){
        //Default fragment is the menulist fragment
        if (result) {
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            categorizeLists();
            tx
            .replace(R.id.main_activity_fragment, new HomeFragment(), GoShopApplicationData.HOME_FRAGMENT)
            .addToBackStack(GoShopApplicationData.HOME_FRAGMENT)
            .commit();
        }
    }

    /**
     *An aysnc task to download image from the given URL
     *
     * @author Hrushikesh Vasista
     * @since 13-Mar-2016
     */
    private class DownloadImage extends AsyncTask<String, Void, Boolean>{

        /**
         * DB_URL where the image resides
         */
        private String uRL;

        /**
         * Location in navigation drawer header to display user image
         */
        ImageView image;

        /**
         * Downloaded image
         */
        Bitmap bitmap;

        public DownloadImage(String url){
            this.uRL = url;
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                InputStream inputStream = new URL(uRL).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                GoShopApplicationData.getInstance().USER_PIC = bitmap;
                return true;

            } catch (Exception e) {

            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean){
                try{
                    image = (ImageView)findViewById(R.id.menuActivityUserImage);
                    image.setImageBitmap(bitmap);
                } catch (Exception e){
                    Toast.makeText(MenuActivity.this, getString(R.string.menuActivityMsg3), Toast.LENGTH_SHORT).show();
                }

            }
        }

    }
}
