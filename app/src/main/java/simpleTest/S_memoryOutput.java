package simpleTest;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;

import QuizPage.QuizPage;
import questions.memoryOutput;
import user.SharedPreference;

public class S_memoryOutput extends AppCompatActivity {
    memoryOutput memo_out;
    MainSTT stt;
    TTS tts;
    QuizPage QP;
    TextView question, type;
    EditText answer;
    ImageButton sttBtn;
    ImageButton submit, undo;
    ImageView helper_img;
    Helper helper;
    ProgressBar pro_bar;
    AppCompatButton donKnow;

    boolean first[];

    private long backBtnTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orientation);
        final TextInputLayout TIL = findViewById(R.id.goolelayout);

        question = (TextView) findViewById(R.id.question);
        type = (TextView) findViewById(R.id.type);
        answer = (EditText) findViewById(R.id.result);
        answer = TIL.getEditText();
        answer.setEnabled(true);
        sttBtn = (ImageButton) findViewById(R.id.sttStart);
        submit = (ImageButton) findViewById(R.id.submit);
        undo = (ImageButton) findViewById(R.id.before);
        memo_out = new memoryOutput();
        helper_img = findViewById(R.id.img);
        pro_bar = (ProgressBar) findViewById(R.id.progressBar);
        donKnow = (AppCompatButton) findViewById(R.id.donknow);

        type.setText("기억회상");
        pro_bar.setProgress(55);
        answer.setHint("답변이 여기에 나타납니다.");

        Intent intent;
        intent = getIntent();
        memo_out.scores = intent.getIntArrayExtra("scores");

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

        QP = new QuizPage(stt, tts, question, answer, sttBtn,submit, memo_out.quiz);

        helper = new Helper(tts, stt, helper_img, this);
        helper.setTest();

        first = new boolean[memo_out.num];
        Arrays.fill(first, false);
        first[0] = true;

        question.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tts.speakOut(question.getText().toString());
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
                QP.current ++;
                if(QP.current == 0) QP.current = 6;
                else{
                    while (QP.current < 6){
                        if(!first[QP.current]){
                            question.setText(memo_out.quiz.get(QP.current));
                            tts.speakOut(question.getText().toString());
                            break;
                        }
                        else QP.current ++;
                    }
                }
                if(QP.current == 6){
                    pro_bar.setProgress(70);
                    memo_out.scores[6] = memo_out.Tscore;

                    Intent intent = new Intent(getApplicationContext(), S_language.class);
                    intent.putExtra("scores", memo_out.scores);
                    startActivity(intent);
                    overridePendingTransition(0, 0);

                    finish();
                }
            }
        });

        undo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(getApplicationContext(), "기억력 항목에서는 뒤로가기를 할 수 없습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 설정 초기화
                stt.Stop();
                tts.Stop();
                sttBtn.setEnabled(true);
                tts.isStopUtt = true;

                QP.user_ans = answer.getText().toString();

                if(QP.user_ans.isEmpty()){
                    tts.speakOut("무응답으로 넘어가실 수 없습니다.\n아시는 대로 천천히 말씀해주시면 됩니다.");
                }
                else{
                    answer.setText("");
                    if(QP.current == 0){
                        pro_bar.setProgress(60);
                        for(int i = 0; i<5; i++){
                            QP.correct = memo_out.crr_ans[0].get(i);
                            if(QP.user_ans.contains(QP.correct)){
                                first[i+1] = true;
                                memo_out.Tscore += 2;
                            }
                            else if(QP.correct == "11시"){
                                if(QP.user_ans.contains("열 한시") || QP.user_ans.contains("열한시")){
                                    first[i+1] = true;
                                    memo_out.Tscore += 2;
                                }
                            }
                        }
                        if(memo_out.Tscore == 10){ // 점수가 만점이면 액티비티 종료.
                            memo_out.scores[6] = memo_out.Tscore;

                            Intent intent = new Intent(getApplicationContext(), S_language.class);
                            intent.putExtra("scores", memo_out.scores);
                            startActivity(intent);
                            overridePendingTransition(0, 0);

                            finish();
                        }
                        else{
                            while (QP.current < 6){
                                if(!first[QP.current]){
                                    question.setText(memo_out.quiz.get(QP.current));
                                    tts.speakOut(question.getText().toString());
                                    break;
                                }
                                else QP.current ++;
                            }
                        }
                    }
                    else {
                        QP.correct = memo_out.crr_ans[QP.current].get(0);
                        if(QP.user_ans.contains(QP.correct)){
                            memo_out.Tscore ++;
                        }
                        else if(QP.correct == "11시"){
                            if(QP.user_ans.contains("열 한시") || QP.user_ans.contains("열한시")){
                                memo_out.Tscore ++;
                            }
                        }
                        QP.current ++;
                        while (QP.current < 6){
                            if(!first[QP.current]){
                                question.setText(memo_out.quiz.get(QP.current));
                                tts.speakOut(question.getText().toString());
                                break;
                            }
                            else QP.current ++;
                        }
                        if(QP.current == 6){
                            pro_bar.setProgress(70);
                            memo_out.scores[6] = memo_out.Tscore;

                            Intent intent = new Intent(getApplicationContext(), S_language.class);
                            intent.putExtra("scores", memo_out.scores);
                            startActivity(intent);
                            overridePendingTransition(0, 0);

                            finish();
                        }
                    }
                }
            }
        });
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
    protected void onStop(){
        tts.isStopUtt = true;
        super.onStop();
        tts.Stop();
        stt.Stop();
    }

    @Override
    protected void onStart(){
        super.onStart();
        answer.setHint("답변이 여기에 나타납니다.");
        if(QP.current < 2){
            QP.Start();
            tts.speakOut(question.getText().toString());
        }
        else{
            tts.speakOut(question.getText().toString());
            sttBtn.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        QP.Destroy();
    }
}
