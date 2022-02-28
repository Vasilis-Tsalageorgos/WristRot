package com.example.wristtracking;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NewActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText activityName;
    private String currentActivity;
    private Button start,back;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        activityName =  findViewById(R.id.et_name);
        start = findViewById(R.id.bt_start);
        start.setOnClickListener(this);
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        title.setText("Start Activity");


    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.bt_start:{
                currentActivity = activityName.getText().toString();
                if(currentActivity.length()>0) {
                    Intent intent = new Intent(this, StartTrackingActivity.class);
                    intent.putExtra("Activity",currentActivity);
                    activityName.setText("");
                    activityName.setHint("Enter Activity Name");
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this, "Enter Activity Name", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.back:{
                finish();
                break;
            }
        }
    }
}