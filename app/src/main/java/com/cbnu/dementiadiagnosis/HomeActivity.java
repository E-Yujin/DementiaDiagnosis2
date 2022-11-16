package com.cbnu.dementiadiagnosis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import fragment.FragmentChart;
import fragment.FragmentHome;
import fragment.FragmentInf;
import fragment.FragmentSetting;
import user.SharedPreference;

public class HomeActivity extends AppCompatActivity {

    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private final FragmentHome fragmentHome = new FragmentHome();
    private final FragmentInf fragmentInf = new FragmentInf();
    private final FragmentChart fragmentChart = new FragmentChart();
    private final FragmentSetting fragmentSetting = new FragmentSetting();
    final int PERMISSION = 1;
    private PermissionSupport permission;
    TranslateAnimation animUpDown;
    ScaleAnimation animScale;
    TextView bubble;
    FloatingActionButton fb;
    boolean isHome = true;
    BottomNavigationItemView home, recog, info, setting;
    int start, end, speed;

    private long backBtnTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        start = SharedPreference.getSTT_start(this);
        end = SharedPreference.getSTT_end(this);
        speed = SharedPreference.getSTT_speed(this);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentHome).commitAllowingStateLoss();
        animUpDown = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,0f,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, -0.005f,
                Animation.RELATIVE_TO_PARENT, 0.005f);
        animUpDown.setDuration(400);
        animUpDown.setRepeatCount(Animation.INFINITE);
        animUpDown.setRepeatMode(Animation.REVERSE);

        animScale = new ScaleAnimation(0,1,1,1,600,0);
        animScale.setDuration(400);

        bubble = findViewById(R.id.bubble);
        bubble.startAnimation(animScale);

        home = findViewById(R.id.homeItem);
        recog = findViewById(R.id.resultItem);
        info = findViewById(R.id.informationItem);
        setting = findViewById(R.id.setItem);

        fb = findViewById(R.id.voiceBot);
        fb.startAnimation(animUpDown);
        fb.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), VoiceBot.class);
            startActivity(intent);
        });



        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnItemSelectedListener(new ItemSelectedListener());

        permissionCheck();
    }

    public void EnableTab(boolean set){
        home.setClickable(set);
        recog.setClickable(set);
        info.setClickable(set);
        setting.setClickable(set);
    }

    private void permissionCheck() {
        // PermissionSupport.java 클래스 객체 생성
        permission = new PermissionSupport(this, this);

        // 권한 체크 후 리턴이 false로 들어오면
        if (!permission.checkPermission()){
            //권한 요청
            permission.requestPermission();
        }
    }
    // Request Permission에 대한 결과 값 받아와
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //여기서도 리턴이 false로 들어온다면 (사용자가 권한 허용 거부)
        if (!permission.permissionResult(requestCode, permissions, grantResults)) {
            // 다시 permission 요청
            permission.requestPermission();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    class ItemSelectedListener implements NavigationBarView.OnItemSelectedListener {

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(item.getItemId())
            {
                case R.id.homeItem:
                    ArrayList<Integer> parameters = new ArrayList<>();
                    parameters.add(speed);
                    parameters.add(start);
                    parameters.add(end);
                    Bundle bundle = new Bundle();
                    bundle.putIntegerArrayList("stt 데이터 전송", parameters);
                    fragmentHome.setArguments(bundle);
                    fb.show();
                    transaction.replace(R.id.frameLayout, fragmentHome).commitAllowingStateLoss();
                    bubble.setVisibility(View.VISIBLE);
                    bubble.startAnimation(animScale);
                    fb.setEnabled(true);
                    isHome = true;
                    break;
                case R.id.informationItem:
                    transaction.replace(R.id.frameLayout, fragmentInf).commitAllowingStateLoss();
                    fb.hide();
                    fb.setEnabled(false);
                    bubble.setVisibility(View.INVISIBLE);
                    isHome = false;
                    break;
                case R.id.resultItem:
                    transaction.replace(R.id.frameLayout, fragmentChart).commitAllowingStateLoss();
                    fb.hide();
                    fb.setEnabled(false);
                    bubble.setVisibility(View.INVISIBLE);
                    isHome = false;
                    break;
                case R.id.setItem:
                    ArrayList<Integer> parameters1 = new ArrayList<>();
                    parameters1.add(speed);
                    parameters1.add(start);
                    parameters1.add(end);
                    Bundle bundle1 = new Bundle();
                    bundle1.putIntegerArrayList("stt 데이터 전송", parameters1);
                    fragmentSetting.setArguments(bundle1);
                    fb.show();
                    fb.setEnabled(true);
                    transaction.replace(R.id.frameLayout, fragmentSetting).commitAllowingStateLoss();
                    bubble.setVisibility(View.INVISIBLE);
                    isHome = false;
                    break;
            }
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if (0 <= gapTime && 2000 >= gapTime) {
            finish();
        } else {
            backBtnTime = curTime;
            Toast.makeText(this, "한 번 더 누르면 종료됩니다.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(isHome) bubble.startAnimation(animScale);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}