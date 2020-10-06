package com.dongcompany.knueverywhere.ui.MapInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dongcompany.knueverywhere.R;

public class MapInfoActivity extends AppCompatActivity {
    private PicFragment picFragment;
    private ReviewFragment reviewFragment;
    private AroundFragment aroundFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_info);


        //이름 세팅
        Intent intent = getIntent();
        TextView NameTextView = findViewById(R.id.MapInfoActivity_NameTextView);
        TextView subTextView = findViewById(R.id.MapInfoActivity_SubNameTextView);
        NameTextView.setText(intent.getStringExtra("장소"));
        subTextView.setText(intent.getStringExtra("설명"));

        //이미지뷰 세팅
        ImageView imageView = findViewById(R.id.MapInfoActivity_ImageView);
        String course = intent.getStringExtra("코스명");
        int courseNum = intent.getIntExtra("코스번호", -1);
        String img_id2 = "R.drawable." + course + "_" + courseNum;
        //imageView.setBackgroundResource(Integer.parseInt(img_id2));

        //출발 and 도착 버튼 (경로찾기)
        findViewById(R.id.MapInfoActivity_StartButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.MapInfoActivity_ArrivalButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //프래그먼트 초기화
        picFragment = new PicFragment(this, course, courseNum);
        reviewFragment = new ReviewFragment(this, course, courseNum);
        aroundFragment = new AroundFragment(this);
        getSupportFragmentManager().beginTransaction().add(R.id.MapInfoActivity_FrameLayout, picFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.MapInfoActivity_FrameLayout, reviewFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.MapInfoActivity_FrameLayout, aroundFragment).commit();
        getSupportFragmentManager().beginTransaction().show(reviewFragment).commit();
        getSupportFragmentManager().beginTransaction().hide(picFragment).commit();
        getSupportFragmentManager().beginTransaction().hide(aroundFragment).commit();

        //메뉴 버튼들 선택 시 프래그먼트 전환
        final TextView picButton = findViewById(R.id.MapInfoActivity_picButton);
        final TextView aroundButton = findViewById(R.id.MapInfoActivity_aroundButton);
        final TextView reviewButton = findViewById(R.id.MapInfoActivity_reviewButton);
        reviewButton.setTextColor(Color.GREEN);
        reviewButton.setTypeface(null, Typeface.BOLD);
        picButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().hide(reviewFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(aroundFragment).commit();
                getSupportFragmentManager().beginTransaction().show(picFragment).commit();
                aroundButton.setTextColor(Color.BLACK); reviewButton.setTextColor(Color.BLACK);
                aroundButton.setTypeface(null, Typeface.NORMAL); reviewButton.setTypeface(null, Typeface.NORMAL);
                picButton.setTextColor(Color.GREEN);
                picButton.setTypeface(null, Typeface.BOLD);
            }
        });
        aroundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().hide(reviewFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(picFragment).commit();
                getSupportFragmentManager().beginTransaction().show(aroundFragment).commit();
                picButton.setTextColor(Color.BLACK); reviewButton.setTextColor(Color.BLACK);
                picButton.setTypeface(null, Typeface.NORMAL); reviewButton.setTypeface(null, Typeface.NORMAL);
                aroundButton.setTextColor(Color.GREEN);
                aroundButton.setTypeface(null, Typeface.BOLD);
            }
        });
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().hide(picFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(aroundFragment).commit();
                getSupportFragmentManager().beginTransaction().show(reviewFragment).commit();
                picButton.setTextColor(Color.BLACK); aroundButton.setTextColor(Color.BLACK);
                picButton.setTypeface(null, Typeface.NORMAL); aroundButton.setTypeface(null, Typeface.NORMAL);
                reviewButton.setTextColor(Color.GREEN);
                reviewButton.setTypeface(null, Typeface.BOLD);
            }
        });

    }
}