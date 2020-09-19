package com.dongcompany.knueverywhere.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.dongcompany.knueverywhere.R
import com.google.firebase.firestore.FirebaseFirestore

class FindIDActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_i_d)

        val nameEdit:EditText = findViewById(R.id.FindIDActivity_NameEditText)
        val stdnumEdit:EditText = findViewById(R.id.FindIDActivity_StdNumEditText)

        findViewById<Button>(R.id.FindIDActivity_CompleteButton).setOnClickListener(View.OnClickListener {
            if(nameEdit.text.toString().length == 0) {
                Toast.makeText(this, "성명을 입력하세요.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if(stdnumEdit.text.toString().length == 0) {
                Toast.makeText(this, "학번을 입력하세요.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            val db = FirebaseFirestore.getInstance()
            var find = false

            db.collection("users")
                    .get()
                    .addOnSuccessListener { result ->
                        for(document in result) {
                            val name = document.data.getValue("이름")
                            val stdnum = document.data.getValue("학번")
                            if(name.equals(nameEdit.text.toString()) && stdnum.equals(stdnumEdit.text.toString())) {
                                find = true
                                val intent = Intent(applicationContext, FindID2Activity::class.java)
                                intent.putExtra("아이디", document.id)
                                startActivity(intent)
                                finish()
                                break
                            }
                        }
                        return@addOnSuccessListener
                    }
            Handler().postDelayed(Runnable {
                if(!find) {
                    Toast.makeText(this, "찾는 정보가 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }, 1600)
        })
    }
}