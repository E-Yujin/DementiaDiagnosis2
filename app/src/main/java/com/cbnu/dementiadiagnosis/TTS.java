package com.cbnu.dementiadiagnosis;

import static android.speech.tts.Voice.QUALITY_HIGH;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.List;
import java.util.Locale;

import QuizPage.fluency_Page;
import memoryQuiz.StartActivity;
import simpleTest.S_orientation;

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
    public void UtteranceProgress(ImageButton sttButt, ImageButton submit, EditText edit, String str, AppCompatActivity content){
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
                            if(str.contains("정규 검사로 바로 이동할게요!")){
                                Log.d("확인", "된다");
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(content, QuizHOME.class);
                                        content.startActivity(intent);
                                        content.finish();
                                    }
                                }, 500);
                            }
                            else if(str.contains("간이 검사로 바로 이동할게요!")){
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(content, S_orientation.class);
                                        content.startActivity(intent);
                                        content.finish();
                                    }
                                }, 500);
                            }
                            else if(str.contains("기억력 테스트로 바로 이동할게요!")){
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(content, StartActivity.class);
                                        content.startActivity(intent);
                                        content.finish();
                                    }
                                }, 500);
                            }
                            else{
                                sttButt.setEnabled(true);
                                submit.setEnabled(true);
                                edit.setEnabled(true);
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
    public void UtteranceProgress(ImageButton sttButt, ImageButton submit, EditText edit){
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
                            sttButt.setEnabled(true);
                            submit.setEnabled(true);
                            edit.setEnabled(true);
                        }
                    }, 500);
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
    // 한 문장만 말할 때, onInit함수랑 함께쓸 때
    public void UtteranceProgress(String say, int time, EditText answer, ImageButton sttButt, ImageButton submit){
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
                            answer.setEnabled(true);
                            sttButt.setEnabled(true);
                            submit.setEnabled(true);
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

    public void UtteranceProgress(List<String> say, String id, TextView text, ImageButton sttButt, ImageButton submit, EditText edit){
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
                                if(i >= say.size()-1){
                                    speakOut(say.get(i), "Done");
                                }
                                else speakOut(say.get(i), id);
                                text.setText(say.get(i));
                                i++;
                            }
                            else{
                                sttButt.setEnabled(true);
                                submit.setEnabled(true);
                                edit.setEnabled(true);
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

    public void UtteranceProgress(List<String> say, String id, TextView text, TextView title_text, TextView textView, ImageView mic, ImageView arrow, ImageView finger, AppCompatButton donknow){
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
                            AlphaAnimation alpha = new AlphaAnimation(0,1);
                            alpha.setDuration(400);
                            alpha.setRepeatCount(Animation.INFINITE);
                            alpha.setRepeatMode(Animation.REVERSE);

                            TranslateAnimation animLeft = new TranslateAnimation(
                                    Animation.RELATIVE_TO_PARENT,0.1f,
                                    Animation.RELATIVE_TO_PARENT, -0.1f,
                                    Animation.RELATIVE_TO_PARENT, 0,
                                    Animation.RELATIVE_TO_PARENT, 0);
                            animLeft.setDuration(600);
                            animLeft.setRepeatCount(Animation.INFINITE);
                            animLeft.setRepeatMode(Animation.START_ON_FIRST_FRAME);

                            TranslateAnimation animRight = new TranslateAnimation(
                                    Animation.RELATIVE_TO_PARENT,-0.1f,
                                    Animation.RELATIVE_TO_PARENT, 0.1f,
                                    Animation.RELATIVE_TO_PARENT, 0,
                                    Animation.RELATIVE_TO_PARENT, 0);
                            animRight.setDuration(600);
                            animRight.setRepeatCount(Animation.INFINITE);
                            animRight.setRepeatMode(Animation.START_ON_FIRST_FRAME);

                            if(!s.contains("Done")){
                                if(i == say.size()-1){
                                    speakOut(say.get(i), "Done");
                                }
                                else speakOut(say.get(i), id);
                                text.setText(say.get(i));
                                if(say.get(i).contains("질문")){
                                    title_text.startAnimation(alpha);

                                    finger.setVisibility(View.INVISIBLE);
                                    arrow.setVisibility(View.INVISIBLE);
                                    mic.clearAnimation();
                                    arrow.clearAnimation();
                                    finger.clearAnimation();
                                    textView.clearAnimation();
                                    donknow.clearAnimation();
                                }
                                else if(say.get(i).contains("마이크")) {
                                    mic.startAnimation(alpha);

                                    finger.setVisibility(View.INVISIBLE);
                                    arrow.setVisibility(View.INVISIBLE);
                                    title_text.clearAnimation();
                                    arrow.clearAnimation();
                                    finger.clearAnimation();
                                    textView.clearAnimation();
                                    donknow.clearAnimation();
                                }
                                else if(say.get(i).contains("네모난 상자")) {
                                    textView.startAnimation(alpha);

                                    finger.setVisibility(View.INVISIBLE);
                                    arrow.setVisibility(View.INVISIBLE);
                                    title_text.clearAnimation();
                                    mic.clearAnimation();
                                    arrow.clearAnimation();
                                    finger.clearAnimation();
                                    donknow.clearAnimation();
                                }
                                else if(say.get(i).contains("잘모르겠어요")) {
                                    donknow.startAnimation(alpha);

                                    finger.setVisibility(View.INVISIBLE);
                                    arrow.setVisibility(View.INVISIBLE);
                                    title_text.clearAnimation();
                                    mic.clearAnimation();
                                    arrow.clearAnimation();
                                    finger.clearAnimation();
                                    textView.clearAnimation();
                                }
                                else if(say.get(i).contains("화면을 밀면")) {
                                    finger.bringToFront();
                                    finger.setVisibility(View.VISIBLE);
                                    arrow.setVisibility(View.VISIBLE);

                                    arrow.startAnimation(alpha);
                                    title_text.clearAnimation();
                                    mic.clearAnimation();
                                    textView.clearAnimation();
                                    donknow.clearAnimation();
                                    if(say.get(i).contains("왼쪽")){
                                        arrow.setRotation(0);
                                        finger.startAnimation(animLeft);
                                    }
                                    else if(say.get(i).contains("오른쪽")){
                                        arrow.setRotation(180);
                                        finger.startAnimation(animRight);
                                    }
                                }
                                else {
                                    title_text.clearAnimation();
                                    finger.setVisibility(View.INVISIBLE);
                                    arrow.setVisibility(View.INVISIBLE);
                                    textView.clearAnimation();
                                    mic.clearAnimation();
                                    arrow.clearAnimation();
                                    finger.clearAnimation();
                                }
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void speakOut(String say, String id, int time, Button donknow) {
        CharSequence text = say;
        tts.setPitch((float) 1.2);
        tts.setSpeechRate((float) 1);
        donknow.setEnabled(false);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,id);
                donknow.setEnabled(true);
            }
        }, time);

    }

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
    public void onInit(int status, String say, String id, int time, Button btn) {
        if (status == TextToSpeech.SUCCESS) {
            Voice voiceobj = new Voice("it-it-x-kda#male_2-local",
                    Locale.getDefault(), 500, 1, false, null);
            tts.setVoice(voiceobj);
            int result = tts.setLanguage(Locale.KOREA);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                speakOut(say, id, time, btn);
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
