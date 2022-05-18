package com.example.myapplication;

import android.content.ClipData;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.TimeZone;

public class Tamagochi extends AppCompatActivity {
    ImageView dish, karIm,bawl;
    ImageButton kar;


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

        TextView textView = findViewById(R.id.test);
        TimeZone tz = TimeZone.getTimeZone("GMT+03");
        java.util.Calendar c = java.util.Calendar.getInstance(tz);
        String time2 = String.format("%02d" , c.get(java.util.Calendar.HOUR_OF_DAY))+":"+String.format("%02d" , c.get(Calendar.MINUTE));
        textView.setText(time2);
        new CountDownTimer(200000, 2000) {

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

        dish = findViewById(R.id.yellow);
        kar = findViewById(R.id.karambola);
        karIm = findViewById(R.id.karambolaIm);
        dish.setOnTouchListener(new ChoiceTouchListener());
        dish.setOnDragListener(new ChoiceDragListener());
    }

    private final class ChoiceTouchListener implements View.OnTouchListener {


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && ((ImageView)v).getDrawable()!=null) {
                ClipData data = ClipData.newPlainText("","");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(data, shadowBuilder,v, 0);
                return true;
            }
            else {return false;}
        }
    }
    private class  ChoiceDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch(event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    ImageView view = (ImageView) event.getLocalState();
                    //((ImageView)v).setImageDrawable(getResources().getDrawable(R.drawable.bowl));
                    ((ImageView)v).setImageDrawable(null);

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    break;
            }
            return false;
        }
    }

}