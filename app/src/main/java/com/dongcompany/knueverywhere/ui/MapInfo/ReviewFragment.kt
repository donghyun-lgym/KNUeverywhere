package com.dongcompany.knueverywhere.ui.MapInfo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dongcompany.knueverywhere.R
import com.google.firebase.firestore.FirebaseFirestore


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
        root.findViewById<Button>(R.id.ReviewFragment_writeButton).setOnClickListener(View.OnClickListener {

        })
        return root
    }

}