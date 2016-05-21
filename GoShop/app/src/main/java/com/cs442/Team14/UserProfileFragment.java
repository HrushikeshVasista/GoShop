package com.cs442.Team14;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * UserProfileFragment -Fragment
 * This Fragment allows the user to View the User's Profile Info
 * and also edit the User's  Basic Information
 * @author PraveenKumar
 */
public class UserProfileFragment extends Fragment {



    private static int RESULT_LOAD_IMAGE = 1;

    private KeyListener listeneruname,listenerphone,listeneraddress;
    ImageView profile;
    String nameuser,phone,address,Email;
    String ProfilePicture;
    EditText userName;
    EditText userPhone;
    EditText userAddress;
    EditText Emailid;



    public UserProfileFragment() {

    }

    public static UserProfileFragment getInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /**
         * Creating an Instance of Shared Preference to retrieve the  values GO_SHOP_SHARED_PREFERENCES
         */

        SharedPreferences mSettings = getActivity().getSharedPreferences(GoShopApplicationData.GO_SHOP_SHARED_PREFERENCES, 0);

        /**
         * Column name in {@link #GO_SHOP_SHARED_PREFERENCES} to hold the user's name
         * Value retrieed and shown as UserName
         */
        nameuser =mSettings.getString(GoShopApplicationData.USER_NAME, "*/no value available*/");
        /**
         * Column name in {@link #GO_SHOP_SHARED_PREFERENCES} to hold the user's phone
         * Value retrieve and shown as User's Phone number
         */
        phone=mSettings.getString(GoShopApplicationData.USER_PHONE, "*/no value available*/");
        /**
         * Column name in {@link #GO_SHOP_SHARED_PREFERENCES} to hold the user's address
         * * Value retrieve and shown User's Address
         */
        address=mSettings.getString(GoShopApplicationData.USER_ADDRESS, "*/no value available*/");
        /**
         * Column name in {@link #GO_SHOP_SHARED_PREFERENCES} to hold the user's Email id
         * Value retrieve and shown as User's Email id
         */
        Email=mSettings.getString(GoShopApplicationData.USER_EMAIL, "");

        /**
         * Column name in {@link #GO_SHOP_SHARED_PREFERENCES} to hold the user's Profile Pic
         * Value retrieve and shown as User's Profile Picture
         * */
        ProfilePicture= mSettings.getString(GoShopApplicationData.pictureurl, "drawable://" + R.drawable.profile);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*
        * Inflates the UserProfile layout for this Fragment
        * */
        View view1 = inflater.inflate(R.layout.fragment_userprofile, container, false);



        /**
         * Assign retrieved values from shared preference to the specific fields
         * in UserProfileFragment Layout
         */
        userName=(EditText)view1.findViewById(R.id.namefield);
        listeneruname = userName.getKeyListener();
        userName.setKeyListener(null);

        userPhone=(EditText)view1.findViewById(R.id.phonefield);
        listenerphone=userPhone.getKeyListener();
        userPhone.setKeyListener(null);

        userAddress=(EditText)view1.findViewById(R.id.addrfield);
        listeneraddress=userAddress.getKeyListener();
        userAddress.setKeyListener(null);

        Emailid=(EditText)view1.findViewById(R.id.emailfield);
        Emailid.setKeyListener(null);

        profile=(ImageView)view1.findViewById(R.id.addpicture);

        userName.setText(nameuser);
        userPhone.setText(phone);
        userAddress.setText(address);
        Emailid.setText(Email);

        if(GoShopApplicationData.USER_PIC!=null)
            profile.setImageBitmap(GoShopApplicationData.USER_PIC);


        /*
        * Edit button-onclicklistener
        * Enables Edit option in User Profile Which allows user to Edit Details
        * */
        Button button = (Button) view1.findViewById(R.id.button2);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                userName.setKeyListener(listeneruname);
                userPhone.setKeyListener(listenerphone);
                userAddress.setKeyListener(listeneraddress);
                //Emailid.setKeyListener(listeneremail);
            }
        });

        /*
        * update button-onclicklistener
        * Updates all changes made by the user to his profile
        * */
        Button button3 = (Button) view1.findViewById(R.id.button3);
        button3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {



                /**
                 * User Edited values are updated to the  Shared Preference
                 */
                SharedPreferences preferences =
                        getActivity().getSharedPreferences(GoShopApplicationData.GO_SHOP_SHARED_PREFERENCES
                                , 0);

                SharedPreferences.Editor editor = preferences.edit();

                String name=userName.getText().toString();
                String email=Emailid.getText().toString();
                // String strUserName = etUserName.getText().toString();

                if(TextUtils.isEmpty(name)) {
                    userName.setError("Name cannot be empty");
                    if(TextUtils.isEmpty(email))
                        Emailid.setError("Email cannot be empty ");
                    return;

                }

                editor.putString(GoShopApplicationData.USER_NAME, userName.getText().toString());
                editor.putString(GoShopApplicationData.USER_PHONE, userPhone.getText().toString());
                editor.putString(GoShopApplicationData.USER_EMAIL, Emailid.getText().toString());
                editor.putString(GoShopApplicationData.USER_ADDRESS, userAddress.getText().toString());
                editor.commit();
                Toast.makeText(getActivity(), "Updating Profile", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), MenuActivity.class);
                startActivity(intent);




            }


        });

        /**
         * Button to Edit/Update User Profile Picture
         * Provides option to enter Gallery and update userProfile Picture
         */

        Button button4 = (Button) view1.findViewById(R.id.button);
        button4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);

            }
        });


        return view1;
    }

    /**
     * OnRecieving Result from Gallery Intent
     * Load Image into Data and retrieve as Bitmap
     * Update to User's Database
     *
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImage=null;
        if (requestCode == RESULT_LOAD_IMAGE  && null != data) {
            MenuActivity.defaultpicture=false;
            selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            String tempurl;
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            tempurl = cursor.getString(columnIndex);
            cursor.close();

            profile.setImageBitmap(BitmapFactory.decodeFile(tempurl));

            SharedPreferences preferences =
                    getActivity().getSharedPreferences(GoShopApplicationData.GO_SHOP_SHARED_PREFERENCES
                            , 0);

            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(GoShopApplicationData.pictureurl, tempurl);
            editor.commit();
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().setTitle(R.string.title_fragment_userProfile);
    }


}
