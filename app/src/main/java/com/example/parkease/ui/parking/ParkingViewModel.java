package com.example.parkease.ui.parking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class ParkingViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<String> mText;

    public ParkingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is parking fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}