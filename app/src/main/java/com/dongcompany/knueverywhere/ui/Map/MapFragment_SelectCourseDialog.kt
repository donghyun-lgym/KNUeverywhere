package com.dongcompany.knueverywhere.ui.Map

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import com.dongcompany.knueverywhere.MainActivity
import com.dongcompany.knueverywhere.R
import com.dongcompany.knueverywhere.SharedPreferenceUtil
import com.google.firebase.firestore.FirebaseFirestore

class MapFragment_SelectCourseDialog(context: Context) : Dialog(context) {

    private var activity: MainActivity = context as MainActivity

    private lateinit var checkBoxArray : Array<CheckBox>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mapfragment_selectcoursedialog)
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

        //체크박스 초기화
        checkBoxArray = arrayOf(findViewById(R.id.MapDialog_CheckBox1_Door), findViewById(R.id.MapDialog_CheckBox2_Restaurent)
                                , findViewById(R.id.MapDialog_CheckBox3_Facility), findViewById(R.id.MapDialog_CheckBox4_Collage))

        for(i in 0..3) {
            val util = SharedPreferenceUtil(activity)
            checkBoxArray[i].isChecked = util.getCourseCheckBox(i)
        }

        //완료(등록) 버튼
        findViewById<Button>(R.id.MapDialog_Btn).setOnClickListener(View.OnClickListener {
            val util = SharedPreferenceUtil(activity)
            val db = FirebaseFirestore.getInstance()

            for(i in 0..3) {
                util.setCourseCheckBox(i, checkBoxArray[i].isChecked)

                val a = hashMapOf(
                        "체크박스_코스0" to checkBoxArray[0].isChecked,
                        "체크박스_코스1" to checkBoxArray[1].isChecked,
                        "체크박스_코스2" to checkBoxArray[2].isChecked,
                        "체크박스_코스3" to checkBoxArray[3].isChecked
                )
                db.collection("users").document(util.getID()).update(a as Map<String, Any>)
            }
            activity.setCourse_MapMarking(checkBoxArray[0].isChecked, checkBoxArray[1].isChecked,
                    checkBoxArray[2].isChecked, checkBoxArray[3].isChecked)
            dismiss()
        })

        findViewById<Button>(R.id.Mapdialog_CanBtn).setOnClickListener(View.OnClickListener { dismiss() })
    }
}