package QuizPage;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
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

import com.cbnu.dementiadiagnosis.AnimationListner;
import com.cbnu.dementiadiagnosis.Helper;
import com.cbnu.dementiadiagnosis.MainSTT;
import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.TTS;
import questions.orientation;

public class orientation_Page extends AppCompatActivity {
    orientation ortt_main;
    MainSTT stt;
    TTS tts;
    QuizPage QP;
    TextView question;
    EditText answer;
    ImageButton sttBtn;
    ImageButton submit;
    ImageView helper_img;
    Helper helper;

    private long backBtnTime = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orientation);

        question = (TextView) findViewById(R.id.question);
        answer = (EditText) findViewById(R.id.result);
        sttBtn = (ImageButton) findViewById(R.id.sttStart);
        submit = (ImageButton) findViewById(R.id.submit);
        helper_img = findViewById(R.id.img);
        ortt_main = new orientation();
        stt = new MainSTT(this, answer, question, sttBtn, submit, tts);
        QP = new QuizPage(stt, tts, question, answer, sttBtn, submit, ortt_main.quiz);

        tts = new TTS(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.onInit(status, question.getText().toString(), "Done", 1000);
            }
        });
        
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
                tts.isStopUtt = true;
                sttBtn.setEnabled(true);

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
                        /*if(QP.current == 0) answer.setText(QP.user_ans+"년");
                        else if(QP.current == 1) answer.setText(QP.user_ans+"월");
                        else if(QP.current == 2) answer.setText(QP.user_ans+"일");*/
                    }
                    else{
                        QP.user_ans = toDigit;
                        /*if(QP.current == 0) answer.setText(QP.user_ans+"년");
                        else if(QP.current == 1) answer.setText(QP.user_ans+"월");
                        else if(QP.current == 2) answer.setText(QP.user_ans+"일");*/
                    }
                }
                //else if(QP.current == 3) answer.setText(QP.user_ans+"요일");

                QP.correct = ortt_main.crr_ans[QP.current].get(0);

                if(QP.user_ans.isEmpty()){
                    tts.speakOut("무응답으로 넘어가실 수 없습니다.\n아시는 대로 천천히 말씀해주시면 됩니다.");
                }
                else
                {
                    if(QP.user_ans.contains(QP.correct)){
                        ortt_main.Tscore ++;
                    }
                    if(QP.current < 4){
                        answer.setText("");
                        tts.isStopUtt = false;
                        QP.Submit();
                        tts.speakOut(question.getText().toString());
                    }
                    else if(QP.current == 4){
                        ortt_main.scores[1] = ortt_main.Tscore;

                        Intent intent = new Intent(getApplicationContext(), memoryInput_Page.class);
                        intent.putExtra("scores", ortt_main.scores);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        QP.Destroy();
    }
}
