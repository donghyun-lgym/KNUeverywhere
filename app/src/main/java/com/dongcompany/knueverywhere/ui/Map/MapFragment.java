package com.dongcompany.knueverywhere.ui.Map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.dongcompany.knueverywhere.MainActivity;
import com.dongcompany.knueverywhere.R;
import com.dongcompany.knueverywhere.SharedPreferenceUtil;
import com.dongcompany.knueverywhere.ui.MapInfo.MapInfoActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private MainActivity activity;

    public MapFragment(Context context) {
        activity = (MainActivity) context;
    }

    private MapView mapView;

    private NaverMap mNaverMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;

    private View root;

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private Button selectCourseButton;
    private Button startButton;
    private SharedPreferenceUtil util;
    //다이얼로그를 통해 받아온 코스 체크들 여부, 이거에 따라 마킹을 하면 됨.
    //0 : 문, 1 : 식당, 2 : 주요 장소, 3 : 단과대학
    Marker[] marker0 = new Marker[11];
    Marker[] marker1 = new Marker[5];
    Marker[] marker2 = new Marker[11];
    Marker[] marker3 = new Marker[12];


    //탐방 시작 시 타이머 가동
    //[코스 선택하기] 불가능하게, [탐방 중지]버튼으로 바꾸기
    private LinearLayout timerLayout;
    private TextView timerTextView;
    private FirebaseFirestore db;
    private CountDownTimer timer = null;
    public void startTimer(int time) { //time : 분단위
        util.setTravelState(true);
        startButton.setText("탐방 중지하기");
        selectCourseButton.setPaintFlags(selectCourseButton.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        timerLayout.setVisibility(View.VISIBLE);

        long conversionTime = (time / 60) * 1000 * 3600 + (time % 60) * 60 * 1000; // 밀리초로 변환

        if(timer == null) {
            makeTimer(conversionTime);
        }
    }
    private void makeTimer(long conversionTime) {
        timer =  new CountDownTimer(conversionTime, 1000) {
            public void onTick(long millisUntilFinished) {
                // 시간단위
                String hour = String.valueOf(millisUntilFinished / (60 * 60 * 1000));
                // 분단위
                long getMin = millisUntilFinished - (millisUntilFinished / (60 * 60 * 1000)) ;
                String min = String.valueOf(getMin / (60 * 1000)); // 몫
                min = String.valueOf(Integer.parseInt(min) % 60);
                // 초단위
                String second = String.valueOf((getMin % (60 * 1000)) / 1000); // 나머지
                // 시간이 한자리면 0을 붙인다
                if (hour.length() == 1) {
                    hour = "0" + hour;
                }
                // 분이 한자리면 0을 붙인다
                if (min.length() == 1) {
                    min = "0" + min;
                }
                // 초가 한자리면 0을 붙인다
                if (second.length() == 1) {
                    second = "0" + second;
                }
                timerTextView.setText(hour + ":" + min + ":" + second);
            }

            // 제한시간 종료시
            public void onFinish() {
                Toast.makeText(activity, "해당 코스가 무효 처리 되었습니다. 다시 도전하세요!", Toast.LENGTH_SHORT).show();
                //탐방 코스의 무효화
                activity.invalidityTravel(activity);
                startButton.setText("탐방 시작하기");
                selectCourseButton.setPaintFlags(0);
                timerLayout.setVisibility(View.INVISIBLE);
            }
        };
        timer.start();
    }
    public void MapMarking(Boolean course0, Boolean course1, Boolean course2, Boolean course3) {
        DeleteMarker();
        if (course0) {
            for (int i = 0; i < 11; i++) {
                marker0[i].setMap(mNaverMap);
            }
        }
        if (course1) {
            for (int i = 0; i < 5; i++) {
                marker1[i].setMap(mNaverMap);
            }
        }
        if (course2) {
            for (int i = 0; i < 11; i++) {
                marker2[i].setMap(mNaverMap);
            }
        }
        if (course3) {
            for (int i = 0; i < 12; i++) {
                marker3[i].setMap(mNaverMap);
            }
        }
    }
    public void cancelTravel() {
        timer.onFinish();
        timer = null;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_map, container, false);
        util = new SharedPreferenceUtil(activity);
        db = FirebaseFirestore.getInstance();
        //타이머 관련 객체들
        timerLayout = root.findViewById(R.id.MapFragment_timerLayout);
        timerLayout.bringToFront();
        timerLayout.setVisibility(View.INVISIBLE);
        timerTextView = root.findViewById(R.id.MapFragment_timerTextView);

        //지도 객체
        mapView = root.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        //위치 추적
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        //코스 선택 버튼
        selectCourseButton = root.findViewById(R.id.MapFragment_SelectButton);
        selectCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (util.getTravelState() == false) {
                    MapFragment_SelectCourseDialog dialog = new MapFragment_SelectCourseDialog(getContext());
                    dialog.show();
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                }
                else Toast.makeText(activity, "탐방 중에는 코스 선택이 불가능합니다.", Toast.LENGTH_SHORT).show();
            }
        });
        //탐방 시작 버튼
        startButton = root.findViewById(R.id.MapFragment_StartButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startButton.getText().equals("탐방 시작하기")) {
                    MapFragment_StartButtonDialog dialog = new MapFragment_StartButtonDialog(getContext());
                    dialog.show();
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
                else {
                    //탐방 중지하기
                    MapFragment_StopButtonDialog dialog = new MapFragment_StopButtonDialog(getContext());
                    dialog.show();
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        //플로팅 버튼 (지도 이미지 보기)
        root.findViewById(R.id.MapFragment_FloatingButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapFragment_FloatingDialog dialog = new MapFragment_FloatingDialog(getContext());
                dialog.show();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        });
//        //플로팅 버튼 (새로고침)
//        root.findViewById(R.id.MapFragment_RefreshButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        //마커 초기화
        MarkerInit();
        return root;
    }

    public void stopTravel() {
        if(timer != null){
            timer.onFinish();
            timer.cancel();
            timer = null;
        }
    }
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        mNaverMap = naverMap;

        SharedPreferenceUtil util = new SharedPreferenceUtil(activity);
        activity.setCourse_MapMarking(util.getCourseCheckBox(0), util.getCourseCheckBox(1), util.getCourseCheckBox(2), util.getCourseCheckBox(3));

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setCompassEnabled(true); // 기본값 : true
        uiSettings.setScaleBarEnabled(true); // 기본값 : true
        uiSettings.setLocationButtonEnabled(true); // 기본값 : false


        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.NoFollow);

        //위치 변경 시 리스너
