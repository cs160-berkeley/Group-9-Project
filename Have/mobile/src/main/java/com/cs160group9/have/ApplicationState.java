package com.cs160group9.have;

import com.cs160group9.common.PatientRequest;

import java.util.ArrayList;

/**
 * Created by marioguerrieri on 4/29/16.
 */
public class ApplicationState {
    private static ApplicationState instance = new ApplicationState();

    public static ApplicationState getInstance() {
        return instance;
    }

    private boolean patientMode;

    // fields for patient mode
    private PatientRequest toSendRequest;

    // fields for expert mode
    private ArrayList<PatientRequest> recievedRequests;

    private ApplicationState() {
        this.patientMode = true;
        this.toSendRequest = new PatientRequest();
    }
}
