package com.dongcompany.knueverywhere.ui.Map;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.dongcompany.knueverywhere.R;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.util.FusedLocationSource;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapViewModel homeViewModel;
    private MapView mapView;

    private NaverMap naverMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;

    private View root;

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(MapViewModel.class);
        root = inflater.inflate(R.layout.fragment_map, container, false);

        //지도 객체
        mapView = root.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        //위치 추적
       locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

       //코스 선택 버튼
        root.findViewById(R.id.MapFragment_SelectButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapFragment_SelectCourseDialog dialog = new MapFragment_SelectCourseDialog(getContext());
                dialog.show();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        });
        //탐방 시작 버튼
        root.findViewById(R.id.MapFragment_StartButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return root;
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        naverMap = naverMap;
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
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults))
        {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
    }
}