//        naverMap.addOnLocationChangeListener(new NaverMap.OnLocationChangeListener() {
//            @Override
//            public void onLocationChange(@NonNull Location location) {
//                Toast.makeText((Activity) getContext(),
//                        location.getLatitude() + ", " + location.getLongitude(),
//                        Toast.LENGTH_SHORT).show();
//            }
//        });

        // 권한확인. 결과는 onRequestPermissionsResult 콜백 매서드 호출
        ActivityCompat.requestPermissions((Activity) getContext(), PERMISSIONS, PERMISSION_REQUEST_CODE);

        //위치 베어링
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(35.890010, 128.611315));
        naverMap.moveCamera(cameraUpdate);// 초기 카메라는 경대 본관!
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                mNaverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
    }

    private void MarkerInit() {
        for (int i = 0; i < 11; i++) marker0[i] = new Marker();
        marker0[0].setPosition(new LatLng(35.892351, 128.609357));//북문
        marker0[1].setPosition(new LatLng(35.894980, 128.612260));//농장문
        marker0[2].setPosition(new LatLng(35.892572, 128.614822));//텍문
        marker0[3].setPosition(new LatLng(35.888089, 128.616282));//동문
        marker0[4].setPosition(new LatLng(35.885227, 128.614624));//정문
        marker0[5].setPosition(new LatLng(35.886163, 128.612994));//수의대문
        marker0[6].setPosition(new LatLng(35.885970, 128.609932));//쪽문
        marker0[7].setPosition(new LatLng(35.886463, 128.607211));//조은문
        marker0[8].setPosition(new LatLng(35.886634, 128.605560));//솔로문
        marker0[9].setPosition(new LatLng(35.888444, 128.603911));//서문
        marker0[10].setPosition(new LatLng(35.890359, 128.605476));//수영장문

        final String[] c0arr = {"북문", "농장문", "테크노문", "동문", "정문", "수의대문", "쪽문", "조은문", "솔로문", "서문", "수영장문"};
        for (int i = 0; i < 11; i++) {
            final int finalI = i;
            marker0[i].setOnClickListener(new Overlay.OnClickListener() {
                @Override
                public boolean onClick(@NonNull Overlay overlay) {
                    Intent intent = new Intent(activity, MapInfoActivity.class);
                    intent.putExtra("장소", c0arr[finalI]);
                    intent.putExtra("설명", "문");
                    intent.putExtra("코스명", "course0");
                    intent.putExtra("코스번호", finalI);
                    startActivity(intent);
                    return true;
                }
            });
            marker0[i].setCaptionColor(Color.BLUE);
            marker0[i].setCaptionHaloColor(Color.rgb(165, 255, 130));
            marker0[i].setCaptionText(c0arr[i]);
            marker0[i].setCaptionMinZoom(15);
            marker0[i].setCaptionMaxZoom(NaverMap.MAXIMUM_ZOOM);
            if(util.getCourseInfo(0, "CLEAR")) {
                marker0[i].setWidth(40);
                marker0[i].setHeight(50);
                marker0[i].setIcon(OverlayImage.fromResource(R.drawable.flag_icon0));
            }
            else {
                marker0[i].setWidth(50);
                marker0[i].setHeight(80);
                marker0[i].setIcon(MarkerIcons.RED);
            }
        }

        for (int i = 0; i < 5; i++) marker1[i] = new Marker();
        marker1[0].setPosition(new LatLng(35.888428, 128.609947));//공식
        marker1[1].setPosition(new LatLng(35.890687, 128.607073));//복현회관
        marker1[2].setPosition(new LatLng(35.891451, 128.612727));//경대리아
        marker1[3].setPosition(new LatLng(35.892290, 128.613229));//종합정보센터
        marker1[4].setPosition(new LatLng(35.888990, 128.614498));//복지관

        final String[] c1arr = {"공대식당", "복현회관", "경대리아", "종합정보센터", "복지관"};

        for (int i = 0; i < 5; i++) {
            final int finalI = i;
            marker1[i].setOnClickListener(new Overlay.OnClickListener() {
                @Override
                public boolean onClick(@NonNull Overlay overlay) {
                    Intent intent = new Intent(activity, MapInfoActivity.class);
                    intent.putExtra("장소", c1arr[finalI]);
                    intent.putExtra("설명", "식당");
                    intent.putExtra("코스명", "course1");
                    intent.putExtra("코스번호", finalI);
                    startActivity(intent);
                    return true;
                }
            });
            marker1[i].setCaptionColor(Color.BLUE);
            marker1[i].setCaptionHaloColor(Color.rgb(165, 255, 130));
            marker1[i].setCaptionText(c1arr[i]);
            marker1[i].setCaptionMinZoom(15);
            marker1[i].setCaptionMaxZoom(NaverMap.MAXIMUM_ZOOM);
            if(util.getCourseInfo(1, "CLEAR")) {
                marker1[i].setWidth(40);
                marker1[i].setHeight(50);
                marker1[i].setIcon(OverlayImage.fromResource(R.drawable.flag_icon1));
            }
            else {
                marker1[i].setWidth(50);
                marker1[i].setHeight(80);
                marker1[i].setIcon(MarkerIcons.LIGHTBLUE);
            }
        }

        for (int i = 0; i < 11; i++) marker2[i] = new Marker();
        marker2[0].setPosition(new LatLng(35.887980, 128.606653));//대운동장
        marker2[1].setPosition(new LatLng(35.888351, 128.604190));//백호관
        marker2[2].setPosition(new LatLng(35.888668, 128.612124));//일청담
        marker2[3].setPosition(new LatLng(35.891808, 128.612018));//도서관
        marker2[4].setPosition(new LatLng(35.890426, 128.612018));//본관
        marker2[5].setPosition(new LatLng(35.888707, 128.610525));//백양로
        marker2[6].setPosition(new LatLng(35.888680, 128.613758));//박물관
        marker2[7].setPosition(new LatLng(35.892575, 128.609793));//미술관
        marker2[8].setPosition(new LatLng(35.892862, 128.610721));//대강당
        marker2[9].setPosition(new LatLng(35.891860, 128.611268));//글플
        marker2[10].setPosition(new LatLng(35.886325, 128.614835));//센팍

        final String[] c2arr = {"대운동장", "백호관", "일청담", "도서관", "본관", "백양로", "박물관", "미술관", "대강당", "글로벌플라자", "센트럴파크"};
        for (int i = 0; i < 11; i++) {
            final int finalI = i;
            marker2[i].setOnClickListener(new Overlay.OnClickListener() {
                @Override
                public boolean onClick(@NonNull Overlay overlay) {
                    Intent intent = new Intent(activity, MapInfoActivity.class);
                    intent.putExtra("장소", c2arr[finalI]);
                    intent.putExtra("설명", "주요 장소");
                    intent.putExtra("코스명", "course2");
                    intent.putExtra("코스번호", finalI);
                    startActivity(intent);
                    return true;
                }
            });
            marker2[i].setCaptionColor(Color.BLUE);
            marker2[i].setCaptionHaloColor(Color.rgb(165, 255, 130));
            marker2[i].setCaptionText(c2arr[i]);
            marker2[i].setCaptionMinZoom(15);
            marker2[i].setCaptionMaxZoom(NaverMap.MAXIMUM_ZOOM);
            if(util.getCourseInfo(2, "CLEAR")) {
                marker2[i].setWidth(40);
                marker2[i].setHeight(50);
                marker2[i].setIcon(OverlayImage.fromResource(R.drawable.flag_icon2));
            }
            else {
                marker2[i].setWidth(50);
                marker2[i].setHeight(80);
                marker2[i].setIcon(MarkerIcons.GREEN);
            }
        }

        for (int i = 0; i < 12; i++) marker3[i] = new Marker();
        marker3[0].setPosition(new LatLng(35.887586, 128.608531));//공대1호관
        marker3[1].setPosition(new LatLng(35.887463, 128.612745));//it1호관
        marker3[2].setPosition(new LatLng(35.888429, 128.615433));//사과대
        marker3[3].setPosition(new LatLng(35.889158, 128.615752));//경상대
        marker3[4].setPosition(new LatLng(35.889905, 128.615859));//생과대
        marker3[5].setPosition(new LatLng(35.890284, 128.606660));//자연대
        marker3[6].setPosition(new LatLng(35.891253, 128.609530));//농대1호관
        marker3[7].setPosition(new LatLng(35.891214, 128.610689));//인문대
        marker3[8].setPosition(new LatLng(35.890296, 128.613778));//사대
        marker3[9].setPosition(new LatLng(35.893543, 128.611129));//예대
        marker3[10].setPosition(new LatLng(35.892625, 128.612392));//약대
        marker3[11].setPosition(new LatLng(35.886761, 128.613228));//수의대

        final String[] c3arr = {"공과대학 1호관", "IT대학 1호관", "사회과학대학", "경상대학", "생활과학대학", "자연과학대학", "농업생명과학대학"
                , "인문대학", "사범대학", "예술대학", "약학대학", "수의과대학"};

        for (int i = 0; i < 12; i++) {
            final int finalI = i;
            marker3[i].setOnClickListener(new Overlay.OnClickListener() {
                @Override
                public boolean onClick(@NonNull Overlay overlay) {
                    Intent intent = new Intent(activity, MapInfoActivity.class);
                    intent.putExtra("장소", c3arr[finalI]);
                    intent.putExtra("설명", "단과대학");
                    intent.putExtra("코스명", "course3");
                    intent.putExtra("코스번호", finalI);
                    startActivity(intent);
                    return true;
                }
            });
            marker3[i].setCaptionColor(Color.BLUE);
            marker3[i].setCaptionHaloColor(Color.rgb(165, 255, 130));
            marker3[i].setCaptionText(c3arr[i]);
            marker3[i].setCaptionMinZoom(15);
            marker3[i].setCaptionMaxZoom(NaverMap.MAXIMUM_ZOOM);
            if(util.getCourseInfo(3, "CLEAR")) {
                marker3[i].setWidth(40);
                marker3[i].setHeight(50);
                marker3[i].setIcon(OverlayImage.fromResource(R.drawable.flag_icon3));
            }
            else {
                marker3[i].setWidth(50);
                marker3[i].setHeight(80);
                marker3[i].setIcon(MarkerIcons.YELLOW);
            }
        }
    }

    private void DeleteMarker() {
        for (int i = 0; i < 11; i++) {
            marker0[i].setMap(null);
        }
        for (int i = 0; i < 5; i++) {
            marker1[i].setMap(null);
        }
        for (int i = 0; i < 11; i++) {
            marker2[i].setMap(null);
        }
        for (int i = 0; i < 12; i++) {
            marker3[i].setMap(null);
        }
    }
}