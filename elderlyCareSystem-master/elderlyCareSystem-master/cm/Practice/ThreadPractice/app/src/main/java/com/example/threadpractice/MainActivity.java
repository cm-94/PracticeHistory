package com.example.threadpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int value = 0;
    TextView valueText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        valueText = findViewById(R.id.value);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackgroundThread thread = new BackgroundThread();
                thread.start();
            }
        });
    }

    class BackgroundThread extends Thread{
        public void run(){
            for(int i = 0 ; i<100 ; i++){
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}

                value += 1;
                Log.d("Thread ", "value: " + value);
                // 스레드 안에서 TextView의 setText() 메서드 호출
                valueText.setText("value값 : "+value);
            }
        }
    }
}