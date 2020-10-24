package com.dongcompany.knueverywhere.ui.Awards;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dongcompany.knueverywhere.R;

public class AwardsFragment extends Fragment {

    private Activity activity;

    public AwardsFragment(Context context) {
        activity = (Activity) context;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_awards, container, false);

        WebView mWebView = root.findViewById(R.id.AwardsFragment_webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("https://knupr.knu.ac.kr/content05/campustour/index.html");

        return root;
    }
}