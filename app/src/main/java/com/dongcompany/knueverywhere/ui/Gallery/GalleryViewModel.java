package com.dongcompany.knueverywhere.ui.Gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GalleryViewModel extends ViewModel {
    private int cnt = 0;

    private MutableLiveData<String> mText;

    public GalleryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(String.valueOf(cnt));
    }

    public LiveData<String> getText() {
        return mText;
    }

}