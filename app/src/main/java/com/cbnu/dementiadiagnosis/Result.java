package com.cbnu.dementiadiagnosis;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import database.DBHelper;
import database.TableContract;
import user.EducationAge;
import user.SharedPreference;
import user.User;

public class Result extends AppCompatActivity {
    int total;
    TextView score;
    TextView name, age, edu;
    TextView msg;
    DBHelper dbHelper;
    int year, month, day;
    String birth, val_name, val_edu;

    List<EducationAge> eduAgeList = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent;

        intent = getIntent();
        total = intent.getIntExtra("result", 0);
        score = findViewById(R.id.total);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        edu = findViewById(R.id.edu);
        msg = findViewById(R.id.resMessage);

        score.setText("총점 : " + Integer.toString(total));
        val_name = SharedPreference.getUserName(getApplication());
        name.setText(val_name);
        val_edu = SharedPreference.getUserEdu(getApplication());
        edu.setText(val_edu);

        birth = SharedPreference.getUserBirth(getApplication());
        Log.d("birth", birth);
        year = Integer.parseInt(birth.substring(0, 4));
        Log.d("year", "" + year);
        if(birth.charAt(5) == '0') {
            month = Integer.parseInt(birth.substring(6, 7));
        } else {
            month = Integer.parseInt(birth.substring(5, 7));
        }
        Log.d("month", ""+month);

        if(birth.charAt(8) == '0') {
            day = Integer.parseInt(birth.substring(9, 10));
        } else {
            day = Integer.parseInt(birth.substring(8, 10));
        }
        Log.d("day", ""+day);
        dbHelper = new DBHelper(getApplication());
        int res = dbHelper.getAge(year, month, day);
        age.setText(Integer.toString(res));

        eduAgeList = dbHelper.getAllEduTable();
        int age_num = 0;
        int edu_num = 0;

        if(res < 60) {
            age_num = 0;
        } else if(res < 70) {
            age_num = 1;
        } else if(res < 80) {
            age_num = 2;
        } else {
            age_num = 3;
        }

        switch (val_edu) {
            case "문맹":
                edu_num = eduAgeList.get(age_num).getIlliterate();
                break;
            case "무학":
                edu_num = eduAgeList.get(age_num).getUneducated();
                break;
            case "초등졸업":
                edu_num = eduAgeList.get(age_num).getElementarySchool();
                break;
            case "중등졸업":
                edu_num = eduAgeList.get(age_num).getSecondarySchool();
                break;
            case "고등졸업":
                edu_num = eduAgeList.get(age_num).getHighSchool();
                break;
            case "대학졸업이상":
                edu_num = eduAgeList.get(age_num).getUniversity();
                break;
            default:
                break;
        }

        if(total < edu_num) {
            msg.setText(val_name + "님의 진단 결과 점수는 " + total + "점 입니다."
                    + "가입 시 입력한 나이와 학력을 바탕으로 출력한 결과 " + edu_num +
                    "점수 미만입니다.\n치매가 의심되는 상태이니 정기 검진을 받아보시는 것을 권해드립니다.");
        } else {
            msg.setText(val_name + "님의 진단 결과 점수는 " + total + "점 입니다."
                    + "가입 시 입력한 나이와 학력을 바탕으로 출력한 결과" + edu_num +
                    "으로 정상 수치가 나왔습니다.");
        }

    }
}