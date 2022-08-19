package simpleTest;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cbnu.dementiadiagnosis.MainSTT;
import com.cbnu.dementiadiagnosis.TTS;

import java.util.List;

public class simple_QuizPage {
    MainSTT stt;
    TTS tts;
    TextView question;
    EditText answer;
    ImageButton sttBtn;
    ImageButton submit;

    public int current = 0;
    protected List<String> title_quest;

    public String user_ans, correct;
    public boolean isOrient = false;

    public simple_QuizPage(MainSTT Stt, TTS Tts, TextView quest, EditText ans,
                           ImageButton sttB, ImageButton sub, List<String> quiz){

        stt = Stt;
        tts = Tts;
        question = quest;
        answer = ans;
        sttBtn = sttB;
        submit = sub;
        title_quest = quiz;

        question.setText(title_quest.get(current));
    }

    public simple_QuizPage(TTS Tts, TextView quest, ImageButton sub, List<String> quiz) {
        tts = Tts;
        question = quest;
        submit = sub;
        title_quest = quiz;
        question.setText(title_quest.get(current));
    }

    public void Submit(){
        current ++;
        question.setText(title_quest.get(current));
    }

    public String ans_filter(String num){
        String tem;
        tem = num.replace(".", "")
                .replace(",", "")
                .replace("?", "")
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

        if(isOrient && current > 0 && current < 5){
            tem = num.replace("년", "")
                    .replace("도", "")
                    .replace("일", "")
                    .replace("시월", "십월")
                    .replace("유월", "육월")
                    .replace("월", "")
                    .replace("요일", "");
        }
        else if (isOrient && current == 5){
            tem = num.replace("요일", "");
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
        answer.setText("");
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
