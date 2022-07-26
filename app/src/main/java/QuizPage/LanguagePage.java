package QuizPage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cbnu.dementiadiagnosis.MainSTT;
import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.TTS;

import java.util.ArrayList;
import java.util.List;

import questions.LanguageFunc;
import questions.attention;

public class LanguagePage extends AppCompatActivity {
    LanguageFunc languageFunc;
    MainSTT stt;
    TTS tts;
    QuizPage QP;
    TextView question;
    TextView announce;
    ImageView image;
    EditText answer;
    ImageButton sttBtn;
    ImageButton submit;
    ActivityResultLauncher<Intent> startActivityResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_func);

        question = (TextView) findViewById(R.id.question);
        announce = (TextView) findViewById(R.id.announce);
        image = (ImageView) findViewById(R.id.question_image);
        answer = (EditText) findViewById(R.id.result);
        sttBtn = (ImageButton) findViewById(R.id.sttStart);
        submit = (ImageButton) findViewById(R.id.btnSubmit);
        languageFunc = new LanguageFunc();

        /*Intent intent;
        intent = getIntent();
        languageFunc.scores = intent.getIntArrayExtra("scores");*/

        image.setImageResource(R.drawable.toothbrush);

        startActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Log.d("launcher_intent", "성공!!!!!!!!!!!");
                            assert result.getData() != null;
                            int num = result.getData().getIntExtra("comprehension", 0);
                            languageFunc.Tscore += num;

                            languageFunc.scores[7] = languageFunc.Tscore;

                            Intent intent = new Intent(getApplicationContext(), fluency_Page.class);
                            intent.putExtra("scores", languageFunc.scores);
                            startActivity(intent);

                            finish();
                        }
                    }
                });

        tts = new TTS(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.onInit(status, question.getText().toString(), "default");
                //tts.UtteranceProgress(announce.getText().toString());
            }
        });
        stt = new MainSTT(this, answer, question, sttBtn, submit, tts);
        QP = new QuizPage(stt, tts, question, answer, sttBtn, submit, languageFunc.quiz);

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
                tts.isStopUtt = true;

                QP.user_ans = answer.getText().toString()
                        .replace(" ", "")
                        .replace(",", "")
                        .replace(".", "");

                List<String> correct = new ArrayList<>();
                correct.clear();
                for (String data : languageFunc.crr_ans[QP.current]) {
                    correct.add(data);
                }

                answer.setText("");

                if (QP.user_ans.isEmpty()) {
                    announce.setText("무응답으로 넘어가실 수 없습니다.\n아시는 대로 천천히 말씀해주시면 됩니다.");
                    tts.speakOut(announce.getText().toString(), "default");
                } else {
                    for (String data : correct) {
                        if (QP.user_ans.contains(data)) {
                            languageFunc.Tscore++;
                        }
                        /*if (QP.current + 1 < languageFunc.score) {
                            languageFunc.score = QP.current + 1;
                        }*/
                    }
                    if (QP.current == 0) {
                        image.setImageResource(R.drawable.swing);
                        tts.isStopUtt = false;
                        QP.Submit();
                        tts.speakOut(question.getText().toString(), "default");
                    } else if (QP.current == 1) {
                        image.setImageResource(R.drawable.dice);
                        tts.isStopUtt = false;
                        QP.Submit();
                        tts.speakOut(question.getText().toString(), "default");
                    } else if (QP.current == 2) {
                        Intent intent = new Intent(LanguagePage.this, ComprehensionPage.class);
                        startActivityResult.launch(intent);
                        overridePendingTransition(0, 0);
                        intent.putExtra("isDone", true);
                        intent.putExtra("score", languageFunc.Tscore);
                        setResult(1, intent);
                        QP.Submit();
                    } /*else if(QP.current == 3) {
                        Intent intent = new Intent();
                        intent.putExtra("isDone", true);
                        intent.putExtra("score", languageFunc.score);
                        setResult(1, intent);
                        finish();
                    }*/
                }
            }
        });
    }
}