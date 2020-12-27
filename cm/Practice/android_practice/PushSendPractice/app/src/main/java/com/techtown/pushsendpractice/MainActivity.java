package com.techtown.pushsendpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    private String name = "노인1";
    EditText editText;
    TextView textView;

    static String regId = "e-A5lnhOXLg:APA91bGTHRMaaKXBT3a_tl5wjApb3pc_bkk7C2GlAqYnqaLqXebScVMZHec2F1qDaytod0CmB9OITs3fo9NNxVKnnMCnPSHhSZEnfox8HI7wa6s3rWc1FsTHKKHEC6Uz-eFk7e5Bygd5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });
    }

    // TODO : Volley로 Push 보내기(Notification & Data)
    public void sendData(){
        try{
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://fcm.googleapis.com/fcm/send";
            JSONObject pushData = new JSONObject();

            pushData.put("ekey", 1);
            pushData.put("ename", name);
            pushData.put("title", name);
            pushData.put("body", "위험");

            JSONObject push = new JSONObject();
            push.put("to", regId);
            push.put("data",pushData);

            JsonObjectRequest request = new JsonObjectRequest(url, push, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    String api_key_header_value = "key=AAAAdhBR2Mk:APA91bGHV1aLRh3fYttfaxRBgDuqszk4kly85eYX2H3i526Hd7WpmRh1YzS5QblOUte9NVGWVhNxvLLo4mpFRtieQ5tLeWw9F7R7x9iNN_JGFTmdkGxRLwAaPSTA32fpi5LWjc77iKgK";
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", api_key_header_value);
                    return headers;
                }
            };
            queue.add(request);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}