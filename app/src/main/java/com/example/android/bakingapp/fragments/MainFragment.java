package com.example.android.bakingapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
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


    public MainFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        return rootView;
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
                    if (ingredientArray != null) {
                        for (int ingredientI = 0;  ingredientI < ingredientArray.length(); ingredientI ++) {
                            ingredientList.add(ingredientList.get(ingredientI));
                        }
                    }

                    List<Step> stepList = new ArrayList<>();
                    JSONArray stepArray = (JSONArray) recipeObject.get("steps");
                    if (stepArray != null) {
                        for (int stepI = 0;  stepI < stepArray.length(); stepI ++) {
                            ingredientList.add(ingredientList.get(stepI));
                        }
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
    }
}
