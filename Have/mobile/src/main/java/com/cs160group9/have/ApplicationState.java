package com.cs160group9.have;

/**
 * Created by marioguerrieri on 4/29/16.
 */
public class ApplicationState {
    private static ApplicationState instance = new ApplicationState();

    public static ApplicationState getInstance() {
        return instance;
    }

    private ApplicationState() {

    }
}
