package com.cs160group9.have;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ConditionActivity extends AppCompatActivity {
    private ConditionActivity activity = this;

    private Button conditionButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_condition);

        this.conditionButton = (Button) this.findViewById(R.id.nextButton);
        this.conditionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoAndReviewIntent = new Intent(activity.getBaseContext(), PhotoAndReviewActivity.class);
                activity.startActivity(photoAndReviewIntent);
            }
        });

        this.backButton = (Button) this.findViewById(R.id.backButton);
        this.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(activity.getBaseContext(), SymptomActivity.class);
                activity.startActivity(backIntent);
            }
        });
    }
}
