package com.example.android.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activities.DetailStepActivity;
import com.example.android.bakingapp.fragments.DetailRecipeFragment;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    private List<Step> steps;

    private Context context;

    private static final String stepIdKey = "stepId";

    private static final String stepListKey = "stepList";

    private String recipeName;

    private static final String recipeNameKey = "recipeName";

    private static final String recipeKey = "recipe";


    private DetailRecipeFragment detailRecipeFragment;

    private int stepId;

    private DetailRecipeFragment.OnRecipeStepClickListener onRecipeStepClickListener;

    class ViewHolder extends RecyclerView.ViewHolder {

        //        @BindView(R.id.recipe_step_description)
        final TextView recipeDescriptionTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeDescriptionTextView = itemView.findViewById(R.id.recipe_step_description);
        }
    }

    public DetailAdapter(Context context, List<Step> steps, DetailRecipeFragment.OnRecipeStepClickListener onRecipeStepClickListener) {
        this.context = context;
        this.steps = steps;
        this.onRecipeStepClickListener = onRecipeStepClickListener;
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
    public void onBindViewHolder(@NonNull final DetailAdapter.ViewHolder viewHolder, final int position) {
        final Step step = steps.get(position);
        TextView recipeDescriptionTextView = viewHolder.recipeDescriptionTextView;

        String recipeDescription = step.getStepId() + " " + step.getStepShortDescription();

        recipeDescriptionTextView.setText(recipeDescription);
        recipeDescriptionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailStepActivity.class);
                stepId = viewHolder.getAdapterPosition();
                intent.putExtra(stepIdKey, stepId);
                intent.putExtra(recipeNameKey, DetailRecipeFragment.recipeName);
                intent.putExtra(recipeKey, DetailRecipeFragment.recipe);
                intent.putParcelableArrayListExtra(stepListKey, (ArrayList<? extends Parcelable>) steps);
                context.startActivity(intent);
            }
        });
    }

    public void setSteps(List<Step> stepList) {
        this.steps = stepList;
        notifyDataSetChanged();
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
