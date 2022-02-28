package com.example.wristtracking;

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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StartTrackingActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView activityName;
    private Button endActivity,back;
    private boolean running;
    private Handler handler;
    private TextView tv_timerText, title;
    private int seconds = 0;
    private Bundle extras;
    String count,time ;
    int hours ;
    int minutes; 
    int secs;
    String activity;
    String currentDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_tracking);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        activityName = findViewById(R.id.tv_activityName);
        endActivity = findViewById(R.id.bt_endActivity);
        endActivity.setOnClickListener(this);
        tv_timerText = findViewById(R.id.textTimer);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        handler = new Handler();
        running = true;
        title = findViewById(R.id.title);
        title.setText("Tracking");
        currentDate =  new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        extras = getIntent().getExtras();
        if (extras != null) {
             activity = extras.getString("Activity");
            activityName.setText(activity);
        }
        runTimer();
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver,messageFilter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_endActivity: {
                if(minutes>=1) {
                    running = false;
                    Intent intent = new Intent(StartTrackingActivity.this, ResultActivity.class);
                    intent.putExtra("count", count);
                    intent.putExtra("time", time);
                    intent.putExtra("activity",activity);
                    intent.putExtra("date",currentDate);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this, "Please wait until 1 minute completes", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.back:{
                finish();
                break;
            }
        }
    }

    private void runTimer() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                 hours = seconds / 3600;
                 minutes = (seconds % 3600) / 60;
                 secs = seconds % 60;
                // Format the seconds into hours, minutes,
                // and seconds.
                 time
                        = String
                        .format(Locale.getDefault(),
                                "%d:%02d:%02d", hours,
                                minutes, secs);

                // Set the text view text.
                tv_timerText.setText(time);
                // If running is true, increment the
                // seconds variable.
                if (running) {
                    seconds++;
                }
                // Post the code again
                // with a delay of 1 second.
                handler.postDelayed(this, 1000);
            }
        });
    }
    class Receiver extends BroadcastReceiver {
      int totalCOunt = 0;
        @Override
        public void onReceive(Context context, Intent intent) {
            String message  = intent.getStringExtra("message");
            System.out.print("Before::"+message+"  "+"After::"+"  "+count);
            totalCOunt = Integer.parseInt(message)/minutes;
            count = String.valueOf(totalCOunt);
            System.out.print("Before::"+message+"  "+"After::"+"  "+count);
        }
    }

    @Override
    public void onBackPressed() {
      //  super.onBackPressed();
        showAlert();
    }

    public void showAlert(){

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
        title.setText("Time Should be stopped.");

        btn_accept.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
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