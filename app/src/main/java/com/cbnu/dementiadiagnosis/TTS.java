package com.cbnu.dementiadiagnosis;

import static android.speech.tts.Voice.QUALITY_HIGH;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.speech.tts.Voice;
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
    boolean isSpeaking = false;

    public TTS(Context context, TextToSpeech.OnInitListener listener){
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, null);
        tts = new TextToSpeech(context, listener);
    }

    public void UtteranceProgress(){
        if(!isStopUtt){
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String s) {
                    isSpeaking = true;
                }

                @Override
                public void onDone(String s) {
                    isSpeaking = false;
                }

                @Override
                public void onError(String s) {

                }
            });
        }
    }
    public void UtteranceProgress(String say, ImageButton sttButt, ImageButton submit){
        if(!isStopUtt){
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String s) {
                    isSpeaking = true;
                }

                @Override
                public void onDone(String s) {
                    isSpeaking = false;
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
    public void UtteranceProgress(String say, int time, ImageButton sttButt, ImageButton submit){
        if(!isStopUtt){
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String s) {
                    isSpeaking = true;
                }

                @Override
                public void onDone(String s) {
                    isSpeaking = false;
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
    public void UtteranceProgress(List<String> say, String id, ImageButton sttButt, ImageButton submit){
        if(!isStopUtt){
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                int i = 0;
                @Override
                public void onStart(String s) {
                    isSpeaking = true;
                }

                @Override
                public void onDone(String s) {
                    isSpeaking = false;
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
    public void UtteranceProgress(List<String> say, String id, ImageButton sttButt, ImageButton submit, EditText answer){
        if(!isStopUtt){
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                int i = 0;
                @Override
                public void onStart(String s) {
                    isSpeaking = true;
                }

                @Override
                public void onDone(String s) {
                    isSpeaking = false;
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
                                answer.setEnabled(true);
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
    public void UtteranceProgress(List<String> say, String id, int[] times, ImageButton sttButt, ImageButton submit){
        if(!isStopUtt){
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                int i = 0;

                @Override
                public void onStart(String s) {
                    isSpeaking = true;
                }

                @Override
                public void onDone(String s) {
                    isSpeaking = false;
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

    public void UtteranceProgress(List<String> say, String id, int[] times, ImageButton submit){
        if(!isStopUtt){
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                int i = 0;

                @Override
                public void onStart(String s) {
                    isSpeaking = true;
                }

                @Override
                public void onDone(String s) {
                    isSpeaking = false;
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
    public void UtteranceProgress(List<String> say, String id, TextView text, ImageButton sttButt, ImageButton submit){
        if(!isStopUtt){
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                int i = 0;
                @Override
                public void onStart(String s) {
                    isSpeaking = true;
                }

                @Override
                public void onDone(String s) {
                    isSpeaking = false;
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
    public void UtteranceProgress(List<String> say, String id, TextView text, ImageButton submit){
        if(!isStopUtt){
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                int i = 0;
                @Override
                public void onStart(String s) {
                    isSpeaking = true;
                }

                @Override
                public void onDone(String s) {
                    isSpeaking = false;
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
    public void UtteranceProgress(List<String> say, String id, TextView text){
        if(!isStopUtt){
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                int i = 0;
                @Override
                public void onStart(String s) {
                    isSpeaking = true;
                }

                @Override
                public void onDone(String s) {
                    isSpeaking = false;
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
    public void UtteranceProgress(List<String> say, String id, int[] times, TextView text, ImageButton sttButt, ImageButton submit){
        if(!isStopUtt){
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                int i = 0;

                @Override
                public void onStart(String s) {
                    isSpeaking = true;
                }

                @Override
                public void onDone(String s) {
                    isSpeaking = false;
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
    public void UtteranceProgress(List<String> say, String id, int[] times, TextView text, EditText ans, MainSTT stt){
        if(!isStopUtt){
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                int i = 0;

                @Override
                public void onStart(String s) {
                    isSpeaking = true;
                }

                @Override
                public void onDone(String s) {
                    isSpeaking = false;
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
                                            stt.start_STT();
                                            i++;
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    stt.start_STT();
                                                    ans.setEnabled(true);
                                                    text.setText("그만!");
                                                    speakOut("그만!", "Done");
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
    public void UtteranceProgress(List<String> say, String id, int[] times, TextView text, EditText ans, MainSTT stt, int playTime){
        if(!isStopUtt){
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                int i = 0;

                @Override
                public void onStart(String s) {
                    isSpeaking = true;
                }

                @Override
                public void onDone(String s) {
                    isSpeaking = false;
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
                                            stt.start_STT();
                                            i++;
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    stt.start_STT();
                                                    ans.setEnabled(true);
                                                    text.setText("그만!");
                                                    speakOut("그만!", "Done");
                                                }
                                            }, playTime);
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
    public void speakOut(String say, String id, int time) {
        CharSequence text = say;
        tts.setPitch((float) 1.2);
        tts.setSpeechRate((float) 1);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,id);
            }
        }, time);

    }

    public void speakOut(String say, int time) {
        CharSequence text = say;
        tts.setPitch((float) 1.2);
        tts.setSpeechRate((float) 1);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tts.speak(text,TextToSpeech.QUEUE_FLUSH,null, "Done");
            }
        }, time);
        UtteranceProgress();
    }

    public void speakOut(String say, String id) {
        CharSequence text = say;
        tts.setPitch((float) 1.2);
        tts.setSpeechRate((float) 1);
        tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,id);
    }

    public void speakOut(String say) {
        CharSequence text = say;
        tts.setPitch((float) 1.2);
        tts.setSpeechRate((float) 1);
        tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,"Done");
        UtteranceProgress();
    }

    public boolean IsTalking(){
        if(isSpeaking) return true;
        else return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onInit(int status, String say) {
        if (status == TextToSpeech.SUCCESS) {
            Voice voiceobj = new Voice("it-it-x-kda#male_2-local",
                    Locale.getDefault(), 500, 1, false, null);
            tts.setVoice(voiceobj);
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
            Voice voiceobj = new Voice("it-it-x-kda#male_2-local",
                    Locale.getDefault(), 500, 1, false, null);
            tts.setVoice(voiceobj);
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
    public void onInit(int status, String say, String id, int time) {
        if (status == TextToSpeech.SUCCESS) {
            Voice voiceobj = new Voice("it-it-x-kda#male_2-local",
                    Locale.getDefault(), 500, 1, false, null);
            tts.setVoice(voiceobj);
            int result = tts.setLanguage(Locale.KOREA);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                speakOut(say, id, time);
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    public void Stop(){
        if(tts!=null){
            isSpeaking = false;
            isStopUtt = true;
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
