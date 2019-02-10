package com.example.android.bakingapp.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.fragments.DetailRecipeFragment;
import com.example.android.bakingapp.models.Step;

import java.util.List;

/**
 * DetailAdapter for the RecyclerView in the DetailRecipeFragment
 */
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

        @BindView(R.id.recipe_step_description)
        TextView recipeDescriptionTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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

        /* Set the onClickListener to the recipe description view */
        viewHolder.recipeDescriptionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecipeStepClickListener.onRecipeStepSelected(position);
            }
        });
    }

    /**
     * Sets the list of steps and notifies the adapter the dataset has changed
     */
    public void setSteps(List<Step> stepList) {
        this.steps = stepList;
        notifyDataSetChanged();
    }

    /**
     * Returns the number of recipe steps
     */
    @Override
    public int getItemCount() {
        if (steps == null) {
            return 0;
        } else {
            return steps.size();
        }
    }
}