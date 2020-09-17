package com.dongcompany.knueverywhere.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dongcompany.knueverywhere.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class MarkerDetail extends AppCompatActivity {

    private FirebaseStorage storage;
    private DatabaseReference mReference;
    private FirebaseDatabase mDatabase;
    private ImageView imageView;
    private TextView textView;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_detail);

        textView.findViewById(R.id.textView);

        Intent intent = getIntent();
        final String where =intent.getExtras().getString("where");
        final String index =intent.getExtras().getString("index");

        storage=FirebaseStorage.getInstance("");
        StorageReference storageRef = storage.getReference().child(index);

        //Glide.with(MarkerDetail.this)
        //        .using(new FirebaseImageLoader())
        //        .load(storageRef)
        //        .into(imageView);

        mDatabase=FirebaseDatabase.getInstance();
        mReference=mDatabase.getReference();
        mReference.child("설명").child(where).child(index).addValueEventListener(new ValueEventListener() {
            String text="";
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text=dataSnapshot.getValue().toString();
                textView.setText(text);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //RecyclerView에 storage에 있는 사진 다 가져오기 하면 끝

    }
}