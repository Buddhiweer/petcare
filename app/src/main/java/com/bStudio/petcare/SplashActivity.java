package com.bStudio.petcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser curentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (curentUser==null){
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                }else {
                    startActivity(new Intent(SplashActivity.this,Home.class));
                }


                finish();
            }
        },1000);
    }
}