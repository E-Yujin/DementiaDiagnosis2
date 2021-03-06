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

        // ???????????? datePicker
        birthFun();
        // ????????? ?????????
        applyHideKeyboard();
        // ???????????? ??????
        btnInsertFun();

    }

    // ????????? ?????????
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // ????????? ????????? ??????
    public void applyHideKeyboard() {
        // ?????? EditView ????????? ????????? ????????? keyboard ?????????
        userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    hideKeyboard(v);
            }
        });
        // ???????????? EditView ????????? ????????? ????????? keyboard ?????????
        userBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    hideKeyboard(v);
            }
        });
    }

    // ????????? ????????? ??????
    public boolean checkAllValue() {
        String name = userName.getText().toString().trim();
        String birth = userBirth.getText().toString().trim();

        if(name.isEmpty()) {
            userName.setError("????????? ??????????????????");
            return false;
        }
        if(birth.isEmpty()) {
            userBirth.setError("??????????????? ??????????????????");
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

    // ???????????? ?????? ??????
    public void btnInsertFun() {
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton sexRb = findViewById(userSex.getCheckedRadioButtonId());
                String name = userName.getText().toString().trim();
                String birth = userBirth.getText().toString().trim();
                boolean checked = checkAllValue();

                // ?????? ????????? ??? ???????????? ??????
                if(checked) {
                    String sex = sexRb.getText().toString();
                    String edu = userEdu.getEduValue();
                    boolean check = db.checkUser(name, birth);
                    boolean autoCheck = checkBox.isChecked();

                    // ?????? ????????? ?????????
                    if(check) {
                        if(autoCheck) {// ?????? ????????? ?????? ????????? ??????
                            SharedPreference.setUserInf(RegisterActivity.this, name, birth, sex, edu);
                        }
                        Toast.makeText(RegisterActivity.this, "?????????", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                        finish();
                    }
                    // ????????? ?????????
                    else {
                        long val = db.registerUser(name, birth, sex, edu);

                        if(val > 0) {
                            if(autoCheck) {// ?????? ????????? ?????? ????????? ??????
                                SharedPreference.setUserInf(RegisterActivity.this, name, birth, sex, edu);
                            }
                            Toast.makeText(RegisterActivity.this, "???????????? ??????", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "???????????? ??????", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    // ???????????? datePicker
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

}