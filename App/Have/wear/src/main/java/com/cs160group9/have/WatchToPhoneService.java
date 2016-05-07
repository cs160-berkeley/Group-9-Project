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

public class WatchToPhoneService extends Service {

    private static String TAG = "WatchToPhoneService";

    private GoogleApiClient mWatchApiClient;

    @Override
    public void onCreate() {
        super.onCreate();

        mWatchApiClient = new GoogleApiClient.Builder(this)
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWatchApiClient.disconnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        final Bundle extras = intent.getExtras();

        new Thread(new Runnable() {
            @Override
            public void run() {
                mWatchApiClient.connect();

                if (extras != null) {
                    String expertResponse = extras.getString("RESPONSE");
                    sendMessage("RESPONSE", expertResponse);
                    Log.e(TAG, expertResponse);

                }

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
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mWatchApiClient).await();

                for (Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mWatchApiClient, node.getId(), path, text.getBytes()).await();
                    Log.e(TAG, result.getStatus().toString());
                }
            }
        }).start();
    }
}
