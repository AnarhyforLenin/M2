package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class NoteDetailsActivity extends AppCompatActivity {

    private static final String EXTRA_NOTE = "NoteDetailsActivity.EXTRA_NOTE";

    private Note note;

    private EditText editText;

    private RadioGroup radioGroup;
    private RadioButton t1, t2, t3;

    private int selectedTag = 0;


    public static void start(Activity caller, Note note) {
        Intent intent = new Intent(caller, NoteDetailsActivity.class);

        if (note != null) {
            intent.putExtra(EXTRA_NOTE, note);

        }
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_note_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle("Напоминание");

        editText = findViewById(R.id.text);

        radioGroup = findViewById(R.id.radio_group);

        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);


        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTag = Tag.red;
            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTag = Tag.blue;
            }
        });

        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTag = Tag.green;
            }
        });

        findViewById(R.id.t_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTag = 0;
                radioGroup.clearCheck();
            }
        });

        if (getIntent().hasExtra(EXTRA_NOTE)) {
            note = getIntent().getParcelableExtra(EXTRA_NOTE);
            editText.setText(note.text);
            if (Tag.red == note.tag) {
                radioGroup.check(R.id.t1);
            } else if (Tag.blue == note.tag) {
                radioGroup.check(R.id.t2);
            } else if (Tag.green == note.tag) {
                radioGroup.check(R.id.t3);
            }
        } else {
            note = new Note();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                if (editText.getText().length() > 0) {
                    note.text = editText.getText().toString();
                    note.done = false;
                    note.timestamp = System.currentTimeMillis();
                    note.tag = selectedTag;
                    if (getIntent().hasExtra(EXTRA_NOTE)) {
                        App.getInstance().getNoteDao().update(note);
                    } else {
                        App.getInstance().getNoteDao().insert(note);
                    }
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
