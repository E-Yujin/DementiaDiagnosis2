package com.cbnu.dementiadiagnosis;

import android.graphics.drawable.AnimationDrawable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.widget.ImageView;

public class Helper{
    AnimationListner jump, nomal, nomal2, speak, speak2, listn_s,listn,listn_f, recog, anal, slow;
    ImageView img;

    public Helper(TTS tts, MainSTT stt, ImageView IMG, AppCompatActivity activity){
        img = IMG;
        nomal = new AnimationListner((AnimationDrawable)
                activity.getResources().getDrawable(
                        R.drawable.helper_default)) {
            @Override
            public void onAnimationFinish() {
            }

            @Override
            public void onAnimationStart() {
                if(tts.IsTalking()){
                    this.stop();
                    img.setImageDrawable(speak);
                    speak.start();
                }
                else if(stt.isListening){
                    this.stop();
                    img.setImageDrawable(listn_s);
                    listn_s.start();
                }
                else{
                    img.setImageDrawable(this);
                    this.start();
                }
            }
        };

        nomal2 = new AnimationListner((AnimationDrawable)
                activity.getResources().getDrawable(
                        R.drawable.helper_default_2)) {
            @Override
            public void onAnimationFinish() {
                img.setImageDrawable(nomal);
                nomal.start();
            }

            @Override
            public void onAnimationStart() {
            }
        };

        jump = new AnimationListner((AnimationDrawable)
                activity.getResources().getDrawable(
                        R.drawable.helper_jump)) {
            @Override
            public void onAnimationFinish() {
                img.setImageDrawable(nomal);
                nomal.start();
            }

            @Override
            public void onAnimationStart() {
            }
        };

        speak = new AnimationListner((AnimationDrawable)
                activity.getResources().getDrawable(
                        R.drawable.helper_talk)) {
            @Override
            public void onAnimationFinish() {
                if(!tts.IsTalking()){
                    this.stop();
                    img.setImageDrawable(nomal2);
                    nomal2.start();
                }
                else{
                    this.stop();
                    img.setImageDrawable(speak2);
                    speak2.start();
                }
            }

            @Override
            public void onAnimationStart() {
            }
        };

        speak2 = new AnimationListner((AnimationDrawable)
                activity.getResources().getDrawable(
                        R.drawable.helper_talk_2)) {
            @Override
            public void onAnimationFinish() {
                if(!tts.IsTalking()){
                    this.stop();
                    img.setImageDrawable(nomal);
                    nomal.start();
                }
                else{
                    this.stop();
                    img.setImageDrawable(speak);
                    speak.start();
                }
            }

            @Override
            public void onAnimationStart() {
            }
        };

        listn = new AnimationListner((AnimationDrawable)
                activity.getResources().getDrawable(
                        R.drawable.helper_listen)) {
            @Override
            public void onAnimationFinish() {
            }

            @Override
            public void onAnimationStart() {
                if(stt.isListening){
                    img.setImageDrawable(this);
                    this.start();
                }
                else if(stt.isTooFast){
                    this.stop();
                    img.setImageDrawable(slow);
                    slow.start();
                }
                else if(stt.isRecognize){
                    this.stop();
                    img.setImageDrawable(recog);
                    recog.start();
                }
                else if(stt.isAnalysing){
                    this.stop();
                    img.setImageDrawable(listn);
                    listn.start();
                }
                else{
                    this.stop();
                    img.setImageDrawable(listn_f);
                    listn_f.start();
                }
            }
        };

        listn_s = new AnimationListner((AnimationDrawable)
                activity.getResources().getDrawable(
                        R.drawable.helper_listen_s)) {

            @Override
            public void onAnimationFinish() {
                img.setImageDrawable(listn);
                listn.start();
            }

            @Override
            public void onAnimationStart() {
            }
        };

        listn_f = new AnimationListner((AnimationDrawable)
                activity.getResources().getDrawable(
                        R.drawable.helper_listen_f)) {

            @Override
            public void onAnimationFinish() {
                img.setImageDrawable(nomal);
                nomal.start();
            }

            @Override
            public void onAnimationStart() {
            }
        };

        recog = new AnimationListner((AnimationDrawable)
                activity.getResources().getDrawable(
                        R.drawable.helper_recog)) {
            @Override
            public void onAnimationFinish() {
            }

            @Override
            public void onAnimationStart() {
                if(stt.isRecognize){
                    img.setImageDrawable(this);
                    this.start();
                }
                else if(stt.isTooFast){
                    this.stop();
                    img.setImageDrawable(slow);
                    slow.start();
                }
                else if(stt.isAnalysing){
                    this.stop();
                    img.setImageDrawable(anal);
                    anal.start();
                }
                else if(stt.isListening){
                    this.stop();
                    img.setImageDrawable(listn);
                    listn.start();
                }
                else{
                    this.stop();
                    if(tts.IsTalking()){
                        img.setImageDrawable(speak);
                        speak.start();
                    }
                    else{
                        img.setImageDrawable(listn_f);
                        listn_f.start();
                    }
                }
            }
        };
        anal = new AnimationListner((AnimationDrawable)
                activity.getResources().getDrawable(
                        R.drawable.helper_analy)) {
            @Override
            public void onAnimationFinish() {
            }

            @Override
            public void onAnimationStart() {
                if(stt.isAnalysing){
                    img.setImageDrawable(this);
                    this.start();
                }
                else if(stt.isTooFast){
                    this.stop();
                    img.setImageDrawable(slow);
                    slow.start();
                }
                else if(stt.isRecognize){
                    this.stop();
                    img.setImageDrawable(recog);
                    recog.start();
                }
                else if(stt.isListening){
                    this.stop();
                    img.setImageDrawable(listn);
                    listn.start();
                }
                else{
                    this.stop();
                    img.setImageDrawable(listn_f);
                    listn_f.start();
                }
            }
        };
        slow = new AnimationListner((AnimationDrawable)
                activity.getResources().getDrawable(
                        R.drawable.helper_slow)) {
            @Override
            public void onAnimationFinish() {
            }

            @Override
            public void onAnimationStart() {
                if(stt.isTooFast){
                    img.setImageDrawable(this);
                    this.start();
                }
                else if(stt.isAnalysing){
                    this.stop();
                    img.setImageDrawable(slow);
                    slow.start();
                }
                else if(stt.isRecognize){
                    this.stop();
                    img.setImageDrawable(recog);
                    recog.start();
                }
                else if(stt.isListening){
                    this.stop();
                    img.setImageDrawable(listn);
                    listn.start();
                }
                else{
                    this.stop();
                    img.setImageDrawable(listn_f);
                    listn_f.start();
                }
            }
        };
    }

