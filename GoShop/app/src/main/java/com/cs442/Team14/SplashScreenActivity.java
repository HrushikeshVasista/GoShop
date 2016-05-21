package com.cs442.Team14;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;


/**
 * Displays splash screen
 *
 * @author Hrushikesh Vasista
 * @since 13-Mar-2016
 */
public class SplashScreenActivity extends AppCompatActivity {

    //Shows splash screen for 2s
    private static final int SPLASH_TIMEOUT = 2000;

    //instance of GoogleApiClient for current login
    private GoogleApiClient mGoogleApiClient;

    private Application application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        application = getApplication();

        //Initialise Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext(), GoShopApplicationData.getInstance().FACEBOOK_LOGIN_START_OFFSET);
        AppEventsLogger.activateApp(application);

        //Initialise Google sign-in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this).
                addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }


    @Override
    protected void onResume() {
        super.onResume();

        //Go to next screen after showing splash screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NextActivityIntent();
            }
        }, SPLASH_TIMEOUT);
    }

    /*
     Transits to next screen either by Google silent sign-in or Facebook auto-login
     */
    private void NextActivityIntent()
    {
        SharedPreferences preferences = getSharedPreferences(GoShopApplicationData.GO_SHOP_SHARED_PREFERENCES, MODE_PRIVATE);
        boolean isLogged = preferences.getBoolean(GoShopApplicationData.USER_LOGGED_IN_STATUS, false);

        if(isLogged)
        {
            //If logged in through Google credentials...
            if(preferences.getString(GoShopApplicationData.SIGN_IN_TYPE, "")
                    .equals(GoShopApplicationData.GOOGLE_SIGNIN_TYPE))
            {
                DoGoogleSilentSignIn();

            }
            //If logged-in through FB credentials...
            else{

                DoFacebookAutoLogIn();
            }

        }
        else
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));

    }


    private void DoFacebookAutoLogIn(){

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Profile.fetchProfileForCurrentAccessToken();
        Profile profile = Profile.getCurrentProfile();
        GoShopApplicationData.getInstance().setFacebookProfile(profile);
        goToMenuActivity();
    }

    private void DoGoogleSilentSignIn(){

        mGoogleApiClient.connect();

        OptionalPendingResult<GoogleSignInResult> pendingResult =
                Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        //Save the GoogleApiClient instance
        GoShopApplicationData.getInstance().setGoogleApiClient(mGoogleApiClient);

        if (pendingResult.isDone()) {

            // There's immediate result available.
            GoogleSignInResult result = pendingResult.get();

            goToMenuActivity();

        } else {

            // There's no immediate result ready. Wait or an asynchronous call to onResult()
            pendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {

                @Override
                public void onResult(GoogleSignInResult result) {
                    if (result.isSuccess())
                        goToMenuActivity();
                    else
                    {
                        goToMenuActivity();
                    }
                }
            });
        }
    }

    private void goToMenuActivity(){

        //Go to home page
        Intent intent = new Intent(SplashScreenActivity.this, MenuActivity.class);
        startActivity(intent);

        //End splash screen
        finish();
    }

}
