package com.tutorials.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.tutorials.testapp.user.SharedPreference;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Handler hand = new Handler();

        hand.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(SharedPreference.getUserName(FirstActivity.this).length() == 0) {
                    startActivity(new Intent(FirstActivity.this, RegisterActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(FirstActivity.this, HomeActivity.class));
                    finish();
                }
            }
        }, 2000);

    }
}