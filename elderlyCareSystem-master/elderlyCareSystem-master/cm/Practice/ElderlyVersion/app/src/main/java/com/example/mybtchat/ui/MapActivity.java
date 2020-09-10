package com.example.mybtchat.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.mybtchat.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

public class MapActivity extends AppCompatActivity implements AutoPermissionsListener {

    private SupportMapFragment mapFragment;
    private GoogleMap map;

    private static double lati;
    private static double longi;

    private static final int INTERVAL_TIME = 5000;
    private static long mLastTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        AutoPermissions.Companion.loadAllPermissions(this, 101);

        // TODO : 1. 내 위치에 아이콘(layer) 띄우기
        // TODO : 1. 내 위치 실시간 갱신(Service로 ㄱㄱ)
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapStart();
    }

    public void mapStart(){
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d("MapReady", "지도 준비됨.");
                map = googleMap;
                map.setMyLocationEnabled(true);

                startLocationService();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("RESUME","onResume() 호출됨");

        if (map != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            //map.setMyLocationEnabled(true);
            mapStart();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (map != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            map.setMyLocationEnabled(false);
        }
    }

    public void startLocationService() {
        lati = MainActivity.getElderlyData().getElatitude();
        longi = MainActivity.getElderlyData().getElongitude();

        Log.d("StartLocationservice","lati: "+lati+", longi: "+longi);
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Log.d("Map_Start", "내 위치 -> Latitude : " + lati + ", Longitude:" + longi);

        showCurrentLocation(lati,longi);
    }

    private void showCurrentLocation(Double latitude, Double longitude) {
        LatLng curPoint = new LatLng(latitude, longitude);
        Log.d("Map_Show", "내 위치 -> Latitude : " + latitude + ", Longitude:" + longitude);
//        String point = "Lati:"+ latitude +", Longi"+longitude;
//        Log.d("Map",point);
        if (latitude != 0 && longitude != 0) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint,15));

            // TODO : 구글 맵에 표시 -> 마커에 대한 옵션 설정  (alpha : 좌표의 투명도)
            MarkerOptions makerOptions = new MarkerOptions();
            makerOptions
                    .position(curPoint)
                    .title("현재 위치")
                    .snippet("위도:"+latitude+"경도:"+longitude)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .alpha(0.5f);
            // 마커를 생성한다. showInfoWindow를 쓰면 처음부터 마커에 상세정보가 뜨게한다. (안쓰면 마커눌러야뜸)
            map.addMarker(makerOptions).showInfoWindow();
        }
    }


    class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) {
            String message = "내 위치 -> Latitude : "+ lati + "\nLongitude:"+ longi;
            Log.d("Map", message);
        }

        public void onProviderDisabled(String provider) { }

        public void onProviderEnabled(String provider) { }

        public void onStatusChanged(String provider, int status, Bundle extras) { }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }

    @Override
    public void onDenied(int requestCode, String[] permissions) {
//        if (permissions.length != 0){
//            Toast.makeText(this, "permissions denied : " + permissions.length, Toast.LENGTH_LONG).show();
//        }
    }

    @Override
    public void onGranted(int requestCode, String[] permissions) {
//        if (permissions.length == 9){
//            Toast.makeText(this, "permissions granted : " + permissions.length, Toast.LENGTH_LONG).show();
//        }
//        else if (permissions.length == 8){
//            Toast.makeText(this, "permissions granted : " + permissions.length, Toast.LENGTH_LONG).show();
//        }
    }
}