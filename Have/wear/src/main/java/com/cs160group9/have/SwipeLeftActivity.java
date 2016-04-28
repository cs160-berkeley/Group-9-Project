package com.cs160group9.have;


import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cs160group9.com.have.R;

public class SwipeLeftActivity extends WearableActivity {

    private static final int SPEECH_REQUEST_CODE = 0;
    private Button recordButton;
    private Button submitButton;
    private TextView recordResponseText;
    private ImageView backgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_left);

        recordButton = (Button) findViewById(R.id.record_btn);
        recordResponseText = (TextView) findViewById(R.id.response_text);
        backgroundImage = (ImageView) findViewById(R.id.background_img);
        submitButton = (Button) findViewById(R.id.submit_btn);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySpeechRecognizer();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loadResultsOnPhoneIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
                loadResultsOnPhoneIntent.putExtra("RESULT", "Result string");
                startService(loadResultsOnPhoneIntent);
                Log.e("SwipeLeftActivity", "RESULT SENT TO SERVICE");
            }
        });

    }

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
    // This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            // Do something with spokenText
            Log.e("SPOKENTEXT", spokenText);
            recordResponseText.setText(spokenText);
            backgroundImage.setVisibility(View.INVISIBLE);
            recordButton.setText("Re-Record");
            submitButton.setVisibility(View.VISIBLE);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
