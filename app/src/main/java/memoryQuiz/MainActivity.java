package memoryQuiz;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cbnu.dementiadiagnosis.Helper;
import com.cbnu.dementiadiagnosis.MainSTT;
import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.TTS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import QuizPage.QuizPage;
import questions.MemoryQuiz;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    int count = 0;
    int checkCnt = 0;
    int arrCnt = 0;
    boolean checkFalse = false;
    CountDownTimer countDownTimer;
    String[] ans = {};
    TextView randomText;
    TextView question;
    TextView time;
    EditText answer;
    ImageButton sttBtn, submit;
    ImageView helper_img;
    ImageView shapeRes, shapeX;
    Helper helper;
    QuizPage QP;
    MainSTT stt;
    TTS tts;
    ProgressBar pro_bar;
    MemoryQuiz memoryQuiz;
    private long backBtnTime = 0;
    HashSet<String> hashSet;
    Handler hand;
    Handler handTimer;

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
        helper_img = (ImageView) findViewById(R.id.img);
        pro_bar = (ProgressBar) findViewById(R.id.progressBar);
        shapeRes = (ImageView) findViewById(R.id.shapeRes);
        shapeX = (ImageView) findViewById(R.id.shapeX);
        hand = new Handler();
        handTimer = new Handler();
        memoryQuiz = new MemoryQuiz();
        hashSet = new HashSet<>();

        tts = new TTS(this, status -> {
            tts.onInit(status, question.getText().toString(), "Done", 1000);
            answer.setEnabled(false);
            sttBtn.setEnabled(false);
            submit.setEnabled(false);
            tts.UtteranceProgress(question.getText().toString(), 1000, answer, sttBtn, submit);
        });
        stt = new MainSTT(this, answer, question, sttBtn, submit, tts);
        QP = new QuizPage(stt, tts, question, answer, sttBtn, submit, memoryQuiz.quiz);

        helper = new Helper(tts, stt, helper_img, this);
        helper.setTest();
        pro_bar.setProgress(20);

        final String[] random = {generateString()};
        Log.e("random", random[0]);
        randomText.setText(random[0]);

        // stt 시작
        sttBtn.setOnClickListener(v -> {
            tts.isStopUtt = true;
            tts.Stop();
            stt.start_STT();
        });

        submit.setOnClickListener(v -> {
            if(countDownTimer != null)
                countDownTimer.cancel();
            answer.setEnabled(false);
            sttBtn.setEnabled(false);
            submit.setEnabled(false);
            checkFalse = false;
            checkCnt = 0;
            count = 0;
            arrCnt = 0;
            stt.Stop();
            tts.Stop();

            if (answer.getText().toString().equals("")) {
                shapeX.setVisibility(View.VISIBLE);
            } else {
                String temp = answer.getText().toString().replace(".", "");
                temp = temp.replace(",", "");
                temp = temp.trim().replaceAll("\\s+", " ");
                Log.e("temp", temp);
                ans = temp.split(" ");
                Log.e("ans", Arrays.toString(ans));
                if (Arrays.asList(ans).contains("")) {
                    shapeX.setVisibility(View.VISIBLE);
                } else {
                    hashSet.addAll(Arrays.asList(ans));
                    ans = hashSet.toArray(new String[0]);
                    if (ans.length < 3) {
                        shapeX.setVisibility(View.VISIBLE);
                    } else {
                        getResultSearch(ans, random[0], ans.length);
                    }
                }
            }
            hand.postDelayed(() -> {
                countDownTimer.cancel();
                switch (QP.current) {
                    case 0:
                        changeQuiz(40);
                        break;
                    case 1:
                        changeQuiz(60);
                        break;
                    case 2:
                        changeQuiz(80);
                        break;
                    case 3:
                        changeQuiz(100);
                        break;
                    case 4:
                        countDownTimer.cancel();
                        resultDialog();
                    default:
                        break;
                }
            }, 3000);
        });

        // 타이머 설정
        handTimer.postDelayed(() -> countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String sDuration = String.format(Locale.ENGLISH, "%02d : %02d"
                        , TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                        , TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                Log.e("sDuration", sDuration);
                time.setText(sDuration);
            }

            @Override
            public void onFinish() {
                stt.Stop();
                tts.Stop();
                answer.setEnabled(false);
                sttBtn.setEnabled(false);
                tts.isStopUtt = true;

                if (answer.getText().toString().equals("")) {
                    shapeX.setVisibility(View.VISIBLE);
                } else {
                    String temp = answer.getText().toString().replace(".", "");
                    temp = temp.replace(",", "");
                    temp = temp.trim().replaceAll("\\s+", " ");
                    Log.e("temp", temp);
                    ans = temp.split(" ");
                    Log.e("ans", Arrays.toString(ans));
                    if (Arrays.asList(ans).contains("")) {
                        shapeX.setVisibility(View.VISIBLE);
                    } else {
                        hashSet.addAll(Arrays.asList(ans));
                        ans = hashSet.toArray(new String[0]);
                        if (ans.length < 3) {
                            shapeX.setVisibility(View.VISIBLE);
                        } else {
                            getResultSearch(ans, random[0], ans.length);
                        }
                    }
                }
                hand.postDelayed(() -> {
                    switch (QP.current) {
                        case 0:
                            changeQuiz(40);
                            break;
                        case 1:
                            changeQuiz(60);
                            break;
                        case 2:
                            changeQuiz(80);
                            break;
                        case 3:
                            changeQuiz(100);
                            break;
                        case 4:
                            countDownTimer.cancel();
                            resultDialog();
                        default:
                            break;
                    }
                }, 3000);
            }
        }.start(), 5000);
    }

    // 바뀔 문제 세팅
    public void changeQuiz(int pro) {
        pro_bar.setProgress(pro);
        tts.isStopUtt = false;
        QP.Submit();
        question.setText(memoryQuiz.quiz.get(QP.current));
        tts.speakOut(question.getText().toString());
        tts.UtteranceProgress(question.getText().toString(), 1000, answer, sttBtn, submit);
        String newRandom = generateString();
        Log.e("newRandom", newRandom);
        randomText.setText(newRandom);
        answer.setText("");
        shapeRes.setVisibility(View.INVISIBLE);
        shapeX.setVisibility(View.INVISIBLE);
        count = 0;
        checkCnt = 0;
        arrCnt = 0;
        time.setText("00 : 30");
        handTimer.postDelayed(() -> countDownTimer.start(), 5000);
    }

    // 백과사전에서 단어 검색
    public void getResultSearch(String[] keyword, String random, int size) {

        ApiInterface apiInterface = ApiClient.getInstance().create(ApiInterface.class);
        String clientID = "EeyUFnx4V2vBUCESZorN";
        String clientSecret = "voTm1j9DwE";

        for(int a = 0; a < size; a++) {
            checkCnt++;
            Log.e("checkCnt", Integer.toString(checkCnt));
            String result = getInitialSound(keyword[a]);
            Log.e("result", result);
            if (result.equals(random)) {
                Call<String> call = apiInterface.getSearchResult(clientID, clientSecret, "encyc.json", keyword[a]);
                String keywordNum = keyword[a];

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String result = response.body();
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                JSONArray dkf = (JSONArray) jsonObject.get("items");
                                if(!dkf.isNull(0)) {
                                    JSONObject obb = new JSONObject();
                                    String[] titleArr = new String[5];

                                    for (int i = 0; i < 5; i++) {
                                        obb = (JSONObject) dkf.get(i);
                                        String temp = (String) obb.get("title");
                                        String titleFilter = temp.replaceAll("<b>", "");
                                        String title = titleFilter.replaceAll("</b>", "");
                                        titleArr[i] = title;
                                    }
                                    Log.e("titleList", Arrays.toString(titleArr));
                                    if (Arrays.asList(titleArr).contains(keywordNum)) {
                                        count++;
                                        Log.e("cnt", Integer.toString(count));
                                    }
                                    arrCnt++;
                                    Log.e("arrCnt", Integer.toString(arrCnt));

                                    if (count >= 3 && arrCnt >= size && !checkFalse) {
                                        Log.e("one", "true!");
                                        shapeRes.setVisibility(View.VISIBLE);
                                        Toast.makeText(MainActivity.this, "o 정답입니다!!", Toast.LENGTH_SHORT).show();
                                    } else if (count < 3 && arrCnt >= size && !checkFalse) {
                                        Log.e("two", "true!");
                                        shapeX.setVisibility(View.VISIBLE);
                                        Toast.makeText(MainActivity.this, "1x 틀렸습니다!!", Toast.LENGTH_SHORT).show();
                                    } else if (count < 3 && arrCnt >= size) {
                                        Log.e("three", "true!");
                                        shapeX.setVisibility(View.VISIBLE);
                                        Toast.makeText(MainActivity.this, "2x 틀렸습니다!!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    arrCnt++;
                                    if (count < 3 && arrCnt >= size && !checkFalse) {
                                        Log.e("two", "true!");
                                        shapeX.setVisibility(View.VISIBLE);
                                        Toast.makeText(MainActivity.this, "1x 틀렸습니다!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e(TAG, "실패 : " + response.body());
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e(TAG, "에러 : " + t.getMessage());
                    }
                });
            }
            else {
                checkFalse = true;
                arrCnt++;
                Log.e("checkFalse", "true");
            }
        }
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

    public void resultDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_quiz_end, null);
        Button restart = (Button) dialogView.findViewById(R.id.restartBtn);
        Button end = (Button) dialogView.findViewById(R.id.endBtn);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(R.layout.dialog_quiz_end);
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.round_button);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(alertDialog.getWindow().getAttributes());
        params.width = 900;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setAttributes(params);

        // 다시하기
        restart.setOnClickListener(v -> {
            alertDialog.dismiss();
            startActivity(new Intent(MainActivity.this, StartActivity.class));
            finish();
        });
        // 끝내기
        end.setOnClickListener(v -> {
            alertDialog.dismiss();
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if (0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed();
            countDownTimer.cancel();
        } else {
            backBtnTime = curTime;
            Toast.makeText(this, "퀴즈를 종료하시겠습니까?",
                    Toast.LENGTH_SHORT).show();
        }
    }

   /* @Override
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
                tts.speakOut(question.getText().toString());
                newRandom = generateString();
                randomText.setText(newRandom);
                break;
            case 1:
                pro_bar.setProgress(60);
                QP.Submit();
                tts.speakOut(question.getText().toString());
                newRandom = generateString();
                randomText.setText(newRandom);
                break;
            case 2:
                pro_bar.setProgress(80);
                QP.Submit();
                tts.speakOut(question.getText().toString());
                newRandom = generateString();
                randomText.setText(newRandom);
                break;
            case 3:
                pro_bar.setProgress(100);
                QP.Submit();
                tts.speakOut(question.getText().toString());
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
        countDownTimer.cancel();
        QP.Destroy();
    }
}