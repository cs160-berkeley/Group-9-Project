package com.cs160group9.have;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
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
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.redbooth.WelcomeCoordinatorLayout;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class PatientOnboardActivity extends AppCompatActivity implements RequestProvider {
    private static final String TAG = "PatientOnboardActivity";

    private PatientOnboardActivity activity = this;

    private JsonArray conditions;
    private JsonObject request;

    private ArrayList<Bitmap> photos;

    private PhotosListViewHolder currentPhotoHolder;
    private ArrayList<PhotosListViewHolder> viewHolders;

    private WelcomeCoordinatorLayout welcomeCoordinator;
    private Button step2NextButton;
    private Button step3NextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_patient_onboard);

        if (this.requestExists()) this.goToSubmitted();

        // load conditions list
        this.conditions = loadJsonFromResource(R.raw.conditions)
                .getAsJsonObject().get("conditions").getAsJsonArray();

        // set up patient request object
        this.request = new JsonObject();
        this.request.add("selectedConditions", new JsonArray());

        // TODO: load photos
        this.photos = new ArrayList<>();
        this.photos.add(((BitmapDrawable) this.getResources().getDrawable(R.drawable.gross)).getBitmap());
        this.photos.add(((BitmapDrawable) this.getResources().getDrawable(R.drawable.monica)).getBitmap());
        this.photos.add(((BitmapDrawable) this.getResources().getDrawable(R.drawable.whitewater)).getBitmap());
        this.photos.add(((BitmapDrawable) this.getResources().getDrawable(R.drawable.hrc)).getBitmap());
        this.photos.add(((BitmapDrawable) this.getResources().getDrawable(R.drawable.selfie)).getBitmap());
        this.photos.add(((BitmapDrawable) this.getResources().getDrawable(R.drawable.hand)).getBitmap());
        this.photos.add(((BitmapDrawable) this.getResources().getDrawable(R.drawable.hand)).getBitmap());

        this.viewHolders = new ArrayList<>();

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
        final EditText step1Field = (EditText) findViewById(R.id.step_1_field);
        assert step1Field != null;
        step1NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.welcomeCoordinator.setCurrentPage(1, true);
                activity.setSymptoms(step1Field.getText().toString());
                activity.hideSoftKeyBoard();
                step1Field.clearFocus();
            }
        });
        step1Field.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    Log.d(TAG, "onEditorAction: " + step1Field.getText());
                    if (step1Field.getText().length() > 0) {
                        activity.welcomeCoordinator.setCurrentPage(1, true);
                        activity.setSymptoms(step1Field.getText().toString());
                        activity.hideSoftKeyBoard();
                        step1Field.clearFocus();
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

        this.step3NextButton = (Button) this.findViewById(R.id.step_3_nextButton);
        assert this.step3NextButton != null;
        this.step3NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.makeSummary();
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

    private void makeSummary() {
        Log.d(TAG, "makeSummary: " + this.request.has("symptoms"));
        TextView symptoms = (TextView) this.findViewById(R.id.summary_symptoms);
        assert symptoms != null;
        symptoms.setText(this.request.get("symptoms").getAsString());

        RecyclerView conditions = (RecyclerView) this.findViewById(R.id.summary_conditions);
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
        ImageView photoView = (ImageView) this.findViewById(R.id.summary_photo);
        assert photoView != null;
        photoView.setImageBitmap(photo);

        Button summarySubmitButton = (Button) this.findViewById(R.id.summary_submitButton);
        assert summarySubmitButton != null;
        summarySubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.saveRequest();
                activity.goToSubmitted();
            }
        });

        Button summaryBackButton = (Button) this.findViewById(R.id.summary_backButton);
        assert summaryBackButton != null;
        summaryBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.welcomeCoordinator.setCurrentPage(2, true);
            }
        });
    }

    private void saveRequest() {
        SharedPreferences store = this.getSharedPreferences("request", 0);
        SharedPreferences.Editor editor = store.edit();
        editor.putString("request", this.request.toString());
        editor.apply();
    }

    private boolean requestExists() {
        return this.getSharedPreferences("request", 0).contains("request");
    }

    private void goToSubmitted() {
        Intent intent = new Intent(activity, PatientSubmitActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        int currentPage = this.welcomeCoordinator.getPageSelected();
        if (currentPage == 0) super.onBackPressed();
        else this.welcomeCoordinator.setCurrentPage(currentPage - 1, true);
    }

    // http://stackoverflow.com/a/18858246
    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if(imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void setSymptoms(String text) {
        if (this.request.has("symptoms")) this.request.remove("symptoms");
        this.request.addProperty("symptoms", text);
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

    private void setPhoto(Bitmap photo) {
        if (this.request.has("photo")) this.request.remove("photo");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitMapData = stream.toByteArray();

        this.request.addProperty("photo", Base64.encodeToString(bitMapData, Base64.DEFAULT));
        this.step3NextButton.setEnabled(true);
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

    @Override
    public JsonObject getRequest() {
        return this.request;
    }

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
            this.symptoms.setText(this.condition.get("symptoms").getAsString());
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
                holder.bind(photo, position);
            } else {
                BitmapDrawable bmd = ((BitmapDrawable) activity.getResources()
                        .getDrawable(R.drawable.newphoto));
                assert bmd != null;
                holder.bind(
                        bmd.getBitmap(),
                        position
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

    private class PhotosListViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private ImageButton img;

        private Bitmap photo;
        private int position;
        private boolean selected;

        public PhotosListViewHolder(View itemView) {
            super(itemView);

            this.img = (ImageButton) itemView.findViewById(R.id.photo);
            itemView.findViewById(R.id.photo).setOnClickListener(this);
        }

        public void bind(Bitmap photo, int position) {
            this.photo = photo;
            this.position = position;
            while (activity.viewHolders.size() <= position) activity.viewHolders.add(null);
            activity.viewHolders.set(position, this);
            this.img.setImageBitmap(this.photo);
            if (this.selected) this.select();
        }

        @Override
        public void onClick(View v) {
            if (this.position > 0) {
                this.select();
            } else {
                EasyImage.openCamera(activity, 0);
            }
        }

        private void select() {
            if (this.position > 0) {
                activity.setPhoto(this.photo);
                if (activity.currentPhotoHolder != null) activity.currentPhotoHolder.deselect();
                activity.currentPhotoHolder = this;
                this.img.setBackgroundColor(activity.getResources().getColor(R.color.colorAccent));
                this.selected = true;
            }
        }

        private void deselect() {
            this.img.setBackgroundColor(0);
            this.selected = false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                //Handle the image
                try {
                    activity.photos.add(0,
                            BitmapFactory.decodeStream(new FileInputStream(imageFile)));
                    activity.photosListAdapter.notifyDataSetChanged();
                    activity.viewHolders.get(1).select();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
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
        public void getItemOffsets(Rect outRect,
                                   View view,
                                   RecyclerView parent,
                                   RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private SummaryConditionsAdapter summaryConditionsAdapter = new SummaryConditionsAdapter(this);
}
