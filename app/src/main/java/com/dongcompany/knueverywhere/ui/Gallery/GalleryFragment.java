package com.dongcompany.knueverywhere.ui.Gallery;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dongcompany.knueverywhere.R;
import com.dongcompany.knueverywhere.SharedPreferenceUtil;
import com.dongcompany.knueverywhere.ui.MapInfo.PicGridAdapter;
import com.dongcompany.knueverywhere.ui.MapInfo.picInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.naver.maps.map.overlay.Marker;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {
    private Activity activity;
    private String uID;
    private GridView gridView;
    private ArrayList<String> courseArray=new ArrayList<String>();
    private ArrayList<picInfo> courseArray2=new ArrayList<picInfo>();

    public GalleryFragment(Context context) {
        activity = (Activity) context;
        SharedPreferenceUtil util = new SharedPreferenceUtil(activity);
        uID = util.getID();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        gridView = root.findViewById(R.id.gridView);
        AddCourse("course0", 11);
        AddCourse("course1", 5);
        AddCourse("course2", 11);
        AddCourse("course3",12);
        GalleryGridAdapter  galleryGridAdapter= new GalleryGridAdapter(activity, uID, courseArray, courseArray2);
        gridView.setAdapter(galleryGridAdapter);
        return root;
    }
    public void AddCourse(final String Course, int end){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for(int i=0;i<end;i++) {
            final int finalI = i;
            db.collection("picture").document(Course).collection(String.valueOf(i))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String Name = (String) document.getId();
                                    if(Name.equals(uID)){
                                        Log.d("갤러리", Name + " - " + Course+"/"+ finalI);
                                        courseArray.add(Course+"/"+ finalI);

                                        courseArray2.add(
                                                new picInfo(document.getId(),
                                                        (String) (document.getData().get("날짜")),
                                                        true, (String) (document.getData().get("이름"))
                                                ));
                                    }
                                }
                            } else {
                                Log.d("갤러리", "Failure");
                            }
                        }
                    });
        }
    }

}