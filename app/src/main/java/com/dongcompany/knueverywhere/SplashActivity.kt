package com.dongcompany.knueverywhere

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.dongcompany.knueverywhere.Login.LoginActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var util = SharedPreferenceUtil(this)
        Handler().postDelayed(
            {
                if(util.getAutoLogin() == false) {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                else {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }, 2000
        )
    }

    override fun onBackPressed() {

    }
}
