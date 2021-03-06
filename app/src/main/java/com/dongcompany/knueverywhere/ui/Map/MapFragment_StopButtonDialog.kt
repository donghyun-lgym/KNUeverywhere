package com.dongcompany.knueverywhere.ui.Map

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import com.dongcompany.knueverywhere.MainActivity
import com.dongcompany.knueverywhere.R
import com.dongcompany.knueverywhere.SharedPreferenceUtil
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class MapFragment_StopButtonDialog(context: Context) : Dialog(context) {

    private var activity: MainActivity = context as MainActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mapfragment_stopbuttondialog)
        setCancelable(false)
//        this.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
//        this.window?.setFlags(
//            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
//            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//        )

        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val params: ViewGroup.LayoutParams? = this.window?.attributes
        val deviceWidth = size.x
        params?.width = (deviceWidth).toInt()
        this.window?.attributes = params as WindowManager.LayoutParams
        this.window?.setBackgroundDrawableResource(R.color.TRANSPARENT)


        //탐방 중지 버튼
        findViewById<Button>(R.id.StopDialog_Btn).setOnClickListener(View.OnClickListener {
            Toast.makeText(activity, "탐방이 중지되었습니다.", Toast.LENGTH_SHORT).show()
            activity.stopTravel();
            dismiss()
        })

        findViewById<Button>(R.id.StopDialog_CanBtn).setOnClickListener(View.OnClickListener { dismiss() })
    }
}