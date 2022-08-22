package QuizPage;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.cbnu.dementiadiagnosis.Helper;
import com.cbnu.dementiadiagnosis.HomeActivity;
import com.cbnu.dementiadiagnosis.MainSTT;
import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.TTS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import questions.attention;

public class attention_Page extends AppCompatActivity {
    attention att;
    MainSTT stt;
    TTS tts;
    QuizPage QP;
    TextView question, type;
    EditText answer;
    ImageButton sttBtn;
    ImageButton submit, undo;
    ImageView helper_img;
    Helper helper;
    String[] U_answers;
    ProgressBar pro_bar;
    AppCompatButton donKnow;

    private long backBtnTime = 0;

    List<String> tem = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orientation);

        question = (TextView) findViewById(R.id.question);
        type = (TextView) findViewById(R.id.type);
        answer = (EditText) findViewById(R.id.result);
        sttBtn = (ImageButton) findViewById(R.id.sttStart);
        submit = (ImageButton) findViewById(R.id.submit);
        undo = (ImageButton) findViewById(R.id.before);
        att = new attention();
        helper_img = findViewById(R.id.img);
        U_answers = new String[att.num];
        pro_bar = (ProgressBar) findViewById(R.id.progressBar);
        donKnow = (AppCompatButton) findViewById(R.id.donknow);

        type.setText("주의력");
        pro_bar.setProgress(20);
        answer.setHint("답변이 여기에 나타납니다.");
        Arrays.fill(U_answers, "");

        Intent intent;
        intent = getIntent();
        att.scores = intent.getIntArrayExtra("scores");

        tts = new TTS(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tem.clear();
                tts.onInit(status, question.getText().toString(), "default", 1000);
                tem.add("6, 9, 7, 3");
                tts.UtteranceProgress(tem, "continue", sttBtn, submit, answer);
                sttBtn.setEnabled(false);
                submit.setEnabled(false);
                answer.setEnabled(false);
            }
        });
        stt = new MainSTT(this, answer, question, sttBtn, submit, tts);
        QP = new QuizPage(stt, tts, question, answer, sttBtn, submit, att.quiz);

        helper = new Helper(tts, stt, helper_img, this);
        helper.setTest();

        question.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                question.setText(att.quiz.get(QP.current));
                tts.speakOut(question.getText().toString(),"default");
                if(QP.current == 0){
                    tem.clear();
                    tem.add("6, 9, 7, 3");
                    tts.UtteranceProgress(tem, "continue", sttBtn, submit, answer);
                }
                else if(QP.current == 1){
                    tem.clear();
                    tem.add("5, 7, 2, 8, 4");
                    tts.UtteranceProgress(tem, "continue", sttBtn, submit, answer);
                }
                else if(QP.current == 2){
                    tem.clear();
                    tem.add("금수강산");
                    tts.UtteranceProgress(tem, "continue", sttBtn, submit, answer);
                }
            }
        });

        helper_img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!tts.IsTalking() && !answer.getText().toString().equals(""))
                    tts.speakOut(answer.getText().toString());
            }
        });

        sttBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tts.isStopUtt = true;
                tts.Stop();
                stt.start_STT();
            }
        });

        donKnow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                answer.setText("");
                if(QP.current == 0){
                    pro_bar.setProgress(25);
                    sttBtn.setEnabled(false);
                    submit.setEnabled(false);
                    answer.setEnabled(false);
                    tts.isStopUtt = false;
                    QP.Submit();
                    tts.speakOut(question.getText().toString(), "default");
                    tem.clear();
                    tem.add("5, 7, 2, 8, 4");
                    tts.UtteranceProgress(tem, "continue", sttBtn, submit, answer);
                }
                else if(QP.current == 1){
                    pro_bar.setProgress(30);
                    sttBtn.setEnabled(false);
                    submit.setEnabled(false);
                    answer.setEnabled(false);
                    tts.isStopUtt = false;
                    QP.Submit();
                    tts.speakOut(question.getText().toString(), "default");
                    tem.clear();
                    tem.add("금수강산");
                    tts.UtteranceProgress(tem, "continue", sttBtn, submit, answer);
                }
                else if(QP.current == 2){
                    pro_bar.setProgress(35);

                    att.Tscore = cal_score(U_answers, att.crr_ans);

                    att.scores[3] = att.Tscore;

                    Intent intent = new Intent(getApplicationContext(), SpaceTimePage.class);
                    intent.putExtra("scores", att.scores);
                    startActivity(intent);

                    finish();
                }
            }
        });

        undo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(QP.current == 0){
                    Toast.makeText(getApplicationContext(), "해당 항목의 첫 문제 입니다.",
                            Toast.LENGTH_SHORT).show();
                }
                if(QP.current > 0){
                    QP.current --;
                    tts.isStopUtt = false;
                    question.setText(att.quiz.get(QP.current));
                    answer.setText("");
                    sttBtn.setEnabled(false);
                    submit.setEnabled(false);
                    answer.setEnabled(false);

                    tts.speakOut(question.getText().toString(),"default");
                    if(QP.current == 0){
                        pro_bar.setProgress(20);
                        tem.clear();
                        tem.add("6, 9, 7, 3");
                        tts.UtteranceProgress(tem, "continue", sttBtn, submit, answer);
                    }
                    else if(QP.current == 1){
                        pro_bar.setProgress(25);
                        tem.clear();
                        tem.add("5, 7, 2, 8, 4");
                        tts.UtteranceProgress(tem, "continue", sttBtn, submit, answer);
                    }
                    else if(QP.current == 2){
                        pro_bar.setProgress(30);
                        tem.clear();
                        tem.add("금수강산");
                        tts.UtteranceProgress(tem, "continue", sttBtn, submit, answer);
                    }
                }
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

                if(QP.user_ans.isEmpty()){
                    tts.speakOut("무응답으로 넘어가실 수 없습니다.\n아시는 대로 천천히 말씀해주시면 됩니다.");
                }
                else
                {
                    U_answers[QP.current] = QP.user_ans;
                    answer.setText("");
                    if(QP.current == 0){
                        pro_bar.setProgress(25);
                        sttBtn.setEnabled(false);
                        submit.setEnabled(false);
                        answer.setEnabled(false);
                        tts.isStopUtt = false;
                        QP.Submit();
                        tts.speakOut(question.getText().toString(), "default");
                        tem.clear();
                        tem.add("5, 7, 2, 8, 4");
                        tts.UtteranceProgress(tem, "continue", sttBtn, submit, answer);
                    }
                    else if(QP.current == 1){
                        pro_bar.setProgress(30);
                        sttBtn.setEnabled(false);
                        submit.setEnabled(false);
                        answer.setEnabled(false);
                        tts.isStopUtt = false;
                        QP.Submit();
                        tts.speakOut(question.getText().toString(), "default");
                        tem.clear();
                        tem.add("금수강산");
                        tts.UtteranceProgress(tem, "continue", sttBtn, submit, answer);
                    }
                    else if(QP.current == 2){
                        pro_bar.setProgress(35);

                        att.Tscore = cal_score(U_answers, att.crr_ans);

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

    int cal_score(String[] ans, List<String>[] crr){
        int score = 0;
        if(ans.length == crr.length){
            for(int i = 0; i < ans.length; i++){
                if(crr[i].size() > 1){
                    for(String s : crr[i]){
                        if(ans[i].contains(s)) score ++;
                    }
                }
                else if(ans[i].contains(crr[i].get(0))) score ++;
            }
        }
        return score;
    }

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if (0 <= gapTime && 2000 >= gapTime) {
            tts.Destroy();
            stt.Destroy();

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
        tts.Stop();
        stt.Stop();
    }

    @Override
    protected void onStart(){
        super.onStart();
        tts.isStopUtt = false;
        answer.setHint("답변이 여기에 나타납니다.");
        QP.Start();
        sttBtn.setEnabled(false);
        submit.setEnabled(false);
        answer.setEnabled(false);
        tts.speakOut(question.getText().toString(),"default");
        if(QP.current == 0){
            tem.clear();
            tem.add("6, 9, 7, 3");
            tts.UtteranceProgress(tem, "continue", sttBtn, submit, answer);
        }
        else if(QP.current == 1){
            tem.clear();
            tem.add("5, 7, 2, 8, 4");
            tts.UtteranceProgress(tem, "continue", sttBtn, submit, answer);
        }
        else if(QP.current == 2){
            tem.clear();
            tem.add("금수강산");
            tts.UtteranceProgress(tem, "continue", sttBtn, submit, answer);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        QP.Destroy();
    }
}
