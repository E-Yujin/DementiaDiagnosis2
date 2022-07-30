package com.cbnu.dementiadiagnosis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import simpleTest.S_orientation;

public class SimpleTestActivity extends AppCompatActivity {

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
                    tem.add("간이검사는 3~4분 내로 빠르게 치매진단을 하기에 적합합니다.");
                    tem.add("확실한 진단을 원하시면 '정규검사'를 진행해주세요.");
                    tem.add("간이검사를 진행할 준비가 되셨다면\n아래 상자를 눌러주세요.");
                    tts.UtteranceProgress(tem, "continue", Announce);
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
                    tem.add("간이검사는 3~4분 내로 빠르게 치매진단을 하기에 적합합니다.");
                    tem.add("확실한 진단을 원하시면 '정규검사'를 진행해주세요.");
                    tem.add("간이검사를 진행할 준비가 되셨다면\n아래 상자를 눌러주세요.");
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
                    tem.add("간이검사는 3~4분 내로 빠르게 치매진단을 하기에 적합합니다.");
                    tem.add("확실한 진단을 원하시면 '정규검사'를 진행해주세요.");
                    tem.add("간이검사를 진행할 준비가 되셨다면\n아래 상자를 눌러주세요.");
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
                Intent intent = null;
                switchPage(intent, view);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

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
                tem.add("간이검사는 3~4분 내로 빠르게 치매진단을 하기에 적합합니다.");
                tem.add("확실한 진단을 원하시면 '정규검사'를 진행해주세요.");
                tem.add("간이검사를 진행할 준비가 되셨다면\n아래 상자를 눌러주세요.");
                tts.UtteranceProgress(tem, "continue", Announce);
            }
            else finish();
        }
        Title.setText(announce.get(current));
    }

    private void init_Announce(){
        announce.add("간이검사");
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
                intent = new Intent(view.getContext(), S_orientation.class);
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