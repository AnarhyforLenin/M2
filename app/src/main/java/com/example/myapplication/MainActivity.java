package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tomer.fadingtextview.FadingTextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private View layoutEncontrarPers;
    private ImageView imgAnim, imgAnim2;
    private Handler handlerAnimationCIMG;
    private TextView textView;
    private FadingTextView fadingTextView;
    private BreathingAnimationThread breathingAnimationThread;
    private boolean isRunning = false;
    private boolean isTapped = false;
    Timerthread timerthread;

    private void setUpFadeAnimation(final TextView textView) {
        // Start from 0.1f if you desire 90% fade animation
        final Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(0);
        fadeIn.setStartOffset(0);
        // End to 0.1f if you desire 90% fade animation
        final Animation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(0);
        fadeOut.setStartOffset(0);

        fadeIn.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationEnd(Animation arg0) {
                textView.startAnimation(fadeOut);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationStart(Animation arg0) {
            }
        });

        fadeOut.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationEnd(Animation arg0) {
                // start fadeIn when fadeOut ends (repeat)
                textView.startAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationStart(Animation arg0) {
            }
        });

        textView.startAnimation(fadeOut);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //layoutEncontrarPers.setVisibility(View.VISIBLE);
        final SharedPreferences loginData = getSharedPreferences("loginData", MODE_PRIVATE);
        boolean firstStart = loginData.getBoolean("firstStart", true);
        if(firstStart) {
            loginData.edit().putBoolean("firstStart", false).commit();

            Intent intent = new Intent(this,Startscreen.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        setContentView(R.layout.activity_main);
        init();
        TextView tv = findViewById(R.id.taimer);
        if (isTapped == false) {
            findViewById(R.id.buttonStart).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: ");
                    startTask();
                    if(timerthread != null)
                    {
                        stopTask();
                        timerthread.stopAnimation();
                        timerthread = null;
                    }
                    timerthread = (new Timerthread(tv));
                    timerthread.start();
                }

            });
            isTapped = true;
        }
        else {
            findViewById(R.id.buttonStart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopTask();
                    timerthread.stopAnimation();
                }
            });
        }

    }
    private void init() {
        this.handlerAnimationCIMG = new Handler();
        this.layoutEncontrarPers = findViewById(R.id.layoutPers);
        this.imgAnim = findViewById(R.id.Anim1);
        this.imgAnim2 = findViewById(R.id.Anim2);

    }

    private void startTask() {
        breathingAnimationThread = new BreathingAnimationThread(imgAnim, 4f, imgAnim2, 2f, 0);
        breathingAnimationThread.start();
        this.layoutEncontrarPers.setVisibility(View.VISIBLE);

    }

    private void stopTask() {
        if(breathingAnimationThread != null)
        breathingAnimationThread.stopAnimation();
        //this.layoutEncontrarPers.setVisibility(View.GONE);
    }




    class BreathingAnimationThread extends Thread{
        private ImageView imageView;
        private float scale;
        private ImageView imageView2;
        private float scale2;
        private boolean isRunning = false;
        private int delay;
        private boolean predefIsExtended = true;
        private void animateTextD(ImageView imgAnim, boolean isExtending, float scale){

            if(!isRunning)
                return;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imgAnim.animate().scaleX(isExtending? scale : 1f).scaleY(isExtending? scale : 1f).alpha(isExtending? 0.2f : 1f).setDuration(4000).withEndAction(new Runnable() {
                        @Override
                        public void run() {


                            (new BreathingAnimationThread(imgAnim,scale, !isExtending, isExtending ? 4000 : 0)).start();

                            //animateTextD(imgAnim, !isExtending, scale);
                        }
                    });
                }
            });

        }

        public BreathingAnimationThread(ImageView imageView, float scale, ImageView imageView2, float scale2, int delay) {
            this.imageView = imageView;
            this.scale = scale;
            this.imageView2 = imageView2;
            this.scale2 = scale2;
            this.delay = delay;
        }

        public BreathingAnimationThread(ImageView imageView, float scale, boolean predefIsExtended, int delay) {
            this.imageView = imageView;
            this.scale = scale;
            this.delay = delay;
            this.predefIsExtended = predefIsExtended;
        }

        public void stopAnimation(){
            isRunning=false;
        }
        @Override
        public void run() {
            isRunning = true;
            try{
                Thread.sleep(delay);
            } catch(InterruptedException ex){}
            animateTextD(imageView, predefIsExtended, scale);
            if(imageView2 != null)
            animateTextD(imageView2, predefIsExtended, scale2);
        }
    }

    class Timerthread extends Thread{
        TextView tv;
        boolean isRunning=false;
        String[] seq = {"4","3","2","1"};
        Timerthread(TextView tv){
            this.tv=tv;
        }
        public void stopAnimation() {
            isRunning = false;
        }
        public void run(){
            isRunning=true;
            while (true){
                for(final String t : seq){
                    if(!isRunning) break;
                    runOnUiThread(new Runnable() { @Override public void run() { tv.setText(t); } });
                    try{Thread.sleep(1000); } catch(Exception ex){}
                }

            }
        }


    }


}