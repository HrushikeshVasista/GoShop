package com.cs442.Team14;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class ContactUsActivity extends AppCompatActivity {

    //EditText fbTitle;
    EditText fbComments;
    RatingBar rating;
    Button submitFb;
    Button gmaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_activity_contact_us);

        //fbTitle = (EditText) findViewById(R.id.feedbckTitle);
        fbComments = (EditText) findViewById(R.id.feedbckComments);
        rating = (RatingBar) findViewById(R.id.ratingBar);
        submitFb = (Button) findViewById(R.id.submit_feedbck);
        rating.setRating(5);
        gmaps = (Button) findViewById(R.id.add_map);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        /*String uri = String.format(Locale.ENGLISH, "https://www.google.com/maps/dir//Illinois+Institute+of+Technology,+3300+South+Federal+Street,+Chicago,+IL+60616/@41.8349317,-87.6620253,13z/data=!3m1!4b1!4m8!4m7!1m0!1m5!1m1!1s0x880e2c72d3e9a80f:0x91cbe4661afb6f1a!2m2!1d-87.6270059!2d41.8348731", 41.8349317, -87.6620253);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
*/

        gmaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "http://maps.google.com/maps?daddr=3300+S+Federal+St,+Chicago,+IL+60616";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });


        submitFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float r = rating.getRating();
                //final String title = fbTitle.getText().toString();
                String comments = fbComments.getText().toString();
                if (comments.equals("")){
                    Toast.makeText(ContactUsActivity.this, R.string.enterTitle, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ContactUsActivity.this, R.string.contactUsMsg1, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

}
