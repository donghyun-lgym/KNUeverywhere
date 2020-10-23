package com.dongcompany.knueverywhere.ui.MapInfo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.dongcompany.knueverywhere.PicGridAdapter;
import com.dongcompany.knueverywhere.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class PicFragment extends Fragment {
    private MapInfoActivity activity;
    private String Course;
    private int courseNum;
    private GridView gridView;
    private ArrayList<String> arrayList;

    private LinearLayout linearLayout;

    private ArrayList<String> usersArray = new ArrayList();


    //Course는 코스명 (course0, course1, course2, course3) : String
    //courseNum은 코스 내 지점들 (0, 1, 2, 3, 4, 5, ....) : int
    //Firebase storage 경로는 Course + "/" + String.valueOf(courseNum) + "아이디명" + ".jpg" 겠지?

    public PicFragment(Context context, String Course, int courseNum) {
        this.activity = (MapInfoActivity) context;
        this.Course = Course; this.courseNum = courseNum;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pic, container, false);
        gridView= root.findViewById(R.id.pic_gridview);




        //DB에서 해당 코스에 등록된 아이디들 끌고옴
        Log.d("nonono", Course + "/" + courseNum);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("picture").document(Course).collection(String.valueOf(courseNum))
                .document("users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map map = documentSnapshot.getData();
                        for(Object key : map.keySet()) {
                            if((Boolean) (map.get((String) key)) == true) {
                                usersArray.add((String) key);

                                Log.d("nonono", (String) key);
                            }
                        }
                    }
                });
        FirebaseStorage mStorage = FirebaseStorage.getInstance("gs://knu-everywhere.appspot.com");
        //Picasso.get().load(URI).placeholder(R.drawable.luggageicon).into(imageView);
        PicGridAdapter picGridAdapter = new PicGridAdapter(activity, usersArray, Course, courseNum);
        gridView.setAdapter(picGridAdapter);

        return root;
    }
}