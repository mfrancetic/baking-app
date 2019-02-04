package com.example.android.bakingapp.activities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapters.MainAdapter;
import com.example.android.bakingapp.fragments.MainFragment;
//import com.example.android.bakingapp.idlingResource.SimpleIdlingResource;
import com.example.android.bakingapp.idlingResource.SimpleIdlingResource;
import com.example.android.bakingapp.models.Recipe;

import androidx.test.espresso.IdlingResource;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainFragment.OnRecipeClickListener,
        View.OnClickListener {

    @Nullable
    private SimpleIdlingResource idlingResource;

//    public static final String RECIPE_KEY = MainAdapter.recipeKey;


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
}