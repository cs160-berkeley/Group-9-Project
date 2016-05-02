package com.cs160group9.have;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cs160group9.common.PatientRequest;

import java.util.List;

public class MainViewWatchAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    private List mRows;
    private PatientRequest patientRequest;

    public MainViewWatchAdapter(Context ctx, FragmentManager fm, PatientRequest pr) {
        super(fm);
        mContext = ctx;
        patientRequest = pr;
    }

    @Override
    public Fragment getItem(int pos) {
        switch(pos) {
            case 0: return new SwipeRightCard(patientRequest);
            case 1: return new PatientRequestInfoCard(patientRequest);
            case 2: return new SwipeLeftCard(patientRequest);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}