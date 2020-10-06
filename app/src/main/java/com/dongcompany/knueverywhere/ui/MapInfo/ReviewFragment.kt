package com.dongcompany.knueverywhere.ui.MapInfo

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dongcompany.knueverywhere.R
import com.dongcompany.knueverywhere.SharedPreferenceUtil
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*


class ReviewFragment(val context: MapInfoActivity, val course: String, val CourseNum: Int) : Fragment() {
    private lateinit var adapter: Review_rvAdapter
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_review, container, false)
        adapter = Review_rvAdapter(context)
        val recyclerView:RecyclerView = root.findViewById(R.id.ReviewFragment_RecyclerView)
        val manager: LinearLayoutManager = LinearLayoutManager(context);
        manager.setOrientation(
                LinearLayoutManager.VERTICAL
        );
        recyclerView.setLayoutManager(manager);

        recyclerView.adapter = adapter
        //db로부터 로드
        db.collection("review").document(course).collection(CourseNum.toString())
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val name:String = document.data.get("이름") as String
                        val date:String = document.data.get("날짜") as String
                        val id:String = document.data.get("ID") as String
                        val content:String = document.data.get("내용") as String
                        adapter.addItem(Info(name, id, date, content))
                    }
                    adapter.setListSort();
                }
                .addOnFailureListener{ result ->
                    Log.e("MainActivity. 데이터로드 에러", "data load error")
                }

        //작성 버튼
        val c0arr = arrayOf("북문", "농장문", "테크노문", "동문", "정문", "수의대문", "쪽문", "조은문", "솔로문", "서문", "수영장문")
        val c1arr = arrayOf("공대식당", "복현회관", "경대리아", "종합정보센터", "복지관")
        val c2arr = arrayOf("대운동장", "백호관", "일청담", "도서관", "본관", "백양로", "박물관", "미술관", "대강당", "글로벌플라자", "센트럴파크")
        val c3arr = arrayOf("공과대학 1호관", "IT대학 1호관", "사회과학대학", "경상대학", "생활과학대학", "자연과학대학", "농업생명과학대학", "인문대학", "사범대학", "예술대학", "약학대학", "수의과대학")
        val writeButton = root.findViewById<Button>(R.id.ReviewFragment_writeButton)
        writeButton.setOnClickListener(View.OnClickListener {
            val util = SharedPreferenceUtil(context)
            val A = course.substring(6, 7)
            when(A) {
                "0" -> {
                    if(util.getCourseInfo(0, c0arr[CourseNum]) == false) {
                        Toast.makeText(context, "탐방한 지점이 아닙니다.", Toast.LENGTH_SHORT).show()
                        return@OnClickListener
                    }
                    else if(util.getCourseInfo(0, "CLEAR") == false) {
                        Toast.makeText(context, "탐방한 코스가 아닙니다.", Toast.LENGTH_SHORT).show()
                        return@OnClickListener
                    }
                }
                "1" -> {
                    if(util.getCourseInfo(0, c0arr[CourseNum]) == false) {
                        Toast.makeText(context, "탐방한 지점이 아닙니다.", Toast.LENGTH_SHORT).show()
                        return@OnClickListener
                    }else if(util.getCourseInfo(0, "CLEAR") == false) {
                        Toast.makeText(context, "탐방한 코스가 아닙니다.", Toast.LENGTH_SHORT).show()
                        return@OnClickListener
                    }
                }
                "2" -> {
                    if(util.getCourseInfo(0, c0arr[CourseNum]) == false) {
                        Toast.makeText(context, "탐방한 지점이 아닙니다.", Toast.LENGTH_SHORT).show()
                        return@OnClickListener
                    }else if(util.getCourseInfo(0, "CLEAR") == false) {
                        Toast.makeText(context, "탐방한 코스가 아닙니다.", Toast.LENGTH_SHORT).show()
                        return@OnClickListener
                    }
                }
                "3" -> {
                    if(util.getCourseInfo(0, c0arr[CourseNum]) == false) {
                        Toast.makeText(context, "탐방한 지점이 아닙니다.", Toast.LENGTH_SHORT).show()
                        return@OnClickListener
                    }else if(util.getCourseInfo(0, "CLEAR") == false) {
                        Toast.makeText(context, "탐방한 코스가 아닙니다.", Toast.LENGTH_SHORT).show()
                        return@OnClickListener
                    }
                }
                else -> {
                    Log.d("nonono", "리뷰 작성 중 에러!!!")
                }
            }
            if (util.getTravelState() == true) {
                Toast.makeText(context, "탐방을 완료한 후 작성하실 수 있습니다.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            val dialog = ReviewWrite_Dialog(context, course, CourseNum)
            dialog.show()
        })
        return root
    }


    class ReviewWrite_Dialog(context: Context, val course: String, val CourseNum: Int) : Dialog(context) {

        private var activity: MapInfoActivity = context as MapInfoActivity


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.reviewfragment_reviewdialog)
            setCancelable(true)
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


            val EditText:EditText = findViewById(R.id.ReviewDialog_EditText)


            //확인 버튼
            findViewById<Button>(R.id.ReviewDialog_Btn).setOnClickListener(View.OnClickListener {
                if (EditText.text.length <= 5) {
                    Toast.makeText(context, "리뷰가 너무 짧습니다.", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }
                val db = FirebaseFirestore.getInstance()
                val id = SharedPreferenceUtil(context).getID()
                val name = SharedPreferenceUtil(context).getName()
                val format = SimpleDateFormat("yyyy. MM. dd")
                val time: String = format.format(Date())
                val map = hashMapOf("ID" to id, "날짜" to time, "내용" to EditText.text.toString(), "이름" to name)
                db.collection("review").document(course).collection(CourseNum.toString())
                        .add(id)
                db.collection("review").document(course).collection(CourseNum.toString())
                        .document(id)
                        .update(map as Map<String, Any>)
                dismiss()
            })
            findViewById<Button>(R.id.ReviewDialog_CanBtn).setOnClickListener(View.OnClickListener {
                dismiss()
            })
        }
    }

}