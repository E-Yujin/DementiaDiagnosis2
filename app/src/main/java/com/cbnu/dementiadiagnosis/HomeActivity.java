package com.cbnu.dementiadiagnosis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import QuizPage.QuizPage;
import user.SharedPreference;

public class HomeActivity extends AppCompatActivity {

    Button simple, formal, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        simple = findViewById(R.id.simpleTest);
        formal = findViewById(R.id.formalTest);
        logout = findViewById(R.id.btnLogout);

        // 간이검사 시작
        simple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SimpleTestActivity.class));
            }
        });

        // 정규검사 시작
        formal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, QuizHOME.class));
                finish();
            }
        });

        // 로그아웃
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreference.clear_user(HomeActivity.this);
                startActivity(new Intent(HomeActivity.this, FirstActivity.class));
            }
        });

    }
}