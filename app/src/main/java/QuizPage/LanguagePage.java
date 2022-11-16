package QuizPage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import questions.LanguageFunc;
import questions.attention;
import user.SharedPreference;

public class LanguagePage extends AppCompatActivity {
    LanguageFunc languageFunc;
    MainSTT stt;
    TTS tts;
    QuizPage QP;
    TextView question, type;
    //TextView p_num;
    ImageView image;
    EditText answer;
    ImageButton sttBtn;
    ImageButton submit, undo;
    String[] U_answers;
    ActivityResultLauncher<Intent> startActivityResult;
    private long backBtnTime = 0;
    ProgressBar pro_bar;
    AppCompatButton donKnow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_func);

        question = (TextView) findViewById(R.id.question);
        type = (TextView) findViewById(R.id.type);
        //p_num = (TextView) findViewById(R.id.process_num);
        image = (ImageView) findViewById(R.id.question_image);
        answer = (EditText) findViewById(R.id.result);
        sttBtn = (ImageButton) findViewById(R.id.sttStart);
        submit = (ImageButton) findViewById(R.id.right);
        languageFunc = new LanguageFunc();
        U_answers = new String[languageFunc.num];
        pro_bar = (ProgressBar) findViewById(R.id.progressBar);
        undo = (ImageButton) findViewById(R.id.left);
        donKnow = (AppCompatButton) findViewById(R.id.donknow);

        answer.setHint("답변이 여기 나타납니다.");

        Intent intent;
        intent = getIntent();
        languageFunc.scores = intent.getIntArrayExtra("scores");

        Arrays.fill(U_answers, "");

        type.setText("언어기능");
        //p_num.setText("13/17");
        pro_bar.setProgress(70);

        tts = new TTS(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.onInit(status, question.getText().toString(), "Done", 1000);
            }
        });

        stt = new MainSTT(this, answer, question, sttBtn, submit, tts,
                SharedPreference.getSTT_start(this), SharedPreference.getSTT_end(this),
                SharedPreference.getSTT_speed(this));
        Log.d("STT_setting", "s= "+stt.getStart()+", e= "+stt.getEnd()+", v= "+stt.getSpeed());

        QP = new QuizPage(stt, tts, question, answer, sttBtn, submit, languageFunc.quiz);

        image.setImageResource(R.drawable.toothbrush);

        startActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Log.d("launcher_intent", "성공!!!!!!!!!!!");
                            assert result.getData() != null;
                            String str = result.getData().getStringExtra("comprehension");
                            int curr = result.getData().getIntExtra("current",-2);

                            if(curr == -1){
                                U_answers[3] = str;
                                languageFunc.Tscore = cal_score(U_answers, languageFunc.crr_ans);

                                languageFunc.scores[7] = languageFunc.Tscore;

                                Intent intent = new Intent(getApplicationContext(), fluency_Page.class);
                                intent.putExtra("scores", languageFunc.scores);
                                startActivity(intent);

                                finish();
                            }
                            else{
                                QP.current = curr;
                                tts.isStopUtt = false;
                                question.setText(languageFunc.quiz.get(QP.current));
                                tts.speakOut(question.getText().toString());
                                answer.setText("");
                                pro_bar.setProgress(80);
                                //p_num.setText("15/17");
                                image.setImageResource(R.drawable.dice);
                            }
                        }
                    }
                });

        question.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tts.isStopUtt = true;
                tts.Stop();
                tts.speakOut(question.getText().toString());
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
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
                tts.isStopUtt = true;
                tts.Stop();
                question.setText(languageFunc.quiz.get(QP.current));
                answer.setText("");
                answer.setHint("답변이 여기 나타납니다.");
                if (QP.current == 0) {
                    pro_bar.setProgress(75);
                    image.setImageResource(R.drawable.swing);
                    tts.isStopUtt = false;
                    QP.Submit();
                    tts.speakOut(question.getText().toString());
                } else if (QP.current == 1) {
                    pro_bar.setProgress(80);
                    image.setImageResource(R.drawable.dice);
                    tts.isStopUtt = false;
                    QP.Submit();
                    tts.speakOut(question.getText().toString());
                } else if (QP.current == 2) {
                    pro_bar.setProgress(80);
                    Intent intent = new Intent(LanguagePage.this, ComprehensionPage.class);
                    startActivityResult.launch(intent);
                    overridePendingTransition(0, 0);
                    intent.putExtra("isDone", true);
                    intent.putExtra("score", languageFunc.Tscore);
                    setResult(1, intent);
                    QP.Submit();
                }
            }
        });

        undo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                tts.isStopUtt = true;
                tts.Stop();
                if(QP.current == 0){
                    Toast.makeText(getApplicationContext(), "해당 항목의 첫 문제 입니다.",
                            Toast.LENGTH_SHORT).show();
                }
                if(QP.current > 0){
                    answer.setHint("답변이 여기 나타납니다.");
                    QP.current --;
                    tts.isStopUtt = false;
                    question.setText(languageFunc.quiz.get(QP.current));
                    answer.setText("");
                    tts.speakOut(question.getText().toString());
                    if(QP.current == 0){
                        pro_bar.setProgress(70);
                        //p_num.setText("13/17");
                        image.setImageResource(R.drawable.toothbrush);
                    }
                    else if(QP.current == 1){
                        pro_bar.setProgress(75);
                        //p_num.setText("14/17");
                        image.setImageResource(R.drawable.swing);
                    }
                    else if(QP.current == 2){
                        pro_bar.setProgress(80);
                        //p_num.setText("15/17");
                        image.setImageResource(R.drawable.dice);
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
                answer.setHint("답변이 여기 나타납니다.");

                QP.user_ans = answer.getText().toString()
                        .replace(" ", "")
                        .replace(",", "")
                        .replace(".", "");


                answer.setText("");

                if (QP.user_ans.isEmpty()) {
                    tts.speakOut("무응답으로 넘어가실 수 없습니다.\n아시는 대로 천천히 말씀해주시면 됩니다.");
                } else {
                    U_answers[QP.current] = QP.user_ans;

                    if (QP.current == 0) {
                        //p_num.setText("14/17");
                        pro_bar.setProgress(75);
                        image.setImageResource(R.drawable.swing);
                        tts.isStopUtt = false;
                        QP.Submit();
                        tts.speakOut(question.getText().toString());
                    } else if (QP.current == 1) {
                        //p_num.setText("15/17");
                        pro_bar.setProgress(80);
                        image.setImageResource(R.drawable.dice);
                        tts.isStopUtt = false;
                        QP.Submit();
                        tts.speakOut(question.getText().toString());
                    } else if (QP.current == 2) {
                        //p_num.setText("16/17");
                        pro_bar.setProgress(80);
                        Intent intent = new Intent(LanguagePage.this, ComprehensionPage.class);
                        startActivityResult.launch(intent);
                        overridePendingTransition(0, 0);
                        intent.putExtra("isDone", true);
                        intent.putExtra("score", languageFunc.Tscore);
                        setResult(1, intent);
                        QP.Submit();
                    } /*else if(QP.current == 3) {
                        Intent intent = new Intent();
                        intent.putExtra("isDone", true);
                        intent.putExtra("score", languageFunc.score);
                        setResult(1, intent);
                        finish();
                    }*/
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

    public boolean onTouchEvent(MotionEvent event) {
        return QP.onTouchEvent(event, undo, submit);
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
    protected void onStart(){
        super.onStart();
        question.setText(languageFunc.quiz.get(QP.current));
        tts.isStopUtt = false;
        answer.setHint("답변이 여기 나타납니다.");
        answer.setText("");
        tts.speakOut(question.getText().toString());
        QP.Start();
        if(QP.current == 0){
            pro_bar.setProgress(70);
            image.setImageResource(R.drawable.toothbrush);
        }
        else if (QP.current == 1) {
            pro_bar.setProgress(75);
            image.setImageResource(R.drawable.swing);
        } else if (QP.current == 2) {
            pro_bar.setProgress(80);
            image.setImageResource(R.drawable.dice);
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
    protected void onDestroy() {
        super.onDestroy();
        QP.Destroy();
    }
}