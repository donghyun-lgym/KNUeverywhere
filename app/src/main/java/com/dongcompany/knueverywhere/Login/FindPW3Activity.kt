package com.dongcompany.knueverywhere.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.dongcompany.knueverywhere.R

class FindPW3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_p_w3)
        val pw:String = intent.getStringExtra("PW")
        val textView:TextView = findViewById(R.id.FindPW3Activity_TextView)
        textView.setText(pw)
        findViewById<Button>(R.id.FindPW3Activity_CompleteButton).setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}