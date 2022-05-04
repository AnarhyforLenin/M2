package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;

public class MainActivity2 extends AppCompatActivity {

    TextView q1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);



        int[] photos={R.drawable.f1, R.drawable.f2, R.drawable.f3, R.drawable.f4, R.drawable.f5, R.drawable.f6, R.drawable.f7, R.drawable.f8, R.drawable.f9, R.drawable.f100, R.drawable.f110, R.drawable.f120, R.drawable.f130, R.drawable.f140, R.drawable.f150};

        //blue -1
        //yellow -2
        //green -3
        //red -4

        int[] f1 = {3,1};
        int[] f2 = {3,2};
        int[] f3 = {3,3};
        int[] f4 = {4,3};
        int[] f5 = {4,1};
        int[] f6 = {4,2};
        int[] f7 = {4,4};
        int[] f8 = {5,4};
        int[] f9 = {5,2};
        int[] f10 = {5,3};
        int[] f11 = {5,1};
        int[] f12 = {6,1};
        int[] f13 = {6,2};
        int[] f14 = {6,3};
        int[] f15 = {6,4};

        int[][] mas = {f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15};

        ImageView image1 = (ImageView) findViewById(R.id.f10);

        q1=findViewById(R.id.q1);

        int ranq1 = (int)(Math.random()*((6-3)+1))+3;
        switch (ranq1) {
            case 3:
                q1.setText("Сколько на экране треугольников?");
                break;
            case 4:
                q1.setText("Сколько на экране квадратов?");
                break;
            case 5:
                q1.setText("Сколько на экране пятиугольников?");
                break;
            case 6:
                q1.setText("Сколько на экране шестиугольников?");
                break;
            default:
                break;

        }


        Random ran1=new Random();
        int i1=ran1.nextInt(photos.length);
        image1.setImageResource(photos[i1]);

        ImageView image2 = (ImageView) findViewById(R.id.f11);

        Random ran2=new Random();
        int i2=ran2.nextInt(photos.length);
        image2.setImageResource(photos[i2]);

        ImageView image3 = (ImageView) findViewById(R.id.f12);

        Random ran3=new Random();
        int i3=ran2.nextInt(photos.length);
        image3.setImageResource(photos[i3]);

        ImageView image4 = (ImageView) findViewById(R.id.f20);

        Random ran4=new Random();
        int i4=ran4.nextInt(photos.length);
        image4.setImageResource(photos[i4]);

        ImageView image5 = (ImageView) findViewById(R.id.f21);

        Random ran5=new Random();
        int i5=ran5.nextInt(photos.length);
        image5.setImageResource(photos[i5]);

        ImageView image6 = (ImageView) findViewById(R.id.f22);

        Random ran6=new Random();
        int i6=ran6.nextInt(photos.length);
        image6.setImageResource(photos[i6]);

        int counterA1 = 0;
        if (mas[i1][0]==ranq1) { counterA1+=1;}
        if (mas[i2][0]==ranq1) { counterA1+=1;}
        if (mas[i3][0]==ranq1) { counterA1+=1;}
        if (mas[i4][0]==ranq1) { counterA1+=1;}
        if (mas[i5][0]==ranq1) { counterA1+=1;}
        if (mas[i6][0]==ranq1) { counterA1+=1;}


        SeekBar seekBar = findViewById(R.id.s1);
        TextView a1 = findViewById(R.id.a1);
        int finalCounterA = counterA1;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                a1.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int answer1 = Integer.valueOf(a1.getText().toString());
                if (answer1== finalCounterA) {
                    findViewById(R.id.right).setVisibility(View.VISIBLE);
                }
                else {findViewById(R.id.right).setVisibility(View.GONE);}
            }
        });


    }
}