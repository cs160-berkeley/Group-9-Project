package com.cs160group9.have;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SymptomActivity extends AppCompatActivity {
    private String TAG = "SymptomActivity";

    private SymptomActivity activity = this;

    private Button worriedAboutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_symptom);

        this.worriedAboutButton = (Button) this.findViewById(R.id.nextButton);
        this.worriedAboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent conditionIntent = new Intent(activity.getBaseContext(), ConditionActivity.class);
                activity.startActivity(conditionIntent);
            }
        });
    }
}
