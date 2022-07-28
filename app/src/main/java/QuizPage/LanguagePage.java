package QuizPage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
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

import com.cbnu.dementiadiagnosis.MainSTT;
import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.TTS;

import java.util.ArrayList;
import java.util.List;

import questions.LanguageFunc;
import questions.attention;

public class LanguagePage extends AppCompatActivity {
    LanguageFunc languageFunc;
    MainSTT stt;
    TTS tts;
    QuizPage QP;
    TextView question, type, p_num;
    ImageView image;
    EditText answer;
    ImageButton sttBtn;
    ImageButton submit, undo;
    String[] U_answers;
    ActivityResultLauncher<Intent> startActivityResult;
    private long backBtnTime = 0;
    ProgressBar pro_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_func);

        question = (TextView) findViewById(R.id.question);
        type = (TextView) findViewById(R.id.type);
        p_num = (TextView) findViewById(R.id.process_num);
        image = (ImageView) findViewById(R.id.question_image);
        answer = (EditText) findViewById(R.id.result);
        sttBtn = (ImageButton) findViewById(R.id.sttStart);
        submit = (ImageButton) findViewById(R.id.btnSubmit);
        languageFunc = new LanguageFunc();
        U_answers = new String[languageFunc.num];
        pro_bar = (ProgressBar) findViewById(R.id.progressBar);
        undo = (ImageButton) findViewById(R.id.before);

        Intent intent;
        intent = getIntent();
        languageFunc.scores = intent.getIntArrayExtra("scores");

        type.setText("언어기능");
        p_num.setText("13/17");
        pro_bar.setProgress(70);

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
                                p_num.setText("15/17");
                                image.setImageResource(R.drawable.dice);
                            }
                        }
                    }
                });

        tts = new TTS(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.onInit(status, question.getText().toString(), "default");
                //tts.UtteranceProgress(announce.getText().toString());
            }
        });
        stt = new MainSTT(this, answer, question, sttBtn, submit, tts);
        QP = new QuizPage(stt, tts, question, answer, sttBtn, submit, languageFunc.quiz);

        sttBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tts.isStopUtt = true;
                tts.Stop();
                stt.start_STT();
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
                    question.setText(languageFunc.quiz.get(QP.current));
                    answer.setText("");
                    tts.speakOut(question.getText().toString());
                    if(QP.current == 0){
                        pro_bar.setProgress(70);
                        p_num.setText("13/17");
                        image.setImageResource(R.drawable.toothbrush);
                    }
                    else if(QP.current == 1){
                        pro_bar.setProgress(75);
                        p_num.setText("14/17");
                        image.setImageResource(R.drawable.swing);
                    }
                    else if(QP.current == 2){
                        pro_bar.setProgress(80);
                        p_num.setText("15/17");
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
                        p_num.setText("14/17");
                        pro_bar.setProgress(75);
                        image.setImageResource(R.drawable.swing);
                        tts.isStopUtt = false;
                        QP.Submit();
                        tts.speakOut(question.getText().toString());
                    } else if (QP.current == 1) {
                        p_num.setText("15/17");
                        pro_bar.setProgress(80);
                        image.setImageResource(R.drawable.dice);
                        tts.isStopUtt = false;
                        QP.Submit();
                        tts.speakOut(question.getText().toString());
                    } else if (QP.current == 2) {
                        p_num.setText("16/17");
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
    protected void onDestroy() {
        super.onDestroy();
        QP.Destroy();
    }
}