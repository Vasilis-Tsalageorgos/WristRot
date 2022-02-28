package com.wristrot.wristtracking;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wristtracking.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StartTrackingActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView activityName;
    private Button endActivity, back;
    private boolean running;
    private Handler handler;
    private TextView tv_timerText, title;
    private int seconds = 0;
    private Bundle extras;
    String count = "", time;
    int hours;
    int minutes;
    int secs;
    String activity;
    String currentDate;
    private String lastStorevalue;
    private String currentValue;
    private String startTime;
    String offDate, onDate;
    PowerManager.WakeLock wakeLock;
    boolean isTimerrunning = false;
    String offScreenValue;

    @SuppressLint("InvalidWakeLockTag")
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
        currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        extras = getIntent().getExtras();
        if (extras != null) {
            activity = extras.getString("Activity");
            activityName.setText(activity);
        }
        runTimer();
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_endActivity: {
                if (minutes >= 1) {

                    running = false;
                    Intent intent = new Intent(StartTrackingActivity.this, ResultActivity.class);
                    if (count == null || count.isEmpty()) {
                        intent.putExtra("count", "0");
                    } else {
                        intent.putExtra("count", count);
                    }
                    intent.putExtra("time", time);
                    intent.putExtra("activity", activity);
                    intent.putExtra("date", currentDate);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Please wait until 1 minute completes", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.back: {
                showAlert();
                break;
            }
        }
    }

// Timer Method
    private void runTimer() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                hours = seconds / 3600;
                minutes = (seconds % 3600) / 60;
                secs = seconds % 60;
                // Format seconds into hours, minutes,
                // and seconds.
                time
                        = String
                        .format(Locale.getDefault(),
                                "%d:%02d:%02d", hours,
                                minutes, secs);

                // Set the text view text.
                tv_timerText.setText(time);
                lastStorevalue = time;
                isTimerrunning = true;
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
// Broadcast Receiver to get the message from watch
    class Receiver extends BroadcastReceiver {
        int totalCOunt = 0;

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            System.out.print("Before::" + message + "  " + "After::" + "  " + count);
            if (hours == 0) {
                totalCOunt = Integer.parseInt(message) / minutes;
                count = String.valueOf(totalCOunt);
                System.out.print("Before::" + message + "  " + "After::" + "  " + count);
            } else {
                totalCOunt = Integer.parseInt(message) / hours * 60;
                count = String.valueOf(totalCOunt);
                System.out.print("Before::" + message + "  " + "After::" + "  " + count);
            }

        }
    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        showAlert();
    }
// Alert
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
        title.setText("Are you sure you want to close the timer ?");

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
// Getting the current timer when user puts the app in background
    @Override
    protected void onPause() {
        super.onPause();
        try {
            offScreenValue = lastStorevalue;
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            Date currentTime = Calendar.getInstance().getTime();
            offDate = format.format(currentTime);
            System.out.println("Action" + "Onpaused::"+"  "+offDate+"Lastsavedvalue::"+"  "+offScreenValue);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
// Handling the timer when app coming back from background.
    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (isTimerrunning) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                Date currentTime = Calendar.getInstance().getTime();
                onDate = simpleDateFormat.format(currentTime);
                System.out.println("Action" + "onResume"+onDate);
                Date startDate = simpleDateFormat.parse(offDate);
                Date endDate = simpleDateFormat.parse(onDate);
                System.out.println("Action" + "formating"+offDate+"  "+onDate);
                long difference = endDate.getTime() - startDate.getTime();
                if(difference<0)
                {
                    System.out.println("Action" + "difference"+">0");
                    Date dateMax = simpleDateFormat.parse("24:00:00");
                    Date dateMin = simpleDateFormat.parse("00:00:00");
                    difference=(dateMax.getTime() -startDate.getTime() )+(endDate.getTime()-dateMin.getTime());
                }
                int days = (int) (difference / (1000*60*60*24));
                int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
                int sec = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours) - (1000*60*min)) / (1000);
                Log.i("log_tag","Hours: "+hours+", Mins: "+min+", Secs: "+sec);
                String totalTimeString = String.format("%02d:%02d:%02d", hours, min,sec);
                System.out.println("Action time difference::"+totalTimeString);
               // System.out.println("Action offtiming::"+offScreenValue+""+"updated value"+totalTimeString);
                String time = offScreenValue;
                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                Date date = df.parse(time);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.HOUR, hours);
                cal.add(Calendar.MINUTE, min);
                cal.add(Calendar.SECOND, sec);
                int h = cal.get(Calendar.HOUR_OF_DAY);
                int m = cal.get(Calendar.MINUTE);
                int s = cal.get(Calendar.SECOND);
                String finalTime = String
                        .format(Locale.getDefault(),
                                "%d:%02d:%02d", h,
                                m, s);
                hours=0;
                minutes = 0;
                secs = 0;
                tv_timerText.setText("");
                tv_timerText.setText(finalTime);
                System.out.println("Action Final String time::"+finalTime);



            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ExMessage:" + e.getMessage());

        }

    }


}