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


class AroundFragment(val context: MapInfoActivity, val course: String, val CourseNum: Int) : Fragment() {
    private lateinit var adapter: Around_rvAdapter
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_around, container, false)
        adapter = Around_rvAdapter(context, course, CourseNum)
        val recyclerView:RecyclerView = root.findViewById(R.id.AroundFragment_RecyclerView)
        val manager: LinearLayoutManager = LinearLayoutManager(context);
        manager.setOrientation(
                LinearLayoutManager.VERTICAL
        );
        recyclerView.setLayoutManager(manager);
        recyclerView.adapter = adapter
        //db로부터 로드
        db.collection("picture").document(course).collection(CourseNum.toString())
                .document("around").collection("around")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val name:String = document.id
                        val time:String = document.data.get("영업시간") as String
                        val content:String = document.data.get("제휴") as String
                        val type:String = document.data.get("종류") as String
                        val address:String = document.data.get("주소") as String
                        adapter.addItem(Info_coalition(name, time, content, type, address))
                    }
                }
                .addOnFailureListener{ result ->
                    Log.e("AroundFragment 데이터로드 에러", "data load error")
                }
        return root
    }
}