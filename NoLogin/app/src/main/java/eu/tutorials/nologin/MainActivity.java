package eu.tutorials.nologin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DBHelper db;
    private EditText nameRegister, birthRegister;
    private Button btnRegister;
    private RadioGroup sexRegister;
    private RadioButton radioOne, radioTwo, radioThree, radioFour, radioFive, radioSix;
    private TextView sexError, eduError;
    private RadioButtonTable eduTable;
    private long time = 0;
    boolean checked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this, 1);
        nameRegister = findViewById(R.id.name);
        birthRegister = findViewById(R.id.birth);
        btnRegister = findViewById(R.id.btnStart);
        sexRegister = findViewById(R.id.radioGroupSex);
        radioOne = findViewById(R.id.radioOne);
        radioTwo = findViewById(R.id.radioTwo);
        radioThree = findViewById(R.id.radioThree);
        radioFour = findViewById(R.id.radioFour);
        radioFive = findViewById(R.id.radioFive);
        radioSix = findViewById(R.id.radioSix);
        sexError = findViewById(R.id.sexError);
        eduError = findViewById(R.id.eduError);
        eduTable = findViewById(R.id.radioGroupEdu);

        // 이름 EditView 이외의 부분을 누르면 keyboard 숨기기
        nameRegister.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    hideKeyboard(v);
            }
        });
        // 생년월일 EditView 이외의 부분을 누르면 keyboard 숨기기
        birthRegister.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    hideKeyboard(v);
            }
        });

        // 사용자 정보 등록버튼 눌렀을 때
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton sexRb = findViewById(sexRegister.getCheckedRadioButtonId());
                String name = nameRegister.getText().toString().trim();
                String birth = birthRegister.getText().toString().trim();

                checked = checkAllValue();

                if(checked) {
                    String sex = sexRb.getText().toString();
                    String edu = eduTable.getEduValue();

                    long val = db.registerUser(name, birth, sex, edu);

                    if(val > 0) {
                        Toast.makeText(MainActivity.this, "회원등록 완료", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "회원등록 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    // 키보드 숨기기
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    // 사용자 입력값 체크
    public boolean checkAllValue() {
        String name = nameRegister.getText().toString().trim();
        String birth = birthRegister.getText().toString().trim();

        if(name.isEmpty()) {
            nameRegister.setError("이름을 입력해주세요");
            return false;
        }
        if(birth.isEmpty()) {
            birthRegister.setError("생년월일을 입력해주세요");
            return false;
        }
        if(sexRegister.getCheckedRadioButtonId() == -1) {
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
}
