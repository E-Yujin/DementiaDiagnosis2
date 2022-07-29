package com.cbnu.dementiadiagnosis;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import database.DBHelper;
import database.TableContract;
import user.EducationAge;
import user.SharedPreference;
import user.User;

public class Result extends AppCompatActivity {
    ProgressBar progressBar1, progressBar2, progressBar3, progressBar4, progressBar5, progressBar6;
    TextView ori_score, mem_score, att_score, spa_score, exe_score, lan_score;
    Button endBtn;
    ImageView helpImage;
    CardView helpMsg;
    DBHelper db;
    TextView scoreText, resText;
    private long backBtnTime = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        db = new DBHelper(this);
        ori_score = (TextView) findViewById(R.id.ori_score);
        mem_score = (TextView) findViewById(R.id.mem_score);
        att_score = (TextView) findViewById(R.id.att_score);
        spa_score = (TextView) findViewById(R.id.spa_score);
        exe_score = (TextView) findViewById(R.id.exe_score);
        lan_score = (TextView) findViewById(R.id.lan_score);
        progressBar1 = findViewById(R.id.barChart);
        progressBar2 = findViewById(R.id.barChart2);
        progressBar3 = findViewById(R.id.barChart3);
        progressBar4 = findViewById(R.id.barChart4);
        progressBar5 = findViewById(R.id.barChart5);
        progressBar6 = findViewById(R.id.barChart6);
        endBtn = findViewById(R.id.endBtn);
        helpImage = findViewById(R.id.help);
        helpMsg = findViewById(R.id.helpMessage);
        scoreText = findViewById(R.id.score);
        resText = findViewById(R.id.result_text);

        // 결과 출력
        showResultChart();

        // 도움말 클릭 시
        helpImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helpMsg.getVisibility() == View.INVISIBLE) {
                    helpMsg.setVisibility(View.VISIBLE);
                    helpImage.setColorFilter(Color.parseColor("#F8DA8D"), PorterDuff.Mode.SRC_IN);
                } else {
                    helpMsg.setVisibility(View.INVISIBLE);
                    helpImage.setColorFilter(Color.parseColor("#7D7D7D"), PorterDuff.Mode.SRC_IN);
                }
            }
        });
    }

    // 결과 차트 출력
    @SuppressLint("SetTextI18n")
    public void showResultChart() {
        Intent resIntent = getIntent();
        int[] part_score = resIntent.getIntArrayExtra("part_score");

        int score_orientation = part_score[1];
        Log.d("ori", part_score[1] + "");
        int score_memory =  part_score[2];
        Log.d("mem", part_score[2] + "");
        int score_attention =  part_score[3];
        Log.d("att", part_score[3] + "");
        int score_spacetime =  part_score[4];
        Log.d("spa", part_score[4] + "");
        int score_execution =  part_score[5];
        Log.d("exe", part_score[5] + "");
        int score_language =  part_score[6];
        Log.d("lan", part_score[6] + "");
        int score_total = score_orientation + score_memory + score_attention + score_spacetime + score_execution + score_language;

        ori_score.setText(score_orientation + "/5");
        mem_score.setText(score_memory + "/10");
        att_score.setText(score_attention + "/3");
        spa_score.setText(score_spacetime + "/2");
        exe_score.setText(score_execution + " /6");
        lan_score.setText(score_language + "/4");
        scoreText.setText(Integer.toString(score_total));
        setProgressWithAnimation(progressBar1, score_orientation * 20);
        setProgressWithAnimation(progressBar2, score_memory * 10);
        setProgressWithAnimation(progressBar3, score_attention * 33 + 1);
        setProgressWithAnimation(progressBar4, score_spacetime * 50);
        setProgressWithAnimation(progressBar5, score_execution * 16 + 4);
        setProgressWithAnimation(progressBar6, score_language * 25);

        String serial_code = SharedPreference.getSerialCode(getApplication());
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String getDate = sdf.format(date);

        endBtn.setOnClickListener(v -> {
            if(db.checkScore(serial_code, getDate)) {
                db.changeScore(serial_code, getDate, part_score);
            }else {
                db.scoreAdd(serial_code, getDate, score_orientation, score_memory, score_attention,
                        score_spacetime, score_execution, score_language, score_total);
            }
            Intent intent = new Intent(Result.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        if(SharedPreference.getUserScore(getApplication()) < score_total) {
            resText.setText(SharedPreference.getUserName(getApplication()) + "님은 진단결과 상 정상 범주에 속하는 수준입니다." +
                    "앞으로도 치매에 관한 꾸준한 관리로 건강한 생활을 유지하시길 바랍니다.");
        }else {
            resText.setText(SharedPreference.getUserName(getApplication()) + "님은 진단결과 상 구체적인 진단이 필요한 수준입니다." +
                    "해당 진단기는 비교적 간단한 자가진단이기에 해당 결과로 치매를 확정하지 않으니" +
                    "정확한 진단을 위해 가까운 병원이나 치매센터에 방문하셔서 보다 정밀한 검사를 받아보시길 바랍니다.");
        }
    }

    // 차트 바 애니메이션
    public void setProgressWithAnimation(ProgressBar progressBar, int progress) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, progress);
        objectAnimator.setDuration(1300);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
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