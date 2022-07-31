package QuizPage;


import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;

import com.cbnu.dementiadiagnosis.MainSTT;
import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.TTS;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fragment.ExecutionOne;
import fragment.ExecutionThree;
import fragment.ExecutionTwo;
import questions.Execution;


public class ExecutionPage extends AppCompatActivity {

    Execution execution;
    FragmentManager fragmentManager;
    ExecutionOne executionOne;
    ExecutionTwo executionTwo;
    ExecutionThree executionThree;
    TextView question, type;
    //TextView p_num;
    EditText answer;
    String total;
    ImageButton sttBtn;
    ImageButton submit, undo;
    QuizPage QP;
    MainSTT stt;
    TTS tts;
    String[] U_answers;
    ProgressBar pro_bar;
    TextInputLayout textInputLayout;

    AppCompatButton donKnow;
    FrameLayout frame;

    private long backBtnTime = 0;
    List<String> tem = new ArrayList<>();
    int[] time = {1000, 1000, 1000, 1000};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.execution);

        question = findViewById(R.id.question);
        type = (TextView) findViewById(R.id.type);
        //p_num = (TextView) findViewById(R.id.process_num);
        textInputLayout = findViewById(R.id.textInput);
        answer = findViewById(R.id.result);
        sttBtn = findViewById(R.id.sttStart);
        submit = findViewById(R.id.right);
        undo = (ImageButton) findViewById(R.id.left);
        execution = new Execution();
        U_answers = new String[execution.num];
        pro_bar = (ProgressBar) findViewById(R.id.progressBar);
        donKnow = (AppCompatButton) findViewById(R.id.donknow);
        frame = findViewById(R.id.frame_layout);

        Intent intent;
        intent = getIntent();
        execution.scores = intent.getIntArrayExtra("scores");

        type.setText("집행기능");
        //p_num.setText("9/17");
        pro_bar.setProgress(40);
        Arrays.fill(U_answers, "");

        // Fragment 객체 선언
        fragmentManager = (FragmentManager)getSupportFragmentManager();
        executionOne = new ExecutionOne();
        executionTwo = new ExecutionTwo();
        executionThree = new ExecutionThree();

        // 제일 처음 띄워줄 뷰 설정
        fragmentManager.beginTransaction().replace(R.id.frame_layout, executionOne).commit();

        tts = new TTS(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.onInit(status, question.getText().toString(), "default");
                tem.clear();
                tem.add("모양들을 보면서 어떤 순서로 나오는지 생각해보세요.");
                tem.add("네모, 동그라미, 세모, 네모, 빈칸, 세모");
                tem.add("그렇다면 빈칸에는 무엇이 들어가야 할까요?");
                tts.UtteranceProgress(tem, "continue", question, sttBtn, submit);
            }
        });
        stt = new MainSTT(this, answer, question, sttBtn, submit, tts);
        QP = new QuizPage(stt, tts, question, answer, sttBtn, submit, execution.quiz);

        sttBtn.setEnabled(false);

        question.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                question.setText(execution.quiz.get(QP.current));
                tts.speakOut(question.getText().toString(), "default");
                if(QP.current == 0){
                    tem.clear();
                    tem.add("모양들을 보면서 어떤 순서로 나오는지 생각해보세요.");
                    tem.add("네모, 동그라미, 세모, 네모, 빈칸, 세모");
                    tem.add("그렇다면 빈칸에는 무엇이 들어가야 할까요?");
                    tts.UtteranceProgress(tem, "continue", question, sttBtn, submit);
                }
                else if(QP.current == 1){
                    tem.clear();
                    tem.add("별이 각자 다른 위치로 이동합니다.");
                    tem.add("어떤 식으로 이동하는지 잘 생각해 보십시오.");
                    tem.add("이 다음에는 네 칸중 별이 어디에 위치하게 될까요?");
                    tts.UtteranceProgress(tem, "continue", time, question, sttBtn, submit);
                }
                else if(QP.current == 2){
                    tts.speakOut(question.getText().toString(), "default");
                    tem.clear();
                    tem.add("'1 봄 2 여름 ~' 이런 형태로 연결되어 나갑니다.");
                    tem.add("빈칸에는 무엇이 들어갈 차례일까요?");
                    tts.UtteranceProgress(tem, "continue", time, question, sttBtn, submit);
                }
            }
        });

        frame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!tts.IsTalking() && !answer.getText().toString().equals(""))
                    tts.speakOut(answer.getText().toString());
            }
        });
        //안되면 위 코드 주석처리


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
                tts.Stop();
                tts.isStopUtt = true;

                if(QP.current == 0) {
                    pro_bar.setProgress(45);
                    tts.isStopUtt = false;
                    QP.current++;
                    question.setText(execution.quiz.get(QP.current));
                    tts.speakOut(question.getText().toString(), "default");
                    tem.clear();
                    tem.add("별이 각자 다른 위치로 이동합니다.");
                    tem.add("어떤 식으로 이동하는지 잘 생각해 보십시오.");
                    tem.add("이 다음에는 네 칸중 별이 어디에 위치하게 될까요?");
                    tts.UtteranceProgress(tem, "continue", time, question, sttBtn, submit);
                    fragmentManager.beginTransaction().replace(R.id.frame_layout, executionTwo).commit();
                }
                else if(QP.current == 1) {
                    pro_bar.setProgress(50);
                    sttBtn.setVisibility(View.VISIBLE);
                    sttBtn.setEnabled(true);
                    textInputLayout.setVisibility(View.VISIBLE);
                    tts.isStopUtt = false;
                    QP.current++;
                    question.setText(execution.quiz.get(QP.current));
                    tts.speakOut(question.getText().toString(), "default");
                    tem.clear();
                    tem.add("'1 봄 2 여름 ~' 이런 형태로 연결되어 나갑니다.");
                    tem.add("빈칸에는 무엇이 들어갈 차례일까요?");
                    tts.UtteranceProgress(tem, "continue", time, question, sttBtn, submit);
                    fragmentManager.beginTransaction().replace(R.id.frame_layout, executionThree).commit();
                }
                else if(QP.current == 2) {
                    pro_bar.setProgress(55);
                    execution.Tscore = cal_score(U_answers, execution.crr_ans);

                    execution.scores[5] = execution.Tscore;

                    Intent intent = new Intent(getApplicationContext(), memoryOutput_Page.class);
                    intent.putExtra("scores", execution.scores);
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
                    question.setText(execution.quiz.get(QP.current));
                    answer.setText("");
                    tts.speakOut(question.getText().toString(),"default");
                    if(QP.current == 0){
                        sttBtn.setVisibility(View.INVISIBLE);
                        sttBtn.setEnabled(false);
                        textInputLayout.setVisibility(View.INVISIBLE);
                        pro_bar.setProgress(40);
                        //p_num.setText("9/17");
                        tem.clear();
                        tem.add("모양들을 보면서 어떤 순서로 나오는지 생각해보세요.");
                        tem.add("네모, 동그라미, 세모, 네모, 빈칸, 세모");
                        tem.add("그렇다면 빈칸에는 무엇이 들어가야 할까요?");
                        tts.UtteranceProgress(tem, "continue", time, question, sttBtn, submit);
                        fragmentManager.beginTransaction().replace(R.id.frame_layout, executionOne).commit();
                    }
                    else if(QP.current == 1){
                        sttBtn.setVisibility(View.INVISIBLE);
                        sttBtn.setEnabled(false);
                        textInputLayout.setVisibility(View.INVISIBLE);
                        pro_bar.setProgress(45);
                        //p_num.setText("10/17");
                        tem.clear();
                        tem.add("별이 각자 다른 위치로 이동합니다.");
                        tem.add("어떤 식으로 이동하는지 잘 생각해 보십시오.");
                        tem.add("이 다음에는 네 칸중 별이 어디에 위치하게 될까요?");
                        tts.UtteranceProgress(tem, "continue", time, question, sttBtn, submit);
                        fragmentManager.beginTransaction().replace(R.id.frame_layout, executionTwo).commit();
                    }
                    else if(QP.current == 2){
                        pro_bar.setProgress(50);
                        sttBtn.setVisibility(View.VISIBLE);
                        sttBtn.setEnabled(true);
                        textInputLayout.setVisibility(View.VISIBLE);
                        //p_num.setText("11/17");
                        tem.clear();
                        tem.add("'1 봄 2 여름 ~' 이런 형태로 연결되어 나갑니다.");
                        tem.add("빈칸에는 무엇이 들어갈 차례일까요?");
                        tts.UtteranceProgress(tem, "continue", time, question, sttBtn, submit);
                        fragmentManager.beginTransaction().replace(R.id.frame_layout, executionThree).commit();
                    }
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stt.Stop();
                tts.Stop();
                tts.isStopUtt = true;

                QP.user_ans = answer.getText().toString()
                        .replace(",","")
                        .replace(".", "");

                switch (QP.current) {
                    case 0:
                        total = executionOne.oneResult();
                        Log.d("result", "oneResult");
                        break;
                    case 1:
                        total = executionTwo.twoResult();
                        Log.d("result", "twoResult");
                        break;
                    case 2:
                        total = answer.getText().toString();
                        Log.d("result", "threeResult " + total);
                        break;
                    default:
                        Log.d("case default!!", "넘어감");
                        break;
                }

                answer.setText("");

                if(total.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"응답을 하셔야 넘어가실 수 있습니다", Toast.LENGTH_SHORT).show();
                }
                else {
                    tts.Stop();
                    tts.isStopUtt = true;

                    U_answers[QP.current] = total;

                    if(QP.current == 0) {
                        pro_bar.setProgress(45);
                        tts.isStopUtt = false;
                        QP.current++;
                        question.setText(execution.quiz.get(QP.current));
                        //p_num.setText("10/17");
                        tts.speakOut(question.getText().toString(), "default");
                        tem.clear();
                        tem.add("별이 각자 다른 위치로 이동합니다.");
                        tem.add("어떤 식으로 이동하는지 잘 생각해 보십시오.");
                        tem.add("이 다음에는 네 칸중 별이 어디에 위치하게 될까요?");
                        tts.UtteranceProgress(tem, "continue", time, question, sttBtn, submit);
                        fragmentManager.beginTransaction().replace(R.id.frame_layout, executionTwo).commit();
                    }
                    else if(QP.current == 1) {
                        pro_bar.setProgress(50);
                        sttBtn.setVisibility(View.VISIBLE);
                        sttBtn.setEnabled(true);
                        textInputLayout.setVisibility(View.VISIBLE);
                        tts.isStopUtt = false;
                        QP.current++;
                        //p_num.setText("11/17");
                        question.setText(execution.quiz.get(QP.current));
                        tts.speakOut(question.getText().toString(), "default");
                        tem.clear();
                        tem.add("'1 봄 2 여름 ~' 이런 형태로 연결되어 나갑니다.");
                        tem.add("빈칸에는 무엇이 들어갈 차례일까요?");
                        tts.UtteranceProgress(tem, "continue", time, question, sttBtn, submit);
                        fragmentManager.beginTransaction().replace(R.id.frame_layout, executionThree).commit();
                    }
                    else if(QP.current == 2) {
                        pro_bar.setProgress(55);
                        execution.Tscore = cal_score(U_answers, execution.crr_ans);

                        execution.scores[5] = execution.Tscore;

                        Intent intent = new Intent(getApplicationContext(), memoryOutput_Page.class);
                        intent.putExtra("scores", execution.scores);
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
    protected void onStop(){
        tts.isStopUtt = true;
        super.onStop();
        tts.Stop();
        stt.Stop();
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
    protected void onStart(){
        super.onStart();
        tts.isStopUtt = false;
        QP.Start();
        tts.speakOut(question.getText().toString(),"default");
        if(QP.current == 0){
            question.setText(execution.quiz.get(QP.current));
            tts.speakOut(question.getText().toString(), "default");
            tem.clear();
            tem.add("모양들을 보면서 어떤 순서로 나오는지 생각해보세요.");
            tem.add("네모, 동그라미, 세모, 네모, 빈칸, 세모");
            tem.add("그렇다면 빈칸에는 무엇이 들어가야 할까요?");
            tts.UtteranceProgress(tem, "continue", question, sttBtn, submit);
            fragmentManager.beginTransaction().replace(R.id.frame_layout, executionOne).commit();
        }
        else if(QP.current == 1){
            question.setText(execution.quiz.get(QP.current));
            tts.speakOut(question.getText().toString(), "default");
            tem.clear();
            tem.add("별이 각자 다른 위치로 이동합니다.");
            tem.add("어떤 식으로 이동하는지 잘 생각해 보십시오.");
            tem.add("이 다음에는 네 칸중 별이 어디에 위치하게 될까요?");
            tts.UtteranceProgress(tem, "continue", time, question, sttBtn, submit);
            fragmentManager.beginTransaction().replace(R.id.frame_layout, executionTwo).commit();
        }
        else if(QP.current == 2){
            question.setText(execution.quiz.get(QP.current));
            tts.speakOut(question.getText().toString(), "default");
            tem.clear();
            tem.add("'1 봄 2 여름 ~' 이런 형태로 연결되어 나갑니다.");
            tem.add("빈칸에는 무엇이 들어갈 차례일까요?");
            tts.UtteranceProgress(tem, "continue", time, question, sttBtn, submit);
            fragmentManager.beginTransaction().replace(R.id.frame_layout, executionThree).commit();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        QP.Destroy();
    }

}
