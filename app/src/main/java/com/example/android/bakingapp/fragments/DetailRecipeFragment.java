package com.example.android.bakingapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapters.DetailAdapter;
import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailRecipeFragment extends Fragment {

    private static final String LOG_TAG = DetailRecipeFragment.class.getSimpleName();

    Recipe recipe;

    List<Step> stepList;

    List<Ingredient> ingredientList;

    DetailAdapter detailAdapter;

    OnRecipeStepClickListener onRecipeStepClickListener;

    @BindView(R.id.detail_recycler_view)
    RecyclerView detailRecyclerView;

    @BindView(R.id.ingredients_text_view)
    TextView ingredientsTextView;

    private static final String recipeKey = "recipe";


    public interface OnRecipeStepClickListener {
        void onRecipeStepSelected(Recipe recipe);
    }

    public DetailRecipeFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            recipe = getActivity().getIntent().getParcelableExtra(recipeKey);
        }

        stepList = recipe.getStepList();

        ingredientList = recipe.getIngredientList();

        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        Context context = rootView.getContext();

        detailAdapter = new DetailAdapter(context, stepList, onRecipeStepClickListener);

        final FragmentActivity fragmentActivity = getActivity();
        ButterKnife.bind(this, rootView);

        detailRecyclerView = rootView.findViewById(R.id.detail_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(fragmentActivity);
        detailRecyclerView.setLayoutManager(layoutManager);
        detailRecyclerView.setAdapter(detailAdapter);

        StringBuilder stringBuilder = new StringBuilder();

        String ingredients;

        for (int i = 0; i < ingredientList.size(); i++) {
            int quantityInt = ingredientList.get(i).getIngredientQuantity();
            String quantity = String.valueOf(quantityInt);
            String measure = ingredientList.get(i).getIngredientMeasure();
            String ingredient = ingredientList.get(i).getIngredientName();
            String ingredientLine = quantity + " " + measure + " " + ingredient + "\n";
            stringBuilder.append(ingredientLine);
        }

        ingredients = stringBuilder.toString();

        detailRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        populateDetailRecipeView(ingredients, stepList);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onRecipeStepClickListener = (OnRecipeStepClickListener) context;
        } catch (ClassCastException e) {
            throw new RuntimeException(context.toString() + "must implement OnRecipeStepClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onRecipeStepClickListener = null;
    }

    public void populateDetailRecipeView(String ingredients, List<Step> stepList) {
        this.stepList = stepList;
        detailAdapter.setSteps(stepList);
        ingredientsTextView.setText(ingredients);
    }
}
