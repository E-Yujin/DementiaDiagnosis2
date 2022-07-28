package QuizPage;


import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.cbnu.dementiadiagnosis.MainSTT;
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
    EditText answer;
    String total;
    ImageButton sttBtn;
    Button submit;
    QuizPage QP;
    MainSTT stt;
    TTS tts;

    private long backBtnTime = 0;
    List<String> tem = new ArrayList<>();
    int[] time = {2000, 2000, 2000, 2000};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.execution);

        question = findViewById(R.id.question);
        answer = findViewById(R.id.result);
        sttBtn = findViewById(R.id.sttStart);
        submit = findViewById(R.id.btnSubmit);
        execution = new Execution();

        Intent intent;
        intent = getIntent();
        execution.scores = intent.getIntArrayExtra("scores");

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
                tts.onInit(status, question.getText().toString(), "default", 1000);
                tem.add("모양들을 보면서 어떤 순서로 나오는지 생각해보세요.");
                tem.add("네모, 동그라미, 세모, 네모, 빈칸, 세모");
                tem.add("그렇다면 빈칸에는 무엇이 들어가야 할까요?");
                tts.UtteranceProgress(tem, "continue", time, question, sttBtn, submit);
            }
        });
        stt = new MainSTT(this, answer, question, sttBtn, submit, tts);
        QP = new QuizPage(stt, tts, question, answer, sttBtn,submit, execution.quiz);

        sttBtn.setEnabled(false);

        sttBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tts.isStopUtt = true;
                tts.Stop();
                stt.start_STT();
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

                List<String> correct = new ArrayList<>();
                correct.clear();
                for(String data : execution.crr_ans[QP.current]){
                    correct.add(data);
                }

                answer.setText("");

                if(total.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"응답을 하셔야 넘어가실 수 있습니다", Toast.LENGTH_SHORT).show();
                }
                else {
                    tts.Stop();
                    tts.isStopUtt = true;

                    for(String data : correct){
                        if(total.contains(data)){
                            execution.Tscore ++;
                        }
                        if(QP.current + 1 < execution.Tscore){
                            execution.Tscore = QP.current + 1;
                        }
                    }

                    if(QP.current == 0) {
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
                        sttBtn.setEnabled(true);
                        answer.setVisibility(View.VISIBLE);
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
