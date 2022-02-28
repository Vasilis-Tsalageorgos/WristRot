package com.example.wristtracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import database.DatabaseHelper;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView title, capturedTime, rotationCount,tv_title;
    private Button save, discard,back;
    int receiverMessage = 0;
    private Handler myHandler;
    int totalCount = 0;
    DatabaseHelper dHelper;
    private String date, time, activityName, rotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_result);
        title = findViewById(R.id.title);
        title.setText("Results");
        save = findViewById(R.id.bt_save);
        back = findViewById(R.id.back);
        rotationCount = findViewById(R.id.tv_rotationData);
        capturedTime = findViewById(R.id.tv_time_value);
        discard = findViewById(R.id.bt_discard);
        tv_title = findViewById(R.id.tv_title);
        save.setOnClickListener(this);
        discard.setOnClickListener(this);
        rotation = getIntent().getStringExtra("count");
        time = getIntent().getStringExtra("time");
        date = getIntent().getStringExtra("date");
        activityName = getIntent().getStringExtra("activity");
        tv_title.setText(activityName);
        capturedTime.setText(time);
        back.setOnClickListener(this);
        rotationCount.setText(rotation);
        dHelper = new DatabaseHelper(this);
        myHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                Bundle stuff = message.getData();
                messageText(stuff.getString(""));
                return true;
            }
        });

    }

    public void messageText(String newinfo) {
        if (newinfo.compareTo("") != 0) {
            Toast.makeText(this, newinfo, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_save: {
                if (dHelper.insertRecoed(date, time, activityName, rotation)) {
                    Toast.makeText(ResultActivity.this, "Entry Stored", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(ResultActivity.this, "No Data Inserted", Toast.LENGTH_SHORT).show();
                }


                break;
            }
            case R.id.back:{

                finish();
                break;
            }
            case R.id.bt_discard:{
                showAlert();
                break;
            }

        }
    }
    public void showAlert() {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alert_dialog, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
        Button btn_accept = (Button) view.findViewById(R.id.btn_yes);
        Button btn_reject = (Button) view.findViewById(R.id.btn_no);
        TextView title = view.findViewById(R.id.txt_dialog);
        title.setText("Are you sure you want to discard ");
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(ResultActivity.this,MainActivity.class));
              finish();
              dialog.dismiss();
            }
        });
        btn_reject.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();

            }
        });
    }

}