package com.dongcompany.knueverywhere.ui.Map

import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.dongcompany.knueverywhere.R


class MapFragment_AirViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_fragment__air_view)

        val mWebView = findViewById<WebView>(R.id.MapFragment_AirViewActivity_webView)
        mWebView.getSettings().setJavaScriptEnabled(true)
        mWebView.loadUrl("https://knupr.knu.ac.kr/content05/campustour/index.html")
        //mWebView.setWebChromeClient(WebChromeClient())
    }


}