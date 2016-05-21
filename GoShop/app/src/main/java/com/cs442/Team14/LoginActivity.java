package com.cs442.Team14;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;


/**
 * A login screen that offers login via email/password or Google sign-in
 *
 * @author Hrushikesh Vasista
 * @since 13-Mar-2016
 */
public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        FacebookCallback<LoginResult> {


    //Points to the {@link GoShopApplicationData#GO_SHOP_SHARED_PREFERENCES} shared preference     *
    private SharedPreferences preferences;

    //To get reference to GoShopApplicationData
    private GoShopApplicationData applicationData;

    //Facebook callback manager
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        applicationData = GoShopApplicationData.getInstance();
        preferences = getSharedPreferences(GoShopApplicationData.GO_SHOP_SHARED_PREFERENCES, MODE_PRIVATE);


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile()
                .build();

        //Store the GoogleApiClient instance
        applicationData.setGoogleApiClient(new GoogleApiClient.Builder(this).
                addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build());

        //On click, start Google sign-in
        findViewById(R.id.sign_in_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(applicationData.getGoogleApiClient());
                applicationData.getGoogleApiClient().connect();
                startActivityForResult(signInIntent, GoShopApplicationData.GOOGLE_SIGN_IN_SUCCESSFUL);

            }
        });

        //Facebook login client
        LoginButton facebookLoginButton = (LoginButton) findViewById(R.id.login_button);

        facebookLoginButton.setReadPermissions("public_profile");
        facebookLoginButton.setReadPermissions("email");

        //Register for callbacks
        callbackManager = CallbackManager.Factory.create();
        facebookLoginButton.registerCallback(callbackManager, this);

        //On click, start Facebook login
        findViewById(R.id.login_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        boolean isFbResultHandled = callbackManager.onActivityResult(requestCode, resultCode, data);

        if(!isFbResultHandled){

            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == GoShopApplicationData.GOOGLE_SIGN_IN_SUCCESSFUL) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {

            String googleUserName = result.getSignInAccount().getDisplayName();
            String email = result.getSignInAccount().getEmail();
            Uri url = result.getSignInAccount().getPhotoUrl();
            SharedPreferences.Editor editor = preferences.edit();


            Toast.makeText(LoginActivity.this, getString(R.string.loginActivityMsg3), Toast.LENGTH_SHORT).show();
            editor.putString(GoShopApplicationData.SIGN_IN_TYPE, GoShopApplicationData.GOOGLE_SIGNIN_TYPE)
                    .putString(GoShopApplicationData.USER_NAME, googleUserName)
                    .putString(GoShopApplicationData.USER_EMAIL, email)
                    .putBoolean(GoShopApplicationData.USER_LOGGED_IN_STATUS, true);

            //Save user's email id
            //GoShopApplicationData.EMAIL_ID = email;

            if(url!=null)
                editor.putString(GoShopApplicationData.GOOGLE_USER_PHOTO_URL
                        , result.getSignInAccount().getPhotoUrl().toString());
            else
                editor.putString(GoShopApplicationData.GOOGLE_USER_PHOTO_URL, "");

            editor.commit();

            //Go to Registration or Menu Activity
            startActivity(IsRegistrationPending());

            //End login activity
            finish();

        } else {
            Toast.makeText(LoginActivity.this, getString(R.string.loginActivityMsg4), Toast.LENGTH_SHORT).show();

        }
    }

    //Google sign-in callback
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(LoginActivity.this, getString(R.string.loginActivityMsg4), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(LoginActivity.this, getString(R.string.loginActivityMsg3), Toast.LENGTH_SHORT).show();
    }

    //-----------------------

    //Facebook login callbacks
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onSuccess(final LoginResult loginResult) {

        preferences.edit()
                .putString(GoShopApplicationData.SIGN_IN_TYPE, GoShopApplicationData.FB_LOGIN_TYPE)
                .putBoolean(GoShopApplicationData.USER_LOGGED_IN_STATUS, true)
                .commit();

        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken()
                , new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    //Save user's email id
                    //GoShopApplicationData.EMAIL_ID = object.getString("email");
                    preferences.edit()
                            .putString(GoShopApplicationData.USER_NAME, object.getString("name"))
                            .putString(GoShopApplicationData.USER_EMAIL, object.getString("email"))
                            .putBoolean(GoShopApplicationData.USER_LOGGED_IN_STATUS, true)
                            .commit();

                    //Go to Registration or Menu Activity
                    startActivity(IsRegistrationPending());

                    //End login activity
                    finish();

                }
                catch(Exception e){
                    Toast.makeText(LoginActivity.this, "Email-id could not be retrieved", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();

        ProfileTracker tracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                Toast.makeText(LoginActivity.this, R.string.loginActivityMsg3, Toast.LENGTH_SHORT).show();
                stopTracking();

                Profile.fetchProfileForCurrentAccessToken();
                Profile profile = Profile.getCurrentProfile();
                GoShopApplicationData.getInstance().setFacebookProfile(profile);
            }
        };

    }

    private Intent IsRegistrationPending(){
        if(!(preferences.getBoolean(GoShopApplicationData.USER_CREDENTIAL_EXISTS, false))){
            //Go to registration page
            return(new Intent(LoginActivity.this, RegistrationActivity.class));
        }
        else{
            //Go to home page
            return(new Intent(LoginActivity.this, MenuActivity.class));
        }
    }
    @Override
    public void onCancel() {
        //do nothing
    }

    @Override
    public void onError(FacebookException error) {
        Toast.makeText(LoginActivity.this, R.string.splashActivityMsg1, Toast.LENGTH_SHORT).show();
    }

}

