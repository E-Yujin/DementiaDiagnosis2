package QuizPage;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cbnu.dementiadiagnosis.MainSTT;
import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.TTS;

import QuizPage.QuizPage;
import questions.orientation;
import questions.spaceTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpaceTimePage extends AppCompatActivity {
    spaceTime ST;
    SpaceTimeView STV;
    TTS tts;
    TextView question;
    Button eraser;
    Button submit;

    private long backBtnTime = 0;
    List<String> tem = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.space_time);

        question = (TextView) findViewById(R.id.announce);
        eraser = (Button) findViewById(R.id.eraser);
        submit = (Button) findViewById(R.id.submit);
        ST = new spaceTime();
        STV = (SpaceTimeView) findViewById(R.id.canvas);
        tts = new TTS(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                question.setText(ST.quiz.get(0));
                tts.onInit(status, ST.quiz.get(0));
                tem.add(ST.quiz.get(1));
                tem.add(ST.quiz.get(2));
                tem.add(ST.quiz.get(3));
                tts.UtteranceProgress(tem, "continue", question);
            }
        }, submit);

        eraser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                STV.clear();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tts.Stop();
                tts.isStopUtt = true;

                if(STV.Score_cal()){
                    ST.score = 1;
                }
                else{
                    ST.score = 0;
                }
                Intent resultIntent = new Intent();

                resultIntent.putExtra("isDone", true);
                resultIntent.putExtra("score", ST.score);

                setResult(1, resultIntent);
                finish();
            }
        });
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
            tts.speakOut(ST.quiz.get(0));
            tem.add(ST.quiz.get(1));
            tem.add(ST.quiz.get(2));
            tem.add(ST.quiz.get(3));
            tts.UtteranceProgress(tem, "continue", question);
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