package com.cs160group9.have;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.wearable.view.CardFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs160group9.common.ExpertResponse;
import com.cs160group9.common.PatientRequest;

import java.util.List;

public class SwipeRightCard extends Fragment {

    private static final int SPEECH_REQUEST_CODE = 0;

    private PatientRequest patientRequest;
    private String expertResponseString;

    private Button recordButton;
    private Button submitButton;
    private TextView recordResponseText;
    private ImageView backgroundImage;

    public SwipeRightCard(PatientRequest p) {
        this.patientRequest = p;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_swipe_right, container, false);

        recordButton = (Button) root.findViewById(R.id.record_btn);
        recordResponseText = (TextView) root.findViewById(R.id.response_text);
        backgroundImage = (ImageView) root.findViewById(R.id.background_img);
        submitButton = (Button) root.findViewById(R.id.submit_btn);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySpeechRecognizer();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loadResultsOnPhoneIntent = new Intent(getActivity(), WatchToPhoneService.class);
                loadResultsOnPhoneIntent.putExtra("RESPONSE", expertResponseString);
                getActivity().startService(loadResultsOnPhoneIntent);
                Log.e("SwipeRight", "RESPONSE SENT TO WatchToPhoneService");
            }
        });

        return root;
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
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            // Do something with spokenText
            Log.e("SPOKENTEXT", spokenText);
            expertResponseString = spokenText;
            recordResponseText.setText(spokenText);
            backgroundImage.setVisibility(View.INVISIBLE);
            recordButton.setText("Re-Record");
            submitButton.setVisibility(View.VISIBLE);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
