package com.dongcompany.knueverywhere

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.dongcompany.knueverywhere.Login.LoginActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        var util = SharedPreferenceUtil(this)
        val autoLogin = util.getAutoLogin()

        Log.d("DynamicLinks", intent.toString())
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent)
                .addOnSuccessListener(this, OnSuccessListener { pendingDynamicLinkData ->
                    var deepLink: Uri? = null
                    if (pendingDynamicLinkData != null) {
                        if (!autoLogin) {
                            Toast.makeText(this, "자동 로그인으로 로그인 후 시도 해 주세요.", Toast.LENGTH_SHORT).show()
                        } else {
                            deepLink = pendingDynamicLinkData.link
                            Log.d("DynamicLinks", deepLink.toString())
                            var intent = Intent(applicationContext, QRcertificationActivity::class.java)
                            intent.putExtra("URL", deepLink.toString())
                            Handler().postDelayed(
                                    {
                                        startActivity(intent)
                                        finish()
                                        return@postDelayed
                                    }, 1900)
                            return@OnSuccessListener
                        }
                    }

                    Handler().postDelayed(
                            {
                                if (util.getAutoLogin() == false) {
                                    startActivity(Intent(this, LoginActivity::class.java))
                                } else {
                                    startActivity(Intent(this, MainActivity::class.java))
                                }
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                finish()
                            }, 1900
                    )

                })
                .addOnFailureListener(this) {
                    Log.d("DynamicLinks", "Failure")
                    finish()
                }
    }

    override fun onBackPressed() {

    }
}
