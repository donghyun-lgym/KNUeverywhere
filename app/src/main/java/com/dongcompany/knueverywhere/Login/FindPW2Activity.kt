package com.dongcompany.knueverywhere.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.dongcompany.knueverywhere.R

class FindPW2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_p_w2)

        val intent = getIntent()
        val qusTextView : TextView = findViewById(R.id.FindPW2Activity_qusTextView)
        qusTextView.setText(intent.getStringExtra("질문"))
        val answer : String = intent.getStringExtra("답변")
        val pw : String = intent.getStringExtra("PW")
        val ansEditText : EditText = findViewById(R.id.FindPW2Activity_ansEditText)
        findViewById<Button>(R.id.FindPW2Activity_CompleteButton).setOnClickListener(View.OnClickListener {
            if(answer.equals(ansEditText.text.toString())) {
                val intent = Intent(this, FindPW3Activity::class.java)
                intent.putExtra("PW", pw)
                startActivity(intent)
                finish()
            }
            else {
                Toast.makeText(this, "답변이 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        })
        findViewById<Button>(R.id.FindPW2Activity_HomeButton).setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        })
    }
}