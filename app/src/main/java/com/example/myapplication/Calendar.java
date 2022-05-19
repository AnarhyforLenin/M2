

package com.example.myapplication;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class Calendar extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private ArrayList<ImageButton> buttons1 = new ArrayList<>();
    private ArrayList<ImageButton> buttons2 = new ArrayList<>();
    private ArrayList<Drawable> pictures = new ArrayList<>();
    private ArrayList<LayerDrawable> pictures2 = new ArrayList<>();
    private ArrayList<LayerDrawable> frames = new ArrayList<>();
    private ArrayList<LayerDrawable> frames2 = new ArrayList<>();
    private HashMap<Date, Drawable> markedDates = new HashMap<>();
    private final CaldroidFragment caldroidFragment = new CaldroidFragment();
    private Date selectedDate = new Date();
    private Date selectedDate2 = new Date();
    private ImageButton selectedButton;
    private ImageButton selectedButton2;
    private ImageButton selectedTriangleButton;
    private ImageButton selectedTriangleButton2;
    private LayerDrawable currentFrame;
    private LayerDrawable currentFrame2;
    private Drawable currentPicture;
    private Drawable currentPicture2;
    private SharedPreferences preferences;
    private SharedPreferences preferences2;
    private RadioButton radio1, radio2;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("com.example.protoitpe", Context.MODE_PRIVATE);
        preferences2 = getSharedPreferences("com.example.protoitpe", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_calendar);

        final boolean[] isTapped1 = {false};
        final boolean[] isTapped2 = {false};

        Bundle args = new Bundle();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(java.util.Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(java.util.Calendar.YEAR));
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
        args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);
        caldroidFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.ConstraintLayout, caldroidFragment);
        fragmentTransaction.commit();

        pictures = new ArrayList<>(Arrays.asList(

                ResourcesCompat.getDrawable(getResources(), R.drawable.pill3, null),
                ResourcesCompat.getDrawable(getResources(), R.drawable.pill1, null),
                ResourcesCompat.getDrawable(getResources(), R.drawable.pill5, null),
                ResourcesCompat.getDrawable(getResources(), R.drawable.pills2, null),
                ResourcesCompat.getDrawable(getResources(), R.drawable.pill5, null)
        ));
/*
        pictures2 = new ArrayList<>(Arrays.asList(

                (LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.mood_ney, null),
                (LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.mood_sad, null),
                (LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.mood_strange, null),
                (LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.mood_happy, null),
                (LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.mood_ney, null)
        ));
*/
        frames = new ArrayList<>(Arrays.asList(
                (LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.blue_pic2_frame_layer, null),
                (LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.blue_pic3_frame_layer, null),
                (LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.blue_pic5_frame_layer, null),
                (LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.blue_pic1_frame_layer, null),
                (LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.red_border_dark3, null)
        ));
/*
        frames2 = new ArrayList<>(Arrays.asList(
                (LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.ney_selected, null),
                (LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.sad_selected, null),
                (LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.strange_selected, null),
                (LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.happy_selected, null),
                (LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.red_border_dark3, null)
        ));
*/
        buttons1 = new ArrayList<>(Arrays.asList(
                findViewById(R.id.blueButton),
                findViewById(R.id.greenButton),
                findViewById(R.id.pinkButton),
                findViewById(R.id.yellowButton),
                findViewById(R.id.cancelButton)
        ));
        buttons2 = new ArrayList<>(Arrays.asList(
                findViewById(R.id.neyButton),
                findViewById(R.id.sadButton),
                findViewById(R.id.strangeButton),
                findViewById(R.id.happyButton),
                findViewById(R.id.cancelButton2)
        ));

        radio1 = findViewById(R.id.radio_pills);
        radio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTapped1[0] == false) {
                    buttons1.forEach(x -> x.setVisibility(View.VISIBLE));
                    isTapped1[0]=true;
                } else {
                    buttons1.forEach(x -> x.setVisibility(View.GONE));
                    isTapped1[0]=false;
                    radio1.setChecked(false);
                }
            }
        });
        radio2 = findViewById(R.id.radio_mood);
        radio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTapped2[0] == false) {
                    buttons2.forEach(x -> x.setVisibility(View.VISIBLE));
                    isTapped2[0]=true;
                } else {
                    buttons2.forEach(x -> x.setVisibility(View.GONE));
                    isTapped2[0]=false;
                    radio2.setChecked(false);
                }
            }
        });

        buttons1.forEach(x -> x.setOnClickListener(this));
        buttons1.forEach(x -> x.setOnLongClickListener(this));

        buttons1.get(0).setBackgroundResource(R.drawable.pill3);
        buttons1.get(1).setBackgroundResource(R.drawable.pill1);
        buttons1.get(2).setBackgroundResource(R.drawable.pill5);
        buttons1.get(3).setBackgroundResource(R.drawable.pills2);
        buttons1.get(4).setBackgroundResource(R.drawable.cancel);


        selectedDate = new Date(preferences.getLong("selectedDate", System.currentTimeMillis())); //check
        //selectedDate2 = new Date(preferences2.getLong("selectedDate2", System.currentTimeMillis())); //check
        selectedButton = this.findViewById(preferences.getInt("selectedButtonId", -1));
        //selectedButton2 = this.findViewById(preferences2.getInt("selectedButtonId2", -1));
        selectedTriangleButton = this.findViewById(preferences.getInt("selectedTriangleButtonId", -1));
        //selectedTriangleButton2 = this.findViewById(preferences2.getInt("selectedTriangleButtonId2", -1));
        this.readHashMapOfDates();
        currentFrame = frames.get(preferences.getInt("currentFrame", 4));        //frames.indexOf(currentFrame)
        //currentFrame2 = frames.get(preferences.getInt("currentFrame2", 4));        //frames.indexOf(currentFrame)
        currentPicture = preferences.getInt("currentColor", -1) != -1 ? pictures.get(preferences.getInt("currentColor", -1)) : null;        //colors.indexOf(currentColor)
        //currentPicture2 = preferences2.getInt("currentColor2", -1) != -1 ? pictures2.get(preferences2.getInt("currentColor2", -1)) : null;        //colors.indexOf(currentColor)

        for (Date date : markedDates.keySet()) {
            if (Objects.equals(this.selectedDate, date)) {
                int index = pictures.indexOf(markedDates.get(date));
                currentFrame = frames.get(index);
                selectedButton = buttons1.get(index);
            }
            caldroidFragment.setBackgroundDrawableForDate(markedDates.get(date), date);
        }
        this.toggleButton(selectedTriangleButton, 0);
        //this.toggleButton(selectedTriangleButton2, 0);
        selectedTriangleButton = null;
        //selectedTriangleButton2 = null;
        if (selectedButton != null)
            this.toggleButton(selectedButton, 1);
        caldroidFragment.setBackgroundDrawableForDate(currentFrame, selectedDate);


        if (selectedButton != null)
            this.toggleButton(selectedButton, 1);
        if (selectedTriangleButton != null)
            this.toggleButton(selectedTriangleButton, 2);


        final CaldroidListener listener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                SharedPreferences.Editor editor;
                SharedPreferences.Editor editor2;

                editor = preferences.edit();
                //editor2 = preferences2.edit();
                if (currentPicture != null)
                    caldroidFragment.setBackgroundDrawableForDate(currentPicture, selectedDate);
                if (currentFrame == frames.get(4))
                    caldroidFragment.clearBackgroundDrawableForDate(selectedDate);
                if (selectedButton != null) {
                    toggleButton(selectedButton, 0);
                    selectedButton = null;
                }
                selectedDate = date;
                if (markedDates.containsKey(date)) {
                    for (int i = 0; i < 4; i++) {
                        if (selectDate(i, date))
                        {
                            toggleButton(selectedButton, 1);

                            caldroidFragment.setBackgroundDrawableForDate(currentFrame, selectedDate);
                            caldroidFragment.setBackgroundDrawableForDate(currentFrame, selectedDate2);
                            caldroidFragment.refreshView();
                            editor.putLong("selectedDate", selectedDate.getTime());
                            editor.putInt("currentFrame", frames.indexOf(currentFrame));
                            editor.putInt("currentColor", pictures.indexOf(currentPicture));
                            editor.putInt("selectedButton", selectedButton == null ? -1 : selectedButton.getId());
                            editor.apply();
                            return;
                        }
                    }
                }
                currentFrame = frames.get(4);

                caldroidFragment.setBackgroundDrawableForDate(currentFrame, selectedDate);
                caldroidFragment.refreshView();
                caldroidFragment.setBackgroundDrawableForDate(currentFrame, selectedDate2);
                caldroidFragment.refreshView();
                editor.putLong("selectedDate", selectedDate.getTime());
                editor.putInt("selectedButton", selectedButton == null ? -1 : selectedButton.getId());
                editor.putInt("currentFrame", frames.indexOf(currentFrame));
                editor.putInt("currentColor", pictures.indexOf(currentPicture));
                editor.apply();
            }
        };
        caldroidFragment.setCaldroidListener(listener);
    }
    @Override
    public boolean onLongClick(View v) {
        if (markedDates.isEmpty()) return true;
        if (selectedTriangleButton != null)
            this.toggleButton(selectedTriangleButton, 0);
        switch (v.getId()) {
            case R.id.blueButton:
                holdButton(0);
                break;
            case R.id.greenButton:
                holdButton(1);
                break;
            case R.id.pinkButton:
                holdButton(2);
                break;
            case R.id.yellowButton:
                holdButton(3);
                break;
        }
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putInt("selectedButton", selectedButton == null ? -1 : selectedButton.getId());
        editor.putInt("selectedTriangleButton", selectedTriangleButton == null ? -1 : selectedTriangleButton.getId());
        editor.putInt("currentFrame", frames.indexOf(currentFrame));
        editor.putInt("currentColor", currentPicture == null ? -1 : pictures.indexOf(currentPicture));
        editor.apply();
        caldroidFragment.setBackgroundDrawableForDate(currentFrame, selectedDate);
        //caldroidFragment.setBackgroundDrawableForDate(ResourcesCompat.getDrawable(getResources(), R.drawable.blue_frame, null), selectedDate);
        caldroidFragment.refreshView();
        return true;
    }
    @Override
    public void onClick(View v) {
        if (selectedButton != null)
            this.toggleButton(selectedButton, 0);
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        if (v.getId() == R.id.cancelButton) {
            currentPicture = null;
            currentFrame = frames.get(4);
            for (Date date : markedDates.keySet())
                caldroidFragment.clearBackgroundDrawableForDate(date);
            markedDates.clear();
            toggleButton(selectedButton, 0);
            selectedButton = null;
            toggleButton(selectedTriangleButton, 0);
            selectedTriangleButton = null;
            caldroidFragment.setBackgroundDrawableForDate(currentFrame, selectedDate);
            editor.putInt("selectedButton", selectedButton == null ? -1 : selectedButton.getId());
            editor.putInt("selectedTriangleButton", selectedTriangleButton == null ? -1 : selectedButton.getId());
            editor.putInt("currentFrame", frames.indexOf(currentFrame));
            editor.putInt("currentColor", currentPicture == null ? -1 : pictures.indexOf(currentPicture));
            editor.apply();
            this.updateHashMapOfDates();
            caldroidFragment.refreshView();
            return;
        }
        if (selectedTriangleButton != null) return;

        switch(v.getId()) {
            case R.id.blueButton:
                clickButton(0);
                break;
            case R.id.greenButton:
                clickButton(1);
                break;
            case R.id.pinkButton:
                clickButton(2);
                break;
            case R.id.yellowButton:
                clickButton(3);
                break;
            default:
                return;
        }
        editor.putInt("selectedButton", selectedButton == null ? -1 : selectedButton.getId());
        editor.putInt("currentFrame", frames.indexOf(currentFrame));
        editor.putInt("currentColor", currentPicture == null ? -1 : pictures.indexOf(currentPicture));
        toggleButton(selectedButton, 1);
        caldroidFragment.setBackgroundDrawableForDate(currentFrame, selectedDate);
        editor.apply();
        this.updateHashMapOfDates();
        caldroidFragment.refreshView();
    }
    private void toggleButton(ImageButton button, int state) {
        if (button == null || state < 0 || state > 2) return;
        switch (button.getId()) {
            case R.id.blueButton:
                button.setBackgroundResource(state != 0 ? state == 1 ? R.drawable.blue_pic2_frame_layer : R.drawable.blue_triangle : R.drawable.pill3);
                break;
            case R.id.greenButton:
                button.setBackgroundResource(state != 0 ? state == 1 ? R.drawable.blue_pic3_frame_layer : R.drawable.green_triangle : R.drawable.pill1);
                break;
            case R.id.pinkButton:
                button.setBackgroundResource(state != 0 ? state == 1 ? R.drawable.blue_pic5_frame_layer : R.drawable.pink_triangle : R.drawable.pill5);
                break;
            case R.id.yellowButton:
                button.setBackgroundResource(state != 0 ? state == 1 ? R.drawable.blue_pic1_frame_layer : R.drawable.yellow_triangle : R.drawable.pills2);
                break;
            case R.id.cancelButton:
                button.setBackgroundResource(R.drawable.cancel);
                break;
            default:
                return;
        }
        caldroidFragment.refreshView();
    }
    private void putDateToSharedPreference(String key, Date obj) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        editor.putString(key, json);
        editor.apply();
    }
    private Date getDateFromKey(String key) {
        Gson gson = new Gson();
        String json = preferences.getString(key, "");
        Date obj = gson.fromJson(json, Date.class);
        if (obj == null) return new Date();
        return obj;
    }
    private void updateHashMapOfDates() {
        SharedPreferences.Editor editor;
        for (int i = 0; i < markedDates.size(); i++) {
            Date markedDate = (Date) markedDates.keySet().toArray()[i];
            putDateToSharedPreference(("Date" + i), markedDate);
            editor = preferences.edit();
            editor.putInt(("Color" + i), pictures.indexOf(markedDates.get(markedDate)));
            editor.apply();
        }
        editor = preferences.edit();
        editor.putInt("Dates", markedDates.size());
        editor.apply();
    }
    private void readHashMapOfDates() {
        markedDates = new HashMap<>();
        int counter = preferences.getInt("Dates", 0);
        for (int i = 0; i < counter; i++)
            markedDates.put(getDateFromKey("Date" + i), pictures.get(preferences.getInt(("Color" + i), 0)));
    }
    private void holdButton(int num) {
        if (Objects.equals(selectedTriangleButton, buttons1.get(num))) {
            for (Date date : markedDates.keySet()) {
                if (Objects.equals(this.selectedDate, date)) {
                    int index = pictures.indexOf(markedDates.get(date));
                    currentFrame = frames.get(index);
                    selectedButton = buttons1.get(index);
                }
                caldroidFragment.setBackgroundDrawableForDate(markedDates.get(date), date);
            }
            this.toggleButton(selectedTriangleButton, 0);
            selectedTriangleButton = null;
            if (selectedButton != null)
                this.toggleButton(selectedButton, 1);
            SharedPreferences.Editor editor;
            editor = preferences.edit();
            editor.putInt("selectedButton", selectedButton == null ? -1 : selectedButton.getId());
            editor.putInt("selectedTriangleButton", selectedTriangleButton == null ? -1 : selectedTriangleButton.getId());
            editor.apply();
            caldroidFragment.setBackgroundDrawableForDate(currentFrame, selectedDate);
            caldroidFragment.refreshView();
            return;
        }

        selectedTriangleButton = buttons1.get(num);
        toggleButton(selectedButton, 0);
        selectedButton = null;
        toggleButton(selectedTriangleButton, 2);
        for (Date date : markedDates.keySet()) {
            if (!Objects.equals(markedDates.get(date), pictures.get(num))) {
                if (Objects.equals(this.selectedDate, date))
                    currentFrame = frames.get(4);
                else
                    caldroidFragment.clearBackgroundDrawableForDate(date);
            }
            else if (Objects.equals(markedDates.get(date), pictures.get(num)) && !Objects.equals(this.selectedDate, date))
                caldroidFragment.setBackgroundDrawableForDate(pictures.get(num), date);
            else {
                currentFrame = frames.get(num);
                currentPicture = pictures.get(num);
            }
        }
    }
    private void clickButton(int num) {
        currentPicture = pictures.get(num);
        markedDates.remove(selectedDate);
        if (selectedButton == buttons1.get(num)) {
            SharedPreferences.Editor editor;
            editor = preferences.edit();
            this.toggleButton(selectedButton, 0);
            this.selectedButton = null;
            currentFrame = frames.get(4);
            caldroidFragment.setBackgroundDrawableForDate(currentFrame, selectedDate);
            this.updateHashMapOfDates();
            caldroidFragment.refreshView();
            editor.putInt("selectedButton", selectedButton == null ? -1 : selectedButton.getId());
            editor.putInt("currentFrame", frames.indexOf(currentFrame));
            editor.putInt("currentColor", currentPicture == null ? -1 : pictures.indexOf(currentPicture));
            editor.apply();
            return;
        }
        currentFrame = frames.get(num);
        selectedButton = buttons1.get(num);
        markedDates.put(selectedDate, pictures.get(num));
    }
    private boolean selectDate(int num, Date date) {
        if (Objects.equals(markedDates.get(date), pictures.get(num))
                && (Objects.equals(selectedTriangleButton, buttons1.get(num))
                || selectedTriangleButton == null)) {
            if (selectedTriangleButton == null)
                selectedButton = buttons1.get(num);
            currentPicture = pictures.get(num);
            currentFrame = frames.get(num);
            return true;
        }
        return false;
    }
}

