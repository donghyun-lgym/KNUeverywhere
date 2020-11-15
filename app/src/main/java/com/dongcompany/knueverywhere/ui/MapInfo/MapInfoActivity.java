package com.dongcompany.knueverywhere.ui.MapInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dongcompany.knueverywhere.LoadingDialog;
import com.dongcompany.knueverywhere.R;

import java.util.HashMap;
import java.util.Map;

public class MapInfoActivity extends AppCompatActivity {
    private PicFragment picFragment;
    private ReviewFragment reviewFragment;
    private AroundFragment aroundFragment;

    public void addReview(HashMap info) {
        reviewFragment.addReview(info);
    }
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
        String img_id2 = "@drawable/" + course + "_" + courseNum;
        String packName = this.getPackageName();
        int resID = getResources().getIdentifier(img_id2, "drawable", packName);
        imageView.setBackgroundResource(resID);


        //프래그먼트 초기화
        picFragment = new PicFragment(this, course, courseNum);
        reviewFragment = new ReviewFragment(this, course, courseNum);
        aroundFragment = new AroundFragment(this, course, courseNum);
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
        Typeface typeface_r = null;
        Typeface typeface = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            typeface = getResources().getFont(R.font.myfont);
            typeface_r = getResources().getFont(R.font.myfont_r);
        }

        reviewButton.setTypeface(typeface);
        final Typeface finalTypeface = typeface;
        final Typeface finalTypeface_r = typeface_r;
        final LoadingDialog dialog2 = new LoadingDialog(MapInfoActivity.this);

        picButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog2.dismiss();
                    }
                }, 1500);

                getSupportFragmentManager().beginTransaction().hide(reviewFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(aroundFragment).commit();
                getSupportFragmentManager().beginTransaction().show(picFragment).commit();
                aroundButton.setTextColor(Color.BLACK); reviewButton.setTextColor(Color.BLACK);
                aroundButton.setTypeface(finalTypeface_r); reviewButton.setTypeface(finalTypeface_r);
                picButton.setTextColor(Color.GREEN);
                picButton.setTypeface(finalTypeface);
            }
        });
        aroundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog2.dismiss();
                    }
                }, 1500);

                getSupportFragmentManager().beginTransaction().hide(reviewFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(picFragment).commit();
                getSupportFragmentManager().beginTransaction().show(aroundFragment).commit();
                picButton.setTextColor(Color.BLACK); reviewButton.setTextColor(Color.BLACK);
                picButton.setTypeface(finalTypeface_r); reviewButton.setTypeface(finalTypeface_r);
                aroundButton.setTextColor(Color.GREEN);
                aroundButton.setTypeface(finalTypeface);
            }
        });
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LoadingDialog dialog2 = new LoadingDialog(MapInfoActivity.this);
                dialog2.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog2.dismiss();
                    }
                }, 1000);

                getSupportFragmentManager().beginTransaction().hide(picFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(aroundFragment).commit();
                getSupportFragmentManager().beginTransaction().show(reviewFragment).commit();
                picButton.setTextColor(Color.BLACK); aroundButton.setTextColor(Color.BLACK);
                picButton.setTypeface(finalTypeface_r); aroundButton.setTypeface(finalTypeface_r);
                reviewButton.setTextColor(Color.GREEN);
                reviewButton.setTypeface(finalTypeface);
            }
        });

    }
}