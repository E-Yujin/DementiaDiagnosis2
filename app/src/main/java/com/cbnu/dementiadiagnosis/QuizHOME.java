package com.cbnu.dementiadiagnosis;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;
import java.util.List;

import QuizPage.orientation_Page;


public class QuizHOME extends AppCompatActivity {

    TextView Announce, title_text, textView, exam, loading_text;
    Button sttBtn;
    ImageView helper_img, mic, arrow, finger, loading;
    AppCompatButton donknow;
    Helper helper;
    TTS tts;
    FrameLayout view;
    Handler handler;
    AnimationListner anim;

    private int current = 0;
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
        setContentView(R.layout.quiz_home1);

        announce = new ArrayList();
        part_score = new int[8];

        sttBtn = findViewById(R.id.sttStart);
        title_text = findViewById(R.id.title_text);
        textView = findViewById(R.id.textView);
        mic = findViewById(R.id.mic);
        finger = findViewById(R.id.finger);
        arrow = findViewById(R.id.arrow);
        donknow = findViewById(R.id.donknow);
        Announce = findViewById(R.id.announce);
        view = findViewById(R.id.view);
        exam = findViewById(R.id.exam);
        loading = findViewById(R.id.loading);
        helper_img = findViewById(R.id.img);
        loading_text = findViewById(R.id.loading_text);
        handler = new Handler();

        anim = new AnimationListner((AnimationDrawable)
                this.getResources().getDrawable(
                        R.drawable.helper_listen)) {
            @Override
            public void onAnimationFinish() {
            }

            @Override
            public void onAnimationStart() {
            }
        };
        loading.setImageDrawable(anim);

        tts = new TTS(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(!isDone){
                    tem.clear();
                    tts.onInit(status, Announce.getText().toString(), "default", 1000);
                    tem.add("진단을 시작하기 전 사용방법을 간단히 안내해드리겠습니다.");
                    tem.add("맨 위에 나타나는 질문을 듣고");
                    tem.add("아래 마이크 버튼을 눌러 음성으로 답변하거나");
                    tem.add("네모난 상자를 눌러 타자로 답변할 수 있습니다.");
                    tem.add("문제의 정답을 모를 땐 '잘모르겠어요'를 눌러주세요.");
                    tem.add("질문이 기억나지 않는다면 질문 문장을 눌러주세요.");
                    tem.add("왼쪽으로 화면을 밀면 다음 문제로 넘어갑니다.");
                    tem.add("오른쪽으로 화면을 밀면 이전 문제로 넘어갑니다.");
                    tem.add("진단은 최대한 조용한 공간에서 혼자 진행해주세요.");
                    tem.add("또한, 정확한 진단을 위해 진단 중엔 이동하지 말아주세요.");
                    tem.add("모두 숙지하셨나요?");
                    tem.add("그렇다면 지금부터 치매 정규 진단을 시작하겠습니다.");
                    tem.add("생각나는 대로 최선을 다해 답변해 주시면 됩니다.");
                    tem.add("준비되셨다면 아래 '시작하기'를 눌러주세요.");
                    tts.UtteranceProgress(tem, "continue", Announce, title_text, textView, mic, arrow, finger, donknow);
                }
            }
        });

        init_Announce();

        helper = new Helper(tts, helper_img, this);
        helper.setStart();

        Announce.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(current == 0){
                    Announce.setText("안녕하세요.");
                    tts.speakOut(Announce.getText().toString(),"default");
                    tem.clear();
                    tem.add("진단을 시작하기 전 사용방법을 간단히 안내해드리겠습니다.");
                    tem.add("맨 위에 나타나는 질문을 듣고");
                    tem.add("아래 마이크 버튼을 눌러 음성으로 답변하거나");
                    tem.add("네모난 상자를 눌러 타자로 답변할 수 있습니다.");
                    tem.add("문제의 정답을 모를 땐 '잘모르겠어요'를 눌러주세요.");
                    tem.add("질문이 기억나지 않는다면 질문 문장을 눌러주세요.");
                    tem.add("왼쪽으로 화면을 밀면 다음 문제로 넘어갑니다.");
                    tem.add("오른쪽으로 화면을 밀면 이전 문제로 넘어갑니다.");
                    tem.add("진단은 최대한 조용한 공간에서 혼자 진행해주세요.");
                    tem.add("또한, 정확한 진단을 위해 진단 중엔 이동하지 말아주세요.");
                    tem.add("모두 숙지하셨나요?");
                    tem.add("그렇다면 지금부터 치매 정규 진단을 시작하겠습니다.");
                    tem.add("생각나는 대로 최선을 다해 답변해 주시면 됩니다.");
                    tem.add("준비되셨다면 아래 '시작하기'를 눌러주세요.");
                    tts.UtteranceProgress(tem, "continue", Announce, title_text, textView, mic, arrow, finger, donknow);
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
            tts.Destroy();
            current = 1;
            view.setVisibility(View.GONE);
            exam.setVisibility(View.GONE);
            Announce.setVisibility(View.GONE);
            sttBtn.setVisibility(View.GONE);
            helper_img.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
            loading_text.setVisibility(View.VISIBLE);
            anim.start();

            for(int i = 0; i < part_score.length; i++){
                total_score += part_score[i];
            }

            Toast.makeText(this, "결과가 저장되었습니다.",
                    Toast.LENGTH_SHORT).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(view.getContext(), Result.class);
                    intent.putExtra("part_score", part_score);
                    intent.putExtra("total_score", total_score);
                    startActivity(intent);
                    finish();
                }
            }, 3000);

        }
        else {
            if(!isStart){
                Announce.setText("안녕하세요.");
                tts.speakOut(Announce.getText().toString(),"default");
                tem.clear();
                tem.add("진단을 시작하기 전 사용방법을 간단히 안내해드리겠습니다.");
                tem.add("맨 위에 나타나는 질문을 듣고");
                tem.add("아래 마이크 버튼을 눌러 음성으로 답변하거나");
                tem.add("네모난 상자를 눌러 타자로 답변할 수 있습니다.");
                tem.add("문제의 정답을 모를 땐 '잘모르겠어요'를 눌러주세요.");
                tem.add("질문이 기억나지 않는다면 질문 문장을 눌러주세요.");
                tem.add("왼쪽으로 화면을 밀면 다음 문제로 넘어갑니다.");
                tem.add("오른쪽으로 화면을 밀면 이전 문제로 넘어갑니다.");
                tem.add("진단은 최대한 조용한 공간에서 혼자 진행해주세요.");
                tem.add("또한, 정확한 진단을 위해 진단 중엔 이동하지 말아주세요.");
                tem.add("모두 숙지하셨나요?");
                tem.add("그렇다면 지금부터 치매 정규 진단을 시작하겠습니다.");
                tem.add("생각나는 대로 최선을 다해 답변해 주시면 됩니다.");
                tem.add("준비되셨다면 아래 '시작하기'를 눌러주세요.");
                tts.UtteranceProgress(tem, "continue", Announce, title_text, textView, mic, arrow, finger, donknow);
            }
            else finish();
        }
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
            tts.Destroy();

            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);

            finish();
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