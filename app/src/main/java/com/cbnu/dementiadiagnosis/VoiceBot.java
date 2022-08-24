package com.cbnu.dementiadiagnosis;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;

import memoryQuiz.StartActivity;

public class VoiceBot extends AppCompatActivity {

    MainSTT stt;
    TTS tts;

    TextView tv_outPut;
    EditText edit;
    ImageButton sttBtn;
    ImageButton submit;
    String request;
    String answer = "";
    String extra_ans = "";
    String img = "";
    ImageView helper_img;
    Helper helper;
    List<String> tem = new ArrayList<>();
    AppCompatButton exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voicebot);

        // 위젯에 대한 참조.
        tv_outPut = (TextView) findViewById(R.id.question);
        sttBtn = findViewById(R.id.sttStart);
        submit = (ImageButton) findViewById(R.id.submit);
        edit =(EditText)findViewById(R.id.result);
        stt = new MainSTT(this, edit, tv_outPut, sttBtn, submit, tts);
        helper_img = findViewById(R.id.img);
        exit = findViewById(R.id.btnLogout);

        tv_outPut.setText("안녕하세요!\n같이 대화해요!");
        edit.setHint("답변이 여기에 나타납니다.");


        tts = new TTS(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.onInit(status, tv_outPut.getText().toString(), "Done", 1000);
            }
        });

        helper = new Helper(tts, stt, helper_img, this);
        helper.setTest();

        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tv_outPut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tts.speakOut(tv_outPut.getText().toString());
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getAnswer();
            }
        });

        sttBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!stt.isRecording){
                    tts.isStopUtt = true;
                    tts.Stop();
                    stt.start_STT();
                }
                else {
                    stt.start_STT();
                    getAnswer();
                }
            }
        });

        // URL 설정.

    }

    public void getAnswer(){
        sttBtn.setEnabled(false);
        submit.setEnabled(false);
        edit.setEnabled(false);
        request = edit.getText().toString();
        Thread thread = new Thread(new Runnable() {
            public void run() {
                filter(sendDataAndGetResult(request));
            }
        });
        thread.start();
        try{
            thread.join();
            Log.d("확인", "answer : "+answer);
            if(!img.equals("")){
                Log.d("확인", "img : "+img);
                tv_outPut.setText(answer + "\n\n" + img);
                readResponse();
            }
            else if(!extra_ans.equals("")){
                Log.d("확인", "extra : "+extra_ans);
                readResponse();
            }
            else if(edit.getText().toString().equals("")){
                tv_outPut.setText("말을 걸어주세요!");
                tts.speakOut(tv_outPut.getText().toString());
                sttBtn.setEnabled(true);
                submit.setEnabled(true);
                edit.setEnabled(true);
            }
            else {
                readResponse();
            }

        }catch (InterruptedException e){
            Log.e("확인", "인터럽트 오류 발생함.");
        }
        answer = "";
        extra_ans = "";
        img = "";
    }

    public void filter(String input){
        String splited[] = input.split(":");
        String s = "";
        String s2 = "";
        if(input.contains("이미지셋")){
            String[] splited1 = input.split("\"");
            for(int i = 0; i<splited1.length; i++){
                if(splited1[i].contains("이미지셋")){
                    String[] splited2 = splited1[i].split(" ");
                    if(splited2[2].contains("u")){
                        String splited3[] = splited2[2].split("\\\\");
                        img = splited3[0];
                    }
                    else img = splited2[2];
                }
            }
        }

        if(input.contains("토픽")){
            int count = 0;
            for(int i= 0; i<splited.length; i++){
                if(splited[i].contains("text") && !splited[i+1].contains("핑퐁")) {
                    s = splited[i+1];
                    String splited2[] = s.split("\"");
                    if(splited2[1].contains("u")){
                        String splited3[] = splited2[1].split("\\\\");
                        answer = splited3[0];
                        count = i+1;
                        break;
                    }
                    else {
                        answer = splited2[1];
                        count = i+1;
                        break;
                    }
                }
            }
            for(int i = count; i<splited.length; i++){
                if(splited[i].contains("text")) {
                    s2 = splited[i+1];
                    String splited2[] = s2.split("\"");
                    if(splited2[1].contains("u")){
                        String splited3[] = splited2[1].split("\\\\");
                        extra_ans = splited3[0];
                    }
                    else extra_ans = splited2[1];
                }
            }
        }
        else{
            for(int i= 0; i<splited.length; i++){
                if(splited[i].contains("text") && !splited[i+1].contains("핑퐁")) {
                    s = splited[i+1];
                    String splited2[] = s.split("\"");
                    if(splited2[1].contains("u")){
                        String splited3[] = splited2[1].split("\\\\");
                        answer = splited3[0];
                        break;
                    }
                    else {
                        answer = splited2[1];
                        break;
                    }
                }
            }
        }
    }

    public void filter2(){

    }

    public void readResponse(){
        tem.clear();
        if(answer.contains("사용자")){
            answer = answer.replace("사용자", "유진");
        }
        if(extra_ans.contains("사용자")){
            extra_ans = extra_ans.replace("사용자", "유진");
        }
        if(extra_ans.equals("")){
            if(answer.contains("@")){
                String[] split = answer.split("@");
                tv_outPut.setText(split[0]);
                tts.speakOut(split[0], "default");
                for(String s : split){
                    tem.add(s);
                }
                tem.remove(0);
                tts.UtteranceProgress(tem, "continue", tv_outPut, sttBtn, submit, edit);
            }
            else{
                tv_outPut.setText(answer);
                tts.speakOut(answer, "Done");
                tts.UtteranceProgress(sttBtn, submit, edit, answer, this);
            }
        }
        else{
            if(answer.contains("@")){
                String[] split = answer.split("@");
                tv_outPut.setText(split[0]);
                tts.speakOut(split[0], "default");
                for(String s : split){
                    tem.add(s);
                }
                tem.remove(0);
            }
            else{
                tv_outPut.setText(answer);
                tts.speakOut(answer, "default");
            }

            if(extra_ans.contains("@")){
                String[] split = extra_ans.split("@");
                for(String s : split){
                    tem.add(s);
                }
            }
            else{
                tem.add(extra_ans);
            }
            tts.UtteranceProgress(tem, "continue", tv_outPut, sttBtn, submit, edit);
        }
    }

    public String sendDataAndGetResult (String answer) {
        String openApiURL = "https://builder.pingpong.us/api/builder/6303553ce4b069adbd03156f/integration/v0.2/custom/{sessionId}";
        String accessKey = "Basic a2V5OmM3ODRlOTMwZGY1ZTlmYjMxZjFhMWMyYmFmYTNmYTMz";

        Gson gson = new Gson();

        java.util.Map<String, Object> request = new HashMap<>();
        Map<String, Object> req = new HashMap<>();

        req.put("query", answer);
        request.put("request", req);

        URL url;
        String responBody;

        try {
            url = new URL(openApiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", accessKey); // ""안에 apikey를 입력
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(gson.toJson(request).getBytes("UTF-8"));
            wr.flush();
            wr.close();

            InputStream is = new BufferedInputStream(con.getInputStream());
            responBody = readStream(is);
            Log.d("확인", "원본 : "+responBody);
            return responBody;
        } catch (Throwable t) {
            return "ERROR: " + t.toString();
        }
    }
    private static String readStream(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }

    @Override
    public void onBackPressed() {
        long backBtnTime = 0;
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if (0 <= gapTime && 2000 >= gapTime) {
            tts.Destroy();
            stt.Destroy();

            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);

            finish();
        } else {
            backBtnTime = curTime;
            Toast.makeText(this, "지금 나가시면 진행된 검사가 저장되지 않습니다.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop(){
        tts.isStopUtt = true;
        super.onStop();
        tts.Stop();
        stt.Stop();
    }

    @Override
    protected void onStart(){
        super.onStart();
        tts.isStopUtt = false;
        edit.setHint("답변이 여기에 나타납니다.");
        edit.setText("");
        tv_outPut.setText("안녕하세요!\n같이 대화해요!");
        sttBtn.setEnabled(true);
        submit.setEnabled(true);
        edit.setEnabled(true);
        tts.speakOut(tv_outPut.getText().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stt.Destroy();
        tts.Destroy();
    }
}