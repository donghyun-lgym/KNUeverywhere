package com.dongcompany.knueverywhere.ui.Map

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import com.dongcompany.knueverywhere.R

class MapFragment_TutorialDialog(context: Context) : Dialog(context) {

    private var activity: Context = context
    private var img_num = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tutorial_dialog)
        setCancelable(false)


        this.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.BLACK))
        val layout : ImageView = findViewById(R.id.Tutorial_iv)
        val btn : Button = findViewById(R.id.Tutorial_Button)
        btn.setOnClickListener(View.OnClickListener {
            when(img_num) {
                1 -> {
                    layout.setBackgroundResource(R.drawable.tutorial2)
                    img_num++
                }
                2 -> {
                    layout.setBackgroundResource(R.drawable.tutorial3)
                    img_num++
                    btn.setText("닫기 ▶")
                }
                3 -> {
                    this.dismiss()
                }
            }
        })
    }
}