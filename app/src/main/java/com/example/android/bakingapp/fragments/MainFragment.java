package com.example.android.bakingapp.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainFragment extends Fragment {

    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    List<Recipe> recipeList;

    OnRecipeClickListener onRecipeClickListener;

    MainAdapter mainAdapter;

    @BindView(R.id.main_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;

    @BindView(R.id.empty_text_view)
    TextView emptyTextView;

    @BindView(R.id.constraint_layout_main_tablet_mode)
    @Nullable ConstraintLayout constraintLayoutTabletMode;

    private boolean twoPane;

    LinearLayoutManager layoutManager;

    int spanCount = 3;

    private SimpleIdlingResource idlingResource;

    public interface OnRecipeClickListener {
        void onRecipeSelected(Recipe recipe);
    }

    public MainFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        recipeList = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, rootView);

        Context context = rootView.getContext();

        final FragmentActivity fragmentActivity = getActivity();

        idlingResource = (SimpleIdlingResource) ((MainActivity) getActivity()).getIdlingResource();
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        if (constraintLayoutTabletMode != null) {
            twoPane = true;
            layoutManager = new GridLayoutManager(context, spanCount);
        } else {
            layoutManager = new LinearLayoutManager(fragmentActivity);
        }

        mainAdapter = new MainAdapter(context, recipeList, onRecipeClickListener);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mainAdapter);

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        new RecipeAsyncTask().execute();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
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

    private class RecipeAsyncTask extends AsyncTask<String, Void, List<Recipe>> {

        @Override
        protected List<Recipe> doInBackground(String... strings) {
            List<Recipe> recipeList = new ArrayList<>();
            try {
                URL url = QueryUtils.createUrl();
                String recipeJson = QueryUtils.makeHttpRequest(url);

                JSONArray recipeArray = new JSONArray(recipeJson);

                for (int i = 0; i < recipeArray.length(); i++) {
                    JSONObject recipeObject = recipeArray.getJSONObject(i);

                    int id = recipeObject.getInt("id");
                    String name = recipeObject.getString("name");

                    List<Ingredient> ingredientList = new ArrayList<>();
                    JSONArray ingredientArray = (JSONArray) recipeObject.get("ingredients");

                    for (int j = 0; j < ingredientArray.length(); j++) {
                        JSONObject ingredientObject = ingredientArray.getJSONObject(j);

                        int quantity = ingredientObject.getInt("quantity");
                        String measure = ingredientObject.getString("measure");
                        String ingredient = ingredientObject.getString("ingredient");

                        Ingredient newIngredient = new Ingredient(quantity, measure, ingredient);

                        ingredientList.add(newIngredient);
                    }

                    List<Step> stepList = new ArrayList<>();
                    JSONArray stepArray = (JSONArray) recipeObject.get("steps");
                    for (int k = 0; k < stepArray.length(); k++) {
                        JSONObject stepObject = stepArray.getJSONObject(k);
                        int stepId = stepObject.getInt("id");
                        String shortDescription = stepObject.getString("shortDescription");
                        String description = stepObject.getString("description");
                        String videoUrl = stepObject.getString("videoURL");
                        String thumbnailUrl = stepObject.getString("thumbnailURL");

                        Step newStep = new Step(stepId, shortDescription, description, videoUrl, thumbnailUrl);

                        stepList.add(newStep);
                    }

                    int servings = recipeObject.getInt("servings");
                    String image = recipeObject.getString("image");

                    Recipe recipe = new Recipe(id, name, ingredientList, stepList, servings, image);

                    recipe.setId(id);
                    recipe.setName(name);
                    recipe.setIngredientList(ingredientList);
                    recipe.setStepList(stepList);
                    recipe.setServings(servings);
                    recipe.setImage(image);

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
                emptyTextView.setVisibility(View.VISIBLE);
                loadingIndicator.setVisibility(View.VISIBLE);
            } else {
                emptyTextView.setVisibility(View.GONE);
                loadingIndicator.setVisibility(View.GONE);
                populateRecipes(recipes);
            }
            idlingResource.setIdleState(true);

        }
    }

    private void populateRecipes(List<Recipe> recipes) {
        this.recipeList = recipes;
        mainAdapter.setRecipes(recipeList);
    }
}
