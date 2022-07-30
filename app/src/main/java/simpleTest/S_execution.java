package simpleTest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.TTS;

import java.util.ArrayList;
import java.util.List;

import QuizPage.QuizPage;

public class S_execution extends AppCompatActivity {

    ImageButton squareOne, squareTwo, squareThree, squareFour;
    ImageButton beforeBtn, nextBtn;
    ImageView star1, star2, star3, star4;
    TextView question, type;
    TTS tts;
    QuizPage QP;
    ProgressBar pro_bar;
    String check="";

    private long backBtnTime = 0;
    List<String> tem = new ArrayList<>();
    int[] time = {1000, 1000, 1000, 1000};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_execution);

        question = findViewById(R.id.question);
        type = findViewById(R.id.type);
        beforeBtn = findViewById(R.id.before);
        nextBtn = findViewById(R.id.next);

        squareOne = findViewById(R.id.btnOne);
        squareTwo = findViewById(R.id.btnTwo);
        squareThree = findViewById(R.id.btnThree);
        squareFour = findViewById(R.id.btnFour);

        star1 = findViewById(R.id.starOne);
        star2 = findViewById(R.id.starTwo);
        star3 = findViewById(R.id.starThree);
        star4 = findViewById(R.id.starFour);

        type.setText("집행기능");
        pro_bar.setProgress(40);

        showStar();
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
    public String twoResult() {
        return check;
    }

    @Override
    protected void onStop(){
        tts.isStopUtt = true;
        super.onStop();
        tts.Stop();
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
    protected void onDestroy() {
        super.onDestroy();
        QP.Destroy();
    }
}