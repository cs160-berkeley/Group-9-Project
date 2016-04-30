package com.cs160group9.have;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.redbooth.WelcomeCoordinatorLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

public class PatientOnboardActivity extends AppCompatActivity {
    private static final String TAG = "PatientOnboardActivity";

    private PatientOnboardActivity activity = this;

    private JsonArray conditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_patient_onboard);

        // load conditions list
        this.conditions = loadJsonFromResource(R.raw.conditions)
                .getAsJsonObject().get("conditions").getAsJsonArray();

        // set up WelcomeCoordinatorLayout (wizard UI)
        final WelcomeCoordinatorLayout welcomeCoordinator =
                (WelcomeCoordinatorLayout) this.findViewById(R.id.welcomeCoordinator);
        assert welcomeCoordinator != null;
        welcomeCoordinator.setScrollingEnabled(false);
        welcomeCoordinator.addPage(R.layout.patient_onboard_step_2);
        welcomeCoordinator.addPage(R.layout.patient_onboard_step_1);

        // set up first page (symptoms entry)
        Button step1NextButton = (Button) this.findViewById(R.id.step_1_nextButton);
        assert step1NextButton != null;
        step1NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                welcomeCoordinator.setCurrentPage(1, true);
            }
        });

        // set up second page (condition selection)
        RecyclerView step2ConditionsList = (RecyclerView) this.findViewById(R.id.step_2_conditions_list);
        assert step2ConditionsList != null;
        step2ConditionsList.setAdapter(this.conditionsListAdapter);
        step2ConditionsList.setLayoutManager(new LinearLayoutManager(this));

        Button step2NextButton = (Button) this.findViewById(R.id.step_2_nextButton);
        assert step2NextButton != null;
        step2NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                welcomeCoordinator.setCurrentPage(2, true);
            }
        });
        Button step2BackButton = (Button) this.findViewById(R.id.step_2_backButton);
        assert step2BackButton != null;
        step2BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                welcomeCoordinator.setCurrentPage(0, true);
            }
        });

        EditText step1Field = (EditText) findViewById(R.id.step_1_field);
        assert step1Field != null;
        step1Field.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    welcomeCoordinator.setCurrentPage(1, true);
                    handled = true;
                }
                return handled;
            }
        });
    }

    private JsonElement loadJsonFromResource(int id) {
        // http://stackoverflow.com/questions/6349759/using-json-file-in-android-app-resources
        InputStream in = this.getResources().openRawResource(id);
        StringWriter writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new JsonParser().parse(writer.toString());
    }

    private RecyclerView.Adapter conditionsListAdapter = new RecyclerView.Adapter<ConditionsListViewHolder>() {
        @Override
        public ConditionsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ConditionsListViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(viewType, parent, false));
        }

        @Override
        public void onBindViewHolder(ConditionsListViewHolder holder, int position) {
            JsonObject condition = activity.conditions.get(position).getAsJsonObject();
            holder.setCondition(condition);
        }

        @Override
        public int getItemCount() {
            return activity.conditions.size();
        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.list_item_condition;
        }
    };

    class ConditionsListViewHolder extends RecyclerView.ViewHolder
            implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
        private JsonObject condition;

        private CheckBox checkBox;
        private TextView name;
        private TextView symptoms;

        public ConditionsListViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            this.checkBox = ((CheckBox) itemView.findViewById(R.id.conditionCheckBox));
            this.checkBox.setOnCheckedChangeListener(this);

            this.name = ((TextView) itemView.findViewById(R.id.conditionName));
            this.symptoms = ((TextView) itemView.findViewById(R.id.conditionSymptoms));
        }

        public void setCondition(JsonObject condition) {
            this.condition = condition;

            this.name.setText(this.condition.get("name").getAsString());
            // TODO: symptoms
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO: add to selected conditions list
        }

        @Override
        public void onClick(View v) {
            this.checkBox.toggle();
        }
    }
}
