package com.example.android.bakingapp.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.fragments.MainFragment;
import com.example.android.bakingapp.models.Recipe;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainFragment.OnRecipeClickListener {


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
}