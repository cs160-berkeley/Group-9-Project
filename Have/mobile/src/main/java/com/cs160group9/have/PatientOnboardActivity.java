package com.cs160group9.have;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.ArrayList;

public class PatientOnboardActivity extends AppCompatActivity {
    private static final String TAG = "PatientOnboardActivity";

    private PatientOnboardActivity activity = this;

    private JsonArray conditions;
    private JsonObject request;

    private ArrayList<Bitmap> photos;

    private WelcomeCoordinatorLayout welcomeCoordinator;
    private Button step2NextButton;

    // TODO: fix back button handling (`onBackPressed`)
    // TODO: fix disabled button style

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_patient_onboard);

        // load conditions list
        this.conditions = loadJsonFromResource(R.raw.conditions)
                .getAsJsonObject().get("conditions").getAsJsonArray();

        // set up patient request object
        this.request = new JsonObject();
        this.request.add("selectedConditions", new JsonArray());

        // TODO: load photos
        this.photos = new ArrayList<>();
        this.photos.add(((BitmapDrawable) this.getResources().getDrawable(R.drawable.hand)).getBitmap());
        this.photos.add(((BitmapDrawable) this.getResources().getDrawable(R.drawable.hand)).getBitmap());
        this.photos.add(((BitmapDrawable) this.getResources().getDrawable(R.drawable.hand)).getBitmap());
        this.photos.add(((BitmapDrawable) this.getResources().getDrawable(R.drawable.hand)).getBitmap());
        this.photos.add(((BitmapDrawable) this.getResources().getDrawable(R.drawable.hand)).getBitmap());
        this.photos.add(((BitmapDrawable) this.getResources().getDrawable(R.drawable.hand)).getBitmap());
        this.photos.add(((BitmapDrawable) this.getResources().getDrawable(R.drawable.hand)).getBitmap());

        // set up WelcomeCoordinatorLayout (wizard UI)
        this.welcomeCoordinator =
                (WelcomeCoordinatorLayout) this.findViewById(R.id.welcomeCoordinator);
        assert this.welcomeCoordinator != null;
        this.welcomeCoordinator.setScrollingEnabled(false);
        this.welcomeCoordinator.addPage(R.layout.patient_onboard_summary);
        this.welcomeCoordinator.addPage(R.layout.patient_onboard_step_3);
        this.welcomeCoordinator.addPage(R.layout.patient_onboard_step_2);
        this.welcomeCoordinator.addPage(R.layout.patient_onboard_step_1);

        // set up first page (symptoms entry)
        final Button step1NextButton = (Button) this.findViewById(R.id.step_1_nextButton);
        assert step1NextButton != null;
        step1NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.welcomeCoordinator.setCurrentPage(1, true);
                activity.hideSoftKeyBoard();
            }
        });
        final EditText step1Field = (EditText) findViewById(R.id.step_1_field);
        assert step1Field != null;
        step1Field.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    Log.d(TAG, "onEditorAction: " + step1Field.getText());
                    if (step1Field.getText().length() > 0) {
                        activity.welcomeCoordinator.setCurrentPage(1, true);
                        activity.hideSoftKeyBoard();
                    }
                    handled = true;
                }
                return handled;
            }
        });
        step1Field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    step1NextButton.setEnabled(true);
                } else step1NextButton.setEnabled(false);
            }
        });

        // set up second page (condition selection)
        RecyclerView step2ConditionsList = (RecyclerView) this.findViewById(R.id.step_2_conditions_list);
        assert step2ConditionsList != null;
        step2ConditionsList.setAdapter(this.conditionsListAdapter);
        step2ConditionsList.setLayoutManager(new LinearLayoutManager(this));

        this.step2NextButton = (Button) this.findViewById(R.id.step_2_nextButton);
        assert this.step2NextButton != null;
        this.step2NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.welcomeCoordinator.setCurrentPage(2, true);
            }
        });
        Button step2BackButton = (Button) this.findViewById(R.id.step_2_backButton);
        assert step2BackButton != null;
        step2BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.welcomeCoordinator.setCurrentPage(0, true);
            }
        });

        // set up third page (photo)
        RecyclerView step3PhotosList = (RecyclerView) this.findViewById(R.id.step_3_photos_list);
        assert step3PhotosList != null;
        step3PhotosList.setAdapter(this.photosListAdapter);
        step3PhotosList.setLayoutManager(new GridLayoutManager(this, 3));
        step3PhotosList.addItemDecoration(new GridSpacingItemDecoration(3, 20, true));

        Button step3SubmitButton = (Button) this.findViewById(R.id.step_3_SubmitButton);
        assert step3SubmitButton != null;
        step3SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.welcomeCoordinator.setCurrentPage(3, true);
            }
        });
        Button step3BackButton = (Button) this.findViewById(R.id.step_3_backButton);
        assert step3BackButton != null;
        step3BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.welcomeCoordinator.setCurrentPage(1, true);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (this.welcomeCoordinator.getPageSelected() == 0) super.onBackPressed();
    }

    // http://stackoverflow.com/a/18858246
    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if(imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void addCondition(JsonObject condition) {
        JsonArray conditions = this.request.get("selectedConditions").getAsJsonArray();
        conditions.add(condition);
        if (conditions.size() > 0) this.step2NextButton.setEnabled(true);
    }
    private void removeCondition(JsonObject condition) {
        JsonArray conditions = this.request.get("selectedConditions").getAsJsonArray();
        conditions.remove(condition);
        if (conditions.size() == 0) this.step2NextButton.setEnabled(false);
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
            if (isChecked) activity.addCondition(this.condition);
            else activity.removeCondition(this.condition);
        }

        @Override
        public void onClick(View v) {
            this.checkBox.toggle();
        }
    }

    private RecyclerView.Adapter photosListAdapter = new RecyclerView.Adapter<PhotosListViewHolder>() {
        @Override
        public PhotosListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PhotosListViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(viewType, parent, false));
        }

        @Override
        public void onBindViewHolder(PhotosListViewHolder holder, int position) {
            if (position > 0) {
                Bitmap photo = activity.photos.get(position - 1);
                holder.setPhoto(photo);
            } else {
                holder.setPhoto(
                        ((BitmapDrawable) activity.getResources()
                                .getDrawable(R.drawable.newphoto))
                        .getBitmap()
                );
            }
        }

        @Override
        public int getItemCount() {
            return activity.photos.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position > 0) return R.layout.list_item_photo;
            else return R.layout.list_item_new_photo;
        }
    };

    private class PhotosListViewHolder extends RecyclerView.ViewHolder {
        private ImageButton img;

        public PhotosListViewHolder(View itemView) {
            super(itemView);

            this.img = (ImageButton) itemView.findViewById(R.id.photo);
        }

        public void setPhoto(Bitmap photo) {
            this.img.setImageBitmap(photo);
        }
    }

    // http://stackoverflow.com/a/30701422
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
