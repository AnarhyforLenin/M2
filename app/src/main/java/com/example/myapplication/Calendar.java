package com.example.myapplication;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.io.ByteArrayOutputStream;
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
	private ArrayList<Integer> resourcesIds = new ArrayList<>();
	private HashMap<Date, LayerDrawable> markedDates = new HashMap<>();
	private HashMap<Date, String> notes = new HashMap<>();
	private Drawable pinnedDate;
	private final CaldroidFragment CALDROID_FRAGMENT = new CaldroidFragment();
	private Date selectedDate = new Date();
	private ImageButton selectedPillButton;
	private ImageButton selectedPillTriangleButton;
	private ImageButton selectedMoodButton;
	private ImageButton selectedMoodTriangleButton;
	private SharedPreferences preferences;
	private RadioButton radioPills;
	private RadioButton radioMood;
	private LayerDrawable currentLayer;

	private int huy;
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferences = getSharedPreferences("com.example.myapplication", Context.MODE_PRIVATE);
		setContentView(R.layout.activity_calendar);

		AtomicBoolean isTappedPill = new AtomicBoolean(false);
		AtomicBoolean isTappedMood = new AtomicBoolean(false);
		ImageButton home = findViewById(R.id.back);
		home.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent back = new Intent(Calendar.this, MainScreen.class);
			}
		});
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

		resourcesIds = new ArrayList<>(Arrays.asList(
				R.drawable.pill11_3,
				R.drawable.pill21_2,
				R.drawable.pill31_2,
				R.drawable.pill41_3,
				R.drawable.pill5,
				R.drawable.happy_n4,
				R.drawable.sad_n2,
				R.drawable.ney_n2,
				R.drawable.strange_n2
		));

		layersPills = new ArrayList<>(Arrays.asList(
				ResourcesCompat.getDrawable(getResources(), resourcesIds.get(0), null),
				ResourcesCompat.getDrawable(getResources(), resourcesIds.get(1), null),
				ResourcesCompat.getDrawable(getResources(), resourcesIds.get(2), null),
				ResourcesCompat.getDrawable(getResources(), resourcesIds.get(3), null),
				ResourcesCompat.getDrawable(getResources(), resourcesIds.get(4), null)
		));

		layersMood = new ArrayList<>(Arrays.asList(
				ResourcesCompat.getDrawable(getResources(), resourcesIds.get(5), null),
				ResourcesCompat.getDrawable(getResources(), resourcesIds.get(6), null),
				ResourcesCompat.getDrawable(getResources(), resourcesIds.get(7), null),
				ResourcesCompat.getDrawable(getResources(), resourcesIds.get(8), null),
				ResourcesCompat.getDrawable(getResources(), resourcesIds.get(4), null)
		));

		frames = new ArrayList<>(Arrays.asList(
				(LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.blue_pic1_frame_layer, null),
				(LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.blue_pic4_frame_layer, null),
				(LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.blue_pic2_frame_layer, null),
				(LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.blue_pic3_frame_layer, null),
				(LayerDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.red_border_dark3, null)
		));

		frames.forEach(x -> x.setVisible(true, true));

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

		pinnedDate = ResourcesCompat.getDrawable(getResources(), R.drawable.pinned_date, null);

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

		updateEverything();
		if (!markedDates.containsKey(selectedDate)) {
			markedDates.put(selectedDate, drawableToLayerDrawable(frames.get(4)));
			CALDROID_FRAGMENT.setBackgroundDrawableForDate(drawableToLayerDrawable(frames.get(4)), selectedDate);
		}
		CALDROID_FRAGMENT.refreshView();

		final CaldroidListener listener = new CaldroidListener() {
			@Override
			public void onSelectDate(Date date, View view) {
				if (markedDates.containsKey(selectedDate)) {
					ArrayList<Drawable> layers = layerDrawableToDrawables(Objects.requireNonNull(markedDates.get(selectedDate)));
					layers.removeIf(x -> compareDrawablesByBitmap(x, frames.get(4)));
//					layers.removeIf(x -> Objects.equals(x, frames.get(4)));
					markedDates.remove(selectedDate);
					if (layers.size() < 1)
						CALDROID_FRAGMENT.clearBackgroundDrawableForDate(selectedDate);
					else {
						markedDates.put(selectedDate, drawablesToLayerDrawable(layers));
						layers.removeIf(x -> !x.isVisible());
						CALDROID_FRAGMENT.setBackgroundDrawableForDate(drawablesToLayerDrawable(layers), selectedDate);
					}
				}
				if (markedDates.containsKey(date)) {
					ArrayList<Drawable> layers = new ArrayList<>();
					layers.add(frames.get(4));
//					for (Drawable layer : layers)
//						if (compareDrawablesByBitmap(layer, frames.get(4)))
//							layer.setVisible(true, true);
					layers.forEach(x -> x.setVisible(true, true));
					layers.addAll(layerDrawableToDrawables(Objects.requireNonNull(markedDates.get(date))));
					markedDates.remove(date);
					markedDates.put(date, drawablesToLayerDrawable(layers));
					layers.removeIf(x -> !x.isVisible());
					CALDROID_FRAGMENT.setBackgroundDrawableForDate(drawablesToLayerDrawable(layers), date);
				}
				else {
					Drawable tempDrawable = frames.get(4);
					tempDrawable.setVisible(true, true);
					LayerDrawable tempLayer = drawableToLayerDrawable(tempDrawable);
					CALDROID_FRAGMENT.setBackgroundDrawableForDate(tempLayer, date);
					markedDates.put(date, tempLayer);
				}

				selectedDate = date;
				if (selectedPillTriangleButton == null && selectedMoodTriangleButton == null) {
					if (selectedPillButton != null) {
						toggleButton(selectedPillButton, 0);
						selectedPillButton = null;
					}
					if (selectedMoodButton != null) {
						toggleButton(selectedMoodButton, 0);
						selectedMoodButton = null;
					}
					ArrayList<ImageButton> tempButtons = getButtonByDate(selectedDate);
					if (tempButtons != null && tempButtons.size() > 0)
						Objects.requireNonNull(getButtonByDate(selectedDate)).forEach(x -> toggleButton(x, 1));
				}
				else {
					boolean pillFlag = false;
					boolean moodFlag = false;
					for (ImageButton button : Objects.requireNonNull(getButtonByDate(selectedDate))) {
						if (buttonsPills.contains(button)) {
							selectedPillButton = button;
							pillFlag = true;
						}
						if (buttonsMood.contains(button)) {
							selectedMoodButton = button;
							moodFlag = true;
						}
					}
					if (!pillFlag) selectedPillButton = null;
					if (!moodFlag) selectedMoodButton = null;
				}
				//CALDROID_FRAGMENT.setBackgroundDrawableForDate(currentFrame, selectedDate);
				CALDROID_FRAGMENT.refreshView();
				saveEverything();
			}
			@Override
			public void onLongClickDate(Date date, View view) {
				if (!notes.containsKey(date))
					addNewNotePopup(date, view);
				else
					removeNotePopup(date, view);
				if (markedDates.containsKey(selectedDate))
					CALDROID_FRAGMENT.setBackgroundDrawableForDate(markedDates.get(selectedDate), selectedDate);
				CALDROID_FRAGMENT.refreshView();
//				CALDROID_FRAGMENT.refreshView();
			}
		};
		CALDROID_FRAGMENT.setCaldroidListener(listener);
	}
	public void addNewNotePopup(Date date, View view) {
		final EditText taskEditText = new EditText(this);
		AlertDialog dialog = new AlertDialog.Builder(this)
				.setTitle("Add a new note")
				.setMessage("What do you want to add?")
				.setView(taskEditText)
				.setPositiveButton("Add", (dialog1, which) -> {
					String note = String.valueOf(taskEditText.getText());
					if (note.isEmpty()) return;
					notes.put(date, note);
					if (markedDates.containsKey(date))
						Objects.requireNonNull(markedDates.get(date)).addLayer(pinnedDate);
					else {
						LayerDrawable layerDrawable = drawableToLayerDrawable(pinnedDate);
						markedDates.put(date, layerDrawable);
					}
					CALDROID_FRAGMENT.setBackgroundDrawableForDate(markedDates.get(date), date);
					CALDROID_FRAGMENT.refreshView();
					this.updateHashMapOfNotes();
					this.updateHashMapOfDates();
				})
				.setNegativeButton("Cancel", null)
				.create();
		dialog.show();
	}
	public void removeNotePopup(Date date, View view) {
		AlertDialog dialog = new AlertDialog.Builder(this)
				.setTitle("Note")
				.setMessage(notes.get(date))
				.setPositiveButton("Remove", (dialog1, which) -> {
					notes.remove(date);
					if (markedDates.containsKey(date))
					{
						ArrayList<Drawable> layers = layerDrawableToDrawables(Objects.requireNonNull(markedDates.get(date)));
						int index = containsByBitmap(layers, pinnedDate);
						if (index > -1) layers.remove(index);
						markedDates.remove(date);
						if (layers.size() < 1)
						{
							CALDROID_FRAGMENT.clearBackgroundDrawableForDate(date);
							CALDROID_FRAGMENT.refreshView();
							this.updateHashMapOfNotes();
							this.updateHashMapOfDates();
							return;
						}
						LayerDrawable layerDrawable = drawablesToLayerDrawable(layers);
						markedDates.put(date, layerDrawable);
						CALDROID_FRAGMENT.setBackgroundDrawableForDate(layerDrawable, date);
					}
					CALDROID_FRAGMENT.refreshView();
					this.updateHashMapOfNotes();
					this.updateHashMapOfDates();
				})
				.setNegativeButton("Cancel", null)
				.create();
		dialog.show();
	}
	@SuppressLint("NonConstantResourceId")
	@Override
	public boolean onLongClick(View v) {
		switch (v.getId()) {
			case R.id.blueButton:
				onLongClickButton(0, false);
				break;
			case R.id.greenButton:
				onLongClickButton(1, false);
				break;
			case R.id.pinkButton:
				onLongClickButton(2, false);
				break;
			case R.id.yellowButton:
				onLongClickButton(3, false);
				break;

			case R.id.neyButton:
				onLongClickButton(0, true);
				break;
			case R.id.sadButton:
				onLongClickButton(1, true);
				break;
			case R.id.strangeButton:
				onLongClickButton(2, true);
				break;
			case R.id.happyButton:
				onLongClickButton(3, true);
				break;
		}
		saveEverything();
		CALDROID_FRAGMENT.refreshView();
		return true;
	}
	@SuppressLint("NonConstantResourceId")
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.cancelButton) {
			onClickDeleteButton(false);
			saveEverything();
			return;
		}
		else if (v.getId() == R.id.cancelButton2) {
			onClickDeleteButton(true);
			saveEverything();
			return;
		}
		if (selectedPillTriangleButton != null || selectedMoodTriangleButton != null) return;

		switch(v.getId()) {
			case R.id.blueButton:
				onShortClickButton(0, false);
				break;
			case R.id.greenButton:
				onShortClickButton(1, false);
				break;
			case R.id.pinkButton:
				onShortClickButton(2, false);
				break;
			case R.id.yellowButton:
				onShortClickButton(3, false);
				break;

			case R.id.neyButton:
				onShortClickButton(0, true);
				break;
			case R.id.sadButton:
				onShortClickButton(1, true);
				break;
			case R.id.strangeButton:
				onShortClickButton(2, true);
				break;
			case R.id.happyButton:
				onShortClickButton(3, true);
				break;
			default:
				return;
		}
		saveEverything();
		CALDROID_FRAGMENT.setBackgroundDrawableForDate(currentLayer, selectedDate);
		CALDROID_FRAGMENT.refreshView();
	}
	@SuppressLint("NonConstantResourceId")
	private void toggleButton(ImageButton button, int state) {
		if (button == null || state < 0 || state > 2) return;
		switch (button.getId()) {
			case R.id.blueButton:
				button.setBackgroundResource(state != 0 ? state == 1 ? R.drawable.blue_pic1_frame_layer : R.drawable.pill1_triangle : R.drawable.pill11);
				break;
			case R.id.greenButton:
				button.setBackgroundResource(state != 0 ? state == 1 ? R.drawable.blue_pic4_frame_layer : R.drawable.pill21_triangle : R.drawable.pill21);
				break;
			case R.id.pinkButton:
				button.setBackgroundResource(state != 0 ? state == 1 ? R.drawable.blue_pic2_frame_layer : R.drawable.pill31_triangle : R.drawable.pill31);
				break;
			case R.id.yellowButton:
				button.setBackgroundResource(state != 0 ? state == 1 ? R.drawable.blue_pic3_frame_layer : R.drawable.pill41_triangle : R.drawable.pill41);
				break;
			case R.id.cancelButton:
				button.setBackgroundResource(R.drawable.cancel);
				break;

			case R.id.neyButton:
				button.setBackgroundResource(state != 0 ? state == 1 ? R.drawable.happy_selected : R.drawable.happy_n5 : R.drawable.happy_n4);
				break;
			case R.id.sadButton:
				button.setBackgroundResource(state != 0 ? state == 1 ? R.drawable.sad_selected : R.drawable.sad_n : R.drawable.sad_n2);
				break;
			case R.id.strangeButton:
				button.setBackgroundResource(state != 0 ? state == 1 ? R.drawable.ney_selected : R.drawable.ney_n : R.drawable.ney_n2);
				break;
			case R.id.happyButton:
				button.setBackgroundResource(state != 0 ? state == 1 ? R.drawable.strange_selected : R.drawable.strange_n : R.drawable.strange_n2);
				break;
			default:
				return;
		}
		saveEverything();
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
	private void putDrawableToSharedPreference(String key, Drawable drawable) {
//		SharedPreferences.Editor editor;
//		editor = preferences.edit();
//		Gson gson = new Gson();
//		String json = gson.toJson(obj);
//		editor.putString(key, json);
//		editor.apply();

		SharedPreferences.Editor editor = preferences.edit();
//		editor.putString(key, encodeToBase64(drawableToBitmap(drawable)));
		if (compareDrawablesByBitmap(drawable, frames.get(4)))
			editor.putInt(key, 121);
		else if (compareDrawablesByBitmap(drawable, pinnedDate))
			editor.putInt(key, 122);
		else
			editor.putInt(key, getIndexOfDrawable(drawable));
		editor.apply();
	}
	private void putLayerDrawable(String key, LayerDrawable layerDrawable)
	{
		SharedPreferences.Editor editor = preferences.edit();
		ArrayList<Drawable> drawables = layerDrawableToDrawables(layerDrawable);
		editor.putInt((key + "LayerSize"), drawables.size());
		editor.apply();
		for (int i = 0; i < drawables.size(); i++)
			this.putDrawableToSharedPreference((key + "Drawable" + i), drawables.get(i));
	}
	private LayerDrawable getLayerDrawablesFromKey(String key)
	{
		ArrayList<Drawable> layers = new ArrayList<>();
		int size = preferences.getInt((key + "LayerSize"), 0);
		for (int i = 0; i < size; i++)
			layers.add(getDrawableFromKey(key + "Drawable" + i));
		return drawablesToLayerDrawable(layers);
	}
	private Date getDateFromKey(String key) {
		Gson gson = new Gson();
		String json = preferences.getString(key, "");
		Date obj = gson.fromJson(json, Date.class);
		if (obj == null) return new Date();
		return obj;
	}
	private Drawable getDrawableFromKey(String key) {
//		Gson gson = new Gson();
//		String json = preferences.getString(key, "");
//		return gson.fromJson(json, Drawable.class);
		//return drawableToBitmap(decodeBase64(preferences.getString(key, "")));
		int indexOfDrawable = preferences.getInt(key, -1);
		if (indexOfDrawable == 121) return frames.get(4);
		else if (indexOfDrawable == 122) return pinnedDate;
		return getDrawableByIndex(indexOfDrawable);
	}
	private void updateHashMapOfDates() {
		SharedPreferences.Editor editor;
		for (int i = 0; i < markedDates.size(); i++) {
			Date markedDate = (Date) markedDates.keySet().toArray()[i];
			putDateToSharedPreference(("MDate" + i), markedDate);
			editor = preferences.edit();
			putLayerDrawable(("Layer" + i), markedDates.get(markedDate));
//			editor.putInt(("Color" + i), layersPills.indexOf(markedDates.get(markedDate)));
			editor.apply();
		}
		editor = preferences.edit();
		editor.putInt("Dates", markedDates.size());
		editor.apply();
	}
	private void updateHashMapOfNotes() {
		SharedPreferences.Editor editor;
		for (int i = 0; i < notes.size(); i++) {
			Date markedDate = (Date) notes.keySet().toArray()[i];
			putDateToSharedPreference(("NDate" + i), markedDate);
			editor = preferences.edit();
			editor.putString(("Note" + i), notes.get(markedDate));
			editor.apply();
		}
		editor = preferences.edit();
		editor.putInt("Notes", notes.size());
		editor.apply();
	}
	private void readHashMapOfDates() {
		markedDates = new HashMap<>();
		int counter = preferences.getInt("Dates", 0);
		for (int i = 0; i < counter; i++)
//			markedDates.put(getDateFromKey("Date" + i), layersPills.get(preferences.getInt(("Color" + i), 0)));
			markedDates.put(getDateFromKey("MDate" + i), getLayerDrawablesFromKey("Layer" + i));
	}
	private void readHashMapOfNotes() {
		notes = new HashMap<>();
		int counter = preferences.getInt("Notes", 0);
		for (int i = 0; i < counter; i++)
//			markedDates.put(getDateFromKey("Date" + i), layersPills.get(preferences.getInt(("Color" + i), 0)));
			notes.put(getDateFromKey("NDate" + i), preferences.getString(("Note" + i), ""));
	}
	private void onLongClickButton(int num, boolean state) {
		if (!state ? Objects.equals(selectedPillTriangleButton, buttonsPills.get(num)) : Objects.equals(selectedMoodTriangleButton, buttonsMood.get(num)))
		{
			@SuppressWarnings("unchecked")
			HashMap<Date, LayerDrawable> totallyNotMarkedDates = (HashMap<Date, LayerDrawable>) markedDates.clone();
			for (Date date : markedDates.keySet()) {
				ArrayList<Drawable> layers = layerDrawableToDrawables(Objects.requireNonNull(markedDates.get(date)));
				layers.forEach(x -> x.setVisible(true, true));
				totallyNotMarkedDates.remove(date);
				totallyNotMarkedDates.put(date, drawablesToLayerDrawable(layers));
				CALDROID_FRAGMENT.setBackgroundDrawableForDate(drawablesToLayerDrawable(layers), date);
			}
			markedDates = totallyNotMarkedDates;
			if (!state)
				this.toggleButton(selectedPillTriangleButton, 0);
			else
				this.toggleButton(selectedMoodTriangleButton, 0);

			if (selectedPillButton != null)
				this.toggleButton(selectedPillButton, 1);
			if (selectedMoodButton != null)
				this.toggleButton(selectedMoodButton, 1);

			selectedMoodTriangleButton = null;
			selectedPillTriangleButton = null;
			saveEverything();
			return;
		}
		if (selectedPillTriangleButton != null) {
			this.toggleButton(selectedPillTriangleButton, 0);
			selectedPillTriangleButton = null;
		}
		if (selectedMoodTriangleButton != null) {
			this.toggleButton(selectedMoodTriangleButton, 0);
			selectedMoodTriangleButton = null;
		}
		toggleButton(selectedMoodButton, 0);
		toggleButton(selectedPillButton, 0);
		if (!state) {
			selectedPillTriangleButton = buttonsPills.get(num);
			toggleButton(selectedMoodTriangleButton, 0);
			toggleButton(selectedPillTriangleButton, 2);
		}
		else {
			selectedMoodTriangleButton = buttonsMood.get(num);
			toggleButton(selectedPillTriangleButton, 0);
			toggleButton(selectedMoodTriangleButton, 2);
		}
		@SuppressWarnings("unchecked")
		HashMap<Date, LayerDrawable> totallyNotMarkedDates = (HashMap<Date, LayerDrawable>) markedDates.clone();
		for (Date date : markedDates.keySet()) {
			ArrayList<Drawable> layers = layerDrawableToDrawables(Objects.requireNonNull(markedDates.get(date)));
			layers.forEach(x -> x.setVisible(true, true));
			for (Drawable layer : layers)
				if (!((compareDrawablesByBitmap(frames.get(4), layer)
						|| compareDrawablesByBitmap(pinnedDate, layer))
						|| (((containsByBitmap(layersPills, layer) >= 0) == !state)
						&& (!state ? compareDrawablesByBitmap(layer, layersPills.get(num)) : compareDrawablesByBitmap(layer, layersMood.get(num))))))
					layer.setVisible(false, true);

			totallyNotMarkedDates.remove(date);
			totallyNotMarkedDates.put(date, drawablesToLayerDrawable(layers));
			CALDROID_FRAGMENT.clearBackgroundDrawableForDate(date);
		}
		markedDates = totallyNotMarkedDates;
		ArrayList<Drawable> outLayers;
		for (Date date : markedDates.keySet()) {
			outLayers = new ArrayList<>();
			ArrayList<Drawable> layers = layerDrawableToDrawables(Objects.requireNonNull(markedDates.get(date)));
			for (Drawable layer : layers)
				if (layer.isVisible())
					outLayers.add(layer);
			CALDROID_FRAGMENT.setBackgroundDrawableForDate(drawablesToLayerDrawable(outLayers), date);
		}
		saveEverything();
	}
	private void onShortClickButton(int num, boolean state) {
		//currentPicturePills = layersPills.get(num);
		if (!state ? selectedPillButton != null : selectedMoodButton != null)
			this.toggleButton(!state ? selectedPillButton : selectedMoodButton, 0);
		ArrayList<Drawable> layers = new ArrayList<>();
		//layers.add(frames.get(4));
		if (markedDates.containsKey(selectedDate)) {
			layers = layerDrawableToDrawables(Objects.requireNonNull(markedDates.get(selectedDate)));
			markedDates.remove(selectedDate);
//			layers.removeIf(x -> (layersPills.contains(x) == !state && x != frames.get(4)));
			@SuppressWarnings("unchecked")
			ArrayList<Drawable> tempLayers = (ArrayList<Drawable>) layers.clone();
			for (Drawable layer : layers)
				if ((containsByBitmap(layersPills, layer) >= 0) == !state
						&& !compareDrawablesByBitmap(layer, frames.get(4))
						&& !compareDrawablesByBitmap(layer, pinnedDate))
					tempLayers.remove(layer);
			layers = tempLayers;
		}
		if ((!state && selectedPillButton == buttonsPills.get(num)) || (state && selectedMoodButton == buttonsMood.get(num))) {
			this.toggleButton(!state ? selectedPillButton : selectedMoodButton, 0);

			if (!state)
				this.selectedPillButton = null;
			else
				this.selectedMoodButton = null;

			//CALDROID_FRAGMENT.setBackgroundDrawableForDate(currentFrame, selectedDate);
			CALDROID_FRAGMENT.setBackgroundDrawableForDate(currentLayer, selectedDate);

			this.updateHashMapOfDates();
			CALDROID_FRAGMENT.refreshView();
			currentLayer = drawablesToLayerDrawable(layers);
			markedDates.put(selectedDate, currentLayer);
			saveEverything();
			return;
		}
		layers.add(!state ? layersPills.get(num) : layersMood.get(num));
		if (!state)
			selectedPillButton = buttonsPills.get(num);
		else
			selectedMoodButton = buttonsMood.get(num);
		currentLayer = drawablesToLayerDrawable(layers);
		//markedDates.put(selectedDate, layersPills.get(num));
		toggleButton(!state ? selectedPillButton : selectedMoodButton, 1);
		markedDates.put(selectedDate, currentLayer);
	}
	private void onClickDeleteButton(boolean state) {
		//currentPicturePills = null;
		@SuppressWarnings("unchecked")
		HashMap<Date, LayerDrawable> totallyNotMarkedDates = (HashMap<Date, LayerDrawable>) markedDates.clone();
		for (Date date : totallyNotMarkedDates.keySet()) {
			ArrayList<Drawable> layers = layerDrawableToDrawables(Objects.requireNonNull(markedDates.get(date)));
//			layers.removeIf(x -> (layersPills.contains(x) == !state && x != frames.get(4)));
			@SuppressWarnings("unchecked")
			ArrayList<Drawable> tempLayers = (ArrayList<Drawable>) layers.clone();
			for (Drawable layer : layers)
				if ((containsByBitmap(layersPills, layer) >= 0) == !state
						&& !compareDrawablesByBitmap(layer, frames.get(4))
						&& !compareDrawablesByBitmap(layer, pinnedDate))
					tempLayers.remove(layer);
			layers = tempLayers;
			markedDates.remove(date);
			if (layers.size() >= 1) {
				LayerDrawable tempLayer = drawablesToLayerDrawable(layers);
				markedDates.put(date, tempLayer);
				CALDROID_FRAGMENT.setBackgroundDrawableForDate(tempLayer, date);
			}
			else
				CALDROID_FRAGMENT.clearBackgroundDrawableForDate(date);
		}

		toggleButton(!state ? selectedPillButton : selectedMoodButton, 0);
		if (!state)
			selectedPillButton = null;
		else
			selectedMoodButton = null;

		toggleButton(selectedMoodTriangleButton, 0);
		selectedMoodTriangleButton = null;

		toggleButton(selectedPillTriangleButton, 0);
		selectedPillTriangleButton = null;

		if (!state ? selectedMoodButton != null : selectedPillButton != null)
			toggleButton(!state ? selectedMoodButton : selectedPillButton, 1);

		CALDROID_FRAGMENT.refreshView();
	}
	private static ArrayList<Drawable> layerDrawableToDrawables(LayerDrawable layer) {
		ArrayList<Drawable> layers = new ArrayList<>();
		for (int i = 0; i < layer.getNumberOfLayers(); i++)
			layers.add(layer.getDrawable(i));
		return layers;
	}
	private static LayerDrawable drawablesToLayerDrawable(ArrayList<Drawable> layers) {
		Drawable[] drawables = new Drawable[layers.size()];
		for (int i = 0; i < drawables.length; i++)
			drawables[i] = layers.get(i);
		return new LayerDrawable(drawables);
	}
	private static LayerDrawable drawableToLayerDrawable(Drawable drawable) {
		Drawable[] thisDrawables = new Drawable[1];
		thisDrawables[0] = drawable;
		return new LayerDrawable(thisDrawables);
	}
	private ArrayList<ImageButton> getButtonByDate(Date date) {
		if (!markedDates.containsKey(date)) return null;
		ArrayList<ImageButton> buttons = new ArrayList<>();
		ArrayList<Drawable> layers = layerDrawableToDrawables(Objects.requireNonNull(markedDates.get(date)));
		layers.removeIf(x -> Objects.equals(x, frames.get(4)));
		layers.removeIf(x -> Objects.equals(x, pinnedDate));
		for (Drawable layer : layers)
		{
			for (int i = 0; i < layersPills.size(); i++)
				if (compareDrawablesByBitmap(layer, layersPills.get(i)))
				{
					selectedPillButton = buttonsPills.get(i);
					buttons.add(selectedPillButton);
				}
			for (int i = 0; i < layersMood.size(); i++) {
				if (compareDrawablesByBitmap(layer, layersMood.get(i))) {
					selectedMoodButton = buttonsMood.get(i);
					buttons.add(selectedMoodButton);
				}
			}
		}
		return buttons;
	}
	private boolean compareDrawables(Drawable drawable1, Drawable drawable2) {
		//return drawable1.getConstantState().equals(drawable2.getConstantState());
		return drawable1.hashCode() == drawable2.hashCode();
	}
	private void saveEverything() {
		SharedPreferences.Editor editor;
		editor = preferences.edit();

		editor.putInt("selectedPillButton", selectedPillButton == null ? -1 : selectedPillButton.getId());
		editor.putInt("selectedMoodButton", selectedMoodButton == null ? -1 : selectedMoodButton.getId());
		editor.putInt("selectedPillTriangleButton", selectedPillTriangleButton == null ? -1 : selectedPillTriangleButton.getId());
		editor.putInt("selectedMoodTriangleButton", selectedMoodTriangleButton == null ? -1 : selectedMoodTriangleButton.getId());
//		this.putDateToSharedPreference("selectedDate", selectedDate);

		editor.putLong("selectedDate", selectedDate.getTime());
		//editor.putInt("kwy",huy);
		this.putLayerDrawable("currentLayer", currentLayer);
		this.updateHashMapOfDates();
		this.updateHashMapOfNotes();
		editor.apply();
	}
	private void updateEverything() {
		selectedPillButton = this.findViewById(preferences.getInt("selectedPillButton", -1));
		selectedMoodButton = this.findViewById(preferences.getInt("selectedMoodButton", -1));
		selectedDate = new Date(preferences.getLong("selectedDate", System.currentTimeMillis()));
		currentLayer = this.getLayerDrawablesFromKey("currentLayer");
		this.readHashMapOfDates();
		this.readHashMapOfNotes();
		markedDates.values().forEach(x -> x.setVisible(true, true));
		for (Date date : markedDates.keySet())
//			if (Objects.requireNonNull(markedDates.get(date)).isVisible())
			CALDROID_FRAGMENT.setBackgroundDrawableForDate(markedDates.get(date), date);
		if (selectedPillButton != null)
			toggleButton(selectedPillButton, 1);
		if (selectedMoodButton != null)
			toggleButton(selectedMoodButton, 1);
	}
	public static String encodeToBase64(Bitmap image) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
		byte[] b = byteArrayOutputStream.toByteArray();
		return Base64.encodeToString(b, Base64.DEFAULT);
	}
	public static Bitmap decodeBase64(String input) {
		byte[] decodedByte = Base64.decode(input, 0);
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}
	public static Bitmap drawableToBitmap (Drawable drawable) {
		Bitmap bitmap;

		if (drawable instanceof BitmapDrawable)
		{
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			if (bitmapDrawable.getBitmap() != null)
				return bitmapDrawable.getBitmap();
		}

		if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0)
			bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
		else
			bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}
	public Drawable bitmapToDrawable(Bitmap bitmap) {
		return new BitmapDrawable(getResources(), bitmap);
	}
	public Drawable getDrawableByIndex(Integer id) {
		//if (id > resourcesIds.size() || id < resourcesIds.size()) return null;
		return ResourcesCompat.getDrawable(getResources(), resourcesIds.get(id), null);
//		for (Integer id : resourcesIds)
//		{
//			Drawable tempDrawable = ResourcesCompat.getDrawable(getResources(), id, null);
//			assert tempDrawable != null;
//			if (compareDrawables(drawable, tempDrawable))
//			{
//				return tempDrawable;
//			}
//		}
	}
	public static boolean compareDrawablesByBitmap(Drawable drawableA, Drawable drawableB) {
		Drawable.ConstantState stateA = drawableA.getConstantState();
		Drawable.ConstantState stateB = drawableB.getConstantState();
		// If the constant state is identical, they are using the same drawable resource.
		// However, the opposite is not necessarily true.
		return (stateA != null && stateA.equals(stateB))
				|| drawableToBitmap(drawableA).sameAs(drawableToBitmap(drawableB));
	}
	public Integer getIndexOfDrawable(Drawable drawable) {
		ArrayList<Drawable> drawables = new ArrayList<>();
		for (Integer id : resourcesIds)
			drawables.add(ResourcesCompat.getDrawable(getResources(), id, null));
		for (int i = 0; i < drawables.size(); i++)
			if (compareDrawablesByBitmap(drawable, drawables.get(i))) return i;
		//int index = drawables.indexOf(drawable);
		return -1;
	}
	private int containsByBitmap(ArrayList<Drawable> list, Drawable a)
	{
		for (int i = 0; i < list.size(); i++)
			if (compareDrawablesByBitmap(list.get(i), a))
				return i;
		return -1;
	}
}