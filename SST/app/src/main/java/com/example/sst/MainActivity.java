package com.example.sst;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;

import org.apache.commons.lang.StringEscapeUtils;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    Intent intent; //어쩌구
    SpeechRecognizer mRecognizer;

    private TextToSpeech tts;

    Button sttBtn;
    EditText result;
    TextView textView;
    TextView question;

    ETRI etri = new ETRI();

    String result_text;

    final int PERMISSION = 1;

    public static final String PREFS_NAME = "prefs";
    private static final String MSG_KEY = "status";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if ( Build.VERSION.SDK_INT >= 23 ){ // 퍼미션 체크
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.INTERNET,
                            Manifest.permission.RECORD_AUDIO},PERMISSION);
        }

        tts = new TextToSpeech(this, this);

        result = (EditText)findViewById(R.id.result);
        textView = (TextView)findViewById(R.id.textView);
        question = (TextView)findViewById(R.id.question);
        sttBtn = (Button) findViewById(R.id.sttStart);

        intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");

        sttBtn.setOnClickListener(new  View.OnClickListener() {
            public void onClick(View v) {
                if (etri.isRecording) {
                    etri.forceStop = true;
                } else {
                    try {
                        new Thread(new Runnable() {
                            public void run() {
                                SendMessage("듣는 중...\n\n" +
                                        "말씀이 끝나셨다면 '말했어요!'를 눌러주세요!", 1);
                                try {
                                    etri.recordSpeech();
                                    SendMessage("인식 중...", 2);
                                } catch (RuntimeException e) {
                                    SendMessage(e.getMessage(), 3);
                                    return;
                                }
                                if(etri.forceStop && etri.isStop_state) {
                                    return;
                                }
                                else{
                                    Thread threadRecog = new Thread(new Runnable() {
                                        public void run() {
                                            result_text = etri.sendDataAndGetResult();
                                        }
                                    });
                                    threadRecog.start();
                                    try {
                                        threadRecog.join(20000);
                                        if (threadRecog.isAlive()) {
                                            threadRecog.interrupt();
                                            SendMessage("20초 동안 말씀하시지 않아 인식을 종료합니다.\n" +
                                                    "'말하기'를 다시 누르고 말씀해주세요.", 4);
                                        } else {
                                            SendMessage("들었어요!", 5);
                                        }
                                    } catch (InterruptedException e) {
                                        SendMessage("Interrupted", 4);
                                    }
                                }
                            }
                        }).start();
                    } catch (Throwable t) {
                        textView.setText("ERROR: " + t.toString());
                        etri.forceStop = false;
                        etri.isRecording = false;
                    }
                }
            }
        });


        textView.setText("대답할 준비가 되셨다면 아래 '말하기'를 눌러주세요!");
        speakOut();
    }


    private final Handler handler = new Handler() {
        @Override
        public synchronized void handleMessage(Message msg) {
            Bundle bd = msg.getData();
            String v = bd.getString(MSG_KEY);
            switch (msg.what) {
                // 녹음이 시작되었음(버튼)
                case 1:
                    textView.setText(v);
                    sttBtn.setText("말했어요!");
                    break;
                // 녹음이 정상적으로 종료되었음(버튼 또는 max time)
                case 2:
                    textView.setText(v);
                    sttBtn.setEnabled(false);
                    break;
                // 녹음이 비정상적으로 종료되었음(마이크 권한 등)
                case 3:
                    textView.setText(v);
                    sttBtn.setText("말하기");
                    break;
                // 인식이 비정상적으로 종료되었음(timeout 등)
                case 4:
                    textView.setText(v);
                    sttBtn.setEnabled(true);
                    sttBtn.setText("말하기");
                    break;
                // 인식이 정상적으로 종료되었음 (thread내에서 exception포함)
                case 5:
                    result.setText(SplitResult());
                    if(SplitResult().equals("ASR_NOTOKEN")){
                        textView.setText("혹시 아무것도 말하지 않으셨나요?\n\n" +
                                "'다시 말하기'를 눌러 다시 말씀하시거나\n" +
                                "자판을 이용해 다시 입력해주세요.");
                    }
                    else{
                        textView.setText("\"" + result.getText() + "\"" +
                                " 라고 말씀하신 게 맞나요?\n\n" +
                                "아니라면 '다시 말하기'를 눌러 다시 말씀하시거나\n" +
                                "자판을 이용해 다시 입력해주세요.");
                    }
                    speakOut();
                    sttBtn.setEnabled(true);
                    sttBtn.setText("다시 말하기");

                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void DelayAndRecord(int time){
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                SendMessage("듣는 중...\n" +
                        "말씀이 끝나셨다면 '말했어요!'를 눌러주세요!", 1);
                etri.recordSpeech();
            }
        }, time);
    }

    public String SplitResult(){
        String splited[] = result_text.split("\"");
        return splited[7];
    }

    private void SendMessage(String str, int id) {
        Message msg = handler.obtainMessage();
        Bundle bd = new Bundle();
        bd.putString(MSG_KEY, str);
        msg.what = id;
        msg.setData(bd);
        handler.sendMessage(msg);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void speakOut() {
        CharSequence text = textView.getText();
        tts.setPitch((float) 0.6);
        tts.setSpeechRate((float) 1);
        tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,"id1");
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.KOREA);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
            { Log.e("TTS", "This Language is not supported");
            }
            else {
                speakOut();
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(tts!=null){
            tts.stop();
        }
        if(etri.isRecording){
            etri.forceStop = true;
            etri.isStop_state = true;
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(textView.getText() != "대답할 준비가 되셨다면 아래 '말하기'를 눌러주세요!"){
            textView.setText("대답할 준비가 되셨다면 아래 '말하기'를 눌러주세요!");
            result.setText("");
            Toast.makeText(this, "음성 인식 도중 나가셨기에 해당 문제부터 다시 시작합니다.",
                    Toast.LENGTH_SHORT).show();
        }
        else speakOut();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tts!=null){
            tts.stop();
            tts.shutdown();
            tts=null;
        }
        if(etri.isRecording){
            etri.forceStop = true;
        }
    }
}

