package com.example.sst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import questions.orientation;
import questions.orientation_Page;

public class QuizHOME extends AppCompatActivity {

    TextView Title;
    TextView Announce;
    Button sttBtn;
    private boolean isDone[];
    private int current = 0;
    private int next = 1;
    private orientation time;
    public List<String> announce;
    private int total_score = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_home);

        time = new orientation();
        announce = new ArrayList();
        isDone = new boolean[8];
        Arrays.fill(isDone,false);

        sttBtn = findViewById(R.id.sttStart);
        Title = findViewById(R.id.title);
        Announce = findViewById(R.id.announce);

        init_Announce();

        Title.setText(announce.get(current));

        sttBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuizHOME.this, orientation_Page.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isDone[current] && current != 7){
            current ++;
            next ++;
        }
        else {
            Toast.makeText(this, "헤이",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void setCurrent(int score){
        isDone[current] = true;
        total_score += score;
    }

    private void init_Announce(){

        announce.add("지금부터 '지남력'을\n검사해보겠습니다.");

        announce.add("지금부터 '기억 등록'을\n시행하겠습니다.");

        announce.add("지금부터 '주의력'을\n검사해보겠습니다.");

        announce.add("지금부터 '시공간 기능'을\n검사해보겠습니다.");

        announce.add("지금부터 '집행 기능'을\n검사해보겠습니다.");

        announce.add("지금부터 '기억력'을\n검사해보겠습니다.");
        announce.add("지금부터 '언어 기능'을\n검사해보겠습니다.");
        announce.add("지금부터 '유창성'을\n검사해보겠습니다.");

    }
}