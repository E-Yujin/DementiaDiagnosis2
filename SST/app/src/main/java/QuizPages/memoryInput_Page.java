package QuizPages;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sst.MainSTT;
import com.example.sst.R;
import com.example.sst.TTS;

import java.util.ArrayList;
import java.util.Arrays;

import questions.memoryInput;
import questions.orientation;

public class memoryInput_Page extends AppCompatActivity {
    memoryInput memo_in;
    MainSTT stt;
    TTS tts;
    QuizPage QP;
    TextView question;
    TextView announce;
    EditText answer;
    Button sttBtn;
    Button submit;

    ArrayList<String> first, second;

    private boolean isDone[];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memory_input);

        this.isDone = new boolean[5];
        Arrays.fill(isDone, false);
        question = (TextView) findViewById(R.id.question);
        announce = (TextView) findViewById(R.id.textView);
        answer = (EditText) findViewById(R.id.result);
        answer.setEnabled(false);
        answer.setHint("이 항목에서는 키보드 입력이 불가능합니다.");
        sttBtn = (Button) findViewById(R.id.sttStart);
        submit = (Button) findViewById(R.id.submit);
        memo_in = new memoryInput();
        tts = new TTS(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.onInit(status, question.getText().toString());
            }
        });
        stt = new MainSTT(this, answer, announce, question, sttBtn, tts);
        QP = new QuizPage(stt, tts, question, announce, answer, sttBtn, submit, isDone,
                memo_in.quiz, memo_in.crr_ans, memo_in.score);
        first = new ArrayList<>();
        second = new ArrayList<>();


        QP.Delay(8000, "민수는  자전거를 타고  공원에 가서  11시부터  야구를 했다");
        QP.Delay(14000, announce.getText().toString());

        sttBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stt.start_STT();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(QP.current == 0){
                    for(int i = 0; i<5; i++){
                        if(answer.getText().toString() == memo_in.crr_ans.get(i)){
                            first.add(memo_in.crr_ans.get(i));
                        }
                    }
                    QP.Submit();
                    tts.speakOut(question.getText().toString());
                    QP.Delay(10000, "민수는  자전거를 타고  공원에 가서  11시부터  야구를 했다");
                    QP.Delay(16000, announce.getText().toString());
                }
                else if(QP.current == 1){
                    for(int i = 0; i<5; i++){
                        if(answer.getText().toString() == memo_in.crr_ans.get(i)){
                            second.add(memo_in.crr_ans.get(i));
                        }
                    }
                    QP.Submit();
                    tts.speakOut(question.getText().toString());
                    announce.setText("다음 단계로 이동하시려면\n아래 파란 상자를 눌러주세요!");
                    QP.Delay(6000, announce.getText().toString());
                    sttBtn.setEnabled(false);
                    submit.setText("확인");
                }
                else{
                    Intent resultIntent = new Intent();

                    resultIntent.putExtra("isDone", true);
                    resultIntent.putExtra("First", first);
                    resultIntent.putExtra("Second", second);

                    setResult(2, resultIntent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        QP.BackPressed(this);
    }

    @Override
    protected void onStop(){
        super.onStop();
        QP.Stop();
    }

    @Override
    protected void onStart(){
        super.onStart();
        QP.isStop = false;
        if(announce.getText() != "대답할 준비가 되셨다면\n아래 보라색 상자를 눌러 말씀해주세요!"){
            QP.Start(this);
            tts.speakOut(question.getText().toString());
            QP.Delay(8000, "민수는  자전거를 타고  공원에 가서  11시부터  야구를 했다");
            QP.Delay(14000, announce.getText().toString());
        }
        else {
            tts.speakOut(question.getText().toString());
            QP.Delay(8000, "민수는  자전거를 타고  공원에 가서  11시부터  야구를 했다");
            QP.Delay(14000, announce.getText().toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        QP.Destroy();
    }
}
