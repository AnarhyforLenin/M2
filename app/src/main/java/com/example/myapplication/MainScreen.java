package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainScreen extends AppCompatActivity {
    ImageButton karambola;
    AnimationDrawable animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        karambola = (ImageButton)findViewById(R.id.karambola);
        karambola.setBackgroundResource(R.drawable.happy);
        animation = (AnimationDrawable) karambola.getBackground();
        MediaPlayer mp1 = MediaPlayer.create(this, R.raw.g1);
        MediaPlayer mp2 = MediaPlayer.create(this, R.raw.g2);
        MediaPlayer mp3 = MediaPlayer.create(this, R.raw.g3);
        MediaPlayer mp4 = MediaPlayer.create(this, R.raw.g4);
        MediaPlayer mp5 = MediaPlayer.create(this, R.raw.g5);
        animation.setOneShot(true);
        karambola.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                animation.stop();
                animation.start();
                int x = (int)(Math.random()*((5-1)+1))+1;
                switch (x) {
                    case(1):
                        mp1.start();
                        break;
                    case(2):
                        mp2.start();
                        break;
                    case(3):
                        mp3.start();
                        break;
                    case(4):
                        mp4.start();
                        break;
                    case(5):
                        mp5.start();
                        break;
                }

                return false;
            }
        });

    }
}