package com.example.android.bakingapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activities.MainActivity;
import com.example.android.bakingapp.adapters.MainAdapter;
import com.example.android.bakingapp.idlingResource.SimpleIdlingResource;
import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Step;
import com.example.android.bakingapp.utils.QueryUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainFragment extends Fragment {

    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    private List<Recipe> recipeList;

    private OnRecipeClickListener onRecipeClickListener;

    private MainAdapter mainAdapter;

    @BindView(R.id.main_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;

    @BindView(R.id.empty_text_view)
    TextView emptyTextView;

    @BindView(R.id.main_scroll_view)
    @Nullable
    NestedScrollView mainScrollView;

    private static final String SCROLL_POSITION_X = "scrollPositionX";

    private static final String SCROLL_POSITION_Y = "scrollPositionY";

    private int scrollX;

    private int scrollY;

    private final static String recipeListKey = "recipeList";

    @BindView(R.id.constraint_layout_main_tablet_mode)
    @Nullable
    ConstraintLayout constraintLayoutTabletMode;

    private SimpleIdlingResource idlingResource;

    public interface OnRecipeClickListener {
    }

    public MainFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /* Create a new recipeList */
        recipeList = new ArrayList<>();

        /* Inflate the rootView */
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        /* Bind the views with their ID's using the Butterknife library */
        ButterKnife.bind(this, rootView);

        /* Get the context from the rootView */
        Context context = rootView.getContext();

        /* Get the fragmentActivity */
        final FragmentActivity fragmentActivity = getActivity();

        /* Get the idlingResource */
        getIdlingResource();

        LinearLayoutManager layoutManager;
        if (constraintLayoutTabletMode != null) {
            int spanCount = 3;
            layoutManager = new GridLayoutManager(context, spanCount);
        } else {
            layoutManager = new LinearLayoutManager(fragmentActivity);
        }

        /* Create a new MainAdapter, set the mainAdapter and the LayoutManager to the recyclerView */
        mainAdapter = new MainAdapter(context, recipeList, onRecipeClickListener);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mainAdapter);

        /* Make detailRecyclerView not focusable and request focus from the detailScrollView */
        recyclerView.setFocusable(false);
        if (mainScrollView != null) {
            mainScrollView.requestFocus();
        }

        /* Set an OnClickListener to the recyclerView */
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        if (savedInstanceState != null) {
            /* If the savedInstanceState exists, get the values under their keys */
            scrollX = savedInstanceState.getInt(SCROLL_POSITION_X);
            scrollY = savedInstanceState.getInt(SCROLL_POSITION_Y);
            recipeList = savedInstanceState.getParcelableArrayList(recipeListKey);
        } else {
            /* If the savedInstanceState doesn't exist, execute a new RecipeAsyncTask */
            new RecipeAsyncTask().execute();
            mainAdapter.notifyDataSetChanged();
        }
        return rootView;
    }

    /**
     * OnAttach, check if the onRecipeClickListener is implemented
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onRecipeClickListener = (OnRecipeClickListener) context;
        } catch (ClassCastException e) {
            throw new RuntimeException(context.toString() + "must implement OnRecipeClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onRecipeClickListener = null;
    }

    /**
     * Get IdlingResource from the MainActivity
     */
    @VisibleForTesting
    private void getIdlingResource() {
        if (getActivity() != null) {
            idlingResource = (SimpleIdlingResource) ((MainActivity) getActivity()).getIdlingResource();
            idlingResource.setIdleState(false);
        }
    }

    /**
     * RecipeAsyncTask class that creates the URL for loading the recipes, makes the HTTP request and
     * parses the JSON String in order to create a new Recipe object.
     * Returns a list of recipes.
     */
    @SuppressLint("StaticFieldLeak")
    private class RecipeAsyncTask extends AsyncTask<String, Void, List<Recipe>> {

        @Override
        protected List<Recipe> doInBackground(String... strings) {
            /* Create a new recipeList */
            List<Recipe> recipeList = new ArrayList<>();

            try {
                URL url = QueryUtils.createUrl();
                String recipeJson = QueryUtils.makeHttpRequest(url);

                /* Create a new recipeArray */
                JSONArray recipeArray = new JSONArray(recipeJson);

                /* For each recipe in the recipeArray, create a Recipe object */
                for (int i = 0; i < recipeArray.length(); i++) {

                    /* Get a single recipe at position i within the list of recipes */
                    JSONObject recipeObject = recipeArray.getJSONObject(i);

                    /* Extract the value for the required keys */
                    int id = recipeObject.getInt("id");
                    String name = recipeObject.getString("name");

                    /* Create a new ingredientList and get the ingredientArray */
                    List<Ingredient> ingredientList = new ArrayList<>();
                    JSONArray ingredientArray = (JSONArray) recipeObject.get("ingredients");

                    /* For each ingredient in the recipeArray, create a Ingredient object */
                    for (int j = 0; j < ingredientArray.length(); j++) {
                        JSONObject ingredientObject = ingredientArray.getJSONObject(j);

                        /* Extract the value for the required keys */
                        int quantity = ingredientObject.getInt("quantity");
                        String measure = ingredientObject.getString("measure");
                        String ingredient = ingredientObject.getString("ingredient");

                        /* Create a new Ingredient and add it to the ingredientList */
                        Ingredient newIngredient = new Ingredient(quantity, measure, ingredient);
                        ingredientList.add(newIngredient);
                    }

                    /* Create a new stepList and get the stepArray */
                    List<Step> stepList = new ArrayList<>();
                    JSONArray stepArray = (JSONArray) recipeObject.get("steps");

                    /* For each step in the stepArray, create a Step object */
                    for (int k = 0; k < stepArray.length(); k++) {

                        /* Get a single step at position i within the list of steps */
                        JSONObject stepObject = stepArray.getJSONObject(k);

                        /* Extract the value for the required keys */
                        int stepId = stepObject.getInt("id");
                        String shortDescription = stepObject.getString("shortDescription");
                        String description = stepObject.getString("description");
                        String videoUrl = stepObject.getString("videoURL");
                        String thumbnailUrl = stepObject.getString("thumbnailURL");

                        /* Create a new Step and add it to the stepList */
                        Step newStep = new Step(stepId, shortDescription, description, videoUrl, thumbnailUrl);
                        stepList.add(newStep);
                    }

                    /* Extract the value for the required keys */
                    int servings = recipeObject.getInt("servings");
                    String image = recipeObject.getString("image");

                    /* Create a new Recipe object and set the values to it */
                    Recipe recipe = new Recipe(id, name, ingredientList, stepList, servings, image);
                    recipe.setId(id);
                    recipe.setName(name);
                    recipe.setIngredientList(ingredientList);
                    recipe.setStepList(stepList);
                    recipe.setServings(servings);
                    recipe.setImage(image);

                    /* Add the recipe to the recipeList */
                    recipeList.add(i, recipe);
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem retrieving the recipe JSON results");
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the recipe JSON response");
            }
            return recipeList;
        }

        @Override
        protected void onPostExecute(List<Recipe> recipes) {
            if (recipes.size() == 0) {
                /* If there are no recipes, show the emptyTextView and loadingIndicator */
                emptyTextView.setVisibility(View.VISIBLE);
                loadingIndicator.setVisibility(View.VISIBLE);
            } else {
                /* If there are recipes available, hide the emptyTextView and loadingIndicator and
                * populate the recipes */
                emptyTextView.setVisibility(View.GONE);
                loadingIndicator.setVisibility(View.GONE);
                populateRecipes(recipes);
            }
            idlingResource.setIdleState(true);
        }
    }

    /**
     * Populate the recipes with the list of recipes
     */
    private void populateRecipes(List<Recipe> recipes) {
        this.recipeList = recipes;
        mainAdapter.setRecipes(recipeList);
        mainAdapter.notifyDataSetChanged();
        if (mainScrollView != null) {
            /* Scroll to the X and Y position of the mainScrollView*/
            mainScrollView.scrollTo(scrollX, scrollY);
        }
    }

    /**
     * Store the values under their keys to the savedInstanceState bundle
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(recipeListKey, (ArrayList<? extends Parcelable>) recipeList);
        if (mainScrollView != null) {
            scrollX = mainScrollView.getScrollX();
            scrollY = mainScrollView.getScrollY();
        }
        savedInstanceState.putInt(SCROLL_POSITION_X, scrollX);
        savedInstanceState.putInt(SCROLL_POSITION_Y, scrollY);
        super.onSaveInstanceState(savedInstanceState);
    }
}