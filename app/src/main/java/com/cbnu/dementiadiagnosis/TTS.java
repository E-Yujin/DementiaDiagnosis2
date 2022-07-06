package com.cbnu.dementiadiagnosis;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.Locale;

import QuizPage.fluency_Page;

public class TTS {
    private TextToSpeech tts;
    private final Bundle params = new Bundle();
    Handler handler = new Handler();
    public boolean isStopUtt = false;
    ImageButton sttButt;
    Button submit;

    public TTS(Context context, TextToSpeech.OnInitListener listener, ImageButton sttB, Button sub){
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, null);
        tts = new TextToSpeech(context, listener);
        sttButt = sttB;
        submit = sub;
    }

    public TTS(Context context, TextToSpeech.OnInitListener listener, Button sub){
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, null);
        tts = new TextToSpeech(context, listener);
        submit = sub;
    }

    public void UtteranceProgress(String say){
        if(!isStopUtt){
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String s) {

                }

                @Override
                public void onDone(String s) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!s.contains("Done"))
                                speakOut(say,"Done");
                            else {
                                sttButt.setEnabled(true);
                                submit.setEnabled(true);
                            }
                        }
                    }, 500);
                }

                @Override
                public void onError(String s) {

                }
            });
        }
    }
    // 한 문장만 말할 때
    public void UtteranceProgress(String say, int time){
        if(!isStopUtt){
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String s) {

                }

                @Override
                public void onDone(String s) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!s.contains("Done"))
                                speakOut(say,"Done");
                            else {
                                sttButt.setEnabled(true);
                                submit.setEnabled(true);
                            }
                        }
                    }, time);
                }

                @Override
                public void onError(String s) {

                }
            });
        }
    }
    // 한 문장을 말하는데 수동으로 텀을 조절하고 싶을 때
    public void UtteranceProgress(List<String> say, String id){
        if(!isStopUtt){
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                int i = 0;
                @Override
                public void onStart(String s) {

                }

                @Override
                public void onDone(String s) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!s.contains("Done")){
                                if(i == say.size()-1){
                                    speakOut(say.get(i), "Done");
                                }
                                else speakOut(say.get(i), id);
                                i++;
                            }
                            else{
                                sttButt.setEnabled(true);
                                submit.setEnabled(true);
                            }
                        }
                    }, 500);
                }

                @Override
                public void onError(String s) {

                }
            });
        }
    }
    // 여러 문장을 말할 때
    public void UtteranceProgress(List<String> say, String id, int[] times){
        if(!isStopUtt){
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                int i = 0;

                @Override
                public void onStart(String s) {

                }

                @Override
                public void onDone(String s) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!s.contains("Done")){
                                if(i == say.size()-1){
                                    speakOut(say.get(i), "Done");
                                }
                                else speakOut(say.get(i), id);
                                i++;
                            }
                            else{
                                sttButt.setEnabled(true);
                                submit.setEnabled(true);
                            }
                        }
                    }, times[i]);
                }

                @Override
                public void onError(String s) {

                }
            });
        }
    }
    // 여러 문장을 말하는데 수동으로 텀을 조절하고 싶을 때
    public void UtteranceProgress(List<String> say, String id, TextView text){
        if(!isStopUtt){
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                int i = 0;
                @Override
                public void onStart(String s) {

                }

                @Override
                public void onDone(String s) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!s.contains("Done")){
                                if(i == say.size()-1){
                                    speakOut(say.get(i), "Done");
                                }
                                else speakOut(say.get(i), id);
                                text.setText(say.get(i));
                                i++;
                            }
                            else{
                                sttButt.setEnabled(true);
                                submit.setEnabled(true);
                            }
                        }
                    }, 500);
                }

                @Override
                public void onError(String s) {

                }
            });
        }
    }
    // 여러 문장을 말하며 텍스트뷰 변경이 필요할 때
    public void UtteranceProgress(List<String> say, String id, int[] times, TextView text){
        if(!isStopUtt){
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                int i = 0;

                @Override
                public void onStart(String s) {

                }

                @Override
                public void onDone(String s) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!s.contains("Done")){
                                if(i == say.size()-1){
                                    speakOut(say.get(i), "Done");
                                }
                                else speakOut(say.get(i), id);
                                text.setText(say.get(i));
                                i++;
                            }
                            else{
                                sttButt.setEnabled(true);
                                submit.setEnabled(true);
                            }
                        }
                    }, times[i]);
                }

                @Override
                public void onError(String s) {

                }
            });
        }
    }
    // 여러 문장을 말하는데 텍스트뷰 변경이 필요하고 수동으로 텀을 조절하고 싶을 때
    public void UtteranceProgress(List<String> say, String id, int[] times, TextView text, ImageButton btn, EditText ans){
        if(!isStopUtt){
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                int i = 0;

                @Override
                public void onStart(String s) {

                }

                @Override
                public void onDone(String s) {
                    if(s.contains("Done")) return;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!s.contains("Done")){
                                if(i < say.size()){
                                    text.setText(say.get(i));
                                    speakOut(say.get(i), id);
                                    i++;
                                }
                                if(i == say.size()){
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            btn.callOnClick();
                                            i++;
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    btn.callOnClick();
                                                    text.setText("그만!");
                                                    speakOut("그만!", "Done");
                                                    ans.setEnabled(true);
                                                }
                                            }, 60000);
                                        }
                                    }, 1000);
                                }
                            }
                            else{
                                return;
                            }
                        }
                    }, times[i]);
                }

                @Override
                public void onError(String s) {

                }
            });
        }
    }
    // 여러 문장을 말하는데 텍스트뷰 변경이 필요하고 수동으로 텀을 조절하고 싶을 때

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void speakOut(String say, String id) {
        CharSequence text = say;
        tts.setPitch((float) 0.6);
        tts.setSpeechRate((float) 1);
        tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,id);
    }

    public void speakOut(String say) {
        CharSequence text = say;
        tts.setPitch((float) 0.6);
        tts.setSpeechRate((float) 1);
        tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,"Done");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onInit(int status, String say) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.KOREA);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                speakOut(say);
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }
    public void onInit(int status, String say, String id) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.KOREA);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                speakOut(say, id);
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    public void Stop(){
        if(tts!=null){
            tts.stop();
        }
    }

    public void Destroy(){
        if(tts!=null){
            tts.stop();
            tts.shutdown();
            tts=null;
        }
    }
}
