package com.cs160group9.have;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RoleChoiceActivity extends AppCompatActivity {
    private RoleChoiceActivity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_role_choice);

        Button patientButton = (Button) this.findViewById(R.id.patientButton);
        assert patientButton != null;
        patientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, PatientOnboardActivity.class));
            }
        });

        Button expertButton = (Button) this.findViewById(R.id.expertButton);
        assert expertButton != null;
        expertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, ExpertOnboardActivity.class));
            }
        });
    }
}
