package com.example.android.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activities.DetailActivity;
import com.example.android.bakingapp.fragments.MainFragment;
import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<Recipe> recipes;

    public static List<Step> currentSteps;

    public static List<Ingredient> currentIngredients;

    public static Recipe currentRecipe;

    private Context context;

    private MainFragment.OnRecipeClickListener onRecipeClickListener;

    public static final String recipeKey = "recipe";

    public static final String stepListKey = "step";


    public static final String ingredientListKey = "ingredient";

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipe_name_text_view)
        TextView recipeNameTextView;
        @BindView(R.id.recipe_image_view)
        ImageView recipeImageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public MainAdapter(Context context, List<Recipe> recipes, MainFragment.OnRecipeClickListener onRecipeClickListener) {
        this.context = context;
        this.recipes = recipes;
        this.onRecipeClickListener = onRecipeClickListener;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View recipeView = inflater.inflate(R.layout.fragment_main_item, parent, false);
        return new ViewHolder(recipeView);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder viewHolder, final int position) {

        currentRecipe = recipes.get(position);
        currentSteps = currentRecipe.getStepList();
        currentIngredients = currentRecipe.getIngredientList();

        TextView recipeNameTextView = viewHolder.recipeNameTextView;
        recipeNameTextView.setText(currentRecipe.getName());

        ImageView recipeImageView = viewHolder.recipeImageView;

        String image = currentRecipe.getImage();
        if (image.isEmpty()) {
            recipeImageView.setImageResource(R.drawable.cupcake);
        } else {
            Uri recipeImageUri = Uri.parse(image);

            com.squareup.picasso.Picasso
                    .get()
                    .load(recipeImageUri)
                    .into(recipeImageView);
        }

        final Context context = recipeNameTextView.getContext();

        recipeNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentRecipe = recipes.get(position);
                Intent openRecipeIntent = new Intent(context, DetailActivity.class);
                openRecipeIntent.putExtra(recipeKey, currentRecipe);
                openRecipeIntent.putParcelableArrayListExtra(ingredientListKey, (ArrayList<? extends Parcelable>) currentIngredients);
                openRecipeIntent.putParcelableArrayListExtra(stepListKey, (ArrayList<? extends Parcelable>) currentSteps);
                context.startActivity(openRecipeIntent);
            }
        });
    }

    public void setRecipes(List<Recipe> recipeList) {
        this.recipes = recipeList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (recipes == null) {
            return 0;
        } else {
            return recipes.size();
        }
    }
}