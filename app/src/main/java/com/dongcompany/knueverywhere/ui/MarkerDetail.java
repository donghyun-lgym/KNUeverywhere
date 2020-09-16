package com.dongcompany.knueverywhere.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.dongcompany.knueverywhere.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MarkerDetail extends AppCompatActivity {

    private FirebaseStorage storage;
    private ImageView imageView;
    private TextView textView;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_detail);

        storage=FirebaseStorage.getInstance("");
        StorageReference storageRef = storage.getReference().child("i");

        //Glide.with(MarkerDetail.this)
        //        .using(new FirebaseImageLoader())
        //        .load(storageRef)
        //        .into(imageView);
        //텍스트 뷰 정보 DB에서 가져오기
        //RecuclerView에 storage에 있는 사진 다 가져오기 하면 끝
    }
}