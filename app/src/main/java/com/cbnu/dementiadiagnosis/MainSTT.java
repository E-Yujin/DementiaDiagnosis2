package com.cbnu.dementiadiagnosis;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

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

public class MainSTT{

    private TTS tts;

    ImageButton sttBtn;
    ImageButton submit;
    EditText result;
    TextView textView;
    TextView question;

    int start = 350;
    int end = 200;
    int speed = 0;


    String result_text;

    final int PERMISSION = 1;

    int maxLenSpeech = 16000 * 45;
    int lenSpeech = 0;
    public boolean isRecording = false;
    public boolean isListening = false;
    boolean forceStop = false;
    boolean isStop_state = false;

    public static final String PREFS_NAME = "prefs";
    private static final String MSG_KEY = "status";

    private boolean voiceReconize = false;
    private int startingIndex = -1; // 녹음 시작 인덱스
    private int endIndex = -1;
    private int cnt = 0;// 카운터
    boolean transforming = false;
    boolean isRecognize = false;
    boolean isAnalysing = false;
    boolean isTooFast = false;
    public boolean isFluency = false;
    MainSTT stt;
    AppCompatActivity contx;
    FragmentActivity Fcontx;

    private short[] buffer = null;

    public MainSTT (AppCompatActivity context, EditText editText, TextView announce,
                    TextView quiz, ImageButton Btn, ImageButton sub, TTS talk){
        if ( Build.VERSION.SDK_INT >= 23 ){ // 퍼미션 체크
            ActivityCompat.requestPermissions(
                    context, new String[]{Manifest.permission.INTERNET,
                            Manifest.permission.RECORD_AUDIO},PERMISSION);
        }
        contx = context;
        result = editText;
        textView = announce;
        question = quiz;
        sttBtn = Btn;
        submit = sub;
        tts = talk;

    }

    public MainSTT (AppCompatActivity context, EditText editText,
                    TextView quiz, ImageButton Btn, ImageButton sub, TTS talk){
        if ( Build.VERSION.SDK_INT >= 23 ){ // 퍼미션 체크
            ActivityCompat.requestPermissions(
                    context, new String[]{Manifest.permission.INTERNET,
                            Manifest.permission.RECORD_AUDIO},PERMISSION);
        }
        contx = context;
        result = editText;
        question = quiz;
        sttBtn = Btn;
        submit = sub;
        tts = talk;
    }

    public MainSTT (FragmentActivity context,EditText editText,
                    ImageButton Btn, int s, int e, int sp){
        if ( Build.VERSION.SDK_INT >= 23 ){ // 퍼미션 체크
            ActivityCompat.requestPermissions(
                    context, new String[]{Manifest.permission.INTERNET,
                            Manifest.permission.RECORD_AUDIO},PERMISSION);
        }
        Fcontx = context;
        result = editText;
        sttBtn = Btn;
        start = s;
        end = e;
        speed = sp;
    }

    public MainSTT (AppCompatActivity context, EditText editText, TextView announce,
                    TextView quiz, ImageButton Btn, ImageButton sub, TTS talk, int s, int e, int v){
        if ( Build.VERSION.SDK_INT >= 23 ){ // 퍼미션 체크
            ActivityCompat.requestPermissions(
                    context, new String[]{Manifest.permission.INTERNET,
                            Manifest.permission.RECORD_AUDIO},PERMISSION);
        }
        contx = context;
        result = editText;
        textView = announce;
        question = quiz;
        sttBtn = Btn;
        submit = sub;
        tts = talk;
        start = s;
        end = e;
        speed = v;
    }

    public MainSTT (AppCompatActivity context, EditText editText,
                    TextView quiz, ImageButton Btn, ImageButton sub, TTS talk, int s, int e, int v){
        if ( Build.VERSION.SDK_INT >= 23 ){ // 퍼미션 체크
            ActivityCompat.requestPermissions(
                    context, new String[]{Manifest.permission.INTERNET,
                            Manifest.permission.RECORD_AUDIO},PERMISSION);
        }
        contx = context;
        result = editText;
        question = quiz;
        sttBtn = Btn;
        submit = sub;
        tts = talk;
        start = s;
        end = e;
        speed = v;
    }

    public void setStart(int s){
        start = s;
    }
    public void setEnd(int e){
        end = e;
    }
    public void setSpeed(int sp){
        speed = sp;
    }
    public int getStart(){return start;}
    public int getEnd(){return end;}
    public int getSpeed(){return speed;}

