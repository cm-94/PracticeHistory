package com.example.elderlyversion.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.elderlyversion.R;
import com.example.elderlyversion.data.KeyData;
import com.example.elderlyversion.data.LoginData;
import com.example.elderlyversion.utils.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText nameText;
    private EditText birthText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

        nameText = findViewById(R.id.nameText);
        birthText = findViewById(R.id.birthText);

        nameText.setText("김노인");
        birthText.setText("19430524");
        progressBar = findViewById(R.id.progressBar);

        requestCallPermission();

        Button startButton = findViewById(R.id.button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                login();
            }
        });
    }

    private void login(){
        final String name = nameText.getText().toString();
        String birth = birthText.getText().toString();

        if (name.length()>0 && birth.length()>0){
            // TODO : 서버에 로그인 정보 Post & {ekey, regId} Response
            final LoginData loginData = new LoginData(name,birth);
            ApiUtils.getElderlyService(getApplicationContext()).login(loginData).enqueue(new Callback<KeyData>() { // .login(loginData) 로 변경하기
                @Override
                public void onResponse(Call<KeyData> call, Response<KeyData> response) {
                    if(response.isSuccessful() && response.body() != null) {
                        // TODO : response.body()의 데이터(3개) 정상 확인 후 MainActivity로..
                        progressBar.setVisibility(View.GONE); // ProgressBar Close
                        Log.d("LoginActivity","Connect Success");
                        Log.d("LoginActivity","Data: "+response.body().getEkey()+
                                ", regId: "+response.body().getRegId()+", HomeIoT: "+response.body().getHomeIoT());

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("ename", name);
                        intent.putExtra("ekey", response.body().getEkey());
                        intent.putExtra("regId", response.body().getRegId());
//                        intent.putExtra("regid", "eTRx-Z31TdCjy00iLSygQB:APA91bHKGYvaPTKc26kIJjhC2Bu_GQf-XPlwnZNMubK4gqptdhxtIEmqdh-r9-RyFClj0BLAoXRQn_xOBN-obMhMsUU__q_JqmKeSN1DCcQlb5zSzgepPzJM6gD_Qwu43S4bpZhhA1Gx");
                        intent.putExtra("homeIoT", response.body().getHomeIoT());
                        Log.d("LoginActivity","Get Data -> regId:" + response.body().getRegId() + ", Ekey:" +response.body().getEkey() + ", HomeIoT:" +response.body().getHomeIoT());
//                        Toast.makeText(LoginActivity.this, "연결성공! Data:. "+response.body().getRegId(), Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }
                }
                @Override
                public void onFailure(Call<KeyData> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "지금은 연결할 수 없습니다. " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }


            });// End of Login
        }else{
            Toast.makeText(LoginActivity.this, "이름 생년월일을 확인하세요. ", Toast.LENGTH_SHORT).show();
            return ;
        }
    }

    private void login2(){
        final String name = nameText.getText().toString();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("homeIot", "http://192.168.1.22:3000/");
        intent.putExtra("ekey",1);
        intent.putExtra("regid", "eTRx-Z31TdCjy00iLSygQB:APA91bHKGYvaPTKc26kIJjhC2Bu_GQf-XPlwnZNMubK4gqptdhxtIEmqdh-r9-RyFClj0BLAoXRQn_xOBN-obMhMsUU__q_JqmKeSN1DCcQlb5zSzgepPzJM6gD_Qwu43S4bpZhhA1Gx");

        startActivity(intent);
    }

    private void requestCallPermission() {
        // CALL_PHONE 위험 권한 요청하기
        String[] targets = {Manifest.permission.CALL_PHONE};
        for (int i =0;i<targets.length;i++){
            if(ContextCompat.checkSelfPermission(this,targets[i])!= PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"전화 권한 없음.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(LoginActivity.this,targets,101);
            }
        }
    }
}