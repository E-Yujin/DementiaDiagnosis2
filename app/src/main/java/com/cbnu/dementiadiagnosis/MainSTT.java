package com.cbnu.dementiadiagnosis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainSTT{

    private TTS tts;

    Button sttBtn;
    Button submit;
    EditText result;
    TextView textView;
    TextView question;

    String result_text;

    final int PERMISSION = 1;

    int maxLenSpeech = 16000 * 45;
    byte [] speechData = new byte [maxLenSpeech * 2];
    int lenSpeech = 0;
    public boolean isRecording = false;
    boolean forceStop = false;
    boolean isStop_state = false;

    public static final String PREFS_NAME = "prefs";
    private static final String MSG_KEY = "status";

    public MainSTT (AppCompatActivity context, EditText editText, TextView announce,
                    TextView quiz, Button Btn, Button sub, TTS talk){
        if ( Build.VERSION.SDK_INT >= 23 ){ // 퍼미션 체크
            ActivityCompat.requestPermissions(
                    context, new String[]{Manifest.permission.INTERNET,
                            Manifest.permission.RECORD_AUDIO},PERMISSION);
        }
        result = editText;
        textView = announce;
        question = quiz;
        sttBtn = Btn;
        submit = sub;
        tts = talk;
    }

    public void start_STT() {
        tts.Stop();
        if (isRecording) {
            forceStop = true;
        } else {
            try {
                new Thread(new Runnable() {
                    public void run() {
                        SendMessage("듣는 중...\n\n" +
                                "말씀이 끝나셨다면 '말했어요!'를 눌러주세요!", 1);
                        try {
                            recordSpeech();
                            SendMessage("인식 중...", 2);
                        } catch (RuntimeException e) {
                            SendMessage(e.getMessage(), 3);
                            return;
                        }
                        if(forceStop && isStop_state) {
                            return;
                        }
                        else{
                            Thread threadRecog = new Thread(new Runnable() {
                                public void run() {
                                    result_text = sendDataAndGetResult();
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
                forceStop = false;
                isRecording = false;
            }
        }
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
                    submit.setEnabled(false);
                    break;
                // 녹음이 정상적으로 종료되었음(버튼 또는 max time)
                case 2:
                    textView.setText(v);
                    sttBtn.setEnabled(false);
                    submit.setEnabled(false);
                    break;
                // 녹음이 비정상적으로 종료되었음(마이크 권한 등)
                case 3:
                    textView.setText(v);
                    sttBtn.setText("말하기");
                    submit.setEnabled(true);
                    break;
                // 인식이 비정상적으로 종료되었음(timeout 등)
                case 4:
                    textView.setText(v);
                    sttBtn.setEnabled(true);
                    sttBtn.setText("말하기");
                    submit.setEnabled(true);
                    break;
                // 인식이 정상적으로 종료되었음 (thread내에서 exception포함)
                case 5:
                    result.setText(SplitResult());
                    if(SplitResult().equals("ASR_NOTOKEN")){
                        result.setText("");
                        textView.setText("혹시 아무것도 말하지 않으셨나요?\n\n" +
                                "보라색 상자를 눌러 다시 말씀해주세요.");
                    }
                    else{
                        textView.setText("\"" + result.getText() + "\"" +
                                " 라고 말씀하신 게 맞나요?\n\n" +
                                "맞으시면 아래 파란 상자를 눌러주세요.\n"+
                                "아니라면 보라색 상자를 눌러 다시 말씀해주세요.");
                    }
                    tts.speakOut(textView.getText().toString(),"Done");
                    sttBtn.setEnabled(true);
                    sttBtn.setText("다시 말하기");
                    submit.setEnabled(true);

                    break;
            }
            super.handleMessage(msg);
        }
    };

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

    private static String readStream(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }

    public void recordSpeech() throws RuntimeException {
        try {
            int bufferSize = AudioRecord.getMinBufferSize(
                    16000, // sampling frequency
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);
            AudioRecord audio = new AudioRecord(
                    MediaRecorder.AudioSource.VOICE_RECOGNITION,
                    16000, // sampling frequency
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSize);
            lenSpeech = 0;
            if (audio.getState() != AudioRecord.STATE_INITIALIZED) {
                throw new RuntimeException("ERROR: Failed to initialize audio device. " +
                        "Allow app to access microphone");
            }
            else {
                short [] inBuffer = new short [bufferSize];
                forceStop = false;
                isRecording = true;
                audio.startRecording();

                while (!forceStop) {
                    int ret = audio.read(inBuffer, 0, bufferSize);
                    for (int i = 0; i < ret ; i++ ) {
                        if (lenSpeech >= maxLenSpeech) {
                            forceStop = true;
                            break;
                        }
                        speechData[lenSpeech*2] = (byte)(inBuffer[i] & 0x00FF);
                        speechData[lenSpeech*2+1] = (byte)((inBuffer[i] & 0xFF00) >> 8);
                        lenSpeech++;
                    }
                }

                audio.stop();
                audio.release();
                isRecording = false;
            }
        } catch(Throwable t) {
            throw new RuntimeException(t.toString());
        }
    }

    public String sendDataAndGetResult () {
        String openApiURL = "http://aiopen.etri.re.kr:8000/WiseASR/Recognition";
        String accessKey = "140889f3-77c9-4522-a5af-5f8a3898b740";
        String languageCode;
        String audioContents;

        Gson gson = new Gson();
        languageCode = "korean";

        Map<String, Object> request = new HashMap<>();
        Map<String, String> argument = new HashMap<>();

        audioContents = Base64.encodeToString(
                speechData, 0, lenSpeech*2, Base64.NO_WRAP);

        argument.put("language_code", languageCode);
        argument.put("audio", audioContents);

        request.put("access_key", accessKey);
        request.put("argument", argument);

        URL url;
        Integer responseCode;
        String responBody;
        try {
            url = new URL(openApiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(gson.toJson(request).getBytes("UTF-8"));
            wr.flush();
            wr.close();

            responseCode = con.getResponseCode();
            if ( responseCode == 200 ) {
                InputStream is = new BufferedInputStream(con.getInputStream());
                responBody = readStream(is);
                return responBody;
            }
            else
                return "ERROR: " + Integer.toString(responseCode);
        } catch (Throwable t) {
            return "ERROR: " + t.toString();
        }
    }

    public void Stop(){
        if(isRecording){
            forceStop = true;
            isStop_state = true;
            sttBtn.setEnabled(true);
            sttBtn.setText("말하기");
        }
    }

    public void Destroy(){
        if(isRecording){
            forceStop = true;
        }
    }
}
