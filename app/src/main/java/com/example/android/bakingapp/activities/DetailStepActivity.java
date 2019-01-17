package com.example.android.bakingapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.fragments.DetailRecipeStepFragment;

public class DetailStepActivity extends AppCompatActivity implements DetailRecipeStepFragment.OnDetailRecipeStepClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail_step);

        DetailRecipeStepFragment detailRecipeStepFragment = new DetailRecipeStepFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.detail_step_fragment_container, detailRecipeStepFragment)
                .commit();
    }

    @Override
    public void onDetailRecipeStepSelected() {

    }
}
