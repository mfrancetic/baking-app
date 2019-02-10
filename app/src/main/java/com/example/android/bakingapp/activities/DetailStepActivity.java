package com.example.android.bakingapp.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.fragments.DetailRecipeStepFragment;

public class DetailStepActivity extends AppCompatActivity implements DetailRecipeStepFragment.OnDetailRecipeStepClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail_step);

        /* Replace the detail_step fragment */
        DetailRecipeStepFragment detailRecipeStepFragment = new DetailRecipeStepFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.detail_step_fragment_container, detailRecipeStepFragment)
                .commit();
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
