package com.cbnu.dementiadiagnosis;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import database.DBHelper;
import user.User;

public class Result extends AppCompatActivity {
    int total;
    TextView score;
    User user;
    SQLiteDatabase user_db;
    DBHelper DBH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent;

        intent = getIntent();
        total = intent.getIntExtra("result", 0);
        score = findViewById(R.id.total);
        score.setText("총점 : " + Integer.toString(total));
    }
}