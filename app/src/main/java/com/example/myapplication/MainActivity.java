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
import java.util.Timer;
import java.util.TimerTask;

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
    Timer timer;

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
        findViewById(R.id.buttonStart).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                if(timerthread != null)
                {
                    stopTask();
                    timerthread.stopAnimation();
                    timerthread = null;
                    return;
                }
                startTask();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Intent intentNew = new Intent(MainActivity.this, MainActivity2.class);
                        startActivity(intentNew);
                        finish();
                    }
                }, 120000);

                timerthread = (new Timerthread(tv));
                timerthread.start();
            }

        });




    }
    private void init() {
        this.handlerAnimationCIMG = new Handler();
        this.layoutEncontrarPers = findViewById(R.id.layoutPers);
        this.imgAnim = findViewById(R.id.Anim1);
        this.imgAnim2 = findViewById(R.id.Anim2);

    }

    private void startTask() {
        breathingAnimationThread = new BreathingAnimationThread(imgAnim, 4f, imgAnim2, 2f, 0, this);
        breathingAnimationThread.start();
        breathingAnimationThread.startAnimation();
        this.layoutEncontrarPers.setVisibility(View.VISIBLE);

    }

    private void stopTask() {
        if(breathingAnimationThread != null)
        breathingAnimationThread.stopAnimation();
        breathingAnimationThread = null;
        //this.layoutEncontrarPers.setVisibility(View.GONE);
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
                    if(!isRunning) {

                        break;
                    }
                    runOnUiThread(new Runnable() { @Override public void run() { tv.setText(t); } });
                    try{Thread.sleep(1000); } catch(Exception ex){}
                }

            }
        }


    }


}