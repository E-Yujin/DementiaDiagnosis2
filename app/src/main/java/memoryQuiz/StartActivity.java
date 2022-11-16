package memoryQuiz;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cbnu.dementiadiagnosis.Helper;
import com.cbnu.dementiadiagnosis.HomeActivity;
import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.TTS;

import java.util.ArrayList;
import java.util.List;

import QuizPage.SpaceTimePage;

public class StartActivity extends AppCompatActivity {

    TextView speakMsg;
    Button startBtn;
    ImageView helper_img;
    Helper helper;
    TTS tts;
    private long backBtnTime = 0;
    List<String> tem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        helper_img = (ImageView) findViewById(R.id.img);
        speakMsg = (TextView) findViewById(R.id.textView3);
        startBtn = (Button) findViewById(R.id.button);

        tts = new TTS(this, status -> {
            tem.clear();
            tts.onInit(status, speakMsg.getText().toString(), "default");
            tem.add("아래 예시를 보면\n각 문제마다 다른 초성이 주어집니다.");
            tem.add("초성을 보고 생각나는\n단어 3개를 말씀해주시면 됩니다.");
            tem.add("답변 시 아래 마이크를\n누르신 후 말씀해주시면 됩니다.");
            tem.add("시작할 준비가 되셨다면\n아래 시작하기 버튼을 눌러주세요.");
            tts.UtteranceProgress(tem,"continue", speakMsg);
        });

        helper = new Helper(tts, helper_img, this);
        helper.setStart();

        speakMsg.setOnClickListener(v -> {
            speakMsg.setText("기억력 향상 퀴즈로는\n초성퀴즈가 진행됩니다.");
            tts.speakOut(speakMsg.getText().toString(),"default");
            tem.clear();
            tem.add("아래 예시를 보면\n각 문제마다 다른 초성이 주어집니다.");
            tem.add("초성을 보고 생각나는\n단어 3개를 말씀해주시면 됩니다.");
            tem.add("답변 시 아래 마이크를\n누르신 후 말씀해주시면 됩니다.");
            tem.add("시작할 준비가 되셨다면\n아래 시작하기 버튼을 눌러주세요.");
            tts.UtteranceProgress(tem, "continue", speakMsg);
        });

        startBtn.setOnClickListener(v -> {
            tts.Destroy();
            Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent1);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if (0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed();
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            tts.Destroy();

            finish();
        } else {
            backBtnTime = curTime;
            Toast.makeText(this, "한번 더 누르시면 퀴즈가 종료됩니다.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        tts.isStopUtt = false;

        speakMsg.setText("기억력 향상 퀴즈로는\n초성퀴즈가 진행됩니다.");
        tts.speakOut(speakMsg.getText().toString(),"default");
        tem.clear();
        tem.add("아래 예시를 보면\n각 문제마다 다른 초성이 주어집니다.");
        tem.add("초성을 보고 생각나는\n단어 3개를 말씀해주시면 됩니다.");
        tem.add("답변 시 아래 마이크를\n누르신 후 말씀해주시면 됩니다.");
        tem.add("시작할 준비가 되셨다면\n아래 시작하기 버튼을 눌러주세요.");
        tts.UtteranceProgress(tem, "continue", speakMsg);
    }

    @Override
    protected void onStop(){
        tts.isStopUtt = true;
        super.onStop();
        tts.Stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tts.Stop();
    }
}
