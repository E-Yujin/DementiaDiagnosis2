package QuizPage;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cbnu.dementiadiagnosis.Helper;
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
    EditText answer;
    ImageButton sttBtn;
    Button submit;
    ImageView helper_img;
    Helper helper;

    private long backBtnTime = 0;

    List<String> tem = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orientation);

        question = (TextView) findViewById(R.id.question);
        answer = (EditText) findViewById(R.id.result);
        sttBtn = (ImageButton) findViewById(R.id.sttStart);
        submit = (Button) findViewById(R.id.submit);
        att = new attention();
        helper_img = findViewById(R.id.img);

        Intent intent;
        intent = getIntent();
        att.scores = intent.getIntArrayExtra("scores");

        tts = new TTS(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.onInit(status, question.getText().toString(), "default", 1000);
                tem.add("6, 9, 7, 3");
                tts.UtteranceProgress(tem, "continue", sttBtn, submit);
                sttBtn.setEnabled(false);
                submit.setEnabled(false);
            }
        });
        stt = new MainSTT(this, answer, question, sttBtn, submit, tts);
        QP = new QuizPage(stt, tts, question, answer, sttBtn, submit, att.quiz);

        helper = new Helper(tts, stt, helper_img, this);
        helper.setTest();

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
                    tts.speakOut("무응답으로 넘어가실 수 없습니다.\n아시는 대로 천천히 말씀해주시면 됩니다.");
                }
                else
                {
                    for(String data : correct){
                        if(QP.user_ans.contains(data)){
                            att.Tscore ++;
                        }
                        if(QP.current + 1 < att.Tscore){
                            att.Tscore = QP.current + 1;
                        }
                    }
                    if(QP.current == 0){
                        sttBtn.setEnabled(false);
                        submit.setEnabled(false);
                        tts.isStopUtt = false;
                        QP.Submit();
                        tts.speakOut(question.getText().toString(), "default");
                        tem.clear();
                        tem.add("5, 7, 2, 8, 4");
                        tts.UtteranceProgress(tem, "continue", sttBtn, submit);
                    }
                    else if(QP.current == 1){
                        sttBtn.setEnabled(false);
                        submit.setEnabled(false);
                        tts.isStopUtt = false;
                        QP.Submit();
                        tts.speakOut(question.getText().toString(), "default");
                        tem.clear();
                        tem.add("금수강산");
                        tts.UtteranceProgress(tem, "continue", sttBtn, submit);
                    }
                    else if(QP.current == 2){

                        att.scores[3] = att.Tscore;

                        Intent intent = new Intent(getApplicationContext(), SpaceTimePage.class);
                        intent.putExtra("scores", att.scores);
                        startActivity(intent);

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
        tts.Stop();
        stt.Stop();
    }

    @Override
    protected void onStart(){
        super.onStart();
        tts.isStopUtt = false;
        QP.Start();
        tts.speakOut(question.getText().toString());
        tts.UtteranceProgress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        QP.Destroy();
    }
}
