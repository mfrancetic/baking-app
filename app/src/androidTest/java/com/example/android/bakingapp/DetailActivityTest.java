package com.example.android.bakingapp;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Parcelable;

import androidx.fragment.app.Fragment;
import androidx.test.espresso.contrib.RecyclerViewActions;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.ingredientListKey;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.recipeKey;
import static org.hamcrest.Matchers.allOf;
import static androidx.test.espresso.intent.Intents.intending;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import com.example.android.bakingapp.activities.DetailActivity;
import com.example.android.bakingapp.fragments.DetailRecipeFragment;
import com.example.android.bakingapp.fragments.DetailRecipeStepFragment;
import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Step;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * Test for opening and displaying the DetailActivity
 */
@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {

    private Instrumentation.ActivityResult result;

    private Intent intent;

    private Recipe recipe;

    private List<Ingredient> ingredientList = new ArrayList<>();

    private List<Step> stepList = new ArrayList<>();

    private int stepId = 0;

    private int ingredientQuantity = 2;

    private String ingredientMeasure = "CUP";

    private String ingredientName = "Graham Cracker crumbs";

    private String stepDescription = "Recipe Introduction";

    private String stepVideoUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4";

    private String recipeName;

    private int recipeId;

    private int servings;

    /**
     * IntentsTestRule for the DetailActivity
     */
    @Rule
    public IntentsTestRule<DetailActivity> intentsTestRule =
            new IntentsTestRule<DetailActivity>(DetailActivity.class) {
                /** Provides data for the intent that opens the DetailActivity */
                @Override
                protected Intent getActivityIntent() {
                    Ingredient ingredient = new Ingredient(ingredientQuantity, ingredientMeasure, ingredientName);
                    Step step = new Step(stepId, stepDescription, stepDescription,
                            stepVideoUrl, null);
                    ingredientList.add(ingredient);
                    stepList.add(step);
                    recipe = new Recipe(recipeId, recipeName, ingredientList, stepList, servings, null);

                    /* Create an intent with the recipe, ingredient and step list */
                    intent = new Intent();
                    intent.putExtra(recipeKey, recipe);
                    intent.putExtra(ingredientListKey, (ArrayList<? extends Parcelable>) ingredientList);
                    intent.putExtra(DetailRecipeFragment.stepListKey, (ArrayList<? extends Parcelable>) stepList);
                    return intent;
                }
            };

    /**
     * Replace the fragments and open the DetailActivity
     */
    @Before
    public void openDetailActivity() {
        if (!DetailActivity.twoPane) {
            /* In phone mode, replace the detail fragment */
            Fragment fragment = new DetailRecipeFragment();
            intentsTestRule.getActivity()
                    .getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, fragment)
                    .commit();
        } else {
            /* In tablet mode, replace both the detail and detail_step fragment */
            Fragment fragment = new DetailRecipeFragment();
            intentsTestRule.getActivity()
                    .getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, fragment)
                    .commit();

            Fragment stepFragment = new DetailRecipeStepFragment();
            intentsTestRule.getActivity()
                    .getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_step_fragment_container, stepFragment)
                    .commit();
        }
    }

    /**
     * Test if opening the DetailActivity displays the ingredients and recipe steps
     */
    @Test
    public void openDetailActivity_checkIngredientSteps() {
        onView(withId(R.id.detail_recycler_view)).check(matches(isDisplayed()));
        onView(withText(stepId + " " + stepDescription)).check(matches(isDisplayed()));
        onView(withText(ingredientQuantity + " " + ingredientMeasure + " " + ingredientName + "\n")).check(matches(isDisplayed()));
        onView(withId(R.id.detail_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        if (DetailActivity.twoPane) {
            /* In tablet mode, check if the layout for the tablet mode is displayed */
            onView(withId(R.id.constraint_layout_step_tablet_mode)).check(matches(isDisplayed()));
        } else {
            /* In phone mode, check if all the correct data is passed to the intent */
            intending(allOf(hasExtra(
                    recipeKey, recipe),
                    hasExtra(ingredientListKey, ingredientList),
                    hasExtra(DetailRecipeFragment.stepListKey, stepList)))
                    .respondWith(result);
        }
    }
}