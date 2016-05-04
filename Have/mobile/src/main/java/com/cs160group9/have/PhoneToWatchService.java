package com.cs160group9.have;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PhoneToWatchService extends Service {
    private static String TAG = "PhoneToWatchService";

    private PhoneToWatchService service = this;

    private GoogleApiClient mApiClient;
    private JsonObject request;

    @Override
    public void onCreate() {
        super.onCreate();

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {

                    }
                    @Override
                    public void onConnectionSuspended(int cause) {

                    }
                })
                .build();
        mApiClient.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.request = new JsonParser()
                .parse(this.getSharedPreferences("request", 0).getString("request", ""))
                .getAsJsonObject();

        new Thread(new Runnable() {
            @Override
            public void run() {
                service.sendMessage("request", service.request.toString());
            }
        }).start();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendMessage(final String path, final String text) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mApiClient).await();
                for (Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mApiClient, node.getId(), path, text.getBytes()).await();
                    Log.d(TAG, "run: sent");
                }
            }
        }).start();
    }
}
