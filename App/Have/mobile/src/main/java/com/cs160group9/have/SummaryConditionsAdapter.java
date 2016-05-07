package com.cs160group9.have;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;

class SummaryConditionsAdapter extends RecyclerView.Adapter<SummaryConditionsViewHolder> {
    private RequestProvider activity;

    public SummaryConditionsAdapter(RequestProvider activity) {
        super();

        this.activity = activity;
    }

    @Override
    public SummaryConditionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SummaryConditionsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(SummaryConditionsViewHolder holder, int position) {
        JsonObject condition = activity.getRequest().get("selectedConditions").getAsJsonArray()
                .get(position).getAsJsonObject();
        holder.setCondition(condition);
    }

    @Override
    public int getItemCount() {
        return activity.getRequest().get("selectedConditions").getAsJsonArray().size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.list_item_summary;
    }
}

class SummaryConditionsViewHolder extends RecyclerView.ViewHolder {
    private JsonObject condition;

    private TextView name;
    private TextView symptoms;

    public SummaryConditionsViewHolder(View itemView) {
        super(itemView);

        this.name = ((TextView) itemView.findViewById(R.id.conditionName));
        this.symptoms = ((TextView) itemView.findViewById(R.id.conditionSymptoms));
    }

    public void setCondition(JsonObject condition) {
        this.condition = condition;

        this.name.setText(this.condition.get("name").getAsString());
        this.symptoms.setText(this.condition.get("symptoms").getAsString());
    }
}