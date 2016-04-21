package com.cs160group9.have;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class PhotoTestActivity extends AppCompatActivity implements PhotoPickerFragment.OnFragmentInteractionListener {

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.image = (ImageView)findViewById(R.id.image);
    }

    @Override
    public void onPhotoReturned(File f) {
        image.setImageURI(Uri.fromFile(f));
        System.out.println("photo");
    }
}
