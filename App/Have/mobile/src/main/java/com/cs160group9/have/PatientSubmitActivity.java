package com.cs160group9.have;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PatientSubmitActivity extends AppCompatActivity {
    private PatientSubmitActivity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_patient_submit);

        Button requestButton = (Button) this.findViewById(R.id.requestButton);
        assert requestButton != null;
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, ViewRequestActivity.class));
            }
        });

        this.startService(new Intent(this, PhoneToWatchService.class));
    }
}
