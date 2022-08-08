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
import com.cbnu.dementiadiagnosis.MainSTT;
import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.TTS;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import QuizPage.QuizPage;
import questions.memoryInput;

public class memoryInput_Page extends AppCompatActivity {
    memoryInput memo_in;
    MainSTT stt;
    TTS tts;
    QuizPage QP;
    TextView question, type;
    TextInputEditText answer;
    ImageButton sttBtn;
    ImageButton submit, undo;
    ImageView helper_img;
    Helper helper;
    ProgressBar pro_bar;
    AppCompatButton donKnow;

    boolean isfirst = true;

    ArrayList<String> first, second;
    List<String> tem = new ArrayList<>();

    private long backBtnTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orientation);
        final TextInputLayout TIL = findViewById(R.id.goolelayout);

        question = (TextView) findViewById(R.id.question);
        type = (TextView) findViewById(R.id.type);
        answer = findViewById(R.id.result);
        answer.setEnabled(true);
        sttBtn = (ImageButton) findViewById(R.id.sttStart);
        submit = (ImageButton) findViewById(R.id.submit);
        undo = (ImageButton) findViewById(R.id.before);
        helper_img = findViewById(R.id.img);
        memo_in = new memoryInput();
        pro_bar = (ProgressBar) findViewById(R.id.progressBar);
        donKnow = (AppCompatButton) findViewById(R.id.donknow);

        type.setText("기억등록");
        pro_bar.setProgress(10);

        Intent intent;
        intent = getIntent();
        memo_in.scores = intent.getIntArrayExtra("scores");

        tts = new TTS(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                int[] time = {1000, 500};
                tts.onInit(status, question.getText().toString(), "continue", 1000);
                tem.add("민수는.....자전거를 타고.....공원에 가서....11시부터...야구를 했다.");
                tts.UtteranceProgress(tem, "continue", time, sttBtn, submit);
            }
        });


        stt = new MainSTT(this, answer, question, sttBtn, submit, tts);
        QP = new QuizPage(stt, tts, question, answer, sttBtn,submit, memo_in.quiz);
        first = new ArrayList<>();
        second = new ArrayList<>();

        helper = new Helper(tts, stt, helper_img, this);
        helper.setTest();

        sttBtn.setEnabled(false);
        submit.setEnabled(false);
        answer.setEnabled(false);
        answer.setHint("이 항목은 자판 입력이 불가능합니다.");

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
                    pro_bar.setProgress(15);
                    tts.isStopUtt = false;
                    sttBtn.setEnabled(false);
                    submit.setEnabled(false);

                    QP.Submit();
                    tts.speakOut(question.getText().toString(), "default");
                    tts.UtteranceProgress(tem,"continue", sttBtn, submit);
                }
                else if(QP.current == 1){
                    pro_bar.setProgress(20);
                    tts.isStopUtt = false;
                    QP.Submit();
                    tts.speakOut(question.getText().toString());
                    tts.UtteranceProgress();
                    sttBtn.setEnabled(false);
                    submit.setEnabled(true);
                }
                else{
                    memo_in.scores[2] = memo_in.Tscore;

                    Intent intent = new Intent(getApplicationContext(), attention_Page.class);
                    intent.putExtra("scores", memo_in.scores);
                    startActivity(intent);

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
                isfirst = false;
                stt.Stop();
                tts.Stop();
                sttBtn.setEnabled(true);
                tts.isStopUtt = true;

                QP.user_ans = answer.getText().toString();

                if(QP.user_ans.isEmpty() && QP.current != 2){
                    tts.speakOut("무응답으로 넘어가실 수 없습니다.\n아시는 대로 천천히 말씀해주시면 됩니다.");
                }
                else{
                    answer.setText("");
                    if(QP.current == 0){
                        pro_bar.setProgress(15);
                        tts.isStopUtt = false;
                        sttBtn.setEnabled(false);
                        submit.setEnabled(false);

                        for(int i = 0; i<5; i++){
                            QP.correct = memo_in.crr_ans[i].get(0);
                            if(QP.user_ans.contains(QP.correct)){
                                first.add(QP.correct);
                            }
                        }
                        QP.Submit();
                        tts.speakOut(question.getText().toString(), "default");
                        tts.UtteranceProgress(tem,"continue", sttBtn, submit);
                    }
                    else if(QP.current == 1){
                        pro_bar.setProgress(20);
                        tts.isStopUtt = false;
                        for(int i = 0; i<5; i++){
                            QP.correct = memo_in.crr_ans[i].get(0);
                            if(QP.user_ans.contains(QP.correct)){
                                second.add(QP.correct);
                            }
                        }
                        QP.Submit();
                        tts.speakOut(question.getText().toString());
                        tts.UtteranceProgress();
                        sttBtn.setEnabled(false);
                        submit.setEnabled(true);
                    }
                    else{
                        memo_in.scores[2] = memo_in.Tscore;

                        Intent intent = new Intent(getApplicationContext(), attention_Page.class);
                        intent.putExtra("scores", memo_in.scores);
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
        sttBtn.setEnabled(false);
        submit.setEnabled(false);
        answer.setEnabled(false);
        answer.setHint("이 항목은 자판 입력이 불가능합니다.");

        if(QP.current < 2){
            question.setText(memo_in.quiz.get(QP.current));
            answer.setText("");
            tts.speakOut(question.getText().toString(),"continue");
            tts.UtteranceProgress(tem,"continue", sttBtn, submit);
        }
        else{
            tts.speakOut(question.getText().toString());
            tts.UtteranceProgress("다음 단계로 이동하시려면\n아래 파란 상자를 눌러주세요!", sttBtn, submit);
            sttBtn.setEnabled(false);
            submit.setEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        QP.Destroy();
    }
}
