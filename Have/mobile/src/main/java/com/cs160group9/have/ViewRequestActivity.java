package com.cs160group9.have;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ViewRequestActivity extends AppCompatActivity implements RequestProvider {
    private JsonObject request;

    private SummaryConditionsAdapter summaryConditionsAdapter = new SummaryConditionsAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);

        this.request = new JsonParser()
                .parse(this.getSharedPreferences("request", 0).getString("request", ""))
                .getAsJsonObject();

        TextView symptoms = (TextView) this.findViewById(R.id.symptoms);
        assert symptoms != null;
        symptoms.setText(this.request.get("symptoms").getAsString());

        RecyclerView conditions = (RecyclerView) this.findViewById(R.id.conditions);
        assert conditions != null;
        conditions.setAdapter(this.summaryConditionsAdapter);
        conditions.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        byte[] photoArray = Base64.decode(this.request.get("photo").getAsString(), 0);
        Bitmap photo = BitmapFactory.decodeByteArray(photoArray, 0, photoArray.length);
        ImageView photoView = (ImageView) this.findViewById(R.id.photo);
        assert photoView != null;
        photoView.setImageBitmap(photo);
    }

    @Override
    public JsonObject getRequest() {
        return this.request;
    }
}
