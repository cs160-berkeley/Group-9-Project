package com.cs160group9.have;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.redbooth.WelcomeCoordinatorLayout;

public class PatientOnboardActivity extends AppCompatActivity {
    private static final String TAG = "PatientOnboardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_patient_onboard);

        final WelcomeCoordinatorLayout welcomeCoordinator =
                (WelcomeCoordinatorLayout) this.findViewById(R.id.welcomeCoordinator);
        assert welcomeCoordinator != null;
        welcomeCoordinator.setScrollingEnabled(false);
        welcomeCoordinator.addPage(R.layout.patient_onboard_step_2);
        welcomeCoordinator.addPage(R.layout.patient_onboard_step_1);

        Button step1NextButton = (Button) this.findViewById(R.id.step_1_nextButton);
        assert step1NextButton != null;
        step1NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                welcomeCoordinator.setCurrentPage(1, true);
            }
        });

        Button step2NextButton = (Button) this.findViewById(R.id.step_2_nextButton);
        assert step2NextButton != null;
        step2NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                welcomeCoordinator.setCurrentPage(2, true);
            }
        });
        Button step2BackButton = (Button) this.findViewById(R.id.step_2_backButton);
        assert step2BackButton != null;
        step2BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                welcomeCoordinator.setCurrentPage(0, true);
            }
        });

        EditText step1Field = (EditText) findViewById(R.id.step_1_field);
        assert step1Field != null;
        step1Field.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    welcomeCoordinator.setCurrentPage(1, true);
                    handled = true;
                }
                return handled;
            }
        });
    }
}
