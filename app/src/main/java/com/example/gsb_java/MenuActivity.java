package com.example.gsb_java;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.gsm.GsmCellLocation;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_visitor);
        Button button1 = findViewById(R.id.button);
        button1.setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, CreateCRActivity.class);
            startActivity(intent);
        });
        Button button2 = findViewById(R.id.button5);
        button2.setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, SearchCRActivity.class);
            startActivity(intent);
        });
    }
}
