package com.cbnu.dementiadiagnosis;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import database.DBHelper;
import user.EducationAge;
import user.RadioButtonTable;
import user.SharedPreference;
import user.User;

public class RegisterActivity extends AppCompatActivity {

    private DBHelper db;
    private EditText userName, userBirth;
    private Button btnInsert;
    private RadioGroup userSex;
    private RadioButton radioOne, radioTwo, radioThree, radioFour, radioFive, radioSix;
    private RadioButtonTable userEdu;
    private TextView sexError, eduError;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private CheckBox checkBox;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DBHelper(this);
        user = new User();
        userName = findViewById(R.id.name);
        userBirth = findViewById(R.id.birth);
        userSex = findViewById(R.id.radioGroupSex);
        userEdu = findViewById(R.id.radioGroupEdu);
        btnInsert = findViewById(R.id.btnInsert);
        radioOne = findViewById(R.id.radioOne);
        radioTwo = findViewById(R.id.radioTwo);
        radioThree = findViewById(R.id.radioThree);
        radioFour = findViewById(R.id.radioFour);
        radioFive = findViewById(R.id.radioFive);
        radioSix = findViewById(R.id.radioSix);
        sexError = findViewById(R.id.sexError);
        eduError = findViewById(R.id.eduError);
        checkBox = findViewById(R.id.checkBox);

