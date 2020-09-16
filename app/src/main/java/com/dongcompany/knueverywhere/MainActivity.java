package com.dongcompany.knueverywhere;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.dongcompany.knueverywhere.ui.Awards.AwardsFragment;
import com.dongcompany.knueverywhere.ui.Gallery.GalleryFragment;
import com.dongcompany.knueverywhere.ui.Map.MapFragment;
import com.google.android.material.navigation.NavigationView;

import java.lang.reflect.Array;

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

    // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        findViewById(R.id.appBar_MenuButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });


        util = new SharedPreferenceUtil(this);
        //네이게이션뷰 아이템 셀렉트
        //프래그먼트를 교체시켜주는 곳
        fg1 = new MapFragment(this);
        fg2 = new GalleryFragment(this);
        fg3 = new AwardsFragment(this);
        getSupportFragmentManager().beginTransaction().add(R.id.nav_frameLayout, fg1).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.nav_frameLayout, fg2).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.nav_frameLayout, fg3).commit();
        getSupportFragmentManager().beginTransaction().show(fg1).commit();
        getSupportFragmentManager().beginTransaction().hide(fg2).commit();
        getSupportFragmentManager().beginTransaction().hide(fg3).commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.nav_Map:
                        getSupportFragmentManager().beginTransaction().hide(fg2).commit();
                        getSupportFragmentManager().beginTransaction().hide(fg3).commit();
                        getSupportFragmentManager().beginTransaction().show(fg1).commit();
                        Toast.makeText(getApplicationContext(), "맵 프래그먼트 선택됨.", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_Gallery:
                        getSupportFragmentManager().beginTransaction().hide(fg1).commit();
                        getSupportFragmentManager().beginTransaction().hide(fg3).commit();
                        getSupportFragmentManager().beginTransaction().show(fg2).commit();
                        Toast.makeText(getApplicationContext(), "사진첩 프래그먼트 선택됨.", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_Awards:
                        getSupportFragmentManager().beginTransaction().hide(fg2).commit();
                        getSupportFragmentManager().beginTransaction().hide(fg1).commit();
                        getSupportFragmentManager().beginTransaction().show(fg3).commit();
                        Toast.makeText(getApplicationContext(), "명예의전당 프래그먼트 선택됨.", Toast.LENGTH_SHORT).show();
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    public void setCourse_MapMarking(Boolean course0, Boolean course1, Boolean course2, Boolean course3) {
        fg1.MapMarking(course0, course1, course2, course3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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