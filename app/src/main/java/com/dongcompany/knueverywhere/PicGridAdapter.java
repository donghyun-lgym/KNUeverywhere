package com.dongcompany.knueverywhere;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.dongcompany.knueverywhere.ui.MapInfo.MapInfoActivity;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PicGridAdapter extends BaseAdapter {
    private MapInfoActivity activity;
    private ArrayList<String> arrayList;
    private String Course;
    private int courseNum;

    public PicGridAdapter(Context context, ArrayList<String> arrayList, String Course, int courseNum) {
        this.activity = (MapInfoActivity) context;
        this.arrayList=arrayList;
        this.Course = Course;
        this.courseNum = courseNum;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.girdview_design,parent,false);

        ImageView imageView1= view.findViewById(R.id.first_ima);
        FirebaseStorage mStorage = FirebaseStorage.getInstance("gs://knu-everywhere.appspot.com");
        StorageReference ref = mStorage.getReference().child("/" + Course + "/" + courseNum + "/" + arrayList.get(position) + ".jpg");

       //arrayList.get(position).toString();
        Log.d("PICTURE", String.valueOf(ref));
//        Picasso.get().load(String.valueOf(ref)).placeholder(R.drawable.app_icon).into(imageView1);
        Glide.with(activity)
                .using(new FirebaseImageLoader())
                .load(ref)
                .into(imageView1);

        return view;
    }

}
