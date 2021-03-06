package com.dongcompany.knueverywhere.ui.Map

import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Context.VIBRATOR_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService

import com.dongcompany.knueverywhere.MainActivity
import com.dongcompany.knueverywhere.R
import com.dongcompany.knueverywhere.SharedPreferenceUtil
import com.dongcompany.knueverywhere.SplashActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class MapFragment_StartButtonDialog(context: Context) : Dialog(context) {

    private var activity: MainActivity = context as MainActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mapfragment_startbuttondialog)
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


        //탐방 시작 버튼
        findViewById<Button>(R.id.StartDialog_Btn).setOnClickListener(View.OnClickListener {
            val timeArray = arrayOf(180, 60, 120, 120)
            var time = 0;
            var selected = 0

            for (i in 0..3) {
                val util = SharedPreferenceUtil(activity)
                if (util.getCourseCheckBox(i) && util.getCourseInfo(i, "CLEAR") == false) {
                    selected++
                    time += timeArray[i]
                }
            }
            if (selected == 0) {
                Toast.makeText(activity, "코스를 선택해 주세요.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            Toast.makeText(activity, "탐방이 시작되었습니다!", Toast.LENGTH_SHORT).show()

            activity.startTimer(time)

            val db = FirebaseFirestore.getInstance()
            var t: Long = System.currentTimeMillis() + (time * 60 * 1000)
            //Log.d("시간시간", System.currentTimeMillis().toString() + "/" + (time * 60 * 1000))
            val m = hashMapOf("탐방종료시간" to t.toString(), "탐방상태" to true)
            db.collection("users").document(SharedPreferenceUtil(activity).getID()).update(m as Map<String, Any>);

            //푸시알림
            var builder : NotificationCompat.Builder? = null
            var manager : NotificationManager = activity.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            //버전 오레오 이상일 경우
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                manager.createNotificationChannel( NotificationChannel("CH1", "CH1", NotificationManager.IMPORTANCE_DEFAULT) )
                builder = NotificationCompat.Builder(activity, "CH1")
            }
            builder?.setContentTitle("KNU TRiP")
                ?.setContentText("탐방이 시작되었습니다! 제한 시간은 ${time}분 입니다.")
                ?.setSmallIcon(R.mipmap.ic_launcher_round)

            val notification = builder?.build()

            manager.notify(1,notification)
            val vib = activity.getSystemService(VIBRATOR_SERVICE) as Vibrator
            vib.vibrate(1000)
            dismiss()
        })

        findViewById<Button>(R.id.StartDialog_CanBtn).setOnClickListener(View.OnClickListener { dismiss() })
    }
}