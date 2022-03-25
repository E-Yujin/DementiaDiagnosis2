package com.example.sst;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class TTS extends AppCompatActivity {
    private TextToSpeech tts;

    public TTS(Context context, TextToSpeech.OnInitListener listener){
        tts = new TextToSpeech(context, listener);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void speakOut(String say) {
        CharSequence text = say;
        tts.setPitch((float) 0.6);
        tts.setSpeechRate((float) 1);
        tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,"id1");
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
