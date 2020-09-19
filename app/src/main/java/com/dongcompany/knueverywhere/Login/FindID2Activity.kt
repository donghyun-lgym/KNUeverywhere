package com.dongcompany.knueverywhere.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.dongcompany.knueverywhere.R
import org.w3c.dom.Text

class FindID2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_i_d2)

        val intent = intent
        val ID = intent.getStringExtra("아이디")
        val safeID : String?

        if(3 <= ID.length && ID.length < 5) {
            safeID = ID.substring(0, ID.length - 2) + "*" + ID.substring(ID.length - 1, ID.length)
        }
        else if(5 <=ID.length && ID.length < 7) {
            safeID = ID.substring(0, ID.length - 3) + "**" + ID.substring(ID.length - 1, ID.length)
        }
        else {
            safeID = ID.substring(0, ID.length - 5) + "***" + ID.substring(ID.length - 2, ID.length)
        }
        val textView: TextView = findViewById(R.id.FindID2Activity_TextView)
        textView.setText(safeID!!)

        findViewById<Button>(R.id.FindID2Activity_CompleteButton).setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}