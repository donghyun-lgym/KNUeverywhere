package com.dongcompany.knueverywhere.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.dongcompany.knueverywhere.MainActivity
import com.dongcompany.knueverywhere.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //로그인 버튼
        findViewById<Button>(R.id.LoginActivity_LoginButton).setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })

        //회원가입 버튼
        findViewById<Button>(R.id.LoginActivity_registButton).setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, RegistActivity::class.java))
        })
    }
}