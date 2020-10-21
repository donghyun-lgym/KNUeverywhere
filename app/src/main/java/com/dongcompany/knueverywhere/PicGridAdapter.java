package com.dongcompany.knueverywhere;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PicGridAdapter extends BaseAdapter {
    private ArrayList<String> arrayList;
    public PicGridAdapter(ArrayList<String> arrayList) {
        this.arrayList=arrayList;
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
        arrayList.get(position).toString();
        Picasso.get().load(String.valueOf(arrayList)).placeholder(R.drawable.app_icon).into(imageView1);

        return view;
    }

}
