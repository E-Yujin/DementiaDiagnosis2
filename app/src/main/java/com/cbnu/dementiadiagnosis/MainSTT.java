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
import android.util.Log;
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
import java.util.LinkedList;
import java.util.Map;

import questions.fluency;

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
    int lenSpeech = 0;
    public boolean isRecording = false;
    boolean forceStop = false;
    boolean isStop_state = false;

    public static final String PREFS_NAME = "prefs";
    private static final String MSG_KEY = "status";

    private boolean voiceReconize = false;
    private int startingIndex = -1; // 녹음 시작 인덱스
    private int endIndex = -1;
    private int cnt = 0;// 카운터

    private short[] buffer = null;

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
        if (isRecording) {
            forceStop = true;
        } else {
            try {
                new Thread(new Runnable() {
                    public void run() {
                        Log.d("record", "녹음 시작!");
                        SendMessage("듣는 중...", 1);
                        try {
                            recordSpeech();
                        } catch (RuntimeException e) {
                            SendMessage(e.getMessage(), 3);
                            return;
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
                    result.setText("");
                    textView.setText(v);
                    sttBtn.setText("말했어요!");
                    submit.setEnabled(false);
                    break;
                // 목소리가 인식되었음(버튼 또는 max time)
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
                    sttBtn.setText("다시 말하기");
                    submit.setEnabled(true);
                    break;
                // 인식이 정상적으로 종료되었음 (thread내에서 exception포함)
                case 5:
                    textView.setText("인식 완료!");
                    if(!SplitResult().equals("ASR_NOTOKEN")){
                        String s;
                        s = result.getText().toString() + SplitResult() + " ";
                        result.setText(s);
                    }
                    sttBtn.setEnabled(true);
                    sttBtn.setText("그만 말하기");
                    submit.setEnabled(false);
                    break;
                case 6:
                    if(!result.getText().toString().equals("")){
                        String str = result.getText().toString().substring(0,
                                result.getText().toString().length() - 1);
                        textView.setText("\"" + str + "\"" +
                                " 라고 말씀하신 게 맞나요?\n\n" +
                                "맞으시면 아래 파란 상자를 눌러주세요.\n"+
                                "아니라면 보라색 상자를 눌러 다시 말씀해주세요.");
                    }
                    else{
                        textView.setText("잘 알아듣지 못했어요.\n다시 말씀해주세요!");
                    }
                    sttBtn.setEnabled(true);
                    sttBtn.setText("다시 말하기");
                    submit.setEnabled(true);
                    break;
                case 7:
                    textView.setText("인식 중...");
                    break;
                case 8:
                    textView.setText("조금 더 천천히 말씀해주세요.");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public String SplitResult(){
        String splited[] = result_text.split("\"");
        splited[7] = splited[7].replace(".","");
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
                    AudioFormat.ENCODING_PCM_16BIT)*5;
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
                forceStop = false;
                isRecording = true;
                audio.startRecording();
                Thread threadRecog = new Thread(new Runnable() {
                    public void run() {
                        Recordingloop(audio, bufferSize);
                        isRecording = false;
                        SendMessage("말하기 완료.", 6);
                        audio.stop();
                        audio.release();
                        Log.d("record_확인", "오디오 릴리즈됨.");
                    }
                });
                threadRecog.start();
                try {
                    threadRecog.join(60000);
                    if (threadRecog.isAlive()) {
                        threadRecog.interrupt();
                        SendMessage("1분 동안 말씀하지 않아 인식을 종료합니다.\n" +
                                "'말하기'를 다시 누르고 말씀해주세요.", 4);
                    }
                } catch (InterruptedException e) {
                    SendMessage("Interrupted", 4);
                }
            }
        } catch(Throwable t) {
            throw new RuntimeException(t.toString());
        }
    }

    public void Recordingloop (AudioRecord audio, int bufferSize){
        int total;
        int level;
        LinkedList<short[]> recData = new LinkedList<short[]>();
        while (!forceStop) {
            short[] inBuffer = new short[bufferSize];
            int ret = audio.read(inBuffer, 0, bufferSize);
            total = 0;
            for (int i = 0; i < ret; i++) {
                total += Math.abs(inBuffer[i]);
            }
            recData.add(inBuffer);
            level = (int) (total / ret);
            if (voiceReconize == false) {
                if (level > 300) {
                    if (cnt == 0)
                        startingIndex = recData.size();
                    cnt++;
                }

                if (cnt > 0) {
                    SendMessage("목소리 감지!", 2);
                    Log.d("record_n", "목소리 감지!");
                    cnt = 0;
                    voiceReconize = true;
                    // level 값이 처음으로 1000 값을 넘은시점으로부터 15 만큼 이전부터 플레이 시점 설정
                    // 시작하는 목소리가 끊겨 들리지 않게 하기 위하여 -15
                    startingIndex -= 15;
                    if (startingIndex < 0)
                        startingIndex = 0;
                }
            }

            if (voiceReconize == true) {
                // 목소리가 끝나고 500이하로 떨어진 상태가 40이상 지속된 경우
                // 더이상 말하지 않는것으로 간주.. 레벨 체킹 끝냄
                if (level < 200) {
                    cnt++;
                }
                // 도중에 다시 소리가 커지는 경우 잠시 쉬었다가 계속 말하는 경우이므로 cnt 값은 0
                if (level > 300) {
                    cnt = 0;
                }
                // endIndex 를 저장하고 레벨체킹을 끝냄
                if (cnt > 0) {
                    voiceReconize = false;
                    cnt = 0;
                    endIndex = recData.size();

                    Log.d("record_n", "녹음 완료!");
                    SendMessage("녹음 완료!", 7);
                    Log.d("record_s", Integer.toString(startingIndex));
                    Log.d("record_s", Integer.toString(endIndex));

                    Thread PR_Thread = new Thread(new Runnable() {
                        @Override
                        public void run(){
                            int flag;
                            flag = PrintResult(endIndex, startingIndex, recData);
                            if(flag == -1){
                                SendMessage("ERROR: 429", 8);
                            }
                            else SendMessage("들었어요!", 5);

                            recData.clear();
                        }
                    });

                    PR_Thread.start();

                }
            }
            Log.d("record", Integer.toString(level));
        }
    }

    public int PrintResult(int endIndex, int startingIndex, LinkedList<short[]> recData){
        short[] buffer;
        int lenSpeech = 0;
        byte [] speechData = new byte [maxLenSpeech * 2];

        for (int i = startingIndex; i < endIndex; i++) {
            buffer = recData.get(i);
            for (int j = 0; j < buffer.length; j++) {
                if (lenSpeech >= maxLenSpeech) {
                    forceStop = true;
                    break;
                }
                speechData[lenSpeech * 2] = (byte) (buffer[j] & 0x00FF);
                speechData[lenSpeech * 2 + 1] = (byte) ((buffer[j] & 0xFF00) >> 8);
                lenSpeech++;
                Log.d("record_i", Integer.toString(lenSpeech));
            }
        }

        Log.d("record_d", sendDataAndGetResult(speechData, lenSpeech));
        result_text = sendDataAndGetResult(speechData, lenSpeech);

        if(result_text.contains("ERROR") && result_text.contains("429")) return -1;
        if(result_text.contains("-1")) return -1;
        else return 0;
    }

    public String sendDataAndGetResult (byte[] speechData, int lenSpeech) {
        Log.d("record_n", "STT 작동!");
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
