package eu.tutorials.nologin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText nameRegister, birthRegister;
    private Button btnRegister;
    private DBHelper db;
    private RadioGroup sexRegister, eduRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this, 1);
        nameRegister = findViewById(R.id.name);
        birthRegister = findViewById(R.id.birth);
        btnRegister = findViewById(R.id.btnStart);
        sexRegister = findViewById(R.id.radioGroupSex);
        eduRegister = findViewById(R.id.radioGroupEdu);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton sexRb, eduRb;
                sexRb = findViewById(sexRegister.getCheckedRadioButtonId());
                eduRb = findViewById(eduRegister.getCheckedRadioButtonId());
                String name = nameRegister.getText().toString().trim();
                String birth = birthRegister.getText().toString().trim();
                String sex = sexRb.getText().toString().trim();
                String edu = eduRb.getText().toString().trim();

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
        });
    }
}