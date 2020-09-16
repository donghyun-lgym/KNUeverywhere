package com.dongcompany.knueverywhere.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.dongcompany.knueverywhere.MainActivity
import com.dongcompany.knueverywhere.R
import com.dongcompany.knueverywhere.SharedPreferenceUtil
import com.google.firebase.firestore.FirebaseFirestore



class LoginActivity : AppCompatActivity() {
    var LoginActivity_login = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val db = FirebaseFirestore.getInstance()

        val idEditText:EditText = findViewById(R.id.LoginActivity_idEditText)
        val pwEditText:EditText = findViewById(R.id.LoginActivity_passwordEditText)

        val checkBox:CheckBox = findViewById(R.id.LoginActivity_autoCheckBox);
        //로그인 버튼
        findViewById<Button>(R.id.LoginActivity_LoginButton).setOnClickListener(View.OnClickListener {
            var LoginActivity_login = 0
            db.collection("users").get()
                    .addOnSuccessListener { result ->
                        val inputID = idEditText.text.toString()
                        val inputPW = pwEditText.text.toString()
                        for (document in result) {
                            val id = document.id
                            if (id.equals(inputID) && document.data.getValue("PW").toString().equals(inputPW)
                                    && document.data.getValue("권한").equals(true)) {
                                LoginActivity_login = 1
                                val util = SharedPreferenceUtil(this)
                                util.setID(document.data.getValue("ID").toString())
                                util.setStdNum(document.data.getValue("학번").toString())
                                util.setPhone(document.data.getValue("연락처").toString())
                                util.setName(document.data.getValue("이름").toString())
                                util.setCourseCheckBox(0, document.data.getValue("체크박스_코스0") as Boolean)
                                util.setCourseCheckBox(1, document.data.getValue("체크박스_코스1") as Boolean)
                                util.setCourseCheckBox(2, document.data.getValue("체크박스_코스2") as Boolean)
                                util.setCourseCheckBox(3, document.data.getValue("체크박스_코스3") as Boolean)
                                break
                            }
                            else if(id.equals(inputID) && document.data.getValue("PW").toString().equals(inputPW)
                                    && document.data.getValue("권한").equals(false)) {
                                LoginActivity_login = 2
                                break
                            }
                        }
                    }
            Handler().postDelayed(Runnable {
                if(LoginActivity_login == 1) {
                    Toast.makeText(this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                    if(checkBox.isChecked) {
                        val util = SharedPreferenceUtil(this)
                        util.setAutoLogin(true)
                    }
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                else if(LoginActivity_login == 2) {
                    Toast.makeText(this, "아직 학생 인증이 되지 않았습니다.", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this, "ID 또는 패스워드를 확인 해 주세요.", Toast.LENGTH_SHORT).show()
                }
            }, 1600)
        })

        //회원가입 버튼
        findViewById<Button>(R.id.LoginActivity_registButton).setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, RegistActivity::class.java))
        })
    }
}