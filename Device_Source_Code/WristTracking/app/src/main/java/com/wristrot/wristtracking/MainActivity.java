package com.wristrot.wristtracking;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wristtracking.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textTimer,title;
    private Handler handler;
    private boolean running;
    private Button buttonActivity,buttonHistory,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        buttonActivity = findViewById(R.id.bt_activity);
        buttonHistory = findViewById(R.id.bt_history);
        buttonActivity.setOnClickListener(this);
        buttonHistory.setOnClickListener(this);
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        back.setVisibility(View.GONE);
        title.setText("Home");
        handler = new Handler();
       // runTimer();
    }




    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.bt_activity:{
          startActivity(new Intent(this,NewActivity.class));
               // running = true;
                break;
            }
            case R.id.bt_history:{
                startActivity(new Intent(this,HistoryDetails.class));
                //running = false;
                break;
            }
            case R.id.back:{
                finish();
                break;
            }
        }
    }



}