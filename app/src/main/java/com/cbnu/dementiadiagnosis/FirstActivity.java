package com.cbnu.dementiadiagnosis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import user.SharedPreference;

public class FirstActivity extends AppCompatActivity {

    Animation ani;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        img = findViewById(R.id.img);
        ani = AnimationUtils.loadAnimation(this, R.anim.pop);
        img.startAnimation(ani);

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