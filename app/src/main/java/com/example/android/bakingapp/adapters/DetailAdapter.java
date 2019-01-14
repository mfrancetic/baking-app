package com.example.android.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Step;

import java.util.List;

import butterknife.BindView;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    List<Step> steps;

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipe_step_description)
        TextView recipeDescriptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.findViewById(R.id.recipe_step_description);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View recipeDescriptionView = inflater.inflate(R.layout.fragment_recipe_detail_item, parent, false);
        return new ViewHolder(recipeDescriptionView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final Step step = steps.get(position);
        TextView recipeDescriptionTextView = viewHolder.recipeDescriptionTextView;

        final Context context = recipeDescriptionTextView.getContext();

        String recipeDescription = step.getStepId() + "" + step.getStepShortDescription();

        recipeDescriptionTextView.setText(recipeDescription);

        recipeDescriptionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        if (steps == null) {
            return 0;
        } else {
            return steps.size();
        }
    }
}