    public Helper(TTS tts, ImageView IMG, AppCompatActivity activity){
        img = IMG;
        nomal = new AnimationListner((AnimationDrawable)
                activity.getResources().getDrawable(
                        R.drawable.helper_default)) {
            @Override
            public void onAnimationFinish() {
            }

            @Override
            public void onAnimationStart() {
                if(tts.IsTalking()){
                    this.stop();
                    img.setImageDrawable(speak);
                    speak.start();
                }
                else{
                    img.setImageDrawable(this);
                    this.start();
                }
            }
        };

        nomal2 = new AnimationListner((AnimationDrawable)
                activity.getResources().getDrawable(
                        R.drawable.helper_default_2)) {
            @Override
            public void onAnimationFinish() {
                img.setImageDrawable(nomal);
                nomal.start();
            }

            @Override
            public void onAnimationStart() {
            }
        };

        jump = new AnimationListner((AnimationDrawable)
                activity.getResources().getDrawable(
                        R.drawable.helper_jump)) {
            @Override
            public void onAnimationFinish() {
                img.setImageDrawable(nomal);
                nomal.start();
            }

            @Override
            public void onAnimationStart() {
            }
        };

        speak = new AnimationListner((AnimationDrawable)
                activity.getResources().getDrawable(
                        R.drawable.helper_talk)) {
            @Override
            public void onAnimationFinish() {
                if(!tts.IsTalking()){
                    this.stop();
                    img.setImageDrawable(nomal2);
                    nomal2.start();
                }
                else{
                    this.stop();
                    img.setImageDrawable(speak2);
                    speak2.start();
                }
            }

            @Override
            public void onAnimationStart() {
            }
        };

        speak2 = new AnimationListner((AnimationDrawable)
                activity.getResources().getDrawable(
                        R.drawable.helper_talk_2)) {
            @Override
            public void onAnimationFinish() {
                if(!tts.IsTalking()){
                    this.stop();
                    img.setImageDrawable(nomal);
                    nomal.start();
                }
                else{
                    this.stop();
                    img.setImageDrawable(speak);
                    speak.start();
                }
            }

            @Override
            public void onAnimationStart() {
            }
        };
    }

    public Helper(ImageView IMG, Fragment activity){
        img = IMG;
        nomal = new AnimationListner((AnimationDrawable)
                activity.getResources().getDrawable(
                        R.drawable.helper_default)) {
            @Override
            public void onAnimationFinish() {
            }

            @Override
            public void onAnimationStart() {
            }
        };

        jump = new AnimationListner((AnimationDrawable)
                activity.getResources().getDrawable(
                        R.drawable.helper_jump)) {
            @Override
            public void onAnimationFinish() {
                img.setImageDrawable(nomal);
                nomal.start();
            }

            @Override
            public void onAnimationStart() {
            }
        };
    }

    public Helper(ImageView IMG, AppCompatActivity activity){
        img = IMG;
        nomal = new AnimationListner((AnimationDrawable)
                activity.getResources().getDrawable(
                        R.drawable.helper_default)) {
            @Override
            public void onAnimationFinish() {
            }

            @Override
            public void onAnimationStart() {
            }
        };

        jump = new AnimationListner((AnimationDrawable)
                activity.getResources().getDrawable(
                        R.drawable.helper_jump)) {
            @Override
            public void onAnimationFinish() {
                img.setImageDrawable(nomal);
                nomal.start();
            }

            @Override
            public void onAnimationStart() {
            }
        };
    }
    public void setNomal(){
        jump.setOneShot(true);
        img.setImageDrawable(jump);
        jump.start();
    }

    public void setStart(){
        nomal2.setOneShot(true);
        speak.setOneShot(true);
        speak2.setOneShot(true);
        jump.setOneShot(true);
        img.setImageDrawable(jump);
        jump.start();
    }

    public void setTest(){
        nomal2.setOneShot(true);
        speak.setOneShot(true);
        speak2.setOneShot(true);
        listn_s.setOneShot(true);
        listn_f.setOneShot(true);
        jump.setOneShot(true);
        img.setImageDrawable(jump);
        jump.start();
    }
}
