package com.example.android.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activities.DetailActivity;
import com.example.android.bakingapp.fragments.MainFragment;
import com.example.android.bakingapp.models.Recipe;

import java.util.List;

import butterknife.BindView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<Recipe> recipes;

    private Context context;

    private MainFragment.OnRecipeClickListener onRecipeClickListener;

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipe_name_text_view)
        TextView recipeNameTextView;
        @BindView(R.id.recipe_image_view)
        ImageView recipeImageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeNameTextView = itemView.findViewById(R.id.recipe_name_text_view);
            recipeImageView = itemView.findViewById(R.id.recipe_image_view);
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
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder viewHolder, int position) {

        Recipe recipe = recipes.get(position);
        TextView recipeNameTextView = viewHolder.recipeNameTextView;
        recipeNameTextView.setText(recipe.getName());

        ImageView recipeImageView = viewHolder.recipeImageView;

        String image = recipe.getImage();
        Uri recipeImageUri = Uri.parse(image);

        com.squareup.picasso.Picasso
                .get()
                .load(recipeImageUri)
                .centerInside()
                .into(recipeImageView);


        final Context context = recipeNameTextView.getContext();

        recipeNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openRecipeIntent = new Intent(context, DetailActivity.class);
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

