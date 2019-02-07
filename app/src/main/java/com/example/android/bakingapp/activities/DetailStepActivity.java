package com.example.android.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.fragments.DetailRecipeStepFragment;

import static com.example.android.bakingapp.fragments.DetailRecipeFragment.recipe;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.recipeName;

public class DetailStepActivity extends AppCompatActivity implements DetailRecipeStepFragment.OnDetailRecipeStepClickListener {

    private static final String recipeNameKey = "recipeName";

    private static final String recipeKey = "recipe";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail_step);

        DetailRecipeStepFragment detailRecipeStepFragment = new DetailRecipeStepFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.detail_step_fragment_container, detailRecipeStepFragment)
                .commit();
    }

    @Override
    public void onDetailRecipeStepSelected() {

    }

//    @Override
//    public void onBackPressed() {
//        int count = getFragmentManager().getBackStackEntryCount();
//        if (count == 0) {
//            super.onBackPressed();
//            Intent intent = new Intent(DetailStepActivity.this, DetailActivity.class);
//            intent.putExtra(recipeNameKey, recipeName);
//            intent.putExtra(recipeKey, recipe);
//            startActivity(intent);
//            finish();
//        } else {
//            getFragmentManager().popBackStack();
//        }
//    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
