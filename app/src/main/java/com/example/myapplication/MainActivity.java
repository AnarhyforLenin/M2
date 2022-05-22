package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tomer.fadingtextview.FadingTextView;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    private View layoutEncontrarPers;
    private ImageView imgAnim, imgAnim2, imgAnim3;
    private Handler handlerAnimationCIMG;
    private TextView textView;
    private TextView tip;
    private TextView timerTextView;
    private FadingTextView fadingTextView;
    private BreathingAnimationThread breathingAnimationThread;
    private boolean isRunning = false;
    private boolean isTapped = false;
    Timerthread timerthread;
    Timer timer;

    private void setUpFadeAnimation(final TextView textView) {
        final Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(0);
        fadeIn.setStartOffset(0);
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
        setContentView(R.layout.activity_main);
        init();
        timerTextView = findViewById(R.id.taimer);

        findViewById(R.id.buttonStart).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");

                if(timerthread == null) {
                    startTask();


                }
                else {
                    imgAnim.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(1000);
                    imgAnim2.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(1000);
                    imgAnim3.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(1000);
                    //tip.setVisibility(View.GONE);
                    stopTask();
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                        startActivity(intent);
                        finish();
                    }
                }, 60000);
            }

        });




    }
    private void init() {
        this.handlerAnimationCIMG = new Handler();
        this.layoutEncontrarPers = findViewById(R.id.layoutPers);
        this.imgAnim = findViewById(R.id.Anim3);
        this.imgAnim2 = findViewById(R.id.Anim1);
        this.imgAnim3 = findViewById(R.id.Anim2);
        this.tip=findViewById(R.id.tip);

    }

    private void startTask() {
        timerthread = new Timerthread(timerTextView);
        timerthread.start();
    }

    private void stopTask() {
        if(timerthread != null) {
            timerthread.stopAnimation();
            timerthread = null;
        }
    }






    class Timerthread extends Thread{
        TextView tv;
        int var = MainActivity2.var;
        String name = MainActivity2.name;
        boolean isRunning=false;
        String[] seq = {"4","3","2","1"};
        Timerthread(TextView tv){
            this.tv=tv;
        }
        public void stopAnimation() {
            isRunning = false;
            tip.setVisibility(View.GONE);
        }
        public void run(){

            Animation a = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale);
            Animation b = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale2);
            final int[] firstTimer = {1};
            isRunning=true;
            while (true){
                for(final String t : seq){
                    if(!isRunning) {
                        firstTimer[0]=1;

                        break;
                    }
                    if(t.equals("4")&& firstTimer[0] ==1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                tip.setVisibility(View.VISIBLE);
                                imgAnim.animate().scaleX(1.5f).scaleY(1.5f).alpha(1f).setDuration(4000);
                                imgAnim2.animate().scaleX(2f).scaleY(2f).alpha(0.5f).setDuration(4000);
                                imgAnim3.animate().scaleX(3f).scaleY(3f).alpha(0.5f).setDuration(4000);
                                if (var ==10) {tip.setText(name+",сделайте вдох");}
                                if (var ==11) {tip.setText(name+",сделай вдох");}
                                if (var ==12) {
                                    tip.setText("Солнце, сделай глубокий вдох");
                                }


                                a.reset();
                                tip.clearAnimation();
                                tip.startAnimation(a);

                                Handler handler1 = new Handler();
                                handler1.postDelayed(new Runnable() {
                                    public void run() {

                                        b.reset();
                                        tip.clearAnimation();
                                        tip.startAnimation(b);
                                    }
                                }, 2000);

                                firstTimer[0] +=1;
                            }
                        });

                    }

                    if(t.equals("4")&& firstTimer[0] ==2) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imgAnim.animate().scaleX(1.5f).scaleY(1.5f).alpha(1f).setDuration(4000);
                                imgAnim2.animate().scaleX(2f).scaleY(2f).alpha(0.5f).setDuration(4000);
                                imgAnim3.animate().scaleX(3f).scaleY(3f).alpha(0.5f).setDuration(4000);
                                if (var ==10) {tip.setText(name+",задержите дыхание");}
                                if (var ==11) {tip.setText(name+",задержи дыхание");}
                                if (var ==12) {tip.setText("Солнце, задержи дыхание");}

                                a.reset();
                                tip.clearAnimation();
                                tip.startAnimation(a);

                                Handler handler1 = new Handler();
                                handler1.postDelayed(new Runnable() {
                                    public void run() {

                                        b.reset();
                                        tip.clearAnimation();
                                        tip.startAnimation(b);
                                    }
                                }, 2000);
                                firstTimer[0] +=1;
                            }
                        });

                    }
                    if(t.equals("4")&& firstTimer[0] ==3) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imgAnim.animate().scaleX(1f).scaleY(1f).alpha(0f).setDuration(4000);
                                imgAnim2.animate().scaleX(1f).scaleY(1f).alpha(0f).setDuration(4000);
                                imgAnim3.animate().scaleX(1f).scaleY(1f).alpha(0f).setDuration(4000);
                                if (var ==10) {tip.setText(name+",сделайте выдох");}
                                if (var ==11) {tip.setText(name+",сделай выдох");}
                                if (var ==12) {tip.setText("Солнце, сделай выдох");}

                                a.reset();
                                tip.clearAnimation();
                                tip.startAnimation(a);

                                Handler handler1 = new Handler();
                                handler1.postDelayed(new Runnable() {
                                    public void run() {

                                        b.reset();
                                        tip.clearAnimation();
                                        tip.startAnimation(b);
                                    }
                                }, 2000);
                                firstTimer[0]+=1;
                            }
                        });

                    }
                    if(t.equals("4")&& firstTimer[0] ==4) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imgAnim.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(4000);
                                imgAnim2.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(4000);
                                imgAnim3.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(4000);
                                if (var ==10) {tip.setText(name+",задержите дыхание");}
                                if (var ==11) {tip.setText(name+",задержи дыхание");}
                                if (var ==12) {tip.setText("Солнце, задержи дыхание");}

                                a.reset();
                                tip.clearAnimation();
                                tip.startAnimation(a);

                                Handler handler1 = new Handler();
                                handler1.postDelayed(new Runnable() {
                                    public void run() {

                                        b.reset();
                                        tip.clearAnimation();
                                        tip.startAnimation(b);
                                    }
                                }, 2000);
                                firstTimer[0]=1;
                            }
                        });
                    }

                    tvSetText(t, tv);
                    try{Thread.sleep(1000); } catch(Exception ex){}
                }

            }
        }


    }

    private void tvSetText(String text, TextView tv) {
        runOnUiThread(new Runnable() { @Override public void run() { tv.setText(text); } });
    }

}