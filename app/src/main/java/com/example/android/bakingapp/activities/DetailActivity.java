package com.example.android.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapters.DetailAdapter;
import com.example.android.bakingapp.adapters.MainAdapter;
import com.example.android.bakingapp.fragments.DetailRecipeFragment;
import com.example.android.bakingapp.fragments.DetailRecipeStepFragment;

import java.util.ArrayList;

import static com.example.android.bakingapp.fragments.DetailRecipeFragment.stepId;

public class DetailActivity extends AppCompatActivity implements DetailRecipeFragment.OnRecipeStepClickListener,
        DetailRecipeStepFragment.OnDetailRecipeStepClickListener {

    /**
     * Boolean that checks if the app is in the tablet or phone mode
     */
    public static boolean twoPane;

    private static final String recipeKey = "recipe";

    private static final String recipeNameKey = "recipeName";

    private static final String stepListKey = "step";

    private static final String ingredientListKey = "ingredient";

    private static final String stepIdKey = "stepId";

    @BindView(R.id.divider_recipe_detail)
    @Nullable
    View dividerRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        /* Bind the views with their ID's using the ButterKnife library */
        ButterKnife.bind(this);

        /* If the divider doesn't exist, apply the code for the phone mode; otherwise, apply the code
         * for the tablet mode */
        if (dividerRecipe != null) {
            /* In tablet mode, replace both the detail and detail_step fragment */
            twoPane = true;
            if (savedInstanceState == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                DetailRecipeFragment detailRecipeFragment = new DetailRecipeFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.detail_fragment_container, detailRecipeFragment)
                        .commit();

                DetailRecipeStepFragment detailRecipeStepFragment = new DetailRecipeStepFragment();
                detailRecipeStepFragment.setRecipeName(MainAdapter.currentRecipeName);
                detailRecipeStepFragment.setRecipe(MainAdapter.currentRecipe);
                detailRecipeStepFragment.setStepList(MainAdapter.currentSteps);
                detailRecipeStepFragment.setStepId(0);
                fragmentManager.beginTransaction()
                        .replace(R.id.detail_step_fragment_container, detailRecipeStepFragment)
                        .commit();
            }
        } else {
            /* In phone mode, replace the detail fragment */
            twoPane = false;
            FragmentManager fragmentManager = getSupportFragmentManager();
            DetailRecipeFragment detailRecipeFragment = new DetailRecipeFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.detail_fragment_container, detailRecipeFragment)
                    .commit();
        }
    }

    /**
     * The method handles clicking on a recipe step in the RecyclerView, depending if it is in the
     * tablet or phone mode
     */
    public void onRecipeStepSelected(int position) {
        if (twoPane) {
            /* In tablet mode, replace the detail_step fragment with the correct recipe step */
            FragmentManager fragmentManager = getSupportFragmentManager();
            DetailRecipeStepFragment newDetailRecipeStepFragment = new DetailRecipeStepFragment();
            newDetailRecipeStepFragment.setRecipeName(DetailRecipeFragment.recipeName);
            newDetailRecipeStepFragment.setRecipe(DetailRecipeFragment.recipe);
            newDetailRecipeStepFragment.setStepList(DetailRecipeFragment.stepList);
            newDetailRecipeStepFragment.setStepId(position);
            fragmentManager.beginTransaction()
                    .replace(R.id.detail_step_fragment_container, newDetailRecipeStepFragment)
                    .commit();
        } else {
            /* In phone mode, launch the intent that opens the DetailStepActivity, with the data
             * for the selected recipe step */
            final Intent intent = new Intent(this, DetailStepActivity.class);
            intent.putExtra(stepIdKey, position);
            intent.putExtra(recipeNameKey, DetailRecipeFragment.recipeName);
            intent.putExtra(recipeKey, DetailRecipeFragment.recipe);
            intent.putParcelableArrayListExtra(stepListKey, (ArrayList<? extends Parcelable>) DetailRecipeFragment.stepList);
            startActivity(intent);
        }
    }

    /**
     * Handles selecting the recipe step
     */
    @Override
    public void onDetailRecipeStepSelected() {
    }

    /**
     * Handles clicking on the Back button, which then leads to the previous activity
     */
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}