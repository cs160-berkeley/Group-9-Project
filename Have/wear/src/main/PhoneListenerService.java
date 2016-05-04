package com.cs160group9.have;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

public class PhoneListenerService extends WearableListenerService {

    private static String TAG = "PhoneListenerService";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equalsIgnoreCase("RESPONSE")) {
            String expertResponseString = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Log.e(TAG, expertResponseString);
            Intent startResponseReceivedIntent = new Intent(this, ResponseReceivedActivity.class);
            startResponseReceivedIntent.putExtra("RESPONSE_STRING", expertResponseString);
            startResponseReceivedIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startResponseReceivedIntent);
        } else {
            Log.e(TAG, "Invalid path");
            super.onMessageReceived(messageEvent);
        }

    }
}
