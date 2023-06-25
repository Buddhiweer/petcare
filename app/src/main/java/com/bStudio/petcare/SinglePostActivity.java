package com.bStudio.petcare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class SinglePostActivity extends AppCompatActivity {

    TextView singleBreed, singleAage, singleSize, singlePublishDate, singleAddress, singleAbout;
    ImageView singleImage;
    Button buttonCall;
    Button buttonLocation;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);


        // Calling method
        buttonCall = findViewById(R.id.btnCall);
        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = getIntent().getStringExtra("singleContactNumber");
                Uri phoneUri = Uri.parse("tel:" + phoneNumber);
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, phoneUri);
                startActivity(dialIntent);
            }
        });

        // View Location method
        buttonLocation = findViewById(R.id.btnViewLocation);
        buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SinglePostActivity.this, ViewLocationActivity.class);
                startActivity(intent);
            }
        });


        singleBreed = findViewById(R.id.singleBreed);
        singleAage = findViewById(R.id.singleAage);
        singleSize = findViewById(R.id.singleSize);
        singlePublishDate = findViewById(R.id.singlePublishDate);
        singleAddress = findViewById(R.id.singleAddress);
        singleAbout = findViewById(R.id.singleAbout);

        singleImage = findViewById(R.id.singleImage);

        Picasso.get().load(getIntent().getStringExtra("singleImage"))
                .placeholder(R.drawable.add_image)
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(singleImage);


        singleBreed.setText(getIntent().getStringExtra("singleBreed"));
        singleAage.setText(getIntent().getStringExtra("singleAage"));
        singleSize.setText(getIntent().getStringExtra("singleSize"));
        singlePublishDate.setText(getIntent().getStringExtra("singlePublishDate"));
        singleAddress.setText(getIntent().getStringExtra("singleAddress"));
        singleAbout.setText(getIntent().getStringExtra("singleAbout"));
//        buttonCall.setText(getIntent().getStringExtra("singleContactNumber"));
    }
}