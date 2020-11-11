package com.dongcompany.knueverywhere.ui.MapInfo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.dongcompany.knueverywhere.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class PicFragment extends Fragment {
    private MapInfoActivity activity;
    private String Course;
    private int courseNum;
    private GridView gridView;


    public PicFragment(Context context, String Course, int courseNum) {
        this.activity = (MapInfoActivity) context;
        this.Course = Course; this.courseNum = courseNum;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pic, container, false);
        gridView= root.findViewById(R.id.pic_gridview);
        final PicGridAdapter picGridAdapter = new PicGridAdapter(activity, Course, courseNum);
        gridView.setAdapter(picGridAdapter);

        //DB에서 해당 코스에 등록된 아이디들 끌고옴
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("picture").document(Course).collection(String.valueOf(courseNum))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if((Boolean) (document.getData().get("업로드")) == true) {
                                    picGridAdapter.addItem(
                                            new picInfo(document.getId(), (String) (document.getData().get("날짜")), true, (String) (document.getData().get("이름")))
                                    );
                                }
                            }
                        } else {

                        }
                    }
                });

        return root;
    }
}