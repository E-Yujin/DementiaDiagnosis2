package questions;

import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sst.MainActivity;
import com.example.sst.MainSTT;
import com.example.sst.R;
import com.example.sst.TTS;

import java.util.Arrays;

public class orientation_Page extends AppCompatActivity {
    orientation ortt_main;
    MainSTT stt;
    TTS tts;
    TextView question;
    TextView announce;
    EditText answer;
    Button sttBtn;
    Button submit;
    private long backBtnTime = 0;
    private boolean isDone[];
    private int current = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orientation);

        this.isDone = new boolean[5];
        Arrays.fill(isDone, false);
        question = (TextView) findViewById(R.id.question);
        announce = (TextView) findViewById(R.id.textView);
        answer = (EditText) findViewById(R.id.result);
        sttBtn = (Button) findViewById(R.id.sttStart);
        submit = (Button) findViewById(R.id.submit);
        ortt_main = new orientation();
        question.setText(ortt_main.quiz.get(current));
        announce.setText("대답할 준비가 되셨다면\n아래 보라색 상자를 눌러 말해주세요!");

        tts = new TTS(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.onInit(status, question.getText().toString());
            }
        });
        stt = new MainSTT(this, answer, announce, question, sttBtn, tts);

        Delay(3000);

        sttBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stt.start_STT();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(current < 5){
                    if(answer.getText().toString().contains(ortt_main.crr_ans.get(current))){
                        ortt_main.score += 1;
                        tts.speakOut("정답입니다.");
                    }
                }
            }
        });
    }

    private void Delay(int time) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tts.speakOut(announce.getText().toString());
            }
        }, time);
    }

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if (0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed();
        } else {
            backBtnTime = curTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        tts.Stop();
        stt.Stop();
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(announce.getText() != "대답할 준비가 되셨다면\n아래 보라색 상자를 눌러 말해주세요!"){
            question.setText(ortt_main.quiz.get(0));
            announce.setText("대답할 준비가 되셨다면\n아래 보라색 상자를 눌러 말해주세요!");
            answer.setText("");
            Toast.makeText(this, "음성 인식 도중 나가셨기에 해당 문제부터 다시 시작합니다.",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            tts.speakOut(question.getText().toString());
            Delay(3000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tts.Destroy();
        stt.Destroy();
    }
}
