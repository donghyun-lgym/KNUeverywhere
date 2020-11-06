package com.dongcompany.knueverywhere.ui.MapInfo

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import com.bumptech.glide.Glide
import com.dongcompany.knueverywhere.LoadingDialog
import com.dongcompany.knueverywhere.MainActivity
import com.dongcompany.knueverywhere.R
import com.dongcompany.knueverywhere.SharedPreferenceUtil
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*

class PicFragment_Dialog(context: Context, val ref : StorageReference, val info:picInfo) : Dialog(context) {

    private var activity: Activity = context as Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.picfragment_dialog)
        setCancelable(false)
        this.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        this.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        )

        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val params: ViewGroup.LayoutParams? = this.window?.attributes
        val deviceWidth = size.x
        params?.width = (deviceWidth).toInt()
        this.window?.attributes = params as WindowManager.LayoutParams
        this.window?.setBackgroundDrawableResource(R.color.TRANSPARENT)


        val imageView:ImageView = findViewById(R.id.picFragmentDialog_imageview);
        Glide.with(activity)
                .using(FirebaseImageLoader())
                .load(ref)
                .into(imageView);
        val dateText:TextView = findViewById(R.id.picFragmentDialog_dateText);
        dateText.setText(info.date)
        val nameText:TextView = findViewById(R.id.picFragmentDialog_nameText);
        val name2 = info.name.substring(0, info.name.length - 1)
        nameText.setText(name2 + "*")

        findViewById<Button>(R.id.picFragmentDialog_CanBtn).setOnClickListener(View.OnClickListener { dismiss() })
    }
}