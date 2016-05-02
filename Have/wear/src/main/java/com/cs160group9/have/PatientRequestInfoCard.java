package com.cs160group9.have;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs160group9.common.PatientRequest;

public class PatientRequestInfoCard extends Fragment {

    private PatientRequest patientRequest;


    public PatientRequestInfoCard(PatientRequest p) {
        this.patientRequest = p;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.patient_request_info, container, false);

        TextView patientSymptoms = (TextView) root.findViewById(R.id.patient_symptoms);
        patientSymptoms.setText(patientRequest.symptoms);

        return root;
    }
}
