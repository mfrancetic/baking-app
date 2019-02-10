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

    public static List<Step> steps;

    public static List<Ingredient> ingredients;

    public static Recipe recipe;

    public static List<Step> currentSteps;

    private static List<Ingredient> currentIngredients;

    public static Recipe currentRecipe;

    private static final String recipeNameKey = "recipeName";

    public static String currentRecipeName;

    private static final String recipeKey = "recipe";

    private static final String stepListKey = "step";

    private static final String ingredientListKey = "ingredient";

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

    public MainAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
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
        /* Get the recipe, step list and ingredient list of the recipe */
        recipe = recipes.get(position);
        steps = recipe.getStepList();
        ingredients = recipe.getIngredientList();

        /* Set the recipe name to the TextView */
        TextView recipeNameTextView = viewHolder.recipeNameTextView;
        recipeNameTextView.setText(recipe.getName());

        /* Set the recipe image to the ImageView */
        ImageView recipeImageView = viewHolder.recipeImageView;
        String image = recipe.getImage();
        /* If there is no image, then set the image of the cupcake. Otherwise, set the image of the recipe */
        if (image.isEmpty()) {
            recipeImageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            Uri recipeImageUri = Uri.parse(image);
            com.squareup.picasso.Picasso
                    .get()
                    .load(recipeImageUri)
                    .into(recipeImageView);
        }

        /* Get the context from the recipeNameTextView */
        final Context context = recipeNameTextView.getContext();

        /* Set an OnClickListener to the recipeNameTextView */
        recipeNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* When clicking on the recipe, launch an intent that opens the details of the selected
                 * recipe in the DetailActivity */
                currentRecipe = recipes.get(position);
                currentSteps = currentRecipe.getStepList();
                currentIngredients = currentRecipe.getIngredientList();
                currentRecipeName = currentRecipe.getName();
                Intent openRecipeIntent = new Intent(context, DetailActivity.class);
                openRecipeIntent.putExtra(recipeKey, currentRecipe);
                openRecipeIntent.putExtra(recipeNameKey, currentRecipeName);
                openRecipeIntent.putParcelableArrayListExtra(ingredientListKey, (ArrayList<? extends Parcelable>) currentIngredients);
                openRecipeIntent.putParcelableArrayListExtra(stepListKey, (ArrayList<? extends Parcelable>) currentSteps);
                context.startActivity(openRecipeIntent);
            }
        });
    }

    /**
     * Sets the list of recipes and notifies the adapter the dataset has changed
     */
    public void setRecipes(List<Recipe> recipeList) {
        this.recipes = recipeList;
        notifyDataSetChanged();
    }

    /**
     * Returns the number of recipes in the list
     */
    @Override
    public int getItemCount() {
        if (recipes == null) {
            return 0;
        } else {
            return recipes.size();
        }
    }
}