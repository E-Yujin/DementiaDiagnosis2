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

import java.util.ArrayList;
import java.util.List;

import questions.attention;

public class attention_Page extends AppCompatActivity {
    attention att;
    MainSTT stt;
    TTS tts;
    QuizPage QP;
    TextView question;
    TextView announce;
    EditText answer;
    Button sttBtn;
    Button submit;

    private long backBtnTime = 0;

    List<String> tem = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orientation);

        question = (TextView) findViewById(R.id.question);
        announce = (TextView) findViewById(R.id.textView);
        answer = (EditText) findViewById(R.id.result);
        sttBtn = (Button) findViewById(R.id.sttStart);
        submit = (Button) findViewById(R.id.submit);
        att = new attention();

        tts = new TTS(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.onInit(status, question.getText().toString());
                tem.add("6, 9, 7, 3");
                tem.add(announce.getText().toString());
                tts.UtteranceProgress(tem, "continue");
                sttBtn.setEnabled(false);
                submit.setEnabled(false);
            }
        }, sttBtn, submit);
        stt = new MainSTT(this, answer, announce, question, sttBtn, submit, tts);
        QP = new QuizPage(stt, tts, question, announce, answer, sttBtn, submit, att.quiz);

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
                sttBtn.setText("말하기");
                tts.isStopUtt = true;

                QP.user_ans = answer.getText().toString()
                        .replace(" ","")
                        .replace(",","")
                        .replace(".", "");

                List<String> correct = new ArrayList<>();
                correct.clear();
                for(String data : att.crr_ans[QP.current]){
                    correct.add(data);
                }

                answer.setText("");

                if(QP.user_ans.isEmpty()){
                    announce.setText("무응답으로 넘어가실 수 없습니다.\n아시는 대로 천천히 말씀해주시면 됩니다.");
                    tts.speakOut(announce.getText().toString());
                }
                else
                {
                    for(String data : correct){
                        if(QP.user_ans.contains(data)){
                            att.score ++;
                        }
                        if(QP.current + 1 < att.score){
                            att.score = QP.current + 1;
                        }
                    }
                    if(QP.current == 0){
                        sttBtn.setEnabled(false);
                        submit.setEnabled(false);
                        tts.isStopUtt = false;
                        QP.Submit();
                        tts.speakOut(question.getText().toString());
                        tem.clear();
                        tem.add("5, 7, 2, 8, 4");
                        tem.add(announce.getText().toString());
                        tts.UtteranceProgress(tem, "continue");
                    }
                    else if(QP.current == 1){
                        sttBtn.setEnabled(false);
                        submit.setEnabled(false);
                        tts.isStopUtt = false;
                        QP.Submit();
                        tts.speakOut(question.getText().toString());
                        tem.clear();
                        tem.add("금수강산");
                        tem.add(announce.getText().toString());
                        tts.UtteranceProgress(tem, "continue");
                    }
                    else if(QP.current == 2){
                        Intent resultIntent = new Intent();

                        resultIntent.putExtra("isDone", true);
                        resultIntent.putExtra("score", att.score);

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
            tts.UtteranceProgress(tem,"continue");
        }
        else {
            QP.Start();
            tts.speakOut(question.getText().toString());
            tts.UtteranceProgress(tem,"continue");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        QP.Destroy();
    }
}
