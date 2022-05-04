package com.example.myapplication;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Start3 extends AppCompatActivity {
    Button b3, b4;
    ImageButton next3;
    EditText e1, e2;
    RadioButton r1, r2, r3;
    public String full_name, name;
    RadioGroup radio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start3);
        r1 = findViewById(R.id.r1);
        r2 = findViewById(R.id.r2);
        r3 = findViewById(R.id.r3);
        final boolean[] isTapped1 = {false};
        final boolean[] isTapped2 = {false};
        final boolean[] isTapped3 = {false};

        radio = (RadioGroup) findViewById(R.id.radioGroup);
        radio.clearCheck();

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                Intent intent5 = new Intent(Start3.this, Start5.class);

                r1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (isTapped1[0] == false) {
                            findViewById(R.id.e1).setVisibility(View.VISIBLE);
                            findViewById(R.id.b3).setVisibility(View.VISIBLE);
                            r2.setVisibility(View.GONE);
                            r3.setVisibility(View.GONE);
                            e1 = findViewById(R.id.e1);
                            findViewById(R.id.b3).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String full_name = e1.getText().toString();
                                    intent5.putExtra("full_name", full_name);
                                    MainActivity2.name=full_name;
                                    intent5.putExtra("var", 10);
                                    MainActivity2.var = 10;
                                    startActivity(intent5);
                                }
                            });
                            isTapped1[0] = true;
                        } else {
                            findViewById(R.id.e1).setVisibility(View.GONE);
                            findViewById(R.id.b3).setVisibility(View.GONE);
                            r2.setVisibility(View.VISIBLE);
                            r3.setVisibility(View.VISIBLE);
                            isTapped1[0] = false;
                            r1.setChecked(false);
                        }

                    }
                });
                r2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isTapped2[0]==false) {
                            findViewById(R.id.e2).setVisibility(View.VISIBLE);
                            findViewById(R.id.b4).setVisibility(View.VISIBLE);
                            findViewById(R.id.gg2).setVisibility(View.VISIBLE);
                            e2 = findViewById(R.id.e2);
                            r1.setVisibility(View.GONE);
                            findViewById(R.id.r3).setVisibility(View.GONE);
                            RadioButton he = findViewById(R.id.he);
                            RadioButton she = findViewById(R.id.she);

                            findViewById(R.id.b4).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String name = e2.getText().toString();

                                    intent5.putExtra("full_name",name);
                                    MainActivity2.name = name;
                                    intent5.putExtra("var",11);
                                    MainActivity2.var = 11;
                                    if (he.isChecked()) {
                                        intent5.putExtra("radiochosen",1);
                                    }
                                    if (she.isChecked()) {
                                        intent5.putExtra("radiochosen",2);
                                    }
                                    startActivity(intent5);
                                }
                            });
                            isTapped2[0]=true;

                        }
                        else {
                            findViewById(R.id.e2).setVisibility(View.GONE);
                            findViewById(R.id.b4).setVisibility(View.GONE);
                            findViewById(R.id.gg2).setVisibility(View.GONE);
                            r1.setVisibility(View.VISIBLE);
                            r3.setVisibility(View.VISIBLE);
                            isTapped2[0] = false;
                            r2.setChecked(false);

                        }
                    }
                });
                r3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isTapped3[0]==false) {
                            r1.setVisibility(View.GONE);
                            r2.setVisibility(View.GONE);
                            findViewById(R.id.next33_button).setVisibility(View.VISIBLE);
                            findViewById(R.id.next33_button).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    intent5.putExtra("var",12);
                                    MainActivity2.var = 12;
                                    startActivity(intent5);
                                }
                            });
                            isTapped3[0]=true;
                        }
                        else {
                            findViewById(R.id.next33_button).setVisibility(View.GONE);
                            r1.setVisibility(View.VISIBLE);
                            r2.setVisibility(View.VISIBLE);
                            isTapped3[0] = false;
                            r3.setChecked(false);
                        }
                    }
                });
            }

        } );
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
