package com.cs160group9.have;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Base64;

import com.cs160group9.common.PatientRequest;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

//    private BoxInsetLayout mContainerView;
//    private android.support.wearable.view.CircularButton swipeLeftButton;
//    private android.support.wearable.view.CircularButton swipeUpButton;
//    private android.support.wearable.view.CircularButton swipeRightButton;

    private ArrayList<PatientRequest> patientRequests = new ArrayList<PatientRequest>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            // Process PatientRequest sent from phone
            Gson gson = new Gson();

        }

        PatientRequest dummyRequest = new PatientRequest();
        dummyRequest.symptoms = "Blisters, sores";
        dummyRequest.conditionsList = new String[]{"Herpes", "Syphilis"};
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.dummy_pic);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitMapData = stream.toByteArray();
        dummyRequest.requestImage = Base64.encodeToString(bitMapData, Base64.DEFAULT);

        patientRequests.add(dummyRequest);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MainViewWatchAdapter(this, getSupportFragmentManager(), dummyRequest));
    }

}
