package com.dongcompany.knueverywhere.ui.Awards;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AwardsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AwardsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}