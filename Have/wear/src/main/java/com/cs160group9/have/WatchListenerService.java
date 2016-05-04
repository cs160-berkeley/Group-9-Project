package com.cs160group9.have;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class WatchListenerService extends WearableListenerService {
    private static String TAG = "WatchListenerService";

    private JsonElement request;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG, "onMessageReceived: " + messageEvent.getPath());
        if (messageEvent.getPath().equalsIgnoreCase("/request")) {
            Log.d(TAG, "onMessageReceived: " + messageEvent.getData().toString());
            this.request =
                    new JsonParser().parse(messageEvent.getData().toString()).getAsJsonObject();
        } else {
            super.onMessageReceived(messageEvent);
        }
    }

    private void saveRequest() {
        SharedPreferences store = this.getSharedPreferences("request", 0);
        SharedPreferences.Editor editor = store.edit();
        editor.putString("request", this.request.toString());
        editor.apply();
    }
}
