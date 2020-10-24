package com.dongcompany.knueverywhere

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget

class LoadingDialog(context: Context) : Dialog(context) {

    private var activity: Context = context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_dialog)
        setCancelable(false)
//        this.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
//        this.window?.setFlags(
//            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
//            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//        )
        this.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
//        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        val display = windowManager.defaultDisplay
//        val size = Point()
//        display.getSize(size)

//        val params: ViewGroup.LayoutParams? = this.window?.attributes
//        val deviceWidth = size.x
//        params?.width = (deviceWidth).toInt()
//        this.window?.attributes = params as WindowManager.LayoutParams
        //this.window?.setBackgroundDrawableResource(R.color.TRANSPARENT)


    }
}