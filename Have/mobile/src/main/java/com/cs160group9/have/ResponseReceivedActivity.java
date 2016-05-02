package com.cs160group9.have;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        this.response = new JsonParser()
                .parse(this.getSharedPreferences("response", 0).getString("response", ""))
                .getAsJsonObject();

        boolean positive = this.response.get("positive").getAsBoolean();
        JsonArray conditions = this.response.get("conditions").getAsJsonArray();

        TextView received = (TextView) this.findViewById(R.id.received);
        assert received != null;
        StringBuilder result = new StringBuilder(positive ?
                this.getString(R.string.positive) : this.getString(R.string.negative));
        if (positive) result.append(conditions.get(0).getAsJsonObject().get("name").getAsString());
        else for (JsonElement e : conditions) {
            result.append(" ").append(e.getAsJsonObject().get("name").getAsString());
        }
        result.append(".");
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
