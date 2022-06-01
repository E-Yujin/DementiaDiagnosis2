package QuizPage;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cbnu.dementiadiagnosis.MainSTT;
import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.TTS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import QuizPage.QuizPage;
import questions.memoryInput;

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
    List<String> tem = new ArrayList<>();

    private long backBtnTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memory_input);

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
                int[] time = {0, 1000, 1000};
                tts.onInit(status, question.getText().toString(), "default");
                tem.add("민수는.....자전거를 타고.....공원에 가서....11시부터...야구를 했다.");
                tem.add(announce.getText().toString());
                tts.UtteranceProgress(tem, "continue", time);
            }
        }, sttBtn, submit);
        stt = new MainSTT(this, answer, announce, question, sttBtn, submit, tts);
        QP = new QuizPage(stt, tts, question, announce, answer, sttBtn,submit, memo_in.quiz);
        first = new ArrayList<>();
        second = new ArrayList<>();

        sttBtn.setEnabled(false);
        submit.setEnabled(false);

        sttBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tts.isStopUtt = true;
                tts.Stop();
                stt.start_STT();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stt.Stop();
                tts.Stop();
                sttBtn.setEnabled(true);
                sttBtn.setText("말하기");
                tts.isStopUtt = true;

                QP.user_ans = answer.getText().toString();

                answer.setText("");

                if(QP.current == 0){
                    tts.isStopUtt = false;
                    sttBtn.setEnabled(false);
                    submit.setEnabled(false);

                    for(int i = 0; i<5; i++){
                        QP.correct = memo_in.crr_ans[i].get(0);
                        if(QP.user_ans.contains(QP.correct)){
                            first.add(QP.correct);
                        }
                    }
                    QP.Submit();
                    tts.speakOut(question.getText().toString(), "default");
                    tts.UtteranceProgress(tem,"continue");
                }
                else if(QP.current == 1){
                    tts.isStopUtt = false;
                    for(int i = 0; i<5; i++){
                        QP.correct = memo_in.crr_ans[i].get(0);
                        if(QP.user_ans.contains(QP.correct)){
                            second.add(QP.correct);
                        }
                    }
                    QP.Submit();
                    tts.speakOut(question.getText().toString());
                    announce.setText("다음 단계로 이동하시려면\n아래 파란 상자를 눌러주세요!");
                    tts.UtteranceProgress(announce.getText().toString());
                    sttBtn.setEnabled(false);
                    submit.setEnabled(true);
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
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if (0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed();
        } else {
            backBtnTime = curTime;
            Toast.makeText(this, "지금 나가시면 진행된 검사가 저장되지 않습니다.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        QP.Stop();
    }

    @Override
    protected void onStart(){
        super.onStart();
        tts.isStopUtt = false;
        sttBtn.setEnabled(false);
        submit.setEnabled(false);
        if(QP.current < 2){
            if(announce.getText() != "대답할 준비가 되셨다면\n아래 보라색 상자를 눌러 말씀해주세요!"){
                QP.Start();
                tts.speakOut(question.getText().toString());
                tts.UtteranceProgress(tem,"continue");
            }
            else {
                tts.speakOut(question.getText().toString());
                tts.UtteranceProgress(tem,"continue");
            }
        }
        else{
            tts.speakOut(question.getText().toString());
            announce.setText("다음 단계로 이동하시려면\n아래 파란 상자를 눌러주세요!");
            tts.UtteranceProgress(announce.getText().toString());
            sttBtn.setEnabled(false);
            submit.setText("확인");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        QP.Destroy();
    }
}
