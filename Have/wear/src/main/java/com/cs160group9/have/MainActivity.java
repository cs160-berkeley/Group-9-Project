package com.cs160group9.have;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;

public class MainActivity extends WearableActivity {

    private BoxInsetLayout mContainerView;
    private android.support.wearable.view.CircularButton swipeLeftButton;
    private android.support.wearable.view.CircularButton swipeUpButton;
    private android.support.wearable.view.CircularButton swipeRightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        swipeLeftButton = (android.support.wearable.view.CircularButton) findViewById(R.id.left_btn);
        swipeUpButton = (android.support.wearable.view.CircularButton) findViewById(R.id.up_btn);
        swipeRightButton = (android.support.wearable.view.CircularButton) findViewById(R.id.right_btn);

        swipeLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent swipeLeftIntent = new Intent(getBaseContext(), SwipeLeftActivity.class);
                startActivity(swipeLeftIntent);
            }
        });

        swipeUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent swipeUpIntent = new Intent(getBaseContext(), SwipeUpActivity.class);
                startActivity(swipeUpIntent);
            }
        });

        swipeRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent swipeRightIntent = new Intent(getBaseContext(), SwipeRightActivity.class);
                startActivity(swipeRightIntent);
            }
        });
    }

}
