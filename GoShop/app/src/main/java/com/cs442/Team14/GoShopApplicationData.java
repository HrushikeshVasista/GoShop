package com.cs442.Team14;

import android.app.Application;
import android.graphics.Bitmap;

import com.facebook.Profile;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * This singleton class contains the project-wide accessed constants
 * (names of databases, shared preferences and columns etc)
 *
 * @author Hrushikesh Vasista
 * @since 13-Mar-2016
 */
public class GoShopApplicationData extends Application {

    private static GoShopApplicationData instance = null;

    private GoogleApiClient googleApiClient;

    private Profile facebookProfile;

    /**
     *Name of the shared preference
     */
    public static final String GO_SHOP_SHARED_PREFERENCES = "GoShop_Shared_Preferences";

    /**
     * Column name in {@link #GO_SHOP_SHARED_PREFERENCES}to hold the user's logged-in status
     * <b>true:</b> user is logged-in
     * <b>false:</b> user is logged-out     *
     */
    public static final String USER_LOGGED_IN_STATUS = "Logged_Status";

    /**
     * DB to be accessed or not
     */
    public static Boolean ACCESS_DB = true;

    /**
     * URL of database
     */
    public static final String DB_URL = //replace with your DB's url;

    public static final String USER = //replace with your DB's user id;

    public static final String PASS = //replace with your DB's password;

    /**
     * Column name in {@link #GO_SHOP_SHARED_PREFERENCES} to hold the user's name     *
     */
    public static final String USER_NAME = "User_Name";

    /**
     * Column name in {@link #GO_SHOP_SHARED_PREFERENCES} to hold the user's email-id     *
     */
    public static final String USER_EMAIL = "User_Email";

    /**
     * Column name in {@link #GO_SHOP_SHARED_PREFERENCES} to hold the user's Phone number     *
     */
    public static final String USER_PHONE = "User_Phone";

    /**
     * Column name in {@link #GO_SHOP_SHARED_PREFERENCES} to hold the user's address     *
     */
    public static final String USER_ADDRESS = "User_Address";

    /**
     * Column name in {@link #GO_SHOP_SHARED_PREFERENCES} to hold whether the user is registered
     * <b>true:</b> user has already registered on this phone
     * <b>false:</b> new user, needs registration
     */
    public static final String USER_CREDENTIAL_EXISTS = "User_Credentials_Status";

    /**
     * Column name in {@link #GO_SHOP_SHARED_PREFERENCES} to hold Sign-in type:
     * {@link #GOOGLE_SIGNIN_TYPE},{@link #FB_LOGIN_TYPE}
     */
    public static final String SIGN_IN_TYPE = "SignIn_Type";

    /**
     * Column name in {@link #GO_SHOP_SHARED_PREFERENCES} to hold DB_URL of user's image
     */
    public static final String GOOGLE_USER_PHOTO_URL = "Google_Photo_Url";

    public static Bitmap USER_PIC = null;
    /**
     * Logged-in through Google
     * @see {@link #SIGN_IN_TYPE}
     */
    public static final String GOOGLE_SIGNIN_TYPE = "GoogleSignIn";

    /**
     * Logged-in through Facebook
     * @see {@link #SIGN_IN_TYPE}
     */
    public static final String FB_LOGIN_TYPE = "FacebookSignIn";

    /**
     * Tag name for {@link HomeFragment}
     */
    public static final String HOME_FRAGMENT = "HomeFragment";

    public static final String HOME_FRAGMENT_X = "ProduceFragment_X";

    /**
     * Tag name for {@link CartFragment}
     */
    public static final String CART_FRAGMENT            = "CartFragment";

    /**
     * Tag name for {@link UserProfileFragment}
     */
    public static final String USER_PROFILE_FRAGMENT    = "UserProfileFragment";

    /**
     * Tag name for {@link FavoritesFragment}
     */
    public static final String FAVORITES_FRAGMENT       = "FavoritesFragment";

    /**
     * Tag name for {@link OrderHistoryFragment}
     */
    public static final String ORDER_HISTORY_FRAGMENT   = "OrderHistFragment";

    /**
     * Tag name for {@link InviteFriendsFragment}
     */
    public static final String INVITE_FRIENDS_FRAGMENT  = "InviteFragment";


    public static final String pictureurl  ="drawable://" + R.drawable.profile;


    //public static String EMAIL_ID = "";

       //MenuList Table details
    public static abstract class InventoryTableInfo {
        public static final String TableName = "inventory";
        public static final String item_name = "itemname";
        public static final String price = "price";
        public static final String description = "description";
        public static final String image = "image";
        public static final String category = "category";
    }

    //CartTable Table details
    public static abstract class CartTableInfo {
        public static final String TableName = "carttable";
        public static final String item_name = "itemname";
        public static final String quantity = "quantity";
        public static final String subtotal = "subtotal";
        public static final String image = "image";
    }

    //FavoritesTable Table details
    public static abstract class FavTableInfo {
        public static final String TableName = "favorites";
        public static final String item_name = "itemname";
        public static final String price = "price";
        public static final String image = "image";
    }

    //OrderHistory Table details
    public static abstract class OrderHistoryTableInfo {
        public static final String TableName = "orderhistory";
        public static final String oid = "orderid";
        public static final String items = "items";
        public static final String total = "total";
        public static final String date = "orderdate";
    }

    /**
     *Request code on payment failure
     */
    public static final int PAYMENT_FAILURE = 0;


    /**
     * Request code for successful Google sign-in
     */
    public static final int GOOGLE_SIGN_IN_SUCCESSFUL = 100;

    /**
     *Request code for succesful payment
     */
    public static final int PAYMENT_SUCCESSFUL = 101;

    /**
     * Start offset for Facebook request codes
     */
    public static final int FACEBOOK_LOGIN_START_OFFSET = 200;


    //This class cannot be instantiated
    private GoShopApplicationData(){

    }

    /**
     * returns the reference to existing GoShopApplicationData instance
     *
     * @return reference to GoShopApplicationData
     */
    public static GoShopApplicationData getInstance() {
        if (null == instance){
            synchronized (GoShopApplicationData.class){
                instance = new GoShopApplicationData();
            }
        }
        return instance;
    }

    /**
     * returns the reference to GoogleApiClient object
     *
     * @return GoogleApiClient of the current log-in
     */
    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    /**
     * stores the GoogleApiClient object
     *
     * @param googleApiClient
     */
    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    public void setFacebookProfile(Profile facebookProfile) {
        this.facebookProfile = facebookProfile;
    }

    public Profile getFacebookProfile() {
        return facebookProfile;
    }
}
