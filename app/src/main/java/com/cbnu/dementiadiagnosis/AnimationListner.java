package com.cbnu.dementiadiagnosis;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;

public abstract class AnimationListner extends AnimationDrawable {
    Handler mAnimationHandler;
    public int changed = 0;
    AnimationDrawable MainAni;

    public AnimationListner(AnimationDrawable ani){
        if(changed == 0){
            MainAni = ani;
        }
        for (int i = 0; i < MainAni.getNumberOfFrames(); i++) {
            this.addFrame(MainAni.getFrame(i), MainAni.getDuration(i));
        }
    }

    @Override
    public void start() {
        super.start();
        mAnimationHandler = new Handler();
        mAnimationHandler.post(new Runnable() {
            @Override
            public void run() {
                onAnimationStart();
            }
        });
        mAnimationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onAnimationFinish();
            }
        }, getTotalDuration());

    }
    public int getTotalDuration() {

        int iDuration = 0;

        for (int i = 0; i < this.getNumberOfFrames(); i++) {
            iDuration += this.getDuration(i);
        }

        return iDuration;
    }
    public abstract void onAnimationFinish();
    public abstract void onAnimationStart();
}
