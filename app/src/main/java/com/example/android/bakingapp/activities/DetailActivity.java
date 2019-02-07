package com.example.android.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.fragments.DetailRecipeFragment;
import com.example.android.bakingapp.fragments.DetailRecipeStepFragment;

import java.util.ArrayList;

import static com.example.android.bakingapp.fragments.DetailRecipeFragment.stepId;

public class DetailActivity extends AppCompatActivity implements DetailRecipeFragment.OnRecipeStepClickListener,
        DetailRecipeStepFragment.OnDetailRecipeStepClickListener {

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

        ButterKnife.bind(this);

        if (dividerRecipe != null) {
            twoPane = true;
            if (savedInstanceState == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                DetailRecipeFragment detailRecipeFragment = new DetailRecipeFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.detail_fragment_container, detailRecipeFragment)
                        .commit();

                DetailRecipeStepFragment detailRecipeStepFragment = new DetailRecipeStepFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.detail_step_fragment_container, detailRecipeStepFragment)
                        .commit();
            }
        } else {
            twoPane = false;

            FragmentManager fragmentManager = getSupportFragmentManager();

            DetailRecipeFragment detailRecipeFragment = new DetailRecipeFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.detail_fragment_container, detailRecipeFragment)
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
            intent.putExtra(stepIdKey, stepId);
            intent.putParcelableArrayListExtra(stepListKey, (ArrayList<? extends Parcelable>) DetailRecipeFragment.stepList);
            startActivity(intent);
        }
    }

    @Override
    public void onDetailRecipeStepSelected() {
    }
}