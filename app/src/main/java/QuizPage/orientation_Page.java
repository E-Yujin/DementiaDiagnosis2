package QuizPage;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    Button sttBtn;
    Button submit;

    private boolean isDone[];



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
        tts = new TTS(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.onInit(status, question.getText().toString());
            }
        });
        stt = new MainSTT(this, answer, announce, question, sttBtn, tts);
        QP = new QuizPage(stt, tts, question, announce, answer, sttBtn, ortt_main.quiz);

        QP.Delay(3000, announce.getText().toString());

        sttBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stt.start_STT();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                QP.user_ans = answer.getText().toString();
                QP.correct = ortt_main.crr_ans[QP.current].get(0);
                answer.setText("");
                if(QP.user_ans.isEmpty()){
                    announce.setText("무응답으로 넘어가실 수 없습니다.\n아시는 대로 천천히 말씀해주시면 됩니다.");
                    tts.speakOut(announce.getText().toString());
                }
                else
                {
                    if(QP.user_ans.contains(QP.correct)){
                        ortt_main.score ++;
                    }
                    if(QP.current < 4){
                        QP.Submit();
                        tts.speakOut(question.getText().toString());
                        QP.Delay(3000, announce.getText().toString());
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
        QP.BackPressed(this);
    }

    @Override
    protected void onStop(){
        super.onStop();
        QP.Stop();
    }

    @Override
    protected void onStart(){
        super.onStart();
        QP.isStop = false;
        if(announce.getText() != "대답할 준비가 되셨다면\n아래 보라색 상자를 눌러 말씀해주세요!"){
            QP.Start(this);
            tts.speakOut(question.getText().toString());
            QP.Delay(3000, announce.getText().toString());
        }
        else {
            tts.speakOut(question.getText().toString());
            QP.Delay(3000, announce.getText().toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        QP.Destroy();
    }
}