    public void start_STT() {
        if (isRecording) {
            sttBtn.setSelected(false);
            Log.d("record_확인", "오디오 릴리즈!");
            forceStop = true;
        } else {
            try {
                new Thread(new Runnable() {
                    public void run() {
                        Log.d("record_확인", "녹음 시작!");
                        sttBtn.setSelected(true);
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
                isListening = false;
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
                    result.setHint("녹음되고 있어요!");
                    //textView.setText(v);
                    isListening = true;
                    isRecognize = false;
                    isAnalysing= false;
                    isTooFast = false;
                    if(submit != null) submit.setEnabled(false);
                    break;
                // 목소리가 인식되었음(버튼 또는 max time)
                case 2:
                    //textView.setText(v);
                    result.setHint("소리를 인식했어요!");
                    isListening = false;
                    isRecognize = true;
                    isAnalysing= false;
                    isTooFast = false;
                    sttBtn.setEnabled(false);
                    if(submit != null) submit.setEnabled(false);
                    break;
                // 녹음이 비정상적으로 종료되었음(마이크 권한 등)
                case 3:
                    result.setHint("녹음을 할 수 없어요.");
                    //textView.setText(v);
                    isListening = false;
                    isRecognize = false;
                    isAnalysing= false;
                    isTooFast = true;
                    if(submit != null) submit.setEnabled(true);
                    break;
                // 인식이 비정상적으로 종료되었음(timeout 등)
                case 4:
                    result.setHint("다시 말씀해주세요.");
                    //textView.setText(v);
                    isListening = false;
                    isRecognize = false;
                    isAnalysing= false;
                    isTooFast = true;
                    sttBtn.setEnabled(true);
                    if(submit != null) submit.setEnabled(true);
                    break;
                // 인식이 정상적으로 종료되었음 (thread내에서 exception포함)
                case 5:
                    //textView.setText("인식 완료!");
                    result.setHint("답변이 여기 나타납니다.");
                    if(isRecording) {
                        isListening = true;
                        isRecognize = false;
                        isAnalysing = false;
                        isTooFast = false;
                        if (isFluency) {
                            sttBtn.setEnabled(false);
                            if(submit != null) submit.setEnabled(false);
                        } else {
                            sttBtn.setEnabled(true);
                            if(submit != null) submit.setEnabled(true);
                        }
                    }
                    else{ // STT가 종료되었는데도 인식은 종료되지 않은 경우
                        result.setHint("답변이 여기 나타납니다.");
                        isListening = false;
                        isRecognize = false;
                        isAnalysing = false;
                        isTooFast = false;
                        if (isFluency) {
                            sttBtn.setEnabled(false);
                            if(submit != null) submit.setEnabled(true);
                        } else {
                            sttBtn.setEnabled(true);
                            if(submit != null) submit.setEnabled(true);
                        }
                    }

                    if (!SplitResult().equals("ASR_NOTOKEN")) {
                        String s;
                        s = result.getText().toString() + SplitResult() + " ";
                        result.setText(s);
                    }
                    break;
                case 6: // 녹음을 정상적으로 마침
                    result.setHint("답변이 여기 나타납니다.");
                    if(!result.getText().toString().equals("")){
                        String str = result.getText().toString().substring(0,
                                result.getText().toString().length() - 1);
                        /*textView.setText("\"" + str + "\"" +
                                " 라고 말씀하신 게 맞나요?\n\n" +
                                "맞으시면 아래 파란 상자를 눌러주세요.\n"+
                                "아니라면 보라색 상자를 눌러 다시 말씀해주세요.");*/
                    }
                    else{
                        //textView.setText("잘 알아듣지 못했어요.\n다시 말씀해주세요!");
                    }
                    isListening = false;
                    isRecognize = false;
                    isAnalysing= false;
                    isTooFast = false;
                    if(isFluency){
                        sttBtn.setEnabled(false);
                        if(submit != null) submit.setEnabled(true);
                    }
                    else{
                        sttBtn.setEnabled(true);
                        if(submit != null) submit.setEnabled(true);
                    }
                    break;
                case 7: // 텍스트 변환이 진행 중임.
                    //textView.setText("인식 중...");
                    result.setHint("무슨 말인지 파악하고 있어요!");
                    isListening = false;
                    isRecognize = false;
                    isAnalysing= true;
                    isTooFast = false;
                    break;
                case 8: // 에러
                    //textView.setText("조금 더 천천히 말씀해주세요.");
                    result.setHint("조금 더 천천히 말씀해주세요.");
                    isListening = false;
                    isRecognize = false;
                    isAnalysing= false;
                    isTooFast = true;
                    if(contx != null){
                        Toast.makeText(contx, "API 서버 연결이 불안정합니다.",
                                Toast.LENGTH_SHORT).show();
                    }
                    else if(Fcontx != null){
                        Toast.makeText(Fcontx, "API 서버 연결이 불안정합니다.",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 9: // 에러
                    //textView.setText("조금 더 천천히 말씀해주세요.");
                    result.setHint("조금 더 천천히 말씀해주세요.");
                    isListening = false;
                    isRecognize = false;
                    isAnalysing= false;
                    isTooFast = true;
                    if(contx != null){
                        Toast.makeText(contx, "조금 더 천천히 말씀해주세요!",
                                Toast.LENGTH_SHORT).show();
                    }
                    else if(Fcontx != null){
                        Toast.makeText(Fcontx, "조금 더 천천히 말씀해주세요!",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 10: // 에러
                    //textView.setText("조금 더 천천히 말씀해주세요.");
                    result.setHint("조금 더 천천히 말씀해주세요.");
                    isListening = false;
                    isRecognize = false;
                    isAnalysing= false;
                    isTooFast = true;
                    if(contx != null){
                        Toast.makeText(contx, "죄송하지만, 다시 한 번 말씀해주세요!",
                                Toast.LENGTH_SHORT).show();
                    }
                    else if(Fcontx != null){
                        Toast.makeText(Fcontx, "죄송하지만, 다시 한 번 말씀해주세요!",
                                Toast.LENGTH_SHORT).show();
                    }
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
                isListening = true;
                audio.startRecording();
                Thread threadRecog = new Thread(new Runnable() {
                    public void run() {
                        Recordingloop(audio, bufferSize);
                        sttBtn.setSelected(false);
                        isRecording = false;
                        isListening = false;
                        SendMessage("말하기 완료.", 6);
                        audio.stop();
                        audio.release();
                        Log.d("record_확인", "오디오 릴리즈됨.");
                    }
                });
                threadRecog.start();
                try {
                    threadRecog.join(120000);
                    if (threadRecog.isAlive()) {
                        threadRecog.interrupt();
                        SendMessage("2분 동안 말씀하지 않아 인식을 종료합니다.\n" +
                                "'말하기'를 다시 누르고 말씀해주세요.", 4);
                        forceStop = true;
                        sttBtn.setSelected(false);
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
                if (level > start) {
                    if (cnt == 0)
                        startingIndex = recData.size();
                    cnt++;
                }

                if (cnt > speed) {
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
                if (level < end) {
                    cnt++;
                }
                // 도중에 다시 소리가 커지는 경우 잠시 쉬었다가 계속 말하는 경우이므로 cnt 값은 0
                if (level > start) {
                    cnt = 0;
                }
                // endIndex 를 저장하고 레벨체킹을 끝냄
                if (cnt > speed) {
                    voiceReconize = false;
                    cnt = 0;
                    endIndex = recData.size();

                    Log.d("record_n", "녹음 완료!");
                    SendMessage("녹음 완료!", 7);
                    Log.d("record_s", Integer.toString(startingIndex));
                    Log.d("record_s", Integer.toString(endIndex));

                    Thread PR_Thread = new Thread(new Runnable() {
                        @Override
                        public synchronized void run(){
                            int flag;
                            flag = PrintResult(this, endIndex, startingIndex, recData);
                            if (flag == -1) {
                                SendMessage("ERROR", 8);
                            }
                            else if(flag == 429){
                                SendMessage("ERROR", 9);
                            }
                            else if(flag == 400){
                                SendMessage("ERROR", 10);
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

    public int PrintResult(Runnable context, int endIndex, int startingIndex, LinkedList<short[]> recData){
        short[] buffer;
        int lenSpeech = 0;
        byte [] speechData = new byte [maxLenSpeech * 2];

        Log.d("record_i", "short to byte");
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
            }
        }

            Log.d("record_d", sendDataAndGetResult(speechData, lenSpeech));
            result_text = sendDataAndGetResult(speechData, lenSpeech);
            if(result_text.contains("ERROR") && result_text.contains("429")) return 429;
            else if(result_text.contains("ERROR") && result_text.contains("400")) return 400;
            if(result_text.contains("-1") || result_text.contains("ERROR")) return -1;
            else return 0;

    }

    public String sendDataAndGetResult (byte[] speechData, int lenSpeech) {
        transforming = true;
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
                transforming = false;
                return responBody;
            }
            else{
                transforming = false;
                Log.e("MainSTT", "ERROR: " + responseCode);
                return "ERROR: " + Integer.toString(responseCode);
            }
        } catch (Throwable t) {
            transforming = false;
            Log.e("MainSTT", "ERROR: " + t.toString());
            return "ERROR: " + t.toString();
        }
    }

    public void Stop(){
        if(isRecording){
            forceStop = true;
            isStop_state = true;
            sttBtn.setEnabled(true);
            isListening = false;
        }
    }

    public void Destroy(){
        if(isRecording){
            forceStop = true;
        }
    }
}
