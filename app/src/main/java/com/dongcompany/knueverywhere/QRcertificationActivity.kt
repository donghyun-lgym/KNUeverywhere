package com.dongcompany.knueverywhere

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class QRcertificationActivity : AppCompatActivity() {
    private lateinit var db:FirebaseFirestore
    private lateinit var util:SharedPreferenceUtil
    private lateinit var userID:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_q_rcertification)
        db = FirebaseFirestore.getInstance()
        util = SharedPreferenceUtil(this)

        userID = util.getID()

        //탐방 중인 상태 초기화
        var chk =false
        db.collection("users").document(userID)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val rst = documentSnapshot.data?.get("탐방상태") as Boolean
                    if (rst) {
                        val now = System.currentTimeMillis()
                        val end = documentSnapshot.data!!["탐방종료시간"].toString().toLong()
                        if (end < now)  // 타임오버
                        {
                            util.setTravelState(false)
                            Toast.makeText(this, "탐방 유효 시간이 지났습니다. 재도전 하세요!", Toast.LENGTH_SHORT).show()
                            MainActivity.invalidityTravel(this)
                            chk = true
                            return@addOnSuccessListener
                        }
                    }
                }
        Handler().postDelayed({
            if(chk) {
                finish()
            }
            else {
                doMainFunction()
            }
        }, 1800)
    }

    private fun doMainFunction() {
        val intent = intent
        val url:String = intent.getStringExtra("URL")
        val url_1:String = url.substring(url.length - 3, url.length)
        val course:Int = Integer.parseInt(url_1.split('_')[0])
        val courseIndex : Int = Integer.parseInt(url_1.split('_')[1])

        Log.d("QR인증", course.toString() + "_" + courseIndex.toString())
        if(util.getCourseCheckBox(course) == false) {
            Toast.makeText(this, "현재 탐방 중인 코스가 아닙니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val courseArray = arrayOf("문", "식당", "주요 장소", "단과대학")
        val courseTextView:TextView = findViewById(R.id.QRActivity_CourseTextView);
        courseTextView.setText(courseArray[course])

        val courseArray0 = arrayOf("북문", "농장문", "테크노문", "동문", "정문", "수의대문", "쪽문", "조은문", "솔로문", "서문", "수영장문")
        val courseArray1 = arrayOf("공대식당", "복현회관", "경대리아", "종합정보센터", "복지관")
        val courseArray2 = arrayOf("대운동장", "백호관", "일청담", "도서관", "본관", "백양로", "박물관", "미술관", "대강당", "글로벌플라자", "센트롤파크")
        val courseArray3 = arrayOf("공과대학 1호관", "IT대학 1호관", "사회과학대학", "경상대학", "생활과학대학", "자연과학대학", "농업생명과학대학", "인문대학", "사범대학", "예술대학", "약학대학", "수의과대학")
        val courseDetailTextView:TextView = findViewById(R.id.QRActivity_CourseDetailTextView);

        when(course) {
            0 -> courseDetailTextView.setText(courseArray0[courseIndex])
            1 -> courseDetailTextView.setText(courseArray1[courseIndex])
            2 -> courseDetailTextView.setText(courseArray2[courseIndex])
            3 -> courseDetailTextView.setText(courseArray3[courseIndex])
        }

        //이미 인증된 곳이라면...
        val collectionArray = arrayOf("경북대학교의 문", "경북대학교의 식당", "경북대학교의 주요 장소", "경북대학교의 단과 대학")
        var alreadyCert = false
        db.collection("users").document(userID).collection(collectionArray[course]).document(collectionArray[course])
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val rst = documentSnapshot.data?.get(courseDetailTextView.text.toString()) as Boolean
                    if (rst) {
                        alreadyCert = true
                    }
                }

        //인증버튼
        findViewById<Button>(R.id.QRActivity_Button).setOnClickListener(View.OnClickListener {
            if(alreadyCert == true) {
                Toast.makeText(this, "이미 인증된 장소입니다.", Toast.LENGTH_SHORT).show()
                finish()
                return@OnClickListener
            }
            val a = hashMapOf(
                    courseDetailTextView.text.toString() to true
            )
            Toast.makeText(this, "인증되었습니다.", Toast.LENGTH_SHORT).show()
            db.collection("users").document(userID).collection(collectionArray[course])
                    .document(collectionArray[course]).update(a as Map<String, Any>)

            //모두 체크해서 CLEAR 하기기
           finish()
        })
    }
}