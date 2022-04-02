package QuizPage;

import android.content.Context;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cbnu.dementiadiagnosis.MainSTT;
import com.cbnu.dementiadiagnosis.TTS;
import java.util.List;

public class QuizPage {
    MainSTT stt;
    TTS tts;
    TextView question;
    TextView announce;
    EditText answer;
    Button sttBtn;

    public int current = 0;
    protected List<String> title_quest;

    public String user_ans, correct;

    public QuizPage(MainSTT Stt, TTS Tts, TextView quest, TextView ann, EditText ans,
                    Button sttB, List<String> quiz){

        stt = Stt;
        tts = Tts;
        question = quest;
        announce = ann;
        answer = ans;
        sttBtn = sttB;
        title_quest = quiz;

        question.setText(title_quest.get(current));
        announce.setText("대답할 준비가 되셨다면\n아래 보라색 상자를 눌러 말씀해주세요!");

    }

    public void Submit(){
        current ++;
        question.setText(title_quest.get(current));
        announce.setText("대답할 준비가 되셨다면\n아래 보라색 상자를 눌러 말씀해주세요!");
    }

    public void Stop(){
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
