package com.example.android.bakingapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.fragments.MainFragment;
import com.example.android.bakingapp.idlingResource.SimpleIdlingResource;
import com.example.android.bakingapp.models.Recipe;

import androidx.fragment.app.FragmentManager;
import androidx.test.espresso.IdlingResource;

public class MainActivity extends AppCompatActivity implements MainFragment.OnRecipeClickListener {

    @Nullable
    private SimpleIdlingResource idlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Replace the main fragment */
        MainFragment mainFragment = new MainFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, mainFragment)
                .commit();
    }

    /**
     * Handles selecting on the recipe
     */
    @Override
    public void onRecipeSelected(Recipe recipe) {
    }

    /**
     * Creates and returns the idling resource
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new SimpleIdlingResource();
        }
        return idlingResource;
    }
}