package com.example.samplelocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationService();
            }
        });
        // 0. 위험권한 추가하기.
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        //checkPermissions(permissions);

    }


    public void checkPermissions(String[] permissions){
        ArrayList<String> targetList = new ArrayList<String>();

        for (int i = 0;i<permissions.length;i++){
            String curPermission = permissions[i];
            Log.d("Permissions",curPermission);
            int permissionCheck = ContextCompat.checkSelfPermission(this,curPermission);
            if(permissionCheck == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,curPermission + " 권한 있음.",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,curPermission + " 권한 없음.",Toast.LENGTH_LONG).show();
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,curPermission)){
                    Toast.makeText(this,curPermission + " 권한 설명 필요함",
                            Toast.LENGTH_LONG).show();
                }else{
                    targetList.add(curPermission);
                }
            }
        }
        String[] targets = new String[targetList.size()];
        targetList.toArray(targets);

        ActivityCompat.requestPermissions(this,targets,101);// 위험권한 부여 요청하기
    }


    // 1. 위치 관리자 객체 참조하기
    public void startLocationService(){
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try{
            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null){
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String message = "최근 위치 -> Latitude: "+ latitude + "\nLongitude: "+longitude;
                Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
                textView.setText(message);
            }

            // 3. 위치정보 업데이트 요청하기
            GPSListener gpsListener = new GPSListener(); // 1. 리스너 객체 생성
            long minTime = 1000;
            float minDistance = 0;

            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
            Toast.makeText(getApplicationContext(),"내 위치 확인 요청함",Toast.LENGTH_SHORT).show();

        }catch (SecurityException e){
            e.printStackTrace();
        }
    }



    // 2. 위치 리스너 구현하기
    class GPSListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Double latitude = location.getLongitude();
            Double longitude = location.getLongitude();
            String message = "내 위치 -> Latitude: " + latitude + "\nLongitude: "+longitude;
            textView.setText(message);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        AutoPermissions.Companion.parsePermissions(this,requestCode,permissions,this);
    }

    @Override
    public void onDenied(int i, String[] permissions) {
        Toast.makeText(this,"permissions denied: "+permissions.length,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGranted(int i, String[] permissions) {
        Toast.makeText(this,"permissions granted: "+permissions.length,Toast.LENGTH_LONG).show();
    }

}