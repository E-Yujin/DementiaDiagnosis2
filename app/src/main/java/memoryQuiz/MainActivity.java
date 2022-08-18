package memoryQuiz;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cbnu.dementiadiagnosis.Helper;
import com.cbnu.dementiadiagnosis.MainSTT;
import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.TTS;

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import QuizPage.QuizPage;
import questions.MemoryQuiz;

public class MainActivity extends AppCompatActivity {

    String keyword = "";
    TextView randomText;
    TextView question;
    TextView time;
    EditText answer;
    ImageButton sttBtn, submit;
    ImageView helper_img;
    Helper helper;
    QuizPage QP;
    MainSTT stt;
    TTS tts;
    ProgressBar pro_bar;
    MemoryQuiz memoryQuiz;

    private long backBtnTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        randomText = (TextView) findViewById(R.id.random_text);
        question = (TextView) findViewById(R.id.question);
        answer = (EditText) findViewById(R.id.result);
        submit = (ImageButton) findViewById(R.id.submit);
        sttBtn = (ImageButton) findViewById(R.id.sttStart);
        time = (TextView) findViewById(R.id.time);
        helper_img = findViewById(R.id.img);
        pro_bar = (ProgressBar) findViewById(R.id.progressBar);
        memoryQuiz = new MemoryQuiz();

        tts = new TTS(this, status -> {
            tts.onInit(status, question.getText().toString(), "Done", 1000);
            sttBtn.setEnabled(true);
            answer.setEnabled(true);
        });
        stt = new MainSTT(this, answer, question, sttBtn, submit, tts);
        QP = new QuizPage(stt, tts, question, answer, sttBtn, submit, memoryQuiz.quiz);

        helper = new Helper(tts, stt, helper_img, this);
        helper.setTest();

        pro_bar.setProgress(20);

        String random = generateString();
        Log.e("random : ", random);
        randomText.setText(random);

        // stt 시작
        sttBtn.setOnClickListener(v -> {
            tts.isStopUtt = true;
            tts.Stop();
            stt.start_STT();
        });

        submit.setOnClickListener(v -> {
            keyword = answer.getText().toString();
            String result = getInitialSound(keyword);
            Log.e("result : ", result);

            if(result.equals(random)) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("keyword", keyword);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "틀렸습니다!!", Toast.LENGTH_SHORT).show();
            }
        });

        /*// 타이머 설정
        long duration = TimeUnit.SECONDS.toMillis(30);

        new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String sDuration = String.format(Locale.ENGLISH, "%02d : %02d"
                        ,TimeUnit.MILLISECONDS.toMinutes(1)
                        ,TimeUnit.MILLISECONDS.toMinutes(1) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(1)));
                time.setText(sDuration);
            }

            @Override
            public void onFinish() {
                stt.Stop();
                tts.Stop();
                sttBtn.setEnabled(true);
                tts.isStopUtt = true;
                String newRandom = "";

                keyword = answer.getText().toString();
                String result = getInitialSound(keyword);
                Log.e("result : ", result);

                if(result.equals(random)) {
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    intent.putExtra("keyword", keyword);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "틀렸습니다!!", Toast.LENGTH_SHORT).show();
                }

                switch (QP.current) {
                    case 0:
                        pro_bar.setProgress(40);
                        QP.Submit();
                        tts.speakOut(question.getText().toString(), "default");
                        newRandom = generateString();
                        randomText.setText(newRandom);
                        break;
                    case 1:
                        pro_bar.setProgress(60);
                        QP.Submit();
                        tts.speakOut(question.getText().toString(), "default");
                        newRandom = generateString();
                        randomText.setText(newRandom);
                        break;
                    case 2:
                        pro_bar.setProgress(80);
                        QP.Submit();
                        tts.speakOut(question.getText().toString(), "default");
                        newRandom = generateString();
                        randomText.setText(newRandom);
                        break;
                    case 3:
                        pro_bar.setProgress(100);
                        QP.Submit();
                        tts.speakOut(question.getText().toString(), "default");
                        newRandom = generateString();
                        randomText.setText(newRandom);
                        break;
                    default:
                        finish();
                }
            }
        }.start();*/
    }

    // 랜덤 초성 생성
    public String generateString() {
        char[] chars = "ㄱㄴㄷㄹㅁㅂㅅㅇㅈㅊㅋㅌㅍㅎ".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 2; i++) {
            char c = chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    // 입력된 문자열에서 자음 추출
    public String getInitialSound(String text) {
        String[] chs = {
                "ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ",
                "ㅂ", "ㅃ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅉ",
                "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"
        };
        StringBuilder stringBuilder = new StringBuilder();

        if(text.length() == 2) {
            for(int i = 0; i < 2; i++) {
                char chName = text.charAt(i);
                if(chName >= 0xAC00) {
                    int uniVal = chName - 0xAC00;
                    int cho = ((uniVal - (uniVal % 28))/28)/21;

                    stringBuilder.append(chs[cho]);
                }
            }
            return stringBuilder.toString();
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if (0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed();
        } else {
            backBtnTime = curTime;
            Toast.makeText(this, "퀴즈를 종료하시겠습니까?",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /*@Override
    protected void onStart(){
        super.onStart();
        String newRandom = "";
        question.setText(memoryQuiz.quiz.get(QP.current));
        tts.isStopUtt = false;
        answer.setText("");
        tts.speakOut(question.getText().toString());
        QP.Start();

        switch (QP.current) {
            case 0:
                pro_bar.setProgress(40);
                QP.Submit();
                tts.speakOut(question.getText().toString(), "default");
                newRandom = generateString();
                randomText.setText(newRandom);
                break;
            case 1:
                pro_bar.setProgress(60);
                QP.Submit();
                tts.speakOut(question.getText().toString(), "default");
                newRandom = generateString();
                randomText.setText(newRandom);
                break;
            case 2:
                pro_bar.setProgress(80);
                QP.Submit();
                tts.speakOut(question.getText().toString(), "default");
                newRandom = generateString();
                randomText.setText(newRandom);
                break;
            case 3:
                pro_bar.setProgress(100);
                QP.Submit();
                tts.speakOut(question.getText().toString(), "default");
                newRandom = generateString();
                randomText.setText(newRandom);
                break;
        }
    }*/

    @Override
    protected void onStop(){
        tts.isStopUtt = true;
        super.onStop();
        tts.Stop();
        stt.Stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        QP.Destroy();
    }

}