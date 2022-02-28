package com.wristrot.wristtracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wristrot.wristtracking.R;
import com.wristrot.wristtracking.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MainActivity extends WearableActivity implements SensorEventListener, View.OnClickListener {
    private int countTrack = 0;
    private Button button;
    private ActivityMainBinding binding;
    private static final float SHAKE_THRESHOLD = 1.1f;
    // private static final float SHAKE_THRESHOLD = 1.1f;
    private static final int SHAKE_WAIT_TIME_MS = 250;
    private static final float ROTATION_THRESHOLD = 2.0f;
    private static final int ROTATION_WAIT_TIME_MS = 100;
    private boolean isClick = false;
    private FrameLayout mView;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private int mSensorType;
    private long mShakeTime = 0;
    private long mRotationTime = 0;
    private Button startTracking;
    String message = "I am the messge from smartwatch";
    int receiveMessageNumber  = 0;
    String datapath = "/my_path";
    long t1, t2, t;
    StringBuilder builder = new StringBuilder();
     String TAG = "ashuData";
    float [] history = new float[2];
    String [] direction = {"NONE","NONE"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mSensorType = 1;
        mView = binding.parent;
        startTracking = binding.startTracking;
        startTracking.setOnClickListener(this);
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        new SendMessage(datapath,message+receiveMessageNumber++).start();
        setAmbientEnabled();
        IntentFilter newFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver,newFilter);

    }
    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Received from device", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //  mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startTracking: {
                if (!isClick) {
                    isClick = true;
                    countTrack = 0;
                    mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    Toast.makeText(this, "Tracking Started", Toast.LENGTH_SHORT).show();
                    startTracking.setText("END");
                } else {
                    System.out.println("Movement Count::" + countTrack);
                    isClick = false;
                    startTracking.setText("Start");
                    Toast.makeText(this, "Tracking Stopped", Toast.LENGTH_SHORT).show();
                    new SendMessage(datapath,countTrack+"").start();
                    mSensorManager.unregisterListener(this);
                }
                break;

            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //  mSensorManager.unregisterListener(this);
    }




   @Override
   public void onAccuracyChanged(Sensor sensor, int i) {

   }
   // Called when detect any movement detects
    @Override
    public void onSensorChanged(SensorEvent event) {

        float xChange = history[0] -event.values[0];
        float yChange =  history[1]-event.values[1] ;

        history[0] = event.values[0];
        history[1] = event.values[1];

// finding out all movements

        if (xChange >2){
            direction[0] = "LEFT";
            //System.out.println("Xchange"+" Left "+xChange+"  "+"YCHANGE::"+" "+yChange);
            System.out.println("Rotation Directions::"+"  "+"Left"+"  "+xChange+"  "+yChange);
           // countTrack = countTrack + 1;
         //   Toast.makeText(this, "Moving LEFT"+countTrack, Toast.LENGTH_SHORT).show();
        }
        else if (xChange < -2){
            direction[0] = "RIGHT";
            System.out.println("Rotation Directions::"+"  "+"RIGHT");
            System.out.println("Xchange"+"  "+xChange+"  "+"YCHANGE::"+" "+yChange);
        //    Toast.makeText(this, "Moving Right", Toast.LENGTH_SHORT).show();
        }
//Supination and Pronation method detection
        if (yChange > 4 && xChange<0){
            direction[1] = "DOWN";
            System.out.println("Rotation Directions::"+"  "+"Down");
            System.out.println("Xchange"+" left "+xChange+"  "+"YCHANGE::"+" "+yChange);
            countTrack = countTrack+1;
            Toast.makeText(this, "Count::"+"   "+countTrack, Toast.LENGTH_SHORT).show();
        }
        else if (yChange < -2){
            direction[1] = "UP";
            System.out.println("Rotation Directions::"+"  "+"Up");
            System.out.println("Xchange"+"  "+xChange+"  "+"YCHANGE::"+" "+yChange);
          //  Toast.makeText(this, "Moving Up", Toast.LENGTH_SHORT).show();
        }

        builder.setLength(0);
        builder.append("x: ");
        builder.append(direction[0]);
        builder.append(" y: ");
        builder.append(direction[1]);

        //textView.setText(builder.toString());
    }

    // Sending message to the device

    class SendMessage extends Thread{
        String path;
        String message;

        SendMessage(String p,String m){
            path = p;
            message = m;
        }

        public void run(){
            Task<List<Node>> nodelistTask = Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();
            try{
                List<Node>  nodes = Tasks.await(nodelistTask);
                for(Node node:nodes){
                    System.out.println("node details::"+node.getDisplayName()+""+node.getId());
                    Task<Integer> sendMessageTask = Wearable.getMessageClient(MainActivity.this).sendMessage(node.getId(),path,message.getBytes());
                    try{
                        Integer result = Tasks.await(sendMessageTask);
                        System.out.println("Result::"+result);
                    }
                    catch(Exception ex){
                        System.out.println("Exception ex:::"+ex.getMessage());
                    }
                }
            }
            catch(Exception ex){
                Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}