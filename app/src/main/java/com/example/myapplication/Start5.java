package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Start5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start5);
        int var =  getIntent().getIntExtra("var",0);
        if (var ==10 || var ==11){
            TextView name = findViewById(R.id.text_hello_name2);
            Bundle arguments = getIntent().getExtras();
            String full_name = arguments.get("full_name").toString();
            name.setText(full_name+"!");
            int pronounce =  getIntent().getIntExtra("radiochosen",0);
            if (pronounce == 1) { findViewById(R.id.pronounce1).setVisibility(View.VISIBLE);}
            if (pronounce == 2) { findViewById(R.id.pronounce2).setVisibility(View.VISIBLE);}
        }
        if (var ==12){
            findViewById(R.id.text_hello_name3).setVisibility(View.VISIBLE);
        }

    findViewById(R.id.next34_button).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent6 = new Intent(Start5.this, MainActivity2.class);
            startActivity(intent6);
        }
    });

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
