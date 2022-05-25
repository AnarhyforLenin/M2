package com.example.myapplication.screens.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.Note;
import com.example.myapplication.App;

import java.util.List;

public class MainViewModel extends ViewModel {

    public LiveData<List<Note>> getNoteLiveData() {
        return App.getInstance().getNoteDao().getAllLiveData();
    }

    public LiveData<List<Note>> getNoteWithTagLiveData(int tag) {
        return App.getInstance().getNoteDao().getAllWithTag(tag);
    }
}
