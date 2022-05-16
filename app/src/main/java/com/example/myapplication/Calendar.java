
package com.example.myapplication;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
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
    private ArrayList<ImageButton> buttons = new ArrayList<>();
    private ArrayList<ColorDrawable> colors = new ArrayList<>();
    private ArrayList<Drawable> frames = new ArrayList<>();
    private HashMap<Date, ColorDrawable> markedDates = new HashMap<>();
    private final CaldroidFragment caldroidFragment = new CaldroidFragment();
    private Date selectedDate = new Date();
    private ImageButton selectedButton;
    private ImageButton selectedTriangleButton;
    private Drawable currentFrame;
    private ColorDrawable currentColor;
    private SharedPreferences preferences;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("com.example.protoitpe", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_calendar);

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

        colors = new ArrayList<>(Arrays.asList(
                new ColorDrawable(getResources().getColor(R.color.light_blue, null)),
                new ColorDrawable(getResources().getColor(R.color.green, null)),
                new ColorDrawable(getResources().getColor(R.color.pink, null)),
                new ColorDrawable(getResources().getColor(R.color.yellow, null))
        ));

        frames = new ArrayList<>(Arrays.asList(
                ResourcesCompat.getDrawable(getResources(), R.drawable.blue_select_frame, null),
                ResourcesCompat.getDrawable(getResources(), R.drawable.green_select_frame, null),
                ResourcesCompat.getDrawable(getResources(), R.drawable.pink_select_frame, null),
                ResourcesCompat.getDrawable(getResources(), R.drawable.yellow_select_frame, null),
                ResourcesCompat.getDrawable(getResources(), R.drawable.blue_frame, null)
        ));

        buttons = new ArrayList<>(Arrays.asList(
                findViewById(R.id.blueButton),
                findViewById(R.id.greenButton),
                findViewById(R.id.pinkButton),
                findViewById(R.id.yellowButton),
                findViewById(R.id.cancelButton)
        ));

        buttons.forEach(x -> x.setOnClickListener(this));
        buttons.forEach(x -> x.setOnLongClickListener(this));

        buttons.get(0).setBackgroundResource(R.drawable.light_blue);
        buttons.get(1).setBackgroundResource(R.drawable.green);
        buttons.get(2).setBackgroundResource(R.drawable.pink);
        buttons.get(3).setBackgroundResource(R.drawable.yellow);
        buttons.get(4).setBackgroundResource(R.drawable.cancel);
        selectedDate = new Date(preferences.getLong("selectedDate", System.currentTimeMillis())); //check
        selectedButton = this.findViewById(preferences.getInt("selectedButtonId", -1));
        selectedTriangleButton = this.findViewById(preferences.getInt("selectedTriangleButtonId", -1));
        this.readHashMapOfDates();
        currentFrame = frames.get(preferences.getInt("currentFrame", 4));        //frames.indexOf(currentFrame)
        currentColor = preferences.getInt("currentColor", -1) != -1 ? colors.get(preferences.getInt("currentColor", -1)) : null;        //colors.indexOf(currentColor)

        for (Date date : markedDates.keySet()) {
            if (Objects.equals(this.selectedDate, date)) {
                int index = colors.indexOf(markedDates.get(date));
                currentFrame = frames.get(index);
                selectedButton = buttons.get(index);
            }
            caldroidFragment.setBackgroundDrawableForDate(markedDates.get(date), date);
        }
        this.toggleButton(selectedTriangleButton, 0);
        selectedTriangleButton = null;
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

                editor = preferences.edit();
                if (currentColor != null)
                    caldroidFragment.setBackgroundDrawableForDate(currentColor, selectedDate);
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
                            caldroidFragment.refreshView();
                            editor.putLong("selectedDate", selectedDate.getTime());
                            editor.putInt("currentFrame", frames.indexOf(currentFrame));
                            editor.putInt("currentColor", colors.indexOf(currentColor));
                            editor.putInt("selectedButton", selectedButton == null ? -1 : selectedButton.getId());
                            editor.apply();
                            return;
                        }
                    }
                }
                currentFrame = frames.get(4);

                caldroidFragment.setBackgroundDrawableForDate(currentFrame, selectedDate);
                caldroidFragment.refreshView();
                editor.putLong("selectedDate", selectedDate.getTime());
                editor.putInt("selectedButton", selectedButton == null ? -1 : selectedButton.getId());
                editor.putInt("currentFrame", frames.indexOf(currentFrame));
                editor.putInt("currentColor", colors.indexOf(currentColor));
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
        editor.putInt("currentColor", currentColor == null ? -1 : colors.indexOf(currentColor));
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
            currentColor = null;
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
            editor.putInt("currentColor", currentColor == null ? -1 : colors.indexOf(currentColor));
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
        editor.putInt("currentColor", currentColor == null ? -1 : colors.indexOf(currentColor));
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
                button.setBackgroundResource(state != 0 ? state == 1 ? R.drawable.blue_active : R.drawable.blue_triangle : R.drawable.light_blue);
                break;
            case R.id.greenButton:
                button.setBackgroundResource(state != 0 ? state == 1 ? R.drawable.green_active : R.drawable.green_triangle : R.drawable.green);
                break;
            case R.id.pinkButton:
                button.setBackgroundResource(state != 0 ? state == 1 ? R.drawable.pink_active : R.drawable.pink_triangle : R.drawable.pink);
                break;
            case R.id.yellowButton:
                button.setBackgroundResource(state != 0 ? state == 1 ? R.drawable.yellow_active : R.drawable.yellow_triangle : R.drawable.yellow);
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
            editor.putInt(("Color" + i), colors.indexOf(markedDates.get(markedDate)));
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
            markedDates.put(getDateFromKey("Date" + i), colors.get(preferences.getInt(("Color" + i), 0)));
    }
    private void holdButton(int num) {
        if (Objects.equals(selectedTriangleButton, buttons.get(num))) {
            for (Date date : markedDates.keySet()) {
                if (Objects.equals(this.selectedDate, date)) {
                    int index = colors.indexOf(markedDates.get(date));
                    currentFrame = frames.get(index);
                    selectedButton = buttons.get(index);
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
        selectedTriangleButton = buttons.get(num);
        toggleButton(selectedButton, 0);
        selectedButton = null;
        toggleButton(selectedTriangleButton, 2);
        for (Date date : markedDates.keySet()) {
            if (!Objects.equals(markedDates.get(date), colors.get(num))) {
                if (Objects.equals(this.selectedDate, date))
                    currentFrame = frames.get(4);
                else
                    caldroidFragment.clearBackgroundDrawableForDate(date);
            }
            else if (Objects.equals(markedDates.get(date), colors.get(num)) && !Objects.equals(this.selectedDate, date))
                caldroidFragment.setBackgroundDrawableForDate(colors.get(num), date);
            else {
                currentFrame = frames.get(num);
                currentColor = colors.get(num);
            }
        }
    }
    private void clickButton(int num) {
        currentColor = colors.get(num);
        markedDates.remove(selectedDate);
        if (selectedButton == buttons.get(num)) {
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
            editor.putInt("currentColor", currentColor == null ? -1 : colors.indexOf(currentColor));
            editor.apply();
            return;
        }
        currentFrame = frames.get(num);
        selectedButton = buttons.get(num);
        markedDates.put(selectedDate, colors.get(num));
    }
    private boolean selectDate(int num, Date date) {
        if (Objects.equals(markedDates.get(date), colors.get(num))
                && (Objects.equals(selectedTriangleButton, buttons.get(num))
                || selectedTriangleButton == null)) {
            if (selectedTriangleButton == null)
                selectedButton = buttons.get(num);
            currentColor = colors.get(num);
            currentFrame = frames.get(num);
            return true;
        }
        return false;
    }
}
