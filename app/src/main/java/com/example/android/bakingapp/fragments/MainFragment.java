package com.example.android.bakingapp.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapters.MainAdapter;
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
import java.util.Queue;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment extends Fragment {

    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    List<Recipe> recipeList;

    OnRecipeClickListener onRecipeClickListener;

    MainAdapter mainAdapter;

    RecyclerView recyclerView;

    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;

    @BindView(R.id.empty_text_view)
    TextView emptyTextView;

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
//        final FragmentActivity fragmentActivity = getActivity();
        Context context = rootView.getContext();

        final FragmentActivity fragmentActivity = getActivity();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.main_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(fragmentActivity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mainAdapter);

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        new RecipeAsyncTask(context, new RecipeCallback()
        new RecipeAsyncTask().execute();
//
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
                emptyTextView.setVisibility(View.GONE);
                loadingIndicator.setVisibility(View.GONE);
            } else {
//                populateRecipes(recipes);
            }
        }
    }

//    private void populateRecipes(List<Recipe> recipes) {
//        this.recipeList = recipes;
//        mainAdapter.setRecipes(recipeList);
//    }


}
