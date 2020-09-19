package com.dongcompany.knueverywhere.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.dongcompany.knueverywhere.R
import com.google.firebase.firestore.FirebaseFirestore

class FindPWActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_p_w)

        val nameEditText : EditText = findViewById(R.id.FindPWActivity_NameEditText)
        val stdnumEditText : EditText = findViewById(R.id.FindPWActivity_StdNumEditText)
        val idEditText : EditText = findViewById(R.id.FindPWActivity_idEditText)
        findViewById<Button>(R.id.FindPWActivity_CompleteButton).setOnClickListener(View.OnClickListener {
            if(nameEditText.text.toString().length == 0) {
                Toast.makeText(this, "이름을 입력 해 주세요.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if(stdnumEditText.text.toString().length == 0) {
                Toast.makeText(this, "학번을 입력 해 주세요.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if(idEditText.text.toString().length == 0) {
                Toast.makeText(this, "아이디를 입력 해 주세요.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            val db = FirebaseFirestore.getInstance()
            db.collection("users").get()
                    .addOnSuccessListener { result ->
                        for(document in result) {
                            if(document.id.equals(idEditText.text.toString())) {
                                if(document.data.getValue("학번").equals(stdnumEditText.text.toString())
                                        && document.data.getValue("이름").equals(nameEditText.text.toString())){
                                    var intent = Intent(this, FindPW2Activity::class.java)
                                    intent.putExtra("질문", document.data.getValue("비밀번호 질문").toString())
                                    intent.putExtra("답변", document.data.getValue("비밀번호 답변").toString())
                                    intent.putExtra("PW", document.data.getValue("PW").toString())
                                    startActivity(intent)
                                    finish()
                                    return@addOnSuccessListener
                                }
                                else break
                            }
                        }
                        Toast.makeText(this, "찾는 정보가 없습니다.", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }
        })
    }
}