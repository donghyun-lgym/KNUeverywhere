package com.dongcompany.knueverywhere;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.dongcompany.knueverywhere.Login.LoginActivity;
import com.dongcompany.knueverywhere.ui.CyberView.AwardsFragment;
import com.dongcompany.knueverywhere.ui.Gallery.GalleryFragment;
import com.dongcompany.knueverywhere.ui.Map.MapFragment;
import com.dongcompany.knueverywhere.ui.Map.MapFragment_TutorialDialog;
import com.dongcompany.knueverywhere.ui.MapInfo.PicFragment_Dialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

//로그인 처음 할 시 서버에서 데이터를 받아와서 SharedPreferenceUtil 초기화해주기

public class MainActivity extends AppCompatActivity  {
    private SharedPreferenceUtil util;

    private MapFragment fg1;
    private GalleryFragment fg2;
    private AwardsFragment fg3;

    private FirebaseFirestore db;

    // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //로딩 다이얼로그
        final LoadingDialog dialog2 = new LoadingDialog(this);
        dialog2.show();

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        findViewById(R.id.appBar_MenuButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        util = new SharedPreferenceUtil(this);

        db = FirebaseFirestore.getInstance();
        final String id = util.getID();

        //튜토리얼
        if(util.getTutorial() == false) {
            util.setTutorial(true);
            HashMap bbb = new HashMap();
            bbb.put("튜토리얼", true);
            db.collection("users").document(id)
                    .update(bbb);

            MapFragment_TutorialDialog dialog = new MapFragment_TutorialDialog(this);
            dialog.show();
        }

        //프래그먼트 추가
        fg1 = new MapFragment(MainActivity.this);
        fg2 = new GalleryFragment(MainActivity.this);
        fg3 = new AwardsFragment(MainActivity.this);
        getSupportFragmentManager().beginTransaction().add(R.id.nav_frameLayout, fg1).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.nav_frameLayout, fg2).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.nav_frameLayout, fg3).commit();
        getSupportFragmentManager().beginTransaction().show(fg1).commit();
        getSupportFragmentManager().beginTransaction().hide(fg2).commit();
        getSupportFragmentManager().beginTransaction().hide(fg3).commit();

        //탐방정보 초기화
        final String[] aaa = {"경북대학교의 문", "경북대학교의 식당", "경북대학교의 주요 장소", "경북대학교의 단과 대학"};
        for(int i = 0; i < 4; i++) {
            final int finalI = i;
            db.collection("users").document(id).collection(aaa[i]).document(aaa[i])
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Map map = documentSnapshot.getData();
                            for(Object key : map.keySet()) {
                                //(0~3, "북문", true)
                                util.setCourseInfo(finalI, key.toString(), (Boolean) map.get(key));
                            }
                            fg1.MarkingFromDB();
                        }
                    });
        }

        //네비게이션 헤더 초기화 및 수정
        View nav_Header = navigationView.getHeaderView(0);
        final TextView nameTextView = nav_Header.findViewById(R.id.nav_header_nameTextView);
        final TextView stdnumTextView = nav_Header.findViewById(R.id.nav_header_stdnumTextView);
        final TextView phoneTextView = nav_Header.findViewById(R.id.nav_header_phoneTextView);
        final TextView travelTextView = nav_Header.findViewById(R.id.nav_header_TravelTextView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int k = 0;
                for(int i = 0; i < 4; i++) {
                    if(util.getCourseInfo(i, "CLEAR") == true) k++;
                }

                nameTextView.setText(util.getName() + " (" + util.getID() + ")");
                stdnumTextView.setText(util.getStdNum());
                phoneTextView.setText(util.getPhone());
                travelTextView.setText("탐방 진행 상황 : " + k + "/4");
                dialog2.dismiss();
            }
        }, 3000);




        //네이게이션뷰 아이템 셀렉트
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.nav_Map:
                        getSupportFragmentManager().beginTransaction().hide(fg2).commit();
                        getSupportFragmentManager().beginTransaction().hide(fg3).commit();
                        getSupportFragmentManager().beginTransaction().show(fg1).commit();
                        break;
                    case R.id.nav_Gallery:
                        getSupportFragmentManager().beginTransaction().hide(fg1).commit();
                        getSupportFragmentManager().beginTransaction().hide(fg3).commit();
                        getSupportFragmentManager().beginTransaction().show(fg2).commit();
                        break;
                    case R.id.nav_Awards:
                        getSupportFragmentManager().beginTransaction().hide(fg2).commit();
                        getSupportFragmentManager().beginTransaction().hide(fg1).commit();
                        getSupportFragmentManager().beginTransaction().show(fg3).commit();
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        //탐방 중인 상태 초기화
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                db.collection("users").document(id)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Boolean rst = (Boolean) documentSnapshot.getData().get("탐방상태");
                                if(rst) {
                                    Long now = System.currentTimeMillis();
                                    Long end = Long.parseLong(documentSnapshot.getData().get("탐방종료시간").toString());

                                    if(end >= now) { // 탐방종료시간이 지나지 않음
                                        util.setTravelState(rst);
                                        fg1.startTimer((int) ((end - now) / 60000));
                                    }
                                    else { // 타임오버
                                        util.setTravelState(false);
                                        Toast.makeText(MainActivity.this, "탐방 유효 시간이 지났습니다. 다시 시도하세요!", Toast.LENGTH_SHORT).show();
                                        invalidityTravel(MainActivity.this);
                                    }
                                }

                            }
                        });
            }
        }, 1700);
    }

    public void startTimer(int time) {
        fg1.startTimer(time);
    }
    public void setCourse_MapMarking(Boolean course0, Boolean course1, Boolean course2, Boolean course3) {
        fg1.MapMarking(course0, course1, course2, course3);
    }

    public static void invalidityTravel(final Context context) {
        final SharedPreferenceUtil util = new SharedPreferenceUtil(context);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final StorageReference reference = FirebaseStorage.getInstance().getReference();

        final HashMap b = new HashMap();
        b.put("탐방상태", false);
        final String[] aaa = {"경북대학교의 문", "경북대학교의 식당", "경북대학교의 주요 장소", "경북대학교의 단과 대학"};
        final int[] bbb = {11, 5, 11, 12};
        for(int i = 0; i < 4; i++) {
            if(util.getCourseCheckBox(i) == true && util.getCourseInfo(i, "CLEAR") == false) {
                //db 수정
                final int finalI = i;
                db.collection("users").document(util.getID()).collection(aaa[finalI]).document(aaa[finalI])
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Map map = documentSnapshot.getData();
                                HashMap t = new HashMap();
                                for(Object key : map.keySet()) {
                                    t.put((String) key, false);
                                }
                                db.collection("users").document(util.getID())
                                        .collection(aaa[finalI]).document(aaa[finalI]).update(t);
                            }
                        });
                //storage 수정 //db - picture 관련 제거
                for(int j = 0; j < bbb[i]; j++) {
                    db.collection("picture").document("course" + i).collection(String.valueOf(j))
                            .document(util.getID()).delete();

                    reference.child("course" + i + "/" + j + "/" + util.getID() + ".jpg").delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("nono", e.toString());
                                }
                            });
                }

            }
        }
        db.collection("users").document(util.getID()).update(b);
        util.setTravelState(false);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public void stopTravel() {
        fg1.stopTravel();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferenceUtil util = new SharedPreferenceUtil(MainActivity.this);

        switch (item.getItemId()) {
            case R.id.action_Logout:
                util.setID("null"); util.setName("null"); util.setPhone("null"); util.setAutoLogin(false);
                util.setCourseCheckBox(0, false); util.setCourseCheckBox(1, false);
                util.setCourseCheckBox(2, false); util.setCourseCheckBox(3, false);
                util.setStdNum("null");
                util.setTravelState(false);
                util.setTutorial(false);
                Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if(System.currentTimeMillis()-backKeyPressedTime>=2000){
            backKeyPressedTime=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis()-backKeyPressedTime<2000){
            finish();
        }
    }
}