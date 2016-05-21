package com.cs442.Team14;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Registration page for first time login
 *
 * @author Hrushikesh Vasista
 * @since 13-Mar-2016
 */
public class RegistrationActivity extends AppCompatActivity {

    String tosendemail;
    String tosendname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        EditText email = (EditText) findViewById(R.id.registrationEmail);
        final EditText userName = (EditText) findViewById(R.id.registrationUserName);

        final SharedPreferences preferences =
                getSharedPreferences(GoShopApplicationData.GO_SHOP_SHARED_PREFERENCES
                        , MODE_PRIVATE);

        //pre-fill email id and username
        email.setText(preferences.getString(GoShopApplicationData.USER_EMAIL, ""));
        userName.setText(preferences.getString(GoShopApplicationData.USER_NAME, ""));

        //on Register button clicked
        Button button = (Button) findViewById(R.id.registrationButtonSubmit);

        try {
            if (button != null) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EditText phone = (EditText) findViewById(R.id.registrationPhone);
                        EditText address = (EditText) findViewById(R.id.registrationAddress);


                        //Validating the entered fields
                        if (userName.getText().toString().equals(""))
                            userName.setError(getString(R.string.registrationActivityMsg1));

                        else if (phone.getText().toString().equals(""))
                            phone.setError(getString(R.string.registrationActivityMsg2));

                        else if (address.getText().toString().equals(""))
                            address.setError(getString(R.string.registrationActivityMsg3));

                        else {
                            //Save the user details in shared preference

                            SharedPreferences.Editor editor = preferences.edit();

                            editor.putString(GoShopApplicationData.USER_NAME, userName.getText().toString());
                            editor.putString(GoShopApplicationData.USER_PHONE, phone.getText().toString());
                            editor.putString(GoShopApplicationData.USER_ADDRESS, address.getText().toString());
                            editor.putBoolean(GoShopApplicationData.USER_LOGGED_IN_STATUS, true);
                            editor.putBoolean(GoShopApplicationData.USER_CREDENTIAL_EXISTS, true);
                            editor.commit();


                            tosendemail=preferences.getString(GoShopApplicationData.USER_EMAIL, "");
                            tosendname=preferences.getString(GoShopApplicationData.USER_NAME, "");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
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

                                    try {
                                        String tmp=tosendemail;
                                        Log.i("Email", tmp);
                                        MimeMessage message = new MimeMessage(session);
                                        message.setFrom(new InternetAddress("goshopappiit@gmail.com"));
                                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(tmp));
                                        message.setSubject("Registration- Goshop App");
                                        message.setText("Dear "+tosendname+",\n\tThank you for Signing up please feel free to contact us at goshopappiit@gmail.com"+"!");
                                        Transport.send(message);
                                    } catch (Exception e) {e.printStackTrace();}

                                }
                            }).start();


                            //Registration complete, display home page
                            Intent intent = new Intent(RegistrationActivity.this, MenuActivity.class);
                            startActivity(intent);

                            //End registration activity
                            finish();
                        }
                    }
                });
            }
        }catch(Exception e){


        }
    }

}