        // 생년월일 datePicker
        birthFun();
        // 키보드 숨기기
        applyHideKeyboard();
        // 등록버튼 실행
        btnInsertFun();
    }

    // 키보드 숨기기
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // 키보드 숨기기 적용
    public void applyHideKeyboard() {
        // 이름 EditView 이외의 부분을 누르면 keyboard 숨기기
        userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    hideKeyboard(v);
            }
        });
        // 생년월일 EditView 이외의 부분을 누르면 keyboard 숨기기
        userBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    hideKeyboard(v);
            }
        });
    }

    // 사용자 입력값 체크
    public boolean checkAllValue() {
        String name = userName.getText().toString().trim();
        String birth = userBirth.getText().toString().trim();

        if(name.isEmpty()) {
            userName.setError("이름을 입력해주세요");
            return false;
        }
        if(birth.isEmpty()) {
            userBirth.setError("생년월일을 입력해주세요");
            return false;
        }
        if(userSex.getCheckedRadioButtonId() == -1) {
            sexError.setVisibility(View.VISIBLE);
            return false;
        }
        else {
            sexError.setVisibility(View.INVISIBLE);
        }
        if(!radioOne.isChecked() && !radioTwo.isChecked() && !radioThree.isChecked() &&
                !radioFour.isChecked() && !radioFive.isChecked() && !radioSix.isChecked()) {
            eduError.setVisibility(View.VISIBLE);
            return false;
        }
        else {
            eduError.setVisibility(View.INVISIBLE);
        }
        return true;
    }

    // 등록버튼 처리 함수
    public void btnInsertFun() {
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton sexRb = findViewById(userSex.getCheckedRadioButtonId());
                boolean checked = checkAllValue();

                // 모든 정보를 다 입력했을 경우
                if(checked) {
                    user.setName(userName.getText().toString().trim());
                    user.setBirth(userBirth.getText().toString().trim());
                    user.setAge(getAge(user.getBirth()));
                    user.setSex(sexRb.getText().toString());
                    user.setEdu(userEdu.getEduValue());
                    user.setScore(getRefScore(user.getAge(), user.getEdu()));

                    boolean check = db.checkUser(user.getName(), user.getBirth(), user.getSex(), user.getEdu());
                    boolean autoCheck = checkBox.isChecked();

                    // 이미 등록한 사용자
                    if(check) {
                        if(autoCheck) {// 자동 사용자 등록 체크한 경우
                            user.setSerial_code(db.saveSerialCode(user.getName(), user.getBirth(), user.getSex(), user.getEdu()));
                            SharedPreference.setUserInf(RegisterActivity.this, user.getSerial_code(), user.getName(), user.getBirth(),
                                    user.getAge(), user.getSex(), user.getEdu(), user.getScore());
                        }else {
                            user.setSerial_code(db.saveSerialCode(user.getName(), user.getBirth(), user.getSex(), user.getEdu()));
                            SharedPreference.setSerialCodeInf(RegisterActivity.this, user.getSerial_code());
                        }
                        Toast.makeText(RegisterActivity.this, "로그인", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                        finish();
                    }
                    // 새로운 사용자
                    else {
                        user.setSerial_code(getRandomString(5));
                        long val = db.registerUser(user.getSerial_code(), user.getName(), user.getBirth(),
                                user.getAge(), user.getSex(), user.getEdu(), user.getScore());

                        if(val > 0) {
                            if(autoCheck) {// 자동 사용자 등록 체크한 경우
                                SharedPreference.setUserInf(RegisterActivity.this, user.getSerial_code(), user.getName(), user.getBirth(),
                                        user.getAge(), user.getSex(), user.getEdu(), user.getScore());
                            }else {
                                SharedPreference.setSerialCodeInf(RegisterActivity.this, user.getSerial_code());
                            }
                            Toast.makeText(RegisterActivity.this, "회원등록 완료", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "회원등록 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    // 생년월일 datePicker
    public void birthFun() {
        userBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = null;
                String s_month = Integer.toString(month), s_day = Integer.toString(day);

                if(month < 10) {
                    s_month = "0" + month;
                }
                if(day < 10) {
                    s_day = "0" + day;
                }
                date = year + "/" + s_month + "/" + s_day;
                userBirth.setText(date);
            }
        };
    }

    // 랜덤 일련번호 부여
    public static String getRandomString(int num) {
        final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder result = new StringBuilder();
        while (num > 0) {
            Random random = new Random();
            result.append(characters.charAt(random.nextInt(characters.length())));
            num--;
        }
        return result.toString();
    }

    // 사용자 만나이 계산
    public int getAge(String birth) {
        int year, month, day;
        int age;

        Calendar current = Calendar.getInstance();
        int currentYear = current.get(Calendar.YEAR);
        int currentMonth = current.get(Calendar.MONTH) + 1;
        int currentDay = current.get(Calendar.DAY_OF_MONTH);

        year = Integer.parseInt(birth.substring(0, 4));

        if(birth.charAt(5) == '0') {
            month = Integer.parseInt(birth.substring(6, 7));
        } else {
            month = Integer.parseInt(birth.substring(5, 7));
        }

        if(birth.charAt(8) == '0') {
            day = Integer.parseInt(birth.substring(9, 10));
        } else {
            day = Integer.parseInt(birth.substring(8, 10));
        }

        age = currentYear - year;

        // 생일이 지나지 않은 경우 -1
        if(month * 100 + day > currentMonth * 100 + currentDay) {
            age--;
        }
        return age;
    }

    // 사용자 치매 기준 점수 구하기
    public int getRefScore(int age, String edu) {
        int age_index = 0;
        int ref_score = 0;

        if(age < 60) {
            age_index = 0;
        } else if(age < 70) {
            age_index = 1;
        } else if(age < 80) {
            age_index = 2;
        } else {
            age_index = 3;
        }

        List<EducationAge> eduAgeList = db.getAllEduTable();

        switch (user.getEdu()) {
            case "문맹":
                ref_score = eduAgeList.get(age_index).getIlliterate();
                break;
            case "무학":
                ref_score = eduAgeList.get(age_index).getUneducated();
                break;
            case "초등졸업":
                ref_score = eduAgeList.get(age_index).getElementarySchool();
                break;
            case "중등졸업":
                ref_score = eduAgeList.get(age_index).getSecondarySchool();
                break;
            case "고등졸업":
                ref_score = eduAgeList.get(age_index).getHighSchool();
                break;
            case "대학졸업이상":
                ref_score = eduAgeList.get(age_index).getUniversity();
                break;
            default:
                break;
        }
        return ref_score;
    }

}