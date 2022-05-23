package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class MainViewModel extends ViewModel {

    public LiveData<List<Note>> getNoteLiveData() {
        return App.getInstance().getNoteDao().getAllLiveData();
    }

    public LiveData<List<Note>> getNoteWithTagLiveData(int tag) {
        return App.getInstance().getNoteDao().getAllWithTag(tag);
    }
}
