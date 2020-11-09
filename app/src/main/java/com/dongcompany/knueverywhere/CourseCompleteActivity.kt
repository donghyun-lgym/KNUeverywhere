package com.dongcompany.knueverywhere

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide


class CourseCompleteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_complete)
            val m: MediaPlayer = MediaPlayer.create(this, R.raw.sound)
            m.start()


        val imageView:ImageView = findViewById(R.id.CourseCompleteActivity_Imageview)
        Glide.with(this)
                .load(R.raw.dance)
                .into(imageView)

        findViewById<Button>(R.id.CourseCompleteActivity_Button).setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}