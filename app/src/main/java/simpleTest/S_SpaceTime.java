package simpleTest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.cbnu.dementiadiagnosis.HomeActivity;
import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.TTS;

import java.util.ArrayList;
import java.util.List;

import QuizPage.SpaceTimeView;
import questions.spaceTime;

public class S_SpaceTime extends AppCompatActivity {
    spaceTime ST;
    SpaceTimeView STV;
    TTS tts;
    TextView question, type;
    Button eraser;
    ImageButton submit, undo;
    ProgressBar pro_bar;
    AppCompatButton donKnow;
    simple_QuizPage QP;

    private long backBtnTime = 0;
    List<String> tem = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.space_time);

        question = (TextView) findViewById(R.id.announce);
        type = (TextView) findViewById(R.id.type);
        eraser = (Button) findViewById(R.id.eraser);
        submit = (ImageButton) findViewById(R.id.submit);
        undo = (ImageButton) findViewById(R.id.before);
        ST = new spaceTime();
        STV = (SpaceTimeView) findViewById(R.id.canvas);
        pro_bar = (ProgressBar) findViewById(R.id.progressBar);
        donKnow = (AppCompatButton) findViewById(R.id.donknow);
        QP = new simple_QuizPage();

        Intent intent;
        intent = getIntent();
        ST.scores = intent.getIntArrayExtra("scores");
        type.setText("시공간 기능");
        pro_bar.setProgress(35);

        tts = new TTS(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tem.clear();
                tts.onInit(status, question.getText().toString(), "default");
                tem.add(ST.quiz.get(1));
                tem.add(ST.quiz.get(2));
                tem.add(ST.quiz.get(3));
                tts.UtteranceProgress(tem, "continue", question, submit);
            }
        });

        question.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tts.isStopUtt = true;
                tts.Stop();
                tts.speakOut(question.getText().toString());
                tem.add(ST.quiz.get(1));
                tem.add(ST.quiz.get(2));
                tem.add(ST.quiz.get(3));
                tts.UtteranceProgress(tem, "continue", question, submit);
            }
        });

        donKnow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tts.Stop();
                tts.isStopUtt = true;

                ST.Tscore = 0;

                ST.scores[4] = ST.Tscore;

                pro_bar.setProgress(40);
                donKnow.setEnabled(false);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), S_execution.class);
                        intent.putExtra("scores", ST.scores);
                        startActivity(intent);
                        overridePendingTransition(0, 0);

                        finish();
                    }
                });
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        eraser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                STV.clear();
            }
        });

        undo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                tts.isStopUtt = true;
                tts.Stop();
                Toast.makeText(getApplicationContext(), "해당 항목의 첫 문제 입니다.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tts.Stop();
                tts.isStopUtt = true;

                ST.Tscore = STV.getScore();

                ST.scores[4] = ST.Tscore;

                pro_bar.setProgress(40);

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), S_execution.class);
                        intent.putExtra("scores", ST.scores);
                        startActivity(intent);
                        overridePendingTransition(0, 0);

                        finish();
                    }
                });
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean onTouchEvent(MotionEvent event) {
        return QP.onTouchEvent(event, undo, submit);
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

    @Override
    protected void onStop(){
        tts.isStopUtt = true;
        super.onStop();
        if(tts != null){
            tts.Stop();
        }
        tem.clear();
    }

    @Override
    protected void onStart(){
        super.onStart();
        tts.isStopUtt = false;
        if(!question.getText().toString().equals(ST.quiz.get(0))){
            question.setText(ST.quiz.get(0));
            tts.speakOut(ST.quiz.get(0), "default");
            tem.add(ST.quiz.get(1));
            tem.add(ST.quiz.get(2));
            tem.add(ST.quiz.get(3));
            tts.UtteranceProgress(tem, "continue", question, submit);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tts != null) {
            tts.Destroy();
        }
    }
}