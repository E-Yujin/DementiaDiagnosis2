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

import com.cbnu.dementiadiagnosis.Helper;
import com.cbnu.dementiadiagnosis.MainSTT;
import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.TTS;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import questions.memoryOutput;

public class memoryOutput_Page extends AppCompatActivity {
    memoryOutput memo_out;
    MainSTT stt;
    TTS tts;
    QuizPage QP;
    TextView question, type, p_num;
    EditText answer;
    ImageButton sttBtn;
    ImageButton submit, undo;
    ImageView helper_img;
    Helper helper;
    ProgressBar pro_bar;

    boolean first[];

    private long backBtnTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memory_input);
        final TextInputLayout TIL = findViewById(R.id.goolelayout);

        question = (TextView) findViewById(R.id.question);
        type = (TextView) findViewById(R.id.type);
        p_num = (TextView) findViewById(R.id.process_num);
        answer = (EditText) findViewById(R.id.result);
        answer = TIL.getEditText();
        answer.setEnabled(true);
        sttBtn = (ImageButton) findViewById(R.id.sttStart);
        submit = (ImageButton) findViewById(R.id.submit);
        undo = (ImageButton) findViewById(R.id.before);
        memo_out = new memoryOutput();
        helper_img = findViewById(R.id.img);
        pro_bar = (ProgressBar) findViewById(R.id.progressBar);

        type.setText("기억회상");
        p_num.setText("12/17");
        pro_bar.setProgress(55);

        Intent intent;
        intent = getIntent();
        memo_out.scores = intent.getIntArrayExtra("scores");

        tts = new TTS(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.onInit(status, question.getText().toString(), "Done", 1000);
            }
        });
        stt = new MainSTT(this, answer, question, sttBtn, submit, tts);
        QP = new QuizPage(stt, tts, question, answer, sttBtn,submit, memo_out.quiz);

        helper = new Helper(tts, stt, helper_img, this);
        helper.setTest();

        first = new boolean[memo_out.num];
        Arrays.fill(first, false);
        first[0] = true;

        sttBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tts.isStopUtt = true;
                tts.Stop();
                stt.start_STT();
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
                            if(QP.user_ans.contains("열 한시")){
                                first[i+1] = true;
                                memo_out.Tscore += 2;
                            }
                        }
                    }
                    if(memo_out.Tscore == 10){ // 점수가 만점이면 액티비티 종료.
                        memo_out.scores[6] = memo_out.Tscore;

                        Intent intent = new Intent(getApplicationContext(), LanguagePage.class);
                        intent.putExtra("scores", memo_out.scores);
                        startActivity(intent);

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
                        if(QP.user_ans.contains("열 한시")){
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

                        Intent intent = new Intent(getApplicationContext(), LanguagePage.class);
                        intent.putExtra("scores", memo_out.scores);
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
