package com.cs160group9.have;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ResponseReceivedActivity extends AppCompatActivity {
    private ResponseReceivedActivity activity = this;

    private JsonObject response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_response_received);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            String expertResponseString = extras.getString("RESPONSE_STRING");

            Log.e("ResponseReceived", expertResponseString);

            this.response = new JsonObject();
            JsonArray conditions = new JsonArray();
            conditions.add(new JsonParser().parse("{'name': 'herpes'}"));
            conditions.add(new JsonParser().parse("{'name': 'syphilis'}"));
            this.response.add("conditions", conditions);
            this.response.addProperty("positive", false);

            boolean positive = this.response.get("positive").getAsBoolean();

            TextView received = (TextView) this.findViewById(R.id.got_consensus);
            assert received != null;
            StringBuilder result = new StringBuilder(positive ?
                    this.getString(R.string.positive) : this.getString(R.string.negative));
            result.append(".\n\nHere's what they said: \"" + expertResponseString + "\"");
            received.setText(result.toString());

            Button requestButton = (Button) this.findViewById(R.id.requestButton);
            assert requestButton != null;
            requestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity, ViewRequestActivity.class));
                }
            });

        }

    }
}
