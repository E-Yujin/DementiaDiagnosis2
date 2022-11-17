package memoryQuiz;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
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
import com.cbnu.dementiadiagnosis.HomeActivity;
import com.cbnu.dementiadiagnosis.MainSTT;
import com.cbnu.dementiadiagnosis.R;
import com.cbnu.dementiadiagnosis.TTS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import QuizPage.QuizPage;
import questions.MemoryQuiz;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import user.SharedPreference;

public class MainActivity extends AppCompatActivity {

    int count = 0;
    int checkCnt = 0;
    int arrCnt = 0;
    boolean checkFalse = false;
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
    Handler hand;
    Handler handTimer;
    String random;
    CoresTimerTask coresTimerTask;
    private long backBtnTime = 0;
    private static final long START_TIME_IN_MILLIS = 30000;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private CountDownTimer countDownTimer;
    private boolean mTimerRunning;
    private boolean stopCheck = false;
    private int resultCnt = 0;

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
        coresTimerTask = new CoresTimerTask();

        answer.setHint("답변이 여기에 나타납니다.");

        tts = new TTS(this, status -> {
            tts.onInit(status, question.getText().toString(), "Done", 1000);
            answer.setEnabled(false);
            sttBtn.setEnabled(false);
            submit.setEnabled(false);
            tts.UtteranceProgress(question.getText().toString(), 1000, answer, sttBtn, submit);
        });
        stt = new MainSTT(this, answer, question, sttBtn, submit, tts,
                SharedPreference.getSTT_start(this), SharedPreference.getSTT_end(this),
                SharedPreference.getSTT_speed(this));
        Log.d("STT_setting", "s= "+stt.getStart()+", e= "+stt.getEnd()+", v= "+stt.getSpeed());
        QP = new QuizPage(stt, tts, question, answer, sttBtn, submit, memoryQuiz.quiz);

        helper = new Helper(tts, stt, helper_img, this);
        helper.setTest();
        pro_bar.setProgress(20);

        // stt 시작
        sttBtn.setOnClickListener(v -> {
            tts.isStopUtt = true;
            tts.Stop();
            stt.start_STT();
        });

        // 타이머 설정
        handTimer.postDelayed(() -> {
            if(!mTimerRunning) {
                boolean checkTimerRun = coresTimerTask.hasRunStarted();
                if (!checkTimerRun)
                    startTimer();
            }
        }, 5000);
        updateCountDownText();

