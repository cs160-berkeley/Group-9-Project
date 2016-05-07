package com.cs160group9.have;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Base64;

import com.cs160group9.common.PatientRequest;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends FragmentActivity {
    private ArrayList<PatientRequest> patientRequests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PatientRequest dummyRequest = new PatientRequest();
        dummyRequest.symptoms = "little red bumps on skin";
        dummyRequest.conditionsList = new String[]{"herpes", "syphilis"};
        Resources res = getResources();
        BitmapDrawable drawable = (BitmapDrawable) res.getDrawable(R.drawable.dummy_pic);
        assert drawable != null;
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitMapData = stream.toByteArray();
        dummyRequest.requestImage = Base64.encodeToString(bitMapData, Base64.DEFAULT);

        patientRequests.add(dummyRequest);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MainViewWatchAdapter(this, getSupportFragmentManager(), dummyRequest));
        pager.setCurrentItem(1);
    }

}
