package com.dongcompany.knueverywhere.ui.CyberView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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