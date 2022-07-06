package QuizPage;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import questions.memoryOutput;

public class memoryOutput_Page extends AppCompatActivity {
    memoryOutput memo_out;
    MainSTT stt;
    TTS tts;
    QuizPage QP;
    TextView question;
    TextView announce;
    EditText answer;
    ImageButton sttBtn;
    Button submit;

    boolean first[];

    private long backBtnTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memory_output);

        question = (TextView) findViewById(R.id.question);
        announce = (TextView) findViewById(R.id.textView);
        answer = (EditText) findViewById(R.id.result);
        answer.setEnabled(true);
        answer.setHint("이 항목에서는 키보드 입력이 불가능합니다.");
        sttBtn = (ImageButton) findViewById(R.id.sttStart);
        submit = (Button) findViewById(R.id.submit);
        memo_out = new memoryOutput();

        tts = new TTS(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.onInit(status, question.getText().toString(), "Done");
            }
        }, sttBtn, submit);
        stt = new MainSTT(this, answer, announce, question, sttBtn, submit, tts);
        QP = new QuizPage(stt, tts, question, announce, answer, sttBtn,submit, memo_out.quiz);

        first = new boolean[memo_out.num];
        Arrays.fill(first, false);
        first[0] = true;

        sttBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tts.isStopUtt = true;
                tts.Stop();
                stt.start_STT();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 설정 초기화
                stt.Stop();
                tts.Stop();
                sttBtn.setEnabled(true);
                tts.isStopUtt = true;

                QP.user_ans = answer.getText().toString();

                answer.setText("");

                if(QP.current == 0){
                    for(int i = 0; i<5; i++){
                        QP.correct = memo_out.crr_ans[0].get(i);
                        if(QP.user_ans.contains(QP.correct)){
                            first[i+1] = true;
                            memo_out.score += 2;
                        }
                    }
                    if(memo_out.score == 10){ // 점수가 만점이면 액티비티 종료.
                        Intent resultIntent = new Intent();

                        resultIntent.putExtra("isDone", true);
                        resultIntent.putExtra("score", memo_out.score);

                        setResult(1, resultIntent);
                        finish();
                    }
                    else{
                        while (QP.current < 6){
                            if(!first[QP.current]){
                                question.setText(memo_out.quiz.get(QP.current));
                                announce.setText("대답할 준비가 되셨다면\n아래 보라색 상자를 눌러 말씀해주세요!");
                                tts.speakOut(question.getText().toString(),"Done");
                                break;
                            }
                            else QP.current ++;
                        }
                    }
                }
                else {
                    QP.correct = memo_out.crr_ans[QP.current].get(0);
                    if(QP.user_ans.contains(QP.correct)){
                        memo_out.score ++;
                    }
                    QP.current ++;
                    while (QP.current < 6){
                        if(!first[QP.current]){
                            question.setText(memo_out.quiz.get(QP.current));
                            announce.setText("대답할 준비가 되셨다면\n아래 보라색 상자를 눌러 말씀해주세요!");
                            tts.speakOut(question.getText().toString(),"Done");
                            break;
                        }
                        else QP.current ++;
                    }
                    if(QP.current == 6){
                        Intent resultIntent = new Intent();

                        resultIntent.putExtra("isDone", true);
                        resultIntent.putExtra("score", memo_out.score);

                        setResult(1, resultIntent);
                        finish();
                    }
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
        if(QP.current < 2){
            if(announce.getText() != "대답할 준비가 되셨다면\n아래 보라색 상자를 눌러 말씀해주세요!"){
                QP.Start();
                tts.speakOut(question.getText().toString(),"Done");
            }
            else {
                tts.speakOut(question.getText().toString(),"Done");
            }
        }
        else{
            tts.speakOut(question.getText().toString(),"Done");
            announce.setText("다음 단계로 이동하시려면\n아래 파란 상자를 눌러주세요!");
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
