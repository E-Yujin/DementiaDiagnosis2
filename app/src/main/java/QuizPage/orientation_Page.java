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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.cbnu.dementiadiagnosis.AnimationListner;
import com.cbnu.dementiadiagnosis.Helper;
import com.cbnu.dementiadiagnosis.MainSTT;
import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.TTS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import questions.orientation;

public class orientation_Page extends AppCompatActivity {
    orientation ortt_main;
    MainSTT stt;
    TTS tts;
    QuizPage QP;
    TextView question, type, announce;
    EditText answer;
    ImageButton sttBtn;
    ImageButton submit, undo;
    ImageView helper_img;
    ProgressBar pro_bar;
    Helper helper;
    boolean[] isCorrect;
    boolean[] isWrong;
    String[] U_answers;
    AppCompatButton donKnow;

    private long backBtnTime = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orientation);

        question = (TextView) findViewById(R.id.question);
        type = (TextView) findViewById(R.id.type);
        announce = (TextView) findViewById(R.id.announce);
        answer = (EditText) findViewById(R.id.result);
        sttBtn = (ImageButton) findViewById(R.id.sttStart);
        submit = (ImageButton) findViewById(R.id.submit);
        undo = (ImageButton) findViewById(R.id.before);
        pro_bar = (ProgressBar) findViewById(R.id.progressBar);
        helper_img = findViewById(R.id.img);
        ortt_main = new orientation();
        donKnow = (AppCompatButton) findViewById(R.id.donknow);


        stt = new MainSTT(this, answer, question, sttBtn, submit, tts);
        QP = new QuizPage(stt, tts, question, answer, sttBtn, submit, ortt_main.quiz);
        QP.isOrient = true;
        isCorrect = new boolean[4];
        Arrays.fill(isCorrect, false);
        isWrong = new boolean[4];
        Arrays.fill(isWrong, false);
        U_answers = new String[ortt_main.num];
        Arrays.fill(U_answers, "");
        announce.setText("년, 월, 일, 요일");
        answer.setHint("답변이 여기에 나타납니다.");

        type.setText("지남력");
        pro_bar.setProgress(0);

        tts = new TTS(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.onInit(status, question.getText().toString(), "Done", 1000);
            }
        });

        helper = new Helper(tts, stt, helper_img, this);
        helper.setTest();

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
                announce.setText("");
                answer.setText("");

                if(QP.current == 0){
                    QP.current = 4;
                    pro_bar.setProgress(10);
                }
                if(QP.current == 3) pro_bar.setProgress(10);

                if(QP.current < 5){
                    answer.setText("");
                    tts.isStopUtt = false;
                    QP.Submit();
                    tts.speakOut(question.getText().toString());
                }
                else if(QP.current == 5){

                    ortt_main.Tscore = cal_score(U_answers, ortt_main.crr_ans);

                    ortt_main.scores[1] = ortt_main.Tscore;

                    Intent intent = new Intent(getApplicationContext(),ExecutionPage.class);
                    intent.putExtra("scores", ortt_main.scores);
                    startActivity(intent);

                    QP.isOrient = false;

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
                    pro_bar.setProgress(5);
                    for(int i = isWrong.length - 1; i > 0; i--){
                        if(!isWrong[i]){
                            QP.current = i;
                            break;
                        } // 모두 정답이면 첫 문제로 점프
                        else QP.current = 0;
                    }
                    if(QP.current == 0) announce.setText("년, 월, 일, 요일");
                    tts.isStopUtt = false;
                    question.setText(ortt_main.quiz.get(QP.current));
                    answer.setText("");
                    tts.speakOut(question.getText().toString());
                    if(QP.current == 0){

                    }
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                announce.setText("");
                stt.Stop();
                tts.Stop();
                tts.isStopUtt = true;
                sttBtn.setEnabled(true);

                QP.user_ans = QP.ans_filter(answer.getText().toString());

                if(QP.user_ans.isEmpty()){
                    tts.speakOut("무응답으로 넘어가실 수 없습니다.\n아시는 대로 천천히 말씀해주시면 됩니다.");
                }
                else
                {
                    //날짜 숫자로 변환
                    if(QP.current == 0 && !QP.user_ans.equals("")){ // 첫 물음의 경우
                        Arrays.fill(isCorrect, false);
                        pro_bar.setProgress(5);
                        String[] answers = QP.user_ans.split(" ");
                        for(int i = 0; i < answers.length; i++){
                            String str = "";
                            if(answers[i].contains("년도")){
                                if(i-2 >= 0){
                                    str = answers[i-2] + answers[i-1] + answers[i].replace("년", "");
                                }
                                else if(i-1 >= 0){
                                    str = answers[i-1] + answers[i].replace("년", "");
                                }
                                else if(i >= 0){
                                    str = answers[i].replace("년", "");
                                }
                                isCorrect[0] = true;
                                U_answers[0] = dateFilter(str);
                            }
                            else if(answers[i].contains("년")){
                                if(i-2 >= 0){
                                    str = answers[i-2] + answers[i-1] + answers[i].replace("년", "");
                                }
                                else if(i-1 >= 0){
                                    str = answers[i-1] + answers[i].replace("년", "");
                                }
                                else if(i >= 0){
                                    str = answers[i].replace("년", "");
                                }
                                isCorrect[0] = true;
                                U_answers[0] = dateFilter(str);
                            }
                            else if(answers[i].contains("월")){
                                U_answers[1] = dateFilter(answers[i]);
                                isCorrect[1] = true;
                            }
                            else if(answers[i].contains("요일")){
                                U_answers[3] = answers[i];
                                isCorrect[3] = true;
                            }
                            else if(answers[i].contains("일")){
                                if(!answers[i-1].contains("월") && !answers[i-1].contains("년")
                                        && !answers[i-1].contains("년도") && !answers[i-1].contains("요일")){
                                    if(answers[i-1].contains("십")){
                                        str = answers[i-1] + answers[i];
                                        str = str.substring(0, str.length()-1);
                                        isCorrect[2] = true;
                                        U_answers[2] = dateFilter(str);
                                    }
                                }
                                else {
                                    isCorrect[2] = true;
                                    U_answers[2] = dateFilter(answers[i]);
                                }
                            }
                        }
                        isWrong = isCorrect;
                    }
                    else if(QP.current < 4 && !QP.user_ans.equals("")){ // 틀린 것이 있을 경우
                        QP.user_ans = dateFilter(QP.user_ans);
                        U_answers[QP.current-1] = dateFilter(QP.user_ans);
                    }
                    else {
                        pro_bar.setProgress(10);
                        U_answers[QP.current-1] = QP.user_ans;
                    }

                    //다음 문제 화면 전환
                    if(QP.current < 4){
                        for(int i = 0; i < isCorrect.length; i++){
                            if(!isCorrect[i]){
                                QP.current = i;
                                isCorrect[i] = true;
                                break;
                            } // 모두 정답이면 공간지남력 문제로 점프
                            else QP.current = 4;
                        }
                    }

                    if(QP.current < 5){
                        answer.setText("");
                        tts.isStopUtt = false;
                        QP.Submit();
                        tts.speakOut(question.getText().toString());
                    }
                    else if(QP.current == 5){

                        ortt_main.Tscore = cal_score(U_answers, ortt_main.crr_ans);

                        ortt_main.scores[1] = ortt_main.Tscore;

                        Intent intent = new Intent(getApplicationContext(),fluency_Page.class);
                        intent.putExtra("scores", ortt_main.scores);
                        startActivity(intent);

                        QP.isOrient = false;

                        finish();
                    }

                }
            }
        });
    }

    String dateFilter(String userAns){
        String[] ansArray = userAns.split("");
        String toDigit = "";
        for(String s : ansArray){
            if(!s.equals("")) {
                if(Character.isDigit(s.charAt(0))) {
                    toDigit += s;
                }
            }
        }

        if(toDigit.equals("")){
            userAns = ortt_main.KorTran(userAns);
        }
        else{
            userAns = toDigit;
        }

        return userAns;
    }

    int cal_score(String[] ans, List<String>[] crr){
        int score = 0;
        if(ans.length == crr.length-1){
            for(int i = 0; i < ans.length; i++){
                if(ans[i].contains(crr[i + 1].get(0))) score ++;
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
    protected void onStart(){
        super.onStart();
        tts.isStopUtt = false;
        answer.setHint("답변이 여기에 나타납니다.");
        QP.Start();
        tts.speakOut(question.getText().toString());
        if(QP.current == 0){
            pro_bar.setProgress(0);
        }
        if(QP.current == 1){
            pro_bar.setProgress(5);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        QP.Destroy();
    }
}