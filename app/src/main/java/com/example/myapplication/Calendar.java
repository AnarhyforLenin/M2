package com.example.myapplication;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class Calendar extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
	private ArrayList<ImageButton> buttonsPills = new ArrayList<>();
	private ArrayList<ImageButton> buttonsMood = new ArrayList<>();
	private ArrayList<Drawable> layersPills = new ArrayList<>();
	private ArrayList<Drawable> layersMood = new ArrayList<>();
	private ArrayList<LayerDrawable> frames = new ArrayList<>();
	private HashMap<Date, LayerDrawable> markedDates = new HashMap<>();
	private final CaldroidFragment CALDROID_FRAGMENT = new CaldroidFragment();
	private Date selectedDate = new Date();
	private ImageButton selectedPillButton;
	private ImageButton selectedPillTriangleButton;
	private ImageButton selectedMoodButton;
	private ImageButton selectedMoodTriangleButton;
	private LayerDrawable currentFrame;
	//private Drawable currentPicturePills;
	//private Drawable currentPictureMood;
	private SharedPreferences preferences;
	private RadioButton radioPills;
	private RadioButton radioMood;
	private LayerDrawable currentLayer;

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferences = getSharedPreferences("com.example.protoitpe", Context.MODE_PRIVATE);
		setContentView(R.layout.activity_calendar);

		AtomicBoolean isTappedPill = new AtomicBoolean(false);
		AtomicBoolean isTappedMood = new AtomicBoolean(false);

		Bundle args = new Bundle();
		java.util.Calendar cal = java.util.Calendar.getInstance();
		args.putInt(CaldroidFragment.MONTH, cal.get(java.util.Calendar.MONTH) + 1);
		args.putInt(CaldroidFragment.YEAR, cal.get(java.util.Calendar.YEAR));
		args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
		args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);
		CALDROID_FRAGMENT.setArguments(args);
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.ConstraintLayout, CALDROID_FRAGMENT);
		fragmentTransaction.commit();

		layersPills = new ArrayList<>(Arrays.asList(
				ResourcesCompat.getDrawable(getResources(), R.drawable.pill11_3, null),
				ResourcesCompat.getDrawable(getResources(), R.drawable.pill21_2, null),
				ResourcesCompat.getDrawable(getResources(), R.drawable.pill31_2, null),
				ResourcesCompat.getDrawable(getResources(), R.drawable.pill41_3, null),
				ResourcesCompat.getDrawable(getResources(), R.drawable.pill5, null)
		));

		layersMood = new ArrayList<>(Arrays.asList(
				ResourcesCompat.getDrawable(getResources(), R.drawable.happy_n, null),
				ResourcesCompat.getDrawable(getResources(), R.drawable.sad_n, null),
				ResourcesCompat.getDrawable(getResources(), R.drawable.ney_n, null),
				ResourcesCompat.getDrawable(getResources(), R.drawable.strange_n, null),
				ResourcesCompat.getDrawable(getResources(), R.drawable.pill5, null)
		));

		frames = new ArrayList<>(Arrays.asList(
				(LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.blue_pic2_frame_layer, null),
				(LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.blue_pic3_frame_layer, null),
				(LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.blue_pic5_frame_layer, null),
				(LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.blue_pic1_frame_layer, null),
				(LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.red_border_dark3, null)
		));
