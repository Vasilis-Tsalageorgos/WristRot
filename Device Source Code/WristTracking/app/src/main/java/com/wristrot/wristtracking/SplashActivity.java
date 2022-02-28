package com.wristrot.wristtracking;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.wristtracking.R;

public class SplashActivity extends AppCompatActivity {
    public static final int TIME_OUT = 5000;
// Initial Screen will be visible for
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
            finish();
            }
        },TIME_OUT);
    }

}