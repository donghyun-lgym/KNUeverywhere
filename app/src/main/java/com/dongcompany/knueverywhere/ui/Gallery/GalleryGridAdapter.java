package com.dongcompany.knueverywhere.ui.Gallery;

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
import com.dongcompany.knueverywhere.SharedPreferenceUtil;
import com.dongcompany.knueverywhere.ui.MapInfo.MapInfoActivity;
import com.dongcompany.knueverywhere.ui.MapInfo.PicFragment_Dialog;
import com.dongcompany.knueverywhere.ui.MapInfo.picInfo;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GalleryGridAdapter extends BaseAdapter {
    private Context activity;
    private ArrayList<String> arrayList;
    private ArrayList<picInfo> arrayList2;
    private String uID;

    public GalleryGridAdapter(Context context, String uID, ArrayList<String> arrayList, ArrayList<picInfo> arrayList2) {
        Log.d("갤러리", "AdapterMake!");
        this.activity = context;
        this.arrayList=arrayList; this.arrayList2 = arrayList2;
        this.uID =uID;
    }

    @Override
    public int getCount() {return arrayList.size();   }

    @Override
    public Object getItem(int position) {return  arrayList.get(position);    }

    @Override
    public long getItemId(int position) {return position;    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.girdview_design,parent,false);

        ImageView imageView1= view.findViewById(R.id.first_ima);
        FirebaseStorage mStorage = FirebaseStorage.getInstance("gs://knu-everywhere.appspot.com");
        final StorageReference ref = mStorage.getReference().child("/" + arrayList.get(position) + "/" + uID +".jpg");
        Glide.with(parent.getContext())
                .using(new FirebaseImageLoader())
                .load(ref)
                .into(imageView1);

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PicFragment_Dialog dialog = new PicFragment_Dialog(activity, ref, arrayList2.get(position));
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