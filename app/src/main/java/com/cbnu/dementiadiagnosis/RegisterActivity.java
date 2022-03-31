package com.cbnu.dementiadiagnosis;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import database.DBHelper;
import user.RadioButtonTable;
import user.SharedPreference;

import java.util.Calendar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DBHelper(this);
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
                String name = userName.getText().toString().trim();
                String birth = userBirth.getText().toString().trim();
                boolean checked = checkAllValue();

                // 모든 정보를 다 입력했을 경우
                if(checked) {
                    String sex = sexRb.getText().toString();
                    String edu = userEdu.getEduValue();
                    boolean check = db.checkUser(name, birth);
                    boolean autoCheck = checkBox.isChecked();

                    // 이미 등록한 사용자
                    if(check) {
                        if(autoCheck) {// 자동 사용자 등록 체크한 경우
                            SharedPreference.setUserInf(RegisterActivity.this, name, birth, sex, edu);
                        }
                        Toast.makeText(RegisterActivity.this, "로그인", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                        finish();
                    }
                    // 새로운 사용자
                    else {
                        long val = db.registerUser(name, birth, sex, edu);

                        if(val > 0) {
                            if(autoCheck) {// 자동 사용자 등록 체크한 경우
                                SharedPreference.setUserInf(RegisterActivity.this, name, birth, sex, edu);
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
                String date = year + "/" + month + "/" + day;
                userBirth.setText(date);
            }
        };
    }

}