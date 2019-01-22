package com.example.android.bakingapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.fragments.DetailRecipeFragment;
import com.example.android.bakingapp.fragments.DetailRecipeStepFragment;
import com.example.android.bakingapp.fragments.MainFragment;
import com.example.android.bakingapp.models.Recipe;

import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements DetailRecipeFragment.OnRecipeStepClickListener,
DetailRecipeStepFragment.OnDetailRecipeStepClickListener{

    private boolean twoPane;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (findViewById(R.id.activity_recipe_detail_linear_layout) != null) {
            twoPane = true;

//            Button previousStepButton = findViewById(R.id.previous_step_button);
//            Button nextStepButton = findViewById(R.id.next_step_button);
//
//            previousStepButton.setVisibility(View.GONE);
//            nextStepButton.setVisibility(View.GONE);

            if (savedInstanceState == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                DetailRecipeFragment detailRecipeFragment = new DetailRecipeFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.detail_fragment_container, detailRecipeFragment)
                        .commit();

//                DetailRecipeStepFragment detailRecipeStepFragment = new DetailRecipeStepFragment();
//                fragmentManager.beginTransaction()
//                        .add(R.id.detail_step_fragment_container, detailRecipeStepFragment)
//                        .commit();
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

    @Override
    public void onRecipeStepSelected(Recipe recipe) {


    }

    @Override
    public void onDetailRecipeStepSelected() {

    }
}
