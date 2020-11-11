package com.dongcompany.knueverywhere.ui.MapInfo;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.dongcompany.knueverywhere.LoadingDialog;
import com.dongcompany.knueverywhere.R;
import com.dongcompany.knueverywhere.ui.MapInfo.MapInfoActivity;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PicGridAdapter extends BaseAdapter {
    private MapInfoActivity activity;
    private ArrayList<picInfo> arrayList = new ArrayList<>();
    private String Course;
    private int courseNum;

    public PicGridAdapter(Context context, String Course, int courseNum) {
        this.activity = (MapInfoActivity) context;
        this.Course = Course;
        this.courseNum = courseNum;
    }

    public void addItem(picInfo item) {
        arrayList.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public picInfo getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.girdview_design,parent,false);

        ImageView imageView1= view.findViewById(R.id.first_ima);
        FirebaseStorage mStorage = FirebaseStorage.getInstance("gs://knu-everywhere.appspot.com");
        final StorageReference ref = mStorage.getReference().child("/" + Course + "/" + courseNum + "/" + arrayList.get(position).getId() + ".jpg");
        Log.d("nonono", ref.toString());
        Glide.with(activity)
                .using(new FirebaseImageLoader())
                .load(ref)
                .into(imageView1);

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PicFragment_Dialog dialog = new PicFragment_Dialog(activity, ref, arrayList.get(position));
                dialog.show();
                final LoadingDialog dialog2 = new LoadingDialog(activity);
                dialog2.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog2.dismiss();
                    }
                }, 800);
            }
        });
        return view;
    }
}
