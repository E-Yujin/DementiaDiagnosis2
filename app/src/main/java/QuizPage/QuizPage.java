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
    Button sttBtn, submit;

    public int current = 0;
    protected List<String> title_quest;

    public String user_ans, correct;

    public QuizPage(MainSTT Stt, TTS Tts, TextView quest, TextView ann, EditText ans,
                    Button sttB,Button sub, List<String> quiz){

        stt = Stt;
        tts = Tts;
        question = quest;
        announce = ann;
        answer = ans;
        sttBtn = sttB;
        submit = sub;
        title_quest = quiz;

        question.setText(title_quest.get(current));
        announce.setText("대답할 준비가 되셨다면\n아래 보라색 상자를 눌러 말씀해주세요!");
    }

    public QuizPage(TTS Tts, TextView quest, TextView ann, Button sttB, Button sub, int n, List<String> quiz) {
        tts = Tts;
        question = quest;
        announce = ann;
        sttBtn = sub;
        submit = sub;
        title_quest = quiz;
        question.setText(title_quest.get(n));
    }

    public void Submit(){
        current ++;
        question.setText(title_quest.get(current));
        announce.setText("대답할 준비가 되셨다면\n아래 보라색 상자를 눌러 말씀해주세요!");
    }

    public String ans_filter(String num){
        String tem;
        if(current != 3){
            tem = num.replace(".", "")
                    .replace(",", "")
                    .replace("?", "")
                    .replace("년", "")
                    .replace("도", "")
                    .replace("일", "")
                    .replace("시월", "십월")
                    .replace("유월", "육월")
                    .replace("월", "")
                    .replace("요일", "")
                    .replace("이다", "")
                    .replace("이요", "")
                    .replace("이야", "")
                    .replace("이지", "")
                    .replace("이죠", "")
                    .replace("이지요", "")
                    .replace("이고", "")
                    .replace("이며", "")
                    .replace("이라서", "")
                    .replace("이어서", "")
                    .replace("이었다", "")
                    .replace("이기", "")
                    .replace("입니다", "")
                    .replace("이기에", "");
        }
        else{
            tem = num.replace(".", "")
                    .replace(",", "")
                    .replace("?", "")
                    .replace("요일", "")
                    .replace("이다", "")
                    .replace("이요", "")
                    .replace("이야", "")
                    .replace("이지", "")
                    .replace("이죠", "")
                    .replace("이지요", "")
                    .replace("이고", "")
                    .replace("이며", "")
                    .replace("이라서", "")
                    .replace("이어서", "")
                    .replace("이었다", "")
                    .replace("이기", "")
                    .replace("입니다", "")
                    .replace("이기에", "");
        }
        return tem;
    }

    public void Stop(){
        if(tts != null && stt != null){
            tts.Stop();
            stt.Stop();
        }
    }

    public void Start(){
        question.setText(title_quest.get(current));
        announce.setText("대답할 준비가 되셨다면\n아래 보라색 상자를 눌러 말씀해주세요!");
        answer.setText("");
        sttBtn.setText("말하기");
        sttBtn.setEnabled(true);
        submit.setEnabled(true);
    }

    public void Destroy() {
        if(tts != null && stt != null) {
            tts.Destroy();
            stt.Destroy();
        }
    }
}
