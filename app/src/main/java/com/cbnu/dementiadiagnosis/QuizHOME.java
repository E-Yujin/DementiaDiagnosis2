package com.cbnu.dementiadiagnosis;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.channels.InterruptedByTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import QuizPage.ExecutionPage;
import QuizPage.LanguagePage;
import QuizPage.SpaceTimePage;
import QuizPage.attention_Page;
import QuizPage.fluency_Page;
import QuizPage.memoryInput_Page;
import QuizPage.memoryOutput_Page;
import QuizPage.orientation_Page;
import fragment.FragmentHome;


public class QuizHOME extends AppCompatActivity {

    TextView Title;
    TextView Announce;
    TextView Intend_value;
    Button sttBtn;
    ImageView helper_img;
    Helper helper;
    TTS tts;

    private int current = 0;
    private ArrayList<String> first, second;
    public List<String> announce;
    private long backBtnTime = 0;
    private int part_score[];
    private int total_score = 0;
    boolean isDone = false;
    List<String> tem = new ArrayList<>();
    boolean isStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_home);

        announce = new ArrayList();
        part_score = new int[8];

        sttBtn = findViewById(R.id.sttStart);
        Title = findViewById(R.id.title);
        Announce = findViewById(R.id.announce);
        Intend_value = findViewById(R.id.intent);
        helper_img = findViewById(R.id.img);
        tts = new TTS(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(!isDone){
                    tem.clear();
                    tts.onInit(status, Announce.getText().toString(), "default", 1000);
                    tem.add("지금부터 기억력과 사고능력을 살펴보기 위한\n질문들을 드리겠습니다.");
                    tem.add("생각나는 대로 최선을 다해 답변해 주시면 됩니다.");
                    tem.add("준비되셨다면 아래 상자를 눌러주세요.");
                    tts.UtteranceProgress(tem, "continue", Announce);
                }
                else
                    tts.onInit(status, Announce.getText().toString(), "default", 1000);
            }
        });

        first = new ArrayList<>();
        second = new ArrayList<>();

        init_Announce();

        Title.setText(announce.get(current));
        helper = new Helper(tts, helper_img, this);
        helper.setStart();

        Announce.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(current == 0){
                    Announce.setText("안녕하세요.");
                    tts.speakOut(Announce.getText().toString(),"default");
                    tem.clear();
                    tem.add("지금부터 기억력과 사고능력을 살펴보기 위한\n질문들을 드리겠습니다.");
                    tem.add("생각나는 대로 최선을 다해 답변해 주시면 됩니다.");
                    tem.add("준비되셨다면 아래 상자를 눌러주세요.");
                    tts.UtteranceProgress(tem, "continue", Announce);
                }
                else if(current == 1){
                    tts.speakOut(Announce.getText().toString(),"default");
                    tts.UtteranceProgress();
                }
            }
        });
        Title.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(current == 0){
                    Announce.setText("안녕하세요.");
                    tts.speakOut(Announce.getText().toString(),"default");
                    tem.clear();
                    tem.add("지금부터 기억력과 사고능력을 살펴보기 위한\n질문들을 드리겠습니다.");
                    tem.add("생각나는 대로 최선을 다해 답변해 주시면 됩니다.");
                    tem.add("준비되셨다면 아래 상자를 눌러주세요.");
                    tts.UtteranceProgress(tem, "continue", Announce);
                }
                else if(current == 1){
                    tts.speakOut(Announce.getText().toString(),"default");
                    tts.UtteranceProgress();
                }
            }
        });

        sttBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.Stop();
                tts.isStopUtt = true;
                Intent intent = null;
                switchPage(intent, view);
            }
        });

    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        super.onActivityResult(requestCode, resultCode, resultIntent);
        if (requestCode == 100 && resultCode == 1) {
            current = 1;
            part_score = resultIntent.getIntArrayExtra("part_score");

            for(int i = 0; i < part_score.length; i++){
                total_score += part_score[i];
            }

            Toast.makeText(this, "결과가 저장되었습니다.",
                    Toast.LENGTH_SHORT).show();
        }
    }*/
    //혹시나 안되면 주석 풀어보기

    @Override
    protected void onStart() {
        super.onStart();
        tts.isStopUtt = false;
        Intent intent;
        intent = getIntent();
        part_score = intent.getIntArrayExtra("scores");
        isDone = intent.getBooleanExtra("isDone", false);

        if(isDone){
            current = 1;
            tem.clear();
            Announce.setText("결과를 출력하기 위해 아래 상자를 눌러주세요.");
            tts.speakOut(Announce.getText().toString());
            sttBtn.setText("결과 보기");
            for(int i = 0; i < part_score.length; i++){
                total_score += part_score[i];
            }
            Intend_value.setText(Integer.toString(total_score));

            Toast.makeText(this, "결과가 저장되었습니다.",
                    Toast.LENGTH_SHORT).show();

        }
        else {
            if(!isStart){
                Announce.setText("안녕하세요.");
                tts.speakOut(Announce.getText().toString(),"default");
                tem.clear();
                tem.add("지금부터 기억력과 사고능력을 살펴보기 위한\n질문들을 드리겠습니다.");
                tem.add("생각나는 대로 최선을 다해 답변해 주시면 됩니다.");
                tem.add("준비되셨다면 아래 상자를 눌러주세요.");
                tts.UtteranceProgress(tem, "continue", Announce);
            }
            else finish();
        }
        Title.setText(announce.get(current));
    }

    private void init_Announce(){
        announce.add("정규검사");
        announce.add("결과 출력");
    }

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if (0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed();
        } else {
            backBtnTime = curTime;
            Toast.makeText(this, "지금 나가시면 진행된 검사가 저장되지 않습니다.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void switchPage(Intent intent, View view){
        switch (current){
            case 0:
                isStart = true;
                intent = new Intent(view.getContext(), orientation_Page.class);
                startActivityForResult(intent, 100);
                break;
            case 1:
                intent = new Intent(view.getContext(), Result.class);
                intent.putExtra("part_score", part_score);
                intent.putExtra("total_score", total_score);
                startActivity(intent);
                finish();
                break;

        }
    }
    @Override
    protected void onStop(){
        tts.isStopUtt = true;
        super.onStop();
        tts.Stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tts.Stop();
    }
}