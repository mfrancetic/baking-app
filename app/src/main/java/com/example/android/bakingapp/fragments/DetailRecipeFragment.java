package com.example.android.bakingapp.fragments;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activities.MainActivity;
import com.example.android.bakingapp.adapters.DetailAdapter;
import com.example.android.bakingapp.data.BakingAppWidget;
import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.bakingapp.fragments.DetailRecipeStepFragment.stepIdKey;

public class DetailRecipeFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static Recipe recipe;

    public static List<Step> stepList;

    public static final String recipeKey = "recipe";

    private List<Ingredient> ingredientList;

    private DetailAdapter detailAdapter;

    public static OnRecipeStepClickListener onRecipeStepClickListener;

    @BindView(R.id.detail_recycler_view)
    RecyclerView detailRecyclerView;

    @BindView(R.id.ingredients_text_view)
    TextView ingredientsTextView;

    public static SharedPreferences sharedPreferences;

    private static final String preferenceId = "preferenceId";

    public static final String preferenceName = "preferenceName";

    public static final String preferenceStepId = "stepId";

    public static final String preferenceIngredients = "preferenceIngredients";

    static final String preferences = "preferences";

    public static String recipeName;

    static String ingredientsString;

    private static final String recipeNameKey = "recipeName";

    public static final String stepListKey = "step";

    public static final String ingredientListKey = "ingredient";

    @BindView(R.id.detail_scroll_view)
    @Nullable
    ScrollView detailScrollView;

    private static final String SCROLL_POSITION_X = "scrollPositionX";

    private static final String SCROLL_POSITION_Y = "scrollPositionY";

    private int scrollX;

    private int scrollY;

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    }

    /**
     * Inteface for the RecipeStepClickListener
     */
    public interface OnRecipeStepClickListener {
        void onRecipeStepSelected(int position);
    }

    public DetailRecipeFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /* If the context exists, get the sharedPreferences */
        if (getContext() != null) {
            sharedPreferences = getContext().getSharedPreferences(preferences, Context.MODE_PRIVATE);
        }

        if (savedInstanceState != null) {
            /* If there is a savedInstanceState, get the recipe name, recipe, step list, ingredient list,
             * scrollX and scrollY position */
            recipeName = savedInstanceState.getString(recipeNameKey);
            recipe = savedInstanceState.getParcelable(recipeKey);
            stepList = savedInstanceState.getParcelableArrayList(stepListKey);
            ingredientList = savedInstanceState.getParcelableArrayList(ingredientListKey);
            scrollX = savedInstanceState.getInt(SCROLL_POSITION_X);
            scrollY = savedInstanceState.getInt(SCROLL_POSITION_Y);
        } else {
            if (getActivity() != null) {
                /* If there is no savedInstanceState, get the intent and the recipe, recipe name, step
                 * list and ingredient list from it */
                Intent intent = getActivity().getIntent();
                if (intent != null && intent.getParcelableExtra(recipeKey) != null) {
                    recipe = getActivity().getIntent().getParcelableExtra(recipeKey);
                    recipeName = recipe.getName();
                    stepList = getActivity().getIntent().getParcelableArrayListExtra(stepListKey);
                    ingredientList = getActivity().getIntent().getParcelableArrayListExtra(ingredientListKey);
                }
            }
        }

        /* If there is no recipe, put the recipeName, ingredientsString in the shared preferences */
        if (recipe == null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear().apply();
            editor.putString(preferenceName, recipeName);
            editor.putString(preferenceIngredients, ingredientsString);
            editor.apply();

            /* Update the app widget */
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
            Intent widgetIntent = new Intent(getContext(), BakingAppWidget.class);
            widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

            /* Send a broadcast for all the app widget ids */
            int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(getContext().getPackageName(),
                    BakingAppWidget.class.getName()));
            widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            getContext().sendBroadcast(widgetIntent);

            /* Launch a new intent that opens the MainActivity */
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        }

        /* If the recipe exists, but the recipe name doesn't, get the name from the recipe */
        if (recipe != null && recipeName == null) {
            recipeName = recipe.getName();
        }

        /* Set the title of the activity to the recipe name */
        if (getActivity() != null) {
            getActivity().setTitle(recipeName);
        }

        /* Get the step list, ingredient list and recipe id */
        stepList = recipe.getStepList();
        ingredientList = recipe.getIngredientList();
        int recipeId = recipe.getId();

        /* Inflate the fragment_recipe_detail */
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        /* Bind the views with their ID's using the Butterknife library */
        ButterKnife.bind(this, rootView);

        /* Get the context of the rootView */
        Context context = rootView.getContext();

        /* Create a new DetailAdapter and get the fragment activity */
        detailAdapter = new DetailAdapter(stepList, onRecipeStepClickListener);
        final FragmentActivity fragmentActivity = getActivity();

        /* Create a new LinearLayoutManager and set it to the RecyclerView, as well as the
         * detailAdapter */
        LinearLayoutManager layoutManager = new LinearLayoutManager(fragmentActivity);
        detailRecyclerView.setLayoutManager(layoutManager);
        detailRecyclerView.setAdapter(detailAdapter);

        /* Make detailRecyclerView not focusable and request focus from the detailScrollView */
        detailRecyclerView.setFocusable(false);
        if (detailScrollView != null) {
            detailScrollView.requestFocus();
        }

        /* Create a new string from the quantity, measure and name of the ingredient */
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < ingredientList.size(); i++) {
            Double quantityInt = ingredientList.get(i).getIngredientQuantity();
            String quantity = String.valueOf(quantityInt);
            String measure = ingredientList.get(i).getIngredientMeasure();
            String ingredient = ingredientList.get(i).getIngredientName();
            String ingredientLine = quantity + " " + measure + " " + ingredient + "\n";
            stringBuilder.append(ingredientLine);
        }
        ingredientsString = stringBuilder.toString();

        /* Register the OnSharedPreferenceChangeListener */
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        /* Clear and then add the preference id, name and ingredients */
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
        editor.putInt(preferenceId, recipeId);
        editor.putString(preferenceName, recipeName);
        editor.putString(preferenceIngredients, ingredientsString);
        editor.apply();

        /* Update the app widget */
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        Intent widgetIntent = new Intent(context, BakingAppWidget.class);
        widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        /* Send the broadcast to update all the app widget id's */
        int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context.getPackageName(), BakingAppWidget.class.getName()));
        widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(widgetIntent);

        /* Set an OnClickListener to the detailRecyclerView */
        detailRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecipeStepClickListener.onRecipeStepSelected(detailRecyclerView.getId());
            }
        });

        /* Populate the DetailRecipeView */
        populateDetailRecipeView(ingredientsString, stepList);
        return rootView;
    }

    /**
     * Store the values under their keys to the savedInstanceState bundle
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(recipeNameKey, recipeName);
        outState.putParcelable(recipeKey, recipe);
        outState.putInt(stepIdKey, DetailRecipeStepFragment.stepId);
        outState.putParcelableArrayList(stepListKey, (ArrayList<? extends Parcelable>) stepList);
        outState.putParcelableArrayList(ingredientListKey, (ArrayList<? extends Parcelable>) ingredientList);
        if (detailScrollView != null) {
            scrollX = detailScrollView.getScrollX();
            scrollY = detailScrollView.getScrollY();
            outState.putInt(SCROLL_POSITION_X, scrollX);
            outState.putInt(SCROLL_POSITION_Y, scrollY);
        }
    }

    /**
     * OnDestroy, clear the sharedPreferences and unregister the OnSharedPreferenceChangeListener
     */
    @Override
    public void onDestroy() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        /* Check if the onRecipeStepClickListener exists; if not, throw a RuntimeException */
        try {
            onRecipeStepClickListener = (OnRecipeStepClickListener) context;
        } catch (ClassCastException e) {
            throw new RuntimeException(context.toString() + "must implement OnDetailRecipeStepClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onRecipeStepClickListener = null;
    }

    /**
     * Populate the DetailRecipeView and scroll to the X and Y coordinates of the ScrollView
     */
    private void populateDetailRecipeView(String ingredients, List<Step> stepList) {
        if (detailScrollView != null) {
            detailScrollView.scrollTo(scrollX, scrollY);
        }
        DetailRecipeFragment.stepList = stepList;
        detailAdapter.setSteps(stepList);
        ingredientsTextView.setText(ingredients);
    }
}