        // 제출버튼
        submit.setOnClickListener(v -> {
            answer.setEnabled(false);
            sttBtn.setEnabled(false);
            submit.setEnabled(false);
            checkFalse = false;
            mTimerRunning = false;
            checkCnt = 0;
            count = 0;
            arrCnt = 0;
            stt.Stop();
            tts.Stop();

            if(countDownTimer != null) {
                countDownTimer.cancel();
            }

            if (answer.getText().toString().equals("")) { // 공백 답변 시
                shapeX.setVisibility(View.VISIBLE);
            } else {
                String temp = answer.getText().toString().replace(".", "");
                temp = temp.replace(",", "");
                temp = temp.replace("?", "");
                temp = temp.trim().replaceAll("\\s+", " ");
                Log.e("temp", temp);
                ans = temp.split(" ");
                Log.e("ans", Arrays.toString(ans));
                if (Arrays.asList(ans).contains("")) {
                    shapeX.setVisibility(View.VISIBLE);
                } else {
                    HashSet<String> hashSet = new HashSet<>(Arrays.asList(ans)); // 중복 항목 체크
                    Log.e("hashSet", hashSet.toString());
                    ans = hashSet.toArray(new String[0]);
                    if (ans.length < 3) {
                        Log.e("ans_length", Integer.toString(ans.length));
                        shapeX.setVisibility(View.VISIBLE);
                    } else {
                        getResultSearch(ans, random, ans.length);
                    }
                }
            }
            hand.postDelayed(() -> {
                Log.e("current_submit", Integer.toString(QP.current));
                switch (QP.current) {
                    case 1:
                        changeQuiz(40);
                        break;
                    case 2:
                        changeQuiz(60);
                        break;
                    case 3:
                        changeQuiz(80);
                        break;
                    case 4:
                        changeQuiz(100);
                        break;
                    case 5:
                        resultDialog();
                    default:
                        break;
                }
            }, 3000);
        });
    }

    // 바뀔 문제 세팅
    public void changeQuiz(int pro) {
        Log.e("progress", Integer.toString(pro));
        pro_bar.setProgress(pro);
        tts.isStopUtt = false;
        QP.Submit();
        question.setText(memoryQuiz.quiz.get(QP.current));
        tts.speakOut(question.getText().toString());
        tts.UtteranceProgress(question.getText().toString(), 1000, answer, sttBtn, submit);
        random = generateString();
        Log.e("random", random);
        randomText.setText(random);
        answer.setText("");
        shapeRes.setVisibility(View.INVISIBLE);
        shapeX.setVisibility(View.INVISIBLE);
        count = 0;
        checkCnt = 0;
        arrCnt = 0;
        time.setText("00 : 30");
        resetTimer();
        handTimer.postDelayed(() -> {
            if(!mTimerRunning) {
                boolean checkTimerRun = coresTimerTask.hasRunStarted();
                if (!checkTimerRun)
                    startTimer();
            }
        }, 5000);
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
            if(result != null)
                Log.e("result", result);
            Log.e("search_random", random);
            assert result != null;
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
                                    String[] titleArr = new String[5];

                                    for (int i = 0; i < 5; i++) {
                                        JSONObject obb = (JSONObject) dkf.get(i);
                                        String temp = (String) obb.get("title");
                                        String titleFilter = temp.replaceAll("<b>", "");
                                        String title = titleFilter.replaceAll("</b>", "");
                                        titleArr[i] = title;
                                        if(titleArr[i].contains(keywordNum))
                                            break;
                                    }
                                    Log.e("titleList", Arrays.toString(titleArr));

                                    for (String s : titleArr) {
                                        if (s.contains(keywordNum)) {
                                            count++;
                                            Log.e("cnt", Integer.toString(count));
                                            break;
                                        }
                                    }
                                    arrCnt++;
                                    Log.e("arrCnt", Integer.toString(arrCnt));

                                    //  count == 3, arrCnt == 4
                                    if (count >= 3 && arrCnt >= size) {
                                        Log.e("one", "true!");
                                        resultCnt++;
                                        shapeRes.setVisibility(View.VISIBLE);
                                        Toast.makeText(MainActivity.this, "o 정답입니다!!", Toast.LENGTH_SHORT).show();
                                    } else if (count < 3 && arrCnt >= size) {
                                        Log.e("two", "true!");
                                        shapeX.setVisibility(View.VISIBLE);
                                        Toast.makeText(MainActivity.this, "x 틀렸습니다!!", Toast.LENGTH_SHORT).show();
                                    } /*else if (count < 3 && arrCnt >= size) {
                                        Log.e("three", "true!");
                                        shapeX.setVisibility(View.VISIBLE);
                                        Toast.makeText(MainActivity.this, "2x 틀렸습니다!!", Toast.LENGTH_SHORT).show();
                                    }*/
                                } else {
                                    arrCnt++;
                                    if (count < 3 && arrCnt >= size) {
                                        Log.e("two", "true!");
                                        shapeX.setVisibility(View.VISIBLE);
                                        Toast.makeText(MainActivity.this, "x 틀렸습니다!!", Toast.LENGTH_SHORT).show();
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
            } else {
                //checkFalse = true;
                arrCnt++;
                if (count >= 3 && arrCnt >= size) {
                    Log.e("one", "true!");
                    resultCnt++;
                    shapeRes.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "o 정답입니다!!", Toast.LENGTH_SHORT).show();
                } else if (count < 3 && arrCnt >= size) {
                    Log.e("two", "true!");
                    shapeX.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "x 틀렸습니다!!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // 타이머 시작
    public void startTimer() {
        countDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                stt.Stop();
                tts.Stop();
                answer.setEnabled(false);
                sttBtn.setEnabled(false);
                tts.isStopUtt = true;
                mTimerRunning = false;

                if (answer.getText().toString().equals("")) {
                    shapeX.setVisibility(View.VISIBLE);
                } else {
                    String temp = answer.getText().toString().replace(".", "");
                    temp = temp.replace(",", "");
                    temp = temp.replace("?", "");
                    temp = temp.trim().replaceAll("\\s+", " ");
                    Log.e("temp", temp);
                    ans = temp.split(" ");
                    Log.e("ans", Arrays.toString(ans));
                    if (Arrays.asList(ans).contains("")) {
                        shapeX.setVisibility(View.VISIBLE);
                    } else {
                        HashSet<String> hashSet = new HashSet<>(Arrays.asList(ans)); // 중복 항목 체크
                        Log.e("hashSet", hashSet.toString());
                        ans = hashSet.toArray(new String[0]);
                        if (ans.length < 3) {
                            Log.e("ans_length", Integer.toString(ans.length));
                            shapeX.setVisibility(View.VISIBLE);
                        } else {
                            getResultSearch(ans, random, ans.length);
                        }
                    }
                }
                hand.postDelayed(() -> {
                    Log.e("current_timer", Integer.toString(QP.current));
                    switch (QP.current) {
                        case 1:
                            changeQuiz(40);
                            break;
                        case 2:
                            changeQuiz(60);
                            break;
                        case 3:
                            changeQuiz(80);
                            break;
                        case 4:
                            changeQuiz(100);
                            break;
                        case 5:
                            countDownTimer.cancel();
                            resultDialog();
                            break;
                        default:
                            break;
                    }
                }, 3000);
            }
        }.start();

        mTimerRunning = true;
    }

    // 타이머 업데이트
    public void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d : %02d", minutes, seconds);
        Log.e("time", timeLeftFormatted);
        time.setText(timeLeftFormatted);
    }

    // 타이머 리셋
    public void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
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
        return "ㄲㄲ";
    }

    @SuppressLint("SetTextI18n")
    public void resultDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_quiz_end, null);
        Button restart = (Button) dialogView.findViewById(R.id.restartBtn);
        Button end = (Button) dialogView.findViewById(R.id.endBtn);
        TextView score = (TextView) dialogView.findViewById(R.id.score);
        TextView text = (TextView) dialogView.findViewById(R.id.text);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(R.layout.dialog_quiz_end);
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.round_button);
        alertDialog.setCancelable(false);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(alertDialog.getWindow().getAttributes());
        params.width = 900;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setAttributes(params);

        score.setText(Integer.toString(resultCnt) + "/5");
        if(resultCnt < 3) {
           score.setTextColor(Color.parseColor("#FF0000"));
            text.setTextColor(Color.parseColor("#FF0000"));
            text.setText("(많은 연습이 필요합니다!)");
        } else if (resultCnt == 3) {
            score.setTextColor(Color.parseColor("#FF6799FF"));
            text.setTextColor(Color.parseColor("#FF6799FF"));
            text.setText("(조금 더 연습이 필요합니다!)");
        } else {
            score.setTextColor(Color.parseColor("#FF03DAC5"));
            text.setTextColor(Color.parseColor("#FF03DAC5"));
            text.setText("(훌륭합니다!)");
        }

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

    public static class CoresTimerTask extends TimerTask {

        private boolean hasStarted = false;

        @Override
        public void run() {
            this.hasStarted = true;
        }

        public boolean hasRunStarted() {
            return this.hasStarted;
        }
    }

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if (0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed();
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            tts.Destroy();
            countDownTimer.cancel();
            finish();
        } else {
            backBtnTime = curTime;
            Toast.makeText(this, "퀴즈를 종료하시겠습니까?",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        tts.isStopUtt = false;
        question.setText(memoryQuiz.quiz.get(QP.current));
        answer.setHint("답변이 여기에 나타납니다.");
        sttBtn.setEnabled(false);
        submit.setEnabled(false);
        tts.speakOut(question.getText().toString());
        tts.UtteranceProgress(question.getText().toString(), 1000, answer, sttBtn, submit);
        Log.e("start_QP.current", Integer.toString(QP.current));

        if(stopCheck) {
            QP.current--;
        }

        switch (QP.current) {
            case 0:
                changeQuiz(20);
                break;
            case 1:
                changeQuiz(40);
                break;
            case 2:
                changeQuiz(60);
                break;
            case 3:
                changeQuiz(80);
                break;
            case 4:
                changeQuiz(100);
                break;
            case 5:
                countDownTimer.cancel();
                resultDialog();
            default:
                break;
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        tts.isStopUtt = true;
        stopCheck = true;
        mTimerRunning = false;
        tts.Stop();
        stt.Stop();

        if(countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
        QP.Destroy();
    }
}