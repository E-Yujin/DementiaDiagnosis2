package QuizPage;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
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
import com.cbnu.dementiadiagnosis.QuizHOME;
import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.TTS;

import java.util.ArrayList;
import java.util.List;

import questions.fluency;
import questions.orientation;

public class fluency_Page extends AppCompatActivity {
    fluency flu;
    MainSTT stt;
    TTS tts;
    QuizPage QP;
    TextView question;
    EditText answer;
    ImageButton sttBtn;
    Button submit;
    List<String> tem = new ArrayList<>();
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
        submit = (Button) findViewById(R.id.submit);
        flu = new fluency();

        Intent intent;
        intent = getIntent();
        flu.scores = intent.getIntArrayExtra("scores");

        stt = new MainSTT(this, answer, question, sttBtn, submit, tts);
        stt.isFluency = true;
        QP = new QuizPage(stt, tts, question, answer, sttBtn, submit, flu.quiz);
        helper_img = findViewById(R.id.img);
        tts = new TTS(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tem.clear();
                answer.setEnabled(false);
                sttBtn.setEnabled(false);
                submit.setEnabled(false);
                int time[] = {1500, 1000, 1000, 1001};
                tts.onInit(status, question.getText().toString(), "default", 1000);
                tem.add(flu.quiz.get(1));
                tem.add(flu.quiz.get(2));
                tem.add(flu.quiz.get(3));
                tts.UtteranceProgress(tem, "continue", time, question, answer, stt);
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
                QP.user_ans = answer.getText().toString().replace(".", "");
                QP.user_ans = answer.getText().toString().replace(",", "");
                String ans[] = QP.user_ans.split(" ");
                int correct = 0;

                for(int i = 0; i < ans.length; i++){
                    if(flu.crr_ans[0].contains(ans[i])){
                        correct ++;
                    }
                }
                if(correct >= 15){
                    flu.Tscore = 2;
                }
                else if(correct >= 9){
                    flu.Tscore = 1;
                }
                else{
                    flu.Tscore = 0;
                }

                flu.scores[8] = flu.Tscore;

                Intent intent = new Intent(getApplicationContext(), QuizHOME.class);
                intent.putExtra("scores", flu.scores);
                intent.putExtra("isDone", true);
                startActivity(intent);

                stt.isFluency = false;

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
        tts.Stop();
        stt.Stop();
    }

    @Override
    protected void onStart(){
        super.onStart();
        tts.isStopUtt = false;
        if(!question.getText().equals("그만!")){
            tem.clear();
            answer.setEnabled(false);
            sttBtn.setEnabled(false);
            submit.setEnabled(false);
            int time[] = {2000, 1000, 1000, 1001};
            question.setText(flu.quiz.get(0));
            answer.setText("");
            tts.speakOut(flu.quiz.get(0), "default");
            tem.add(flu.quiz.get(1));
            tem.add(flu.quiz.get(2));
            tem.add(flu.quiz.get(3));
            tts.UtteranceProgress(tem, "continue", time, question, answer, stt);
        }
        else{
            tem.clear();
            answer.setEnabled(true);
            sttBtn.setEnabled(false);
            submit.setEnabled(true);
            question.setText("답변을 완료하셨습니다.\n파란색 상자를 눌러 답변을 저장해주세요!");
            tts.speakOut(question.getText().toString(), "Done");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tts.Destroy();
        stt.Destroy();
    }
}
