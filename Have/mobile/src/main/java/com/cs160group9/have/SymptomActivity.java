package com.cs160group9.have;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SymptomActivity extends AppCompatActivity {

    private Button worriedAboutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom);

        worriedAboutButton = (Button) findViewById(R.id.worried_about_btn);

        worriedAboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent conditionIntent = new Intent(getBaseContext(), ConditionActivity.class);
                startActivity(conditionIntent);
            }
        });
    }
}
