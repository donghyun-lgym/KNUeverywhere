package com.dongcompany.knueverywhere.ui.Gallery;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dongcompany.knueverywhere.R;
import com.dongcompany.knueverywhere.SharedPreferenceUtil;
import com.dongcompany.knueverywhere.ui.MapInfo.MapInfoActivity;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GalleryGridAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> arrayList= new ArrayList<String>();
    private String uID;

    public GalleryGridAdapter(String uID, ArrayList<String> arrayList) {
        Log.d("갤러리", "AdapterMake!");

        this.arrayList=arrayList;
        this.uID =uID;
    }

    @Override
    public int getCount() {return arrayList.size();   }

    @Override
    public Object getItem(int position) {return  arrayList.get(position);    }

    @Override
    public long getItemId(int position) {return position;    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.girdview_design,parent,false);

        ImageView imageView1= view.findViewById(R.id.first_ima);
        FirebaseStorage mStorage = FirebaseStorage.getInstance("gs://knu-everywhere.appspot.com");
        StorageReference ref = mStorage.getReference().child("/" + arrayList.get(position) + "/" + uID +".jpg");
        Glide.with(parent.getContext())
                .using(new FirebaseImageLoader())
                .load(ref)
                .into(imageView1);
        return view;
    }
}