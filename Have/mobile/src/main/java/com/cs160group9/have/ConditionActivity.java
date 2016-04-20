package com.cs160group9.have;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ConditionActivity extends AppCompatActivity {

    private Button conditionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition);

        conditionButton = (Button) findViewById(R.id.condition_btn);

        conditionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoAndReviewIntent = new Intent(getBaseContext(), PhotoAndReviewActivity.class);
                startActivity(photoAndReviewIntent);
            }
        });
    }
}
