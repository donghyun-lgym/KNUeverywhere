package com.dongcompany.knueverywhere.ui.MapInfo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dongcompany.knueverywhere.R;

import androidx.fragment.app.Fragment;

public class AroundFragment extends Fragment {

    private MapInfoActivity activity;
    public AroundFragment(Context context) {
        this.activity = (MapInfoActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_around, container, false);
    }
}