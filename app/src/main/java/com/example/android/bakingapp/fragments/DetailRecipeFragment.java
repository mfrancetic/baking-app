package com.example.android.bakingapp.fragments;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activities.MainActivity;
import com.example.android.bakingapp.adapters.DetailAdapter;
//import com.example.android.bakingapp.data.WidgetRemoteViewsService;
import com.example.android.bakingapp.data.BakingAppWidget;
//import com.example.android.bakingapp.data.WidgetRemoteViewsService;
import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailRecipeFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = DetailRecipeFragment.class.getSimpleName();

    public static Recipe recipe;

    public static List<Step> stepList;

    public static final String recipeKey = "recipe";

    List<Ingredient> ingredientList;

    DetailAdapter detailAdapter;

    OnRecipeStepClickListener onRecipeStepClickListener;

    @BindView(R.id.detail_recycler_view)
    RecyclerView detailRecyclerView;

    @BindView(R.id.ingredients_text_view)
    TextView ingredientsTextView;

    public static SharedPreferences sharedPreferences;

    public static final String preferenceId = "preferenceId";

    public static final String preferenceName = "preferenceName";

    public static final String preferenceIngredients = "preferenceIngredients";

    public static final String preferences = "preferences";

    public static String recipeName;

    public static String ingredientsString;

    public static final String recipeNameKey = "recipeName";


//    WidgetRemoteViewsService widgetRemoteViewsService;

    public static final String stepListKey = "step";


    public static final String ingredientListKey = "ingredient";

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
//
//        Intent widgetIntent = new Intent(getContext(), BakingAppWidget.class);
//        widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//
//        int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(getContext().getPackageName(), BakingAppWidget.class.getName()));
//
//        widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
//        getContext().sendBroadcast(widgetIntent);
    }


    public interface OnRecipeStepClickListener {
        void onRecipeStepSelected(int position);
    }

    public DetailRecipeFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getContext() != null) {
            sharedPreferences = getContext().getSharedPreferences(preferences, Context.MODE_PRIVATE);
        }

//            if (sharedPreferences != null && recipe != null) {
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.clear().apply();
//
//                editor.putString(preferenceName, recipeName);
//                editor.putString(preferenceIngredients, ingredientsString);
//                editor.apply();
//
//                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
//
//                Intent widgetIntent = new Intent(getContext(), BakingAppWidget.class);
//                widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//
//                int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(getContext().getPackageName(), BakingAppWidget.class.getName()));
//
//                widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
//                getContext().sendBroadcast(widgetIntent);
//
//            }
//        }


        if (savedInstanceState != null) {
            recipeName = savedInstanceState.getString(recipeNameKey);
            recipe = savedInstanceState.getParcelable(recipeKey);
            stepList = savedInstanceState.getParcelableArrayList(stepListKey);
            ingredientList = savedInstanceState.getParcelableArrayList(ingredientListKey);
        } else {
            Intent intent = getActivity().getIntent();
            if (intent != null && intent.getParcelableExtra(recipeKey) != null) {
                recipe = getActivity().getIntent().getParcelableExtra(recipeKey);
                recipeName = recipe.getName();
                stepList = getActivity().getIntent().getParcelableArrayListExtra(stepListKey);
                ingredientList = getActivity().getIntent().getParcelableArrayListExtra(ingredientListKey);
            }
        }

        if (recipe == null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear().apply();

            editor.putString(preferenceName, recipeName);
            editor.putString(preferenceIngredients, ingredientsString);
            editor.apply();

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());

            Intent widgetIntent = new Intent(getContext(), BakingAppWidget.class);
            widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

            int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(getContext().getPackageName(), BakingAppWidget.class.getName()));

            widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            getContext().sendBroadcast(widgetIntent);

            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        }

        recipeName = recipe.getName();

        if (getActivity() != null) {
            getActivity().setTitle(recipeName);
        }

        stepList = recipe.getStepList();

        ingredientList = recipe.getIngredientList();

        int recipeId = recipe.getId();

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

        for (int i = 0; i < ingredientList.size(); i++) {
            int quantityInt = ingredientList.get(i).getIngredientQuantity();
            String quantity = String.valueOf(quantityInt);
            String measure = ingredientList.get(i).getIngredientMeasure();
            String ingredient = ingredientList.get(i).getIngredientName();
            String ingredientLine = quantity + " " + measure + " " + ingredient + "\n";
            stringBuilder.append(ingredientLine);
        }

        ingredientsString = stringBuilder.toString();


        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();

        editor.putInt(preferenceId, recipeId);
        editor.putString(preferenceName, recipeName);
        editor.putString(preferenceIngredients, ingredientsString);
        editor.apply();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        Intent widgetIntent = new Intent(context, BakingAppWidget.class);
        widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context.getPackageName(), BakingAppWidget.class.getName()));

        widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(widgetIntent);

        detailRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecipeStepClickListener.onRecipeStepSelected(detailRecyclerView.getId());
            }
        });

        populateDetailRecipeView(ingredientsString, stepList);

        return rootView;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(recipeNameKey, recipeName);
        outState.putParcelable(recipeKey, recipe);
        outState.putParcelableArrayList(stepListKey, (ArrayList<? extends Parcelable>) stepList);
        outState.putParcelableArrayList(ingredientListKey, (ArrayList<? extends Parcelable>) ingredientList);
    }


    @Override
    public void onDestroy() {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();

        Log.i("preferenceId -onDestroy", String.valueOf(sharedPreferences.getInt(preferenceId, 0)));
        Log.i("prefName-onDest", String.valueOf(sharedPreferences.getString(preferenceName, "default")));
        Log.i("preferenceIngred-onDest", String.valueOf(sharedPreferences.getString(preferenceIngredients, "default")));

        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);

//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
//
//        Intent widgetIntent = new Intent(getContext(), BakingAppWidget.class);
//        widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//
//        int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(getContext().getPackageName(), BakingAppWidget.class.getName()));
//
//        widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
//        getContext().sendBroadcast(widgetIntent);

        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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

    public void populateDetailRecipeView(String ingredients, List<Step> stepList) {
        this.stepList = stepList;
        detailAdapter.setSteps(stepList);
        ingredientsTextView.setText(ingredients);
    }
}
