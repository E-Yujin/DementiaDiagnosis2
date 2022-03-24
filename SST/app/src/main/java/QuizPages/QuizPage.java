package QuizPages;

import android.content.Context;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sst.MainSTT;
import com.example.sst.TTS;
import java.util.List;

public class QuizPage {
    MainSTT stt;
    TTS tts;
    TextView question;
    TextView announce;
    EditText answer;
    Button sttBtn;
    Button submit;

    protected boolean isStop = false;
    protected long backBtnTime = 0;
    protected boolean isDone[];
    public int current = 0;
    protected List<String> title_quest, Correct;
    protected int Tscore;

    public String user_ans, correct;

    public QuizPage(MainSTT Stt, TTS Tts, TextView quest, TextView ann, EditText ans, Button sttB,
                    Button sub, boolean isD[], List<String> quiz, List<String> crr, int Score){

        stt = Stt;
        tts = Tts;
        question = quest;
        announce = ann;
        answer = ans;
        sttBtn = sttB;
        submit = sub;
        isDone = isD;
        title_quest = quiz;
        Correct = crr;
        Tscore = Score;

        question.setText(title_quest.get(current));
        announce.setText("대답할 준비가 되셨다면\n아래 보라색 상자를 눌러 말씀해주세요!");

    }

    public void Submit(){
        current ++;
        question.setText(title_quest.get(current));
        announce.setText("대답할 준비가 되셨다면\n아래 보라색 상자를 눌러 말씀해주세요!");
    }

    public void Delay(int time, String say) {
        int tem = current;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isStop && tem == current && !stt.isRecording && !user_ans.isEmpty())
                tts.speakOut(say);
            }
        }, time);
    }

    public void BackPressed(Context context) {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if (0 <= gapTime && 2000 >= gapTime) {
        } else {
            backBtnTime = curTime;
            Toast.makeText(context, "지금 나가시면 진행된 검사가 저장되지 않습니다.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void Stop(){
        isStop = true;
        tts.Stop();
        stt.Stop();
    }

    public void Start(Context context){
        question.setText(title_quest.get(current));
        announce.setText("대답할 준비가 되셨다면\n아래 보라색 상자를 눌러 말씀해주세요!");
        answer.setText("");
        Toast.makeText(context, "음성 인식 도중 나가셨기에 해당 문제부터 다시 시작합니다.",
                Toast.LENGTH_SHORT).show();
        sttBtn.setText("말하기");
        sttBtn.setEnabled(true);
    }

    public void Destroy() {
        tts.Destroy();
        stt.Destroy();
    }
}
