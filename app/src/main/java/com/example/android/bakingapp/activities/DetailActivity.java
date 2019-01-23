package com.example.android.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.fragments.DetailRecipeFragment;
import com.example.android.bakingapp.fragments.DetailRecipeStepFragment;
import com.example.android.bakingapp.fragments.MainFragment;
import com.example.android.bakingapp.models.Recipe;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements DetailRecipeFragment.OnRecipeStepClickListener,
        DetailRecipeStepFragment.OnDetailRecipeStepClickListener {

    private boolean twoPane;

    private static final String recipeKey = "recipe";

    private static final String recipeNameKey = "recipeName";

    private static final String stepListKey = "step";

    private static final String ingredientListKey = "ingredient";

    private static final String stepIdKey = "stepId";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (findViewById(R.id.divider_recipe_detail) != null) {
            twoPane = true;

            if (savedInstanceState == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                DetailRecipeFragment detailRecipeFragment = new DetailRecipeFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.detail_fragment_container, detailRecipeFragment)
                        .commit();

                DetailRecipeStepFragment detailRecipeStepFragment = new DetailRecipeStepFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.detail_step_fragment_container, detailRecipeStepFragment)
                        .commit();
            }
        } else {
            twoPane = false;

            FragmentManager fragmentManager = getSupportFragmentManager();

            DetailRecipeFragment detailRecipeFragment = new DetailRecipeFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.detail_fragment_container, detailRecipeFragment)
                    .commit();
        }
    }


    public void onRecipeStepSelected(int position) {
        if (twoPane) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            DetailRecipeStepFragment newDetailRecipeStepFragment = new DetailRecipeStepFragment();
            newDetailRecipeStepFragment.setStepId(position);
            newDetailRecipeStepFragment.setRecipeName(DetailRecipeFragment.recipeName);
            newDetailRecipeStepFragment.setRecipe(DetailRecipeFragment.recipe);
            newDetailRecipeStepFragment.setStepList(DetailRecipeFragment.stepList);
            fragmentManager.beginTransaction()
                    .replace(R.id.detail_step_fragment_container, newDetailRecipeStepFragment)
                    .commit();
        } else {
            final Intent intent = new Intent(this, DetailStepActivity.class);
            intent.putExtra(stepIdKey, position);
            intent.putExtra(recipeNameKey, DetailRecipeFragment.recipeName);
            intent.putExtra(recipeKey, DetailRecipeFragment.recipe);
            intent.putParcelableArrayListExtra(stepListKey, (ArrayList<? extends Parcelable>) DetailRecipeFragment.stepList);

            startActivity(intent);
//            TextView recipeDescriptionTextView = (TextView) findViewById(R.id.recipe_step_description);
//            recipeDescriptionTextView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(intent);
//                }
//            });
        }
    }


    @Override
    public void onDetailRecipeStepSelected() {

    }
}
