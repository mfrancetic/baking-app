package com.example.android.bakingapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.fragments.DetailRecipeFragment;
import com.example.android.bakingapp.fragments.MainFragment;
//import com.example.android.bakingapp.idlingResource.SimpleIdlingResource;
import com.example.android.bakingapp.idlingResource.SimpleIdlingResource;
import com.example.android.bakingapp.models.Recipe;

import java.util.ArrayList;

import androidx.fragment.app.FragmentManager;
import androidx.test.espresso.IdlingResource;

import static com.example.android.bakingapp.adapters.MainAdapter.recipeKey;
import static com.example.android.bakingapp.adapters.MainAdapter.stepListKey;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.recipeNameKey;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.stepId;

public class MainActivity extends AppCompatActivity implements MainFragment.OnRecipeClickListener,
        View.OnClickListener {

    @Nullable
    private SimpleIdlingResource idlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainFragment mainFragment = new MainFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, mainFragment)
                .commit();
    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
//            final Intent intent = new Intent(this, DetailStepActivity.class);
//            intent.putExtra(recipeNameKey, DetailRecipeFragment.recipeName);
//            intent.putExtra(recipeKey, recipe);
//            intent.putParcelableArrayListExtra(stepListKey, (ArrayList<? extends Parcelable>) DetailRecipeFragment.stepList);
//            startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new SimpleIdlingResource();
        }
        return idlingResource;
    }

//    @Override
//    public void onBackPressed() {
//        int count = getFragmentManager().getBackStackEntryCount();
//        if (count == 0) {
//            super.onBackPressed();
//            finish();
//        } else {
//            getFragmentManager().popBackStack();
//        }
//    }
}