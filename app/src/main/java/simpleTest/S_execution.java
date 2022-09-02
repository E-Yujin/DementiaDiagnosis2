package simpleTest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cbnu.dementiadiagnosis.HomeActivity;
import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.TTS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import QuizPage.QuizPage;
import questions.Execution;

public class S_execution extends AppCompatActivity {

    Execution execution;
    ImageButton squareOne, squareTwo, squareThree, squareFour;
    ImageButton beforeBtn, nextBtn;
    ImageView star1, star2, star3, star4;
    TextView question, type;
    AppCompatButton donKnow;
    TTS tts;
    ProgressBar pro_bar;
    String check="";
    simple_QuizPage QP;

    private long backBtnTime = 0;
    List<String> quiz = new ArrayList<>();
    int[] time = {1000, 1000, 1000, 1000};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_execution);

        execution = new Execution();
        question = findViewById(R.id.question);
        type = findViewById(R.id.type);
        beforeBtn = findViewById(R.id.before);
        nextBtn = findViewById(R.id.next);
        pro_bar = findViewById(R.id.progressBar);
        donKnow = (AppCompatButton) findViewById(R.id.donknow);
        QP = new simple_QuizPage();

        squareOne = findViewById(R.id.btnOne);
        squareTwo = findViewById(R.id.btnTwo);
        squareThree = findViewById(R.id.btnThree);
        squareFour = findViewById(R.id.btnFour);

        star1 = findViewById(R.id.starOne);
        star2 = findViewById(R.id.starTwo);
        star3 = findViewById(R.id.starThree);
        star4 = findViewById(R.id.starFour);

        Intent intent;
        intent = getIntent();
        execution.scores = intent.getIntArrayExtra("scores");

        showStar();

        tts = new TTS(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.onInit(status, question.getText().toString(), "default");
                question.setText(execution.quiz.get(1));
                quiz.clear();
                quiz.add("별이 각자 다른 위치로 이동합니다.");
                quiz.add("어떤 식으로 이동하는지 잘 생각해 보십시오.");
                quiz.add("이 다음에는 네 칸중 별이 어디에 위치하게 될까요?");
                tts.UtteranceProgress(quiz, "continue", question, nextBtn);
            }
        });
        type.setText("집행기능");
        pro_bar.setProgress(40);

        question.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tts.speakOut(question.getText().toString());
            }
        });

        donKnow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tts.Stop();
                tts.isStopUtt = true;

                execution.Tscore = 0;

                execution.scores[5] = execution.Tscore;

                pro_bar.setProgress(50);

                Intent intent = new Intent(getApplicationContext(), S_memoryOutput.class);
                intent.putExtra("scores", execution.scores);
                startActivity(intent);
                overridePendingTransition(0, 0);

                finish();
            }
        });

        beforeBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(getApplicationContext(), "해당 항목의 첫 문제 입니다.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tts.Stop();
                if(check.equals("3")) {
                    execution.Tscore = 1;
                }else {
                    execution.Tscore = 0;
                }
                execution.scores[5] = execution.Tscore;
                Intent intent = new Intent(getApplicationContext(), S_memoryOutput.class);
                intent.putExtra("scores", execution.scores);
                startActivity(intent);
                overridePendingTransition(0, 0);

                finish();
            }
        });
    }

    // 정답 영역 클릭 시, 별 모양 표시
    public void showStar() {
        squareOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = "1";
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.INVISIBLE);
                star3.setVisibility(View.INVISIBLE);
                star4.setVisibility(View.INVISIBLE);
            }
        });
        squareTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = "2";
                star2.setVisibility(View.VISIBLE);
                star1.setVisibility(View.INVISIBLE);
                star3.setVisibility(View.INVISIBLE);
                star4.setVisibility(View.INVISIBLE);
            }
        });
        squareThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = "3";
                star3.setVisibility(View.VISIBLE);
                star1.setVisibility(View.INVISIBLE);
                star2.setVisibility(View.INVISIBLE);
                star4.setVisibility(View.INVISIBLE);
            }
        });
        squareFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = "4";
                star4.setVisibility(View.VISIBLE);
                star1.setVisibility(View.INVISIBLE);
                star2.setVisibility(View.INVISIBLE);
                star3.setVisibility(View.INVISIBLE);
            }
        });
    }

    public boolean onTouchEvent(MotionEvent event) {
        return QP.onTouchEvent(event, beforeBtn, nextBtn);
    }

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if (0 <= gapTime && 2000 >= gapTime) {
            tts.Destroy();

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
        if(tts != null){
            tts.Stop();
        }
        quiz.clear();
    }

    @Override
    protected void onStart(){
        super.onStart();
        tts.isStopUtt = false;
        if(!question.getText().toString().equals(execution.quiz.get(1))){
            question.setText(execution.quiz.get(1));
            tts.speakOut(execution.quiz.get(1), "default");
            quiz.clear();
            quiz.add("별이 각자 다른 위치로 이동합니다.");
            quiz.add("어떤 식으로 이동하는지 잘 생각해 보십시오.");
            quiz.add("이 다음에는 네 칸중 별이 어디에 위치하게 될까요?");
            tts.UtteranceProgress(quiz, "continue", question, nextBtn);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tts != null) {
            tts.Destroy();
        }
    }
}