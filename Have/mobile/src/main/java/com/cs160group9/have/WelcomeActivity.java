package com.cs160group9.have;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WelcomeActivity extends AppCompatActivity {
    private static final String TAG = "WelcomeActivity";

    private WelcomeActivity activity = this;

    private JsonObject request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_welcome);

        SharedPreferences prefs = this.getSharedPreferences("request", 0);
//        prefs.edit().remove("request").commit();
        if (prefs.contains("request")) this.goToSubmitted();

        Button getStartedButton = (Button) this.findViewById(R.id.getStartedButton);
        assert getStartedButton != null;
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, RoleChoiceActivity.class));
            }
        });
    }

    private void goToSubmitted() {
        Intent intent = new Intent(this, PatientSubmitActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }
}
