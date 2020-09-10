package com.example.mybtchat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mybtchat.R;
import com.example.mybtchat.utils.Constants;

public class LoginActivity extends AppCompatActivity {
    private EditText nameText;
    private EditText birthText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nameText = findViewById(R.id.nameText);
        birthText = findViewById(R.id.birthText);
        Button startButton = findViewById(R.id.button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                String birth = birthText.getText().toString();
                if (name != null && birth != null){
                    // TODO : 서버에 로그인 정보 Post & {ekey, regId} Response


                }
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra(Constants.USER_NAME,name);
                startActivity(intent);
            }
        });
    }
}