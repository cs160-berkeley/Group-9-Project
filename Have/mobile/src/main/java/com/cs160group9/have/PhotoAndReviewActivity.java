package com.cs160group9.have;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class PhotoAndReviewActivity extends AppCompatActivity {
    private PhotoAndReviewActivity activity = this;

    private Button submitButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_photo_and_review);

        this.submitButton = (Button) this.findViewById(R.id.submitButton);
        this.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent requestSubmittedIntent = new Intent(activity.getBaseContext(), RequestSubmittedActivity.class);
                activity.startActivity(requestSubmittedIntent);

                Intent loadRequestOnWatchIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                loadRequestOnWatchIntent.putExtra("REQUEST", "Request string"); // Right now service doesn't do anything with this
                startService(loadRequestOnWatchIntent);
                Log.e("PhotoAndReviewActivity", "REQUEST SENT TO SERVICE");
            }
        });

        this.backButton = (Button) this.findViewById(R.id.backButton);
        this.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(activity.getBaseContext(), ConditionActivity.class);
                activity.startActivity(backIntent);
            }
        });
    }
}