//        currentLayer = frames.get(0);
//        removeLayer(currentLayer, ResourcesCompat.getDrawable(getResources(), R.drawable.pill3, null));
		buttonsPills = new ArrayList<>(Arrays.asList(
				findViewById(R.id.blueButton),
				findViewById(R.id.greenButton),
				findViewById(R.id.pinkButton),
				findViewById(R.id.yellowButton),
				findViewById(R.id.cancelButton)
		));

		buttonsMood = new ArrayList<>(Arrays.asList(
				findViewById(R.id.neyButton),
				findViewById(R.id.sadButton),
				findViewById(R.id.strangeButton),
				findViewById(R.id.happyButton),
				findViewById(R.id.cancelButton2)
		));

		radioPills = findViewById(R.id.radio_pills);
		radioPills.setOnClickListener(v -> {
			if (!isTappedPill.get()) {
				buttonsPills.forEach(x -> x.setVisibility(View.VISIBLE));
				isTappedPill.set(true);
			} else {
				buttonsPills.forEach(x -> x.setVisibility(View.GONE));
				isTappedPill.set(false);
				radioPills.setChecked(false);
			}
		});
		radioMood = findViewById(R.id.radio_mood);
		radioMood.setOnClickListener(v -> {
			if (!isTappedMood.get()) {
				buttonsMood.forEach(x -> x.setVisibility(View.VISIBLE));
				isTappedMood.set(true);
			} else {
				buttonsMood.forEach(x -> x.setVisibility(View.GONE));
				isTappedMood.set(false);
				radioMood.setChecked(false);
			}
		});

		buttonsPills.forEach(x -> x.setOnClickListener(this));
		buttonsPills.forEach(x -> x.setOnLongClickListener(this));

		buttonsMood.forEach(x -> x.setOnClickListener(this));
		buttonsMood.forEach(x -> x.setOnLongClickListener(this));

		buttonsPills.get(0).setBackgroundResource(R.drawable.pill11);
		buttonsPills.get(1).setBackgroundResource(R.drawable.pill21);
		buttonsPills.get(2).setBackgroundResource(R.drawable.pill31);
		buttonsPills.get(3).setBackgroundResource(R.drawable.pill41);
		buttonsPills.get(4).setBackgroundResource(R.drawable.cancel);

		selectedDate = new Date(preferences.getLong("selectedDate", System.currentTimeMillis())); //check

		selectedPillButton = this.findViewById(preferences.getInt("selectedButtonId", -1));
		selectedPillTriangleButton = this.findViewById(preferences.getInt("selectedTriangleButtonId", -1));
		this.readHashMapOfDates();

		currentFrame = frames.get(preferences.getInt("currentFrame", 4));        //frames.indexOf(currentFrame)
		//currentPicturePills = preferences.getInt("currentColor", -1) != -1 ? layersPills.get(preferences.getInt("currentColor", -1)) : null;        //colors.indexOf(currentColor)

		for (Date date : markedDates.keySet()) {
			if (Objects.equals(this.selectedDate, date)) {
				int index = layersPills.indexOf(markedDates.get(date));
				currentFrame = frames.get(index);
				selectedPillButton = buttonsPills.get(index);
			}
			CALDROID_FRAGMENT.setBackgroundDrawableForDate(markedDates.get(date), date);
		}
		if (!markedDates.containsKey(selectedDate))
			markedDates.put(selectedDate, drawableToLayerDrawable(frames.get(4)));
		this.toggleButton(selectedPillTriangleButton, 0);
		selectedPillTriangleButton = null;
		if (selectedPillButton != null)
			this.toggleButton(selectedPillButton, 1);
		CALDROID_FRAGMENT.setBackgroundDrawableForDate(currentFrame, selectedDate);

		if (selectedPillButton != null)
			this.toggleButton(selectedPillButton, 1);
		if (selectedPillTriangleButton != null)
			this.toggleButton(selectedPillTriangleButton, 2);

		final CaldroidListener listener = new CaldroidListener() {
			@Override
			public void onSelectDate(Date date, View view) {
				SharedPreferences.Editor editor;
				editor = preferences.edit();
//				if (currentLayer != null)
//					CALDROID_FRAGMENT.setBackgroundDrawableForDate(currentLayer, selectedDate);
//				if (currentPicturePills != null)
//					CALDROID_FRAGMENT.setBackgroundDrawableForDate(currentPicturePills, selectedDate);
//				if (currentFrame == frames.get(4))
//					CALDROID_FRAGMENT.clearBackgroundDrawableForDate(selectedDate);
				if (markedDates.containsKey(selectedDate))
				{
					ArrayList<Drawable> layers = layerDrawableToDrawables(Objects.requireNonNull(markedDates.get(selectedDate)));
					layers.removeIf(x -> Objects.equals(x, frames.get(4)));
					markedDates.remove(selectedDate);
					if (layers.size() < 1)
						CALDROID_FRAGMENT.clearBackgroundDrawableForDate(selectedDate);
					else
					{
						LayerDrawable tempLayer = drawablesToLayerDrawable(layers);
						CALDROID_FRAGMENT.setBackgroundDrawableForDate(tempLayer, selectedDate);
						markedDates.put(selectedDate, tempLayer);
					}
				}
				if (markedDates.containsKey(date))
				{
					ArrayList<Drawable> layers = new ArrayList<>();
					layers.add(frames.get(4));
					layers.addAll(layerDrawableToDrawables(Objects.requireNonNull(markedDates.get(date))));
					LayerDrawable tempLayer = drawablesToLayerDrawable(layers);
					markedDates.remove(date);
					CALDROID_FRAGMENT.setBackgroundDrawableForDate(tempLayer, date);
					markedDates.put(date, tempLayer);
				}
				else
				{
					LayerDrawable tempLayer = drawableToLayerDrawable(frames.get(4));
					CALDROID_FRAGMENT.setBackgroundDrawableForDate(tempLayer, date);
					markedDates.put(date, tempLayer);
				}
//				if (markedDates.containsKey(selectedDate)) {
//					LayerDrawable tempLayer = removeLayer(Objects.requireNonNull(markedDates.get(selectedDate)), frames.get(4));
//					markedDates.remove(selectedDate);
//					markedDates.put(selectedDate, tempLayer);
//					CALDROID_FRAGMENT.setBackgroundDrawableForDate(tempLayer, selectedDate);
//				}
//				if (markedDates.containsKey(date)) {
//					ArrayList<Drawable> drawables = new ArrayList<>();
//					drawables.add(frames.get(4));
//					drawables.addAll(layerDrawableToDrawables(markedDates.get(date)));
//					markedDates.put(date, drawablesToLayerDrawable(drawables));
//				}
//				else markedDates.put(date, frames.get(4));
//				CALDROID_FRAGMENT.setBackgroundDrawableForDate(markedDates.get(date), date);

//				else
//				{
//					Drawable[] tempDrawable = new Drawable[1];
//					tempDrawable[0] = frames.get(4);
//					markedDates.put(selectedDate, new LayerDrawable(tempDrawable));
//					CALDROID_FRAGMENT.setBackgroundDrawableForDate(frames.get(4), selectedDate);
//				}
				if (selectedPillButton != null) {
					toggleButton(selectedPillButton, 0);
					selectedPillButton = null;
				}
				if (selectedMoodButton != null) {
					toggleButton(selectedMoodButton, 0);
					selectedMoodButton = null;
				}
				selectedDate = date;
//				if (markedDates.containsKey(date)) {
//					for (int i = 0; i < 4; i++) {
//						if (selectDate(i, date))
//						{
//							//if (layerDrawableToDrawables(currentLayer).contains(layersPills.forEach(x -> )))
//							ArrayList<Drawable> tempList = layerDrawableToDrawables(currentLayer);
//							for (Drawable drawable : layersPills)
//								if (tempList.contains(drawable))
//								{
//									toggleButton(selectedPillButton, 1);
//									break;
//								}
//							for (Drawable drawable : layersMood)
//								if (tempList.contains(drawable))
//								{
//									toggleButton(selectedMoodButton, 1);
//									break;
//								}
//							CALDROID_FRAGMENT.setBackgroundDrawableForDate(currentFrame, selectedDate);
//							CALDROID_FRAGMENT.refreshView();
//							editor.putLong("selectedDate", selectedDate.getTime());
//							editor.putInt("currentFrame", frames.indexOf(currentFrame));
//							//editor.putInt("currentColor", layersPills.indexOf(currentPicturePills));
//							editor.putInt("selectedButton", selectedPillButton == null ? -1 : selectedPillButton.getId());
//							editor.apply();
//							return;
//						}
//					}
//				}
				ArrayList<ImageButton> tempButtons = getButtonByDate(selectedDate);
				if (tempButtons != null && tempButtons.size() > 0)
					Objects.requireNonNull(getButtonByDate(selectedDate)).forEach(x -> toggleButton(x, 1));
				currentFrame = frames.get(4);

				//CALDROID_FRAGMENT.setBackgroundDrawableForDate(currentFrame, selectedDate);
				CALDROID_FRAGMENT.refreshView();
				editor.putLong("selectedDate", selectedDate.getTime());
				editor.putInt("selectedButton", selectedPillButton == null ? -1 : selectedPillButton.getId());
				editor.putInt("currentFrame", frames.indexOf(currentFrame));
				//editor.putInt("currentColor", layersPills.indexOf(currentPicturePills));
				editor.apply();
			}
		};
		CALDROID_FRAGMENT.setCaldroidListener(listener);
	}
	@SuppressLint("NonConstantResourceId")
	@Override
	public boolean onLongClick(View v) {
		if (markedDates.isEmpty()) return true;
		if (selectedPillTriangleButton != null)
			this.toggleButton(selectedPillTriangleButton, 0);
		switch (v.getId()) {
			case R.id.blueButton:
				holdButton(0, false);
				break;
			case R.id.greenButton:
				holdButton(1, false);
				break;
			case R.id.pinkButton:
				holdButton(2, false);
				break;
			case R.id.yellowButton:
				holdButton(3, false);
				break;

			case R.id.neyButton:
				holdButton(0, true);
				break;
			case R.id.sadButton:
				holdButton(1, true);
				break;
			case R.id.strangeButton:
				holdButton(2, true);
				break;
			case R.id.happyButton:
				holdButton(3, true);
				break;
		}
		SharedPreferences.Editor editor;
		editor = preferences.edit();
		editor.putInt("selectedButton", selectedPillButton == null ? -1 : selectedPillButton.getId());
		editor.putInt("selectedTriangleButton", selectedPillTriangleButton == null ? -1 : selectedPillTriangleButton.getId());
		editor.putInt("currentFrame", frames.indexOf(currentFrame));
		//editor.putInt("currentColor", currentPicturePills == null ? -1 : layersPills.indexOf(currentPicturePills));
		editor.apply();
		CALDROID_FRAGMENT.setBackgroundDrawableForDate(currentFrame, selectedDate);
		//caldroidFragment.setBackgroundDrawableForDate(ResourcesCompat.getDrawable(getResources(), R.drawable.blue_frame, null), selectedDate);
		CALDROID_FRAGMENT.refreshView();
		return true;
	}
	@SuppressLint("NonConstantResourceId")
	@Override
	public void onClick(View v) {
//		if (selectedPillButton != null)
//			this.toggleButton(selectedPillButton, 0);
//		if (selectedPillButton != null)
//			this.toggleButton(selectedMoodButton, 0);
		SharedPreferences.Editor editor;
		editor = preferences.edit();
		if (v.getId() == R.id.cancelButton) {
			//currentPicturePills = null;
			currentFrame = frames.get(4);
			for (Date date : markedDates.keySet())
				CALDROID_FRAGMENT.clearBackgroundDrawableForDate(date);
			markedDates.clear();
			toggleButton(selectedPillButton, 0);
			selectedPillButton = null;
			toggleButton(selectedPillTriangleButton, 0);
			selectedPillTriangleButton = null;
			CALDROID_FRAGMENT.setBackgroundDrawableForDate(currentFrame, selectedDate);
			editor.putInt("selectedButton", selectedPillButton == null ? -1 : selectedPillButton.getId());
			editor.putInt("selectedTriangleButton", selectedPillTriangleButton == null ? -1 : selectedPillButton.getId());
			editor.putInt("currentFrame", frames.indexOf(currentFrame));
			//editor.putInt("currentColor", currentPicturePills == null ? -1 : layersPills.indexOf(currentPicturePills));
			editor.apply();
			this.updateHashMapOfDates();
			CALDROID_FRAGMENT.refreshView();
			return;
		}
		if (selectedPillTriangleButton != null || selectedMoodTriangleButton != null) return;

		switch(v.getId()) {
			case R.id.blueButton:
				clickButton(0, false);
				break;
			case R.id.greenButton:
				clickButton(1, false);
				break;
			case R.id.pinkButton:
				clickButton(2, false);
				break;
			case R.id.yellowButton:
				clickButton(3, false);
				break;

			case R.id.neyButton:
				clickButton(0, true);
				break;
			case R.id.sadButton:
				clickButton(1, true);
				break;
			case R.id.strangeButton:
				clickButton(2, true);
				break;
			case R.id.happyButton:
				clickButton(3, true);
				break;
			default:
				return;
		}
		editor.putInt("selectedButton", selectedPillButton == null ? -1 : selectedPillButton.getId());
		editor.putInt("currentFrame", frames.indexOf(currentFrame));
		//editor.putInt("currentColor", currentPicturePills == null ? -1 : layersPills.indexOf(currentPicturePills));
		//CALDROID_FRAGMENT.setBackgroundDrawableForDate(currentFrame, selectedDate);
		CALDROID_FRAGMENT.setBackgroundDrawableForDate(currentLayer, selectedDate);
		editor.apply();
		this.updateHashMapOfDates();
		CALDROID_FRAGMENT.refreshView();
	}
	@SuppressLint("NonConstantResourceId")
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

			case R.id.neyButton:
				button.setBackgroundResource(state != 0 ? state == 1 ? R.drawable.ney_selected : R.drawable.blue_triangle : R.drawable.mood_ney);
				break;
			case R.id.sadButton:
				button.setBackgroundResource(state != 0 ? state == 1 ? R.drawable.sad_selected : R.drawable.green_triangle : R.drawable.mood_sad);
				break;
			case R.id.strangeButton:
				button.setBackgroundResource(state != 0 ? state == 1 ? R.drawable.strange_selected : R.drawable.pink_triangle : R.drawable.mood_strange);
				break;
			case R.id.happyButton:
				button.setBackgroundResource(state != 0 ? state == 1 ? R.drawable.happy_selected : R.drawable.yellow_triangle : R.drawable.mood_happy);
				break;
			default:
				return;
		}
		CALDROID_FRAGMENT.refreshView();
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
			editor.putInt(("Color" + i), layersPills.indexOf(markedDates.get(markedDate)));
			editor.apply();
		}
		editor = preferences.edit();
		editor.putInt("Dates", markedDates.size());
		editor.apply();
	}
	private void readHashMapOfDates() {
		markedDates = new HashMap<>();
		int counter = preferences.getInt("Dates", 0);
		//for (int i = 0; i < counter; i++)
		//markedDates.put(getDateFromKey("Date" + i), layersPills.get(preferences.getInt(("Color" + i), 0)));
	}
	private void holdButton(int num, boolean state) {
		if (Objects.equals(selectedPillTriangleButton, buttonsPills.get(num))) {
			for (Date date : markedDates.keySet()) {
				if (Objects.equals(this.selectedDate, date)) {
					int index = layersPills.indexOf(markedDates.get(date));
					currentFrame = frames.get(index);
					selectedPillButton = buttonsPills.get(index);
				}
				CALDROID_FRAGMENT.setBackgroundDrawableForDate(markedDates.get(date), date);
			}
			this.toggleButton(selectedPillTriangleButton, 0);
			selectedPillTriangleButton = null;
			if (selectedPillButton != null)
				this.toggleButton(selectedPillButton, 1);
			SharedPreferences.Editor editor;
			editor = preferences.edit();
			editor.putInt("selectedButton", selectedPillButton == null ? -1 : selectedPillButton.getId());
			editor.putInt("selectedTriangleButton", selectedPillTriangleButton == null ? -1 : selectedPillTriangleButton.getId());
			editor.apply();
			CALDROID_FRAGMENT.setBackgroundDrawableForDate(currentFrame, selectedDate);
			CALDROID_FRAGMENT.refreshView();
			return;
		}
		if (!state) {
			selectedPillTriangleButton = buttonsPills.get(num);
			toggleButton(selectedPillButton, 0);
			selectedPillButton = null;
			toggleButton(selectedPillTriangleButton, 2);
		}
		else
		{
			selectedMoodTriangleButton = buttonsMood.get(num);
			toggleButton(selectedMoodButton, 0);
			selectedMoodButton = null;
			toggleButton(selectedMoodTriangleButton, 2);
		}

//		for (Date date : markedDates.keySet()) {
//			if (!Objects.equals(markedDates.get(date), layersPills.get(num))) {
//				if (Objects.equals(this.selectedDate, date))
//					currentFrame = frames.get(4);
//				else
//					CALDROID_FRAGMENT.clearBackgroundDrawableForDate(date);
//			}
//			else if (Objects.equals(markedDates.get(date), layersPills.get(num)) && !Objects.equals(this.selectedDate, date))
//				CALDROID_FRAGMENT.setBackgroundDrawableForDate(layersPills.get(num), date);
//			else {
//				currentFrame = frames.get(num);
//				//currentPicturePills = layersPills.get(num);
//			}
//		}
		HashMap<Date, LayerDrawable> totallyNotMarkedDates = markedDates;
		for (Date date : markedDates.keySet())
		{
			ArrayList<Drawable> layers = layerDrawableToDrawables(Objects.requireNonNull(markedDates.get(date)));
			for (Drawable layer : layers)
				if (layersPills.contains(layer) == !state && (!state ? !Objects.equals(layer, layersPills.get(num)) : !Objects.equals(layer, layersMood.get(num))))
					layer.setVisible(false, true);
			totallyNotMarkedDates.remove(date);
			totallyNotMarkedDates.put(date, drawablesToLayerDrawable(layers));
			CALDROID_FRAGMENT.clearBackgroundDrawableForDate(date);
		}
		markedDates = totallyNotMarkedDates;
		ArrayList<Drawable> outLayers = new ArrayList<>();
		for (Date date : markedDates.keySet()) {
			ArrayList<Drawable> layers = layerDrawableToDrawables(Objects.requireNonNull(markedDates.get(date)));
			for (Drawable layer : layers)
			{
				if (layer.isVisible())
				{
					outLayers.add(layer);
				}
			}
			CALDROID_FRAGMENT.setBackgroundDrawableForDate(drawablesToLayerDrawable(outLayers), date);
		}
	}
	private void clickButton(int num, boolean state) {
		//currentPicturePills = layersPills.get(num);
		if (!state ? selectedPillButton != null : selectedMoodButton != null)
			this.toggleButton(!state ? selectedPillButton : selectedMoodButton, 0);
		ArrayList<Drawable> layers = new ArrayList<>();
		layers.add(frames.get(4));
		if (markedDates.containsKey(selectedDate))
			layers = layerDrawableToDrawables(Objects.requireNonNull(markedDates.get(selectedDate)));
		markedDates.remove(selectedDate);
		layers.removeIf(x -> (layersPills.contains(x) == !state && x != frames.get(4)));
		if ((!state && selectedPillButton == buttonsPills.get(num)) || (state && selectedMoodButton == buttonsMood.get(num))) {
			SharedPreferences.Editor editor;
			editor = preferences.edit();
			this.toggleButton(!state ? selectedPillButton : selectedMoodButton, 0);

			if (!state)
				this.selectedPillButton = null;
			else
				this.selectedMoodButton = null;

			currentFrame = frames.get(4);
			CALDROID_FRAGMENT.setBackgroundDrawableForDate(currentFrame, selectedDate);

			this.updateHashMapOfDates();
			CALDROID_FRAGMENT.refreshView();
			currentLayer = drawablesToLayerDrawable(layers);
			markedDates.put(selectedDate, currentLayer);

			editor.putInt("selectedButton", selectedPillButton == null ? -1 : selectedPillButton.getId());
			editor.putInt("currentFrame", frames.indexOf(currentFrame));
			//editor.putInt("currentColor", currentPicturePills == null ? -1 : layersPills.indexOf(currentPicturePills));
			editor.apply();
			return;
		}
		layers.add(!state ? layersPills.get(num) : layersMood.get(num));
		currentFrame = frames.get(num);
		if (!state)
			selectedPillButton = buttonsPills.get(num);
		else
			selectedMoodButton = buttonsMood.get(num);
		currentLayer = drawablesToLayerDrawable(layers);
		//markedDates.put(selectedDate, layersPills.get(num));
		toggleButton(!state ? selectedPillButton : selectedMoodButton, 1);
		markedDates.put(selectedDate, currentLayer);
	}
	private ArrayList<Drawable> layerDrawableToDrawables(LayerDrawable layer)
	{
		ArrayList<Drawable> layers = new ArrayList<>();
		for (int i = 0; i < layer.getNumberOfLayers(); i++)
			layers.add(layer.getDrawable(i));
		return layers;
	}
	private LayerDrawable removeLayer(@NonNull LayerDrawable layer, Drawable removableLayer)
	{
		ArrayList<Drawable> layers = layerDrawableToDrawables(layer);
		layers.remove(removableLayer);
		return drawablesToLayerDrawable(layers);
	}
	private LayerDrawable popLayer(@NonNull LayerDrawable layer)
	{
		int count = layer.getNumberOfLayers();
		if (count < 1) return layer;
		return removeLayer(layer, layer.getDrawable(count - 1));
	}
	private LayerDrawable drawablesToLayerDrawable(ArrayList<Drawable> layers)
	{
		Drawable[] drawables = new Drawable[layers.size()];
		for (int i = 0; i < drawables.length; i++)
			drawables[i] = layers.get(i);
		return new LayerDrawable(drawables);
	}
	private LayerDrawable drawableToLayerDrawable(Drawable drawable)
	{
		Drawable[] thisDrawables = new Drawable[1];
		thisDrawables[0] = drawable;
		return new LayerDrawable(thisDrawables);
	}
	private ArrayList<ImageButton> getButtonByDate(Date date)
	{
		if (!markedDates.containsKey(date)) return null;
		ArrayList<ImageButton> buttons = new ArrayList<>();
		ArrayList<Drawable> layers = layerDrawableToDrawables(Objects.requireNonNull(markedDates.get(date)));
		layers.removeIf(x -> Objects.equals(x, frames.get(4)));
		for (Drawable layer : layers)
		{
			if (layersPills.contains(layer)) {
				selectedPillButton = buttonsPills.get(layersPills.indexOf(layer));
				buttons.add(selectedPillButton);
			}
			else if (layersMood.contains(layer)) {
				selectedMoodButton = buttonsMood.get(layersMood.indexOf(layer));
				buttons.add(selectedMoodButton);
			}
		}
		return buttons;
	}
//	private boolean selectDate(int num, Date date) {
//		if (Objects.equals(markedDates.get(date), layersPills.get(num))
//				&& (Objects.equals(selectedPillTriangleButton, buttonsPills.get(num))
//				|| selectedPillTriangleButton == null))
//		{
//			if (selectedPillTriangleButton == null)
//				selectedPillButton = buttonsPills.get(num);
//			//currentPicturePills = layersPills.get(num);
//			currentFrame = frames.get(num);
//			return true;
//		}
//		return false;
//	}
}

