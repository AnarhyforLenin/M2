package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

class BreathingAnimationThread extends Thread{
    private ImageView imageView;
    private Activity context;
    private float scale;
    private ImageView imageView2;
    private float scale2;
    private static boolean isRunning = false;
    private int delay;
    private boolean predefIsExtended = true;
    private void animateTextD(ImageView imgAnim, boolean isExtending, float scale){

        if(!isRunning)
            return;
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imgAnim.animate().scaleX(isExtending? scale : 1f).scaleY(isExtending? scale : 1f).alpha(isExtending? 0.2f : 1f).setDuration(4000).withEndAction(new Runnable() {
                    @Override
                    public void run() {

                        if(!isRunning)
                            return;
                        (new BreathingAnimationThread(imgAnim,scale, !isExtending, isExtending ? 4000 : 0,context)).start();

                        //animateTextD(imgAnim, !isExtending, scale);
                    }
                });
            }
        });

    }

    public BreathingAnimationThread(ImageView imageView, float scale, ImageView imageView2, float scale2, int delay, Activity context) {
        this.imageView = imageView;
        this.scale = scale;
        this.imageView2 = imageView2;
        this.scale2 = scale2;
        this.delay = delay;
        this.context = context;
    }

    public BreathingAnimationThread(ImageView imageView, float scale, boolean predefIsExtended, int delay, Activity context) {
        this.imageView = imageView;
        this.scale = scale;
        this.delay = delay;
        this.predefIsExtended = predefIsExtended;
        this.context = context;
    }

    public void stopAnimation(){
        isRunning=false;
    }

    public void startAnimation() {
        isRunning =true;
    }

    @Override
    public void run() {
        try{
            Thread.sleep(delay);
        } catch(InterruptedException ex){}
        animateTextD(imageView, predefIsExtended, scale);
        if(imageView2 != null)
            animateTextD(imageView2, predefIsExtended, scale2);
    }
}