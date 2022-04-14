package QuizPage;


import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.TTS;

import java.util.ArrayList;
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
    TextView question;
    TextView announce, total_score;
    String total;
    Button submit;
    QuizPage QP;
    TTS tts;
    int c = 0;

    private long backBtnTime = 0;
    List<String> tem = new ArrayList<>();
    int[] time = {2000, 2000, 2000, 2000};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.execution);

        question = findViewById(R.id.question);
        announce = findViewById(R.id.textView);
        submit = findViewById(R.id.btnSubmit);
        execution = new Execution();
        total_score = findViewById(R.id.score);

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
                tts.onInit(status, question.getText().toString());
                tem.add("모양들을 보면서 어떤 순서로 나오는지 생각해보세요.");
                tem.add("네모, 동그라미, 세모, 네모, 빈칸, 세모");
                tem.add("그렇다면 여기 빈칸에는 무엇이 들어가야 할까요?");
                tem.add(announce.getText().toString());
                tts.UtteranceProgress(tem, "continue", time, question);
            }
        }, submit);
        QP = new QuizPage(tts, question, announce, submit, execution.quiz);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (c) {
                    case 0:
                        total = executionOne.oneResult();
                        Log.d("total_one", total);
                        break;
                    case 1:
                        total = executionTwo.twoResult();
                        Log.d("total_two", total);
                        break;
                    case 2:
                        total = executionThree.threeResult();
                        Log.d("total_three", total);
                        break;
                    default:
                        Log.d("case default!!", "넘어감");
                        break;
                }
                if(total.isEmpty()) {
                    total_score.setText("응답을 하셔야 넘어가실 수 있습니다");
                }
                else {
                    tts.Stop();
                    tts.isStopUtt = true;

                    String correct = execution.crr_ans[QP.current].get(0);

                    if(correct.equals(total)) {
                        execution.score++;
                    }
                    c++;
                    if(QP.current == 0) {
                        tts.isStopUtt = false;
                        QP.Submit();
                        tts.speakOut(question.getText().toString());
                        tem.clear();
                        tem.add("별이 이렇게 다른 위치로 이동합니다.");
                        tem.add("어떤 식으로 이동하는지 잘 생각해 보십시오.");
                        tem.add("여기서는 네 칸중 별이 어디에 위치하게 될까요?");
                        announce.setText("잘 생각해보시고, 네 칸중 한칸을 선택해주세요");
                        tem.add(announce.getText().toString());
                        tts.UtteranceProgress(tem, "continue", time, question);
                        fragmentManager.beginTransaction().replace(R.id.frame_layout, executionTwo).commit();
                    }
                    else if(QP.current == 1) {
                        tts.isStopUtt = false;
                        QP.Submit();//2
                        tts.speakOut(question.getText().toString());
                        tem.clear();
                        tem.add("'1 봄 2 여름 ~' 이런 형태로 연결되어 나갑니다.");
                        tem.add("여기는 무엇이 들어갈 차례일까요?");
                        announce.setText("잘 생각해보시고, 들어갈 단어를 입력해주세요");
                        tem.add(announce.getText().toString());
                        tts.UtteranceProgress(tem, "continue", time, question);
                        fragmentManager.beginTransaction().replace(R.id.frame_layout, executionThree).commit();
                    }
                    else if(QP.current == 2) {
                        Intent resultIntent = new Intent();

                        resultIntent.putExtra("isDone", true);
                        resultIntent.putExtra("score", execution.score);

                        setResult(1, resultIntent);
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

}
