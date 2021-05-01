package com.example.gsb_java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = findViewById(R.id.b_begin);
        button1.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SigninActivity.class);
            startActivity(intent);
        });
    }
}