package com.dongcompany.knueverywhere.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import com.dongcompany.knueverywhere.MainActivity
import com.dongcompany.knueverywhere.R
import com.dongcompany.knueverywhere.SharedPreferenceUtil

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val checkBox:CheckBox = findViewById(R.id.LoginActivity_autoCheckBox);
        //로그인 버튼
        findViewById<Button>(R.id.LoginActivity_LoginButton).setOnClickListener(View.OnClickListener {
            if(checkBox.isChecked) {
                val sharedPreferenceUtil = SharedPreferenceUtil(this)
                sharedPreferenceUtil.setAutoLogin(true)
            }
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })

        //회원가입 버튼
        findViewById<Button>(R.id.LoginActivity_registButton).setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, RegistActivity::class.java))
        })
    }
}