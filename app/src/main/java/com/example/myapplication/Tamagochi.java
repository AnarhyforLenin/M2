package com.example.myapplication;

import android.content.ClipData;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.DragEvent;
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
        dish.setOnLongClickListener(longClickListener);
        karIm.setOnDragListener(dragListener);

    }

    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            ClipData data = ClipData.newPlainText("","");
            View.DragShadowBuilder myshadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, myshadowBuilder,v,0);
            return false;
        }
    };

    View.OnDragListener dragListener = new View.OnDragListener(){

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int dragEvent = event.getAction();
            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    final View view = (View) event.getLocalState();
                    if (view.getId()== R.id.yellow) {
                        TextView test2 = findViewById(R.id.test2);
                        test2.setText("ГОВНО ЗАЛУПА ПЕНИС ХЕР");
                    }
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    break;
            }
            return true;
        }
    };
}
