package com.wristrot.wristtracking;

import android.content.Intent;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MessageService extends WearableListenerService {

    @Override
    public void onMessageReceived( MessageEvent messageEvent) {
        if(messageEvent.getPath().equals("/my_path")){
            String message = new String(messageEvent.getData());
            Intent messageIntent = new Intent();
            messageIntent.putExtra("message",message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
        }
        else {
            super.onMessageReceived(messageEvent);
        }
    }
}