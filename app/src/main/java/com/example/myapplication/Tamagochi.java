package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.BreakIterator;

public class Tamagochi extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tamagochi);
        final int[] time = {100};
        TextView textHealth = (TextView)findViewById(R.id.health);
        ProgressBar progressHealth = (ProgressBar)findViewById(R.id.vertical_progressbar1);
        TextView textEat = (TextView)findViewById(R.id.eat);
        ProgressBar progressEat = (ProgressBar)findViewById(R.id.vertical_progressbar2);
        TextView textHappy = (TextView)findViewById(R.id.happyness);
        ProgressBar progressHappy = (ProgressBar)findViewById(R.id.vertical_progressbar3);

        new CountDownTimer(100000, 1000) {

            public void onTick(long millisUntilFinished) {
                textHealth.setText(checkDigit(time[0]));
                progressHealth.setProgress(time[0]);
                textEat.setText(checkDigit(time[0]));
                progressEat.setProgress(time[0]);
                textHappy.setText(checkDigit(time[0]));
                progressHappy.setProgress(time[0]);
                time[0]--;
            }

            private String checkDigit(int time) {
                return String.valueOf(time);
            }

            public void onFinish() {
                textHealth.setText("0");
                textEat.setText("0");
                textHappy.setText("0");
            }

        }.start();




    }
}