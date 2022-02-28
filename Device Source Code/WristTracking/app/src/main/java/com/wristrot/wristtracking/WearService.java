package com.wristrot.wristtracking;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class WearService extends WearableListenerService  {
// Service that gets the method from the watch and passing from broadcast receiver
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
         System.out.println("Got the message:"+messageEvent.getData());
        if(messageEvent.getPath().equals("/my_path")) {
            String message = new String(messageEvent.getData());
            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
        }
        else{
            super.onMessageReceived(messageEvent);
        }
    }

}
