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

        final String[] c0arr = {"북문", "농장문", "테크노문", "동문", "정문", "수의대문", "쪽문", "조은문", "솔로문", "서문", "수영장문"};
        final String[] c1arr = {"공대식당", "복현회관", "경대리아", "종합정보센터", "복지관"};
        final String[] c2arr = {"대운동장", "백호관", "일청담", "도서관", "본관", "백양로", "박물관", "미술관", "대강당", "글로벌플라자", "센트럴파크"};
        final String[] c3arr = {"공과대학 1호관", "IT대학 1호관", "사회과학대학", "경상대학", "생활과학대학", "자연과학대학", "농업생명과학대학"
                , "인문대학", "사범대학", "예술대학", "약학대학", "수의과대학"};

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                String Course = arrayList.get(position).split("/")[0];
                int courseNum = Integer.valueOf(arrayList.get(position).split("/")[1]);
                switch (Course) {
                    case "course0" :
                        name = c0arr[courseNum];
                        break;
                    case "course1" : name = c1arr[courseNum];break;
                    case "course2" : name = c2arr[courseNum];break;
                    case "course3" : name = c3arr[courseNum];break;
                    default: name = "NULL"; break;
                }
                PicFragment_Dialog dialog = new PicFragment_Dialog(activity, ref, arrayList2.get(position), name);
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