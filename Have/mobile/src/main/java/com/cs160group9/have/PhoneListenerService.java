package com.cs160group9.have;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class PhoneListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.e("PhoneListenerService", "MessageEvent received");
        if (messageEvent.getPath().equalsIgnoreCase("RESULT")) {
            Log.e("PhoneListenerService", "Result Received");
            Intent intent = new Intent(this, RequestResultsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Log.e("PhoneListenerService", "No msg received");
            super.onMessageReceived(messageEvent);
        }
    }

}
