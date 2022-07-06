package QuizPage;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cbnu.dementiadiagnosis.MainSTT;
import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.TTS;

import QuizPage.QuizPage;
import questions.orientation;

import java.util.Arrays;

public class orientation_Page extends AppCompatActivity {
    orientation ortt_main;
    MainSTT stt;
    TTS tts;
    QuizPage QP;
    TextView question;
    TextView announce;
    EditText answer;
    ImageButton sttBtn;
    Button submit;

    private long backBtnTime = 0;

    boolean is_confirmation = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orientation);

        question = (TextView) findViewById(R.id.question);
        announce = (TextView) findViewById(R.id.textView);
        answer = (EditText) findViewById(R.id.result);
        sttBtn = (ImageButton) findViewById(R.id.sttStart);
        submit = (Button) findViewById(R.id.submit);
        ortt_main = new orientation();
        tts = new TTS(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.onInit(status, question.getText().toString());
                tts.UtteranceProgress(announce.getText().toString());
            }
        }, sttBtn, submit);
        stt = new MainSTT(this, answer, announce, question, sttBtn, submit, tts);
        QP = new QuizPage(stt, tts, question, announce, answer, sttBtn, submit, ortt_main.quiz);

        sttBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tts.isStopUtt = true;
                tts.Stop();
                stt.start_STT();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stt.Stop();
                tts.Stop();
                sttBtn.setEnabled(true);
                tts.isStopUtt = true;
                QP.user_ans = QP.ans_filter(answer.getText().toString());

                if(QP.current < 3 && !QP.user_ans.equals("")){
                    String[] ansArray = QP.user_ans.split("");
                    String toDigit = "";
                    for(String s : ansArray){
                        if(!s.equals("")) {
                            if(Character.isDigit(s.charAt(0))) {
                                toDigit += s;
                            }
                        }
                    }
                    if(toDigit.equals("")){
                        QP.user_ans = ortt_main.KorTran(QP.user_ans);
                        if(QP.current == 0) answer.setText(QP.user_ans+"년");
                        else if(QP.current == 1) answer.setText(QP.user_ans+"월");
                        else if(QP.current == 2) answer.setText(QP.user_ans+"일");
                    }
                    else{
                        QP.user_ans = toDigit;
                        if(QP.current == 0) answer.setText(QP.user_ans+"년");
                        else if(QP.current == 1) answer.setText(QP.user_ans+"월");
                        else if(QP.current == 2) answer.setText(QP.user_ans+"일");
                    }
                }
                else if(QP.current == 3) answer.setText(QP.user_ans+"요일");
                if(QP.current < 4){
                    announce.setText("\""+answer.getText()+"\""+
                            " 이라고 하신 게 맞으시면\n파란 상자를 다시 눌러주세요.");
                    tts.speakOut(announce.getText().toString());
                }

                QP.correct = ortt_main.crr_ans[QP.current].get(0);
                if(QP.user_ans.isEmpty()){
                    announce.setText("무응답으로 넘어가실 수 없습니다.\n아시는 대로 천천히 말씀해주시면 됩니다.");
                    tts.speakOut(announce.getText().toString());
                }
                else if(!is_confirmation && QP.current < 4){
                    is_confirmation = true;
                }
                else
                {
                    if(QP.user_ans.contains(QP.correct)){
                        ortt_main.score ++;
                    }
                    if(QP.current < 4){
                        answer.setText("");
                        tts.isStopUtt = false;
                        QP.Submit();
                        tts.speakOut(question.getText().toString());
                        tts.UtteranceProgress(announce.getText().toString());
                        is_confirmation = false;
                    }
                    else if(QP.current == 4){
                        Intent resultIntent = new Intent();

                        resultIntent.putExtra("isDone", true);
                        resultIntent.putExtra("score", ortt_main.score);

                        setResult(1, resultIntent);
                        finish();
                    }
                }
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
        QP.Stop();
    }

    @Override
    protected void onStart(){
        super.onStart();
        tts.isStopUtt = false;
        if(announce.getText() != "대답할 준비가 되셨다면\n아래 보라색 상자를 눌러 말씀해주세요!"){
            QP.Start();
            tts.speakOut(question.getText().toString());
            tts.UtteranceProgress(announce.getText().toString());
        }
        else {
            QP.Start();
            tts.speakOut(question.getText().toString());
            tts.UtteranceProgress(announce.getText().toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        QP.Destroy();
    }
}
