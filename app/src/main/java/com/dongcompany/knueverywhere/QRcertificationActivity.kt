package com.dongcompany.knueverywhere

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class QRcertificationActivity : AppCompatActivity() {
    private lateinit var db:FirebaseFirestore
    private lateinit var storage:FirebaseStorage
    private lateinit var util:SharedPreferenceUtil
    private lateinit var userID:String

    private var imageUploaded = false
    private lateinit var imageView:ImageView
    private var bitmap: Bitmap? = null
    private var uri: Uri? = null

    private lateinit var builder:AlertDialog.Builder
    private lateinit var alertDialog : AlertDialog
    private lateinit var dialog2 : LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_q_rcertification)
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        util = SharedPreferenceUtil(this)
        userID = util.getID()

        dialog2 = LoadingDialog(this)
        dialog2.show()
        Handler().postDelayed({ dialog2.dismiss() }, 2000)

        builder = AlertDialog.Builder(this)
        builder.setTitle("오류").setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                    this.finish()
                })
                .setCancelable(false)
        alertDialog = builder.create()
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
                            //Toast.makeText(this, "탐방 유효 시간이 지났습니다. 재도전 하세요!", Toast.LENGTH_SHORT).show()
                            MainActivity.invalidityTravel(this)
                            chk = true
                            alertDialog.setMessage("탐방 유효 시간이 지났습니다. 재도전 하세요!\n앱이 종료됩니다.")
                            alertDialog.show()
                            return@addOnSuccessListener
                        }
                    }
                    else {
                        alertDialog.setMessage("현재 탐방 중이 아닙니다.\n앱이 종료됩니다.")
                        alertDialog.show()
                        return@addOnSuccessListener
                    }
                }
        Handler().postDelayed({
            if (!chk) {
                doMainFunction()
            }
        }, 1800)

        imageView = findViewById(R.id.QRActivity_ImageView)
        imageView.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1000);
        })
    }

    private fun doMainFunction() {
        val intent = intent
        val url:String = intent.getStringExtra("URL")
        val url_1:String = url.substring(url.length - 3, url.length)
        val course:Int = Integer.parseInt(url_1.split('_')[0])
        val courseIndex : Int = Integer.parseInt(url_1.split('_')[1])

        Log.d("QR인증", course.toString() + "_" + courseIndex.toString())
        if(util.getCourseCheckBox(course) == false) {
            alertDialog.setMessage("현재 탐방 중인 코스가 아닙니다.\n앱이 종료됩니다.")
            alertDialog.show()
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
        val btn:Button = findViewById(R.id.QRActivity_Button);
        btn.setOnClickListener(View.OnClickListener {
            if (!imageUploaded) {
                Toast.makeText(this, "인증샷을 업로드 해 주세요.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (alreadyCert == true) {
                alertDialog.setMessage("이미 인증된 장소입니다.\n앱이 종료됩니다.")
                alertDialog.show()
                return@OnClickListener
            }

            dialog2.show()

            val a = hashMapOf(
                    courseDetailTextView.text.toString() to true
            )

            db.collection("users").document(userID).collection(collectionArray[course])
                    .document(collectionArray[course]).update(a as Map<String, Any>)

            val format1 = SimpleDateFormat("yyyy년 MM월dd일 HH:mm")
            val time = Date();
            var bbb = hashMapOf("업로드" to true,
                    "날짜" to format1.format(time),
                    "이름" to util.getName()
            )
            Log.d("nonono", "course" + course.toString() + "-" + courseIndex.toString() + "-" + util.getID())
            db.collection("picture").document("course" + course.toString())
                    .collection(courseIndex.toString()).document(util.getID()).set(bbb as Map<String, Any>)

            //인증샷 업로드
            if (bitmap != null) {
                val storageRef = storage.getReferenceFromUrl("gs://knu-everywhere.appspot.com/course" + course + "/" + courseIndex + "/" + util.getID() + ".jpg")

                val uploadTask: UploadTask = storageRef.putFile(uri!!);
                uploadTask.addOnFailureListener {
                }.addOnSuccessListener {
                    //Toast.makeText(this, "업로드 성공", Toast.LENGTH_SHORT).show()
                }
            }

            //모두 체크해서 CLEAR 하기
            var c = false
            for (i in 0..3) {
                if (c == false && util.getCourseCheckBox(i)) {
                    db.collection("users").document(userID).collection(collectionArray[i]).document(collectionArray[i])
                            .get()
                            .addOnSuccessListener { documentSnapshot ->
                                val map = documentSnapshot.getData()

                                for (key in map!!.keys) {
                                    if (!(key.equals("CLEAR")) && map.get(key) == false) {
                                        c = true
                                        return@addOnSuccessListener
                                    }
                                }
                            }
                }
            }

            Handler().postDelayed({
                Toast.makeText(this, "인증되었습니다.", Toast.LENGTH_SHORT).show()
                dialog2.dismiss()
                if (c == false) {
                    val aa = hashMapOf("CLEAR" to true)
                    var bb = hashMapOf("탐방상태" to false)
                    for (i in 0..3) {
                        if (util.getCourseCheckBox(i)) {
                            db.collection("users").document(userID).collection(collectionArray[i]).document(collectionArray[i])
                                    .update(aa as Map<String, Any>)
                            bb.put("체크박스_코스" + i.toString(), false)
                        }
                    }
                    util.setTravelState(false)
                    db.collection("users").document(userID)
                            .update(bb as Map<String, Any>)
                    startActivity(Intent(this, CourseCompleteActivity::class.java))
                }
                finish()
            }, 2100)
        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1000) {
            if(resultCode == RESULT_OK) {
                try {
                    val inputStream : InputStream? = contentResolver.openInputStream(data?.data!!)

                    bitmap= BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()
                    imageView.setBackgroundResource(0)
                    imageView.setImageBitmap(bitmap)

                    uri = data?.data!!
                    imageUploaded = true
                }
                catch (e: Exception){
                    Toast.makeText(this, "사진 선택 에러", Toast.LENGTH_LONG).show()
                }
            }
            else if(resultCode == RESULT_CANCELED)
            {
                //Toast.makeText(this,"사진 선택 취소",Toast.LENGTH_LONG).show()
            }
        }
    }
}