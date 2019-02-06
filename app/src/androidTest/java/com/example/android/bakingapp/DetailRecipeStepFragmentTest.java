package com.example.android.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;

import androidx.fragment.app.Fragment;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.android.bakingapp.adapters.MainAdapter.ingredients;
import static com.example.android.bakingapp.adapters.MainAdapter.stepListKey;
import static com.example.android.bakingapp.adapters.MainAdapter.steps;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.ingredientListKey;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.ingredientsString;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.preferenceIngredients;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.preferenceName;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.preferences;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.recipe;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.recipeKey;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.recipeName;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.recipeNameKey;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.sharedPreferences;
import static org.hamcrest.Matchers.allOf;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;

import androidx.test.espresso.intent.rule.IntentsTestRule;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.any;

import com.example.android.bakingapp.activities.DetailActivity;
import com.example.android.bakingapp.activities.MainActivity;
import com.example.android.bakingapp.adapters.MainAdapter;
import com.example.android.bakingapp.data.BakingAppWidget;
import com.example.android.bakingapp.fragments.DetailRecipeFragment;
import com.example.android.bakingapp.fragments.DetailRecipeStepFragment;
import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Step;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


@RunWith(AndroidJUnit4.class)
public class DetailRecipeStepFragmentTest {

    private IdlingResource idlingResource;

    private Instrumentation.ActivityResult result;

    private Intent intent;

    private Recipe recipe;

    private List<Ingredient> ingredientList = new ArrayList<>();

    private List<Step> stepList = new ArrayList<>();

    private int stepId;

    @Rule
    public IntentsTestRule<DetailActivity> intentsTestRule =
            new IntentsTestRule<DetailActivity>(DetailActivity.class) {
                @Override
                protected Intent getActivityIntent() {

                    Ingredient ingredient = new Ingredient(2, "CUP", "Graham Cracker crumbs");
                    Step step = new Step(0, "Recipe Introduction", "Recipe Introduction",
                            "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4", null);

                    ingredientList.add(ingredient);
                    stepList.add(step);

                    recipe = new Recipe(1, "Nutella Pie", ingredientList, stepList, 8, null);

                    intent = new Intent();
                    intent.putExtra(recipeKey, recipe);
                    intent.putExtra(ingredientListKey, (ArrayList<? extends Parcelable>) ingredientList);
                    intent.putExtra(DetailRecipeFragment.stepListKey, (ArrayList<? extends Parcelable>) stepList);
                    return intent;
                }
            };

    @Before
    public void openDetailActivity() {

        if (!DetailActivity.twoPane) {
            Fragment fragment = new DetailRecipeFragment();
            intentsTestRule.getActivity()
                    .getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, fragment)
                    .commit();
        } else {
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


    @Test
    public void openDetailActivity_checkIngredientSteps() {

        onView(withId(R.id.detail_recycler_view)).check(matches(isDisplayed()));

        onView(withText("0 Recipe Introduction")).check(matches(isDisplayed()));

        onView(withText("2 CUP Graham Cracker crumbs" + "\n")).check(matches(isDisplayed()));

        onView(withId(R.id.detail_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        if (DetailActivity.twoPane) {
            onView(withId(R.id.constraint_layout_step_tablet_mode)).check(matches(isDisplayed()));
        } else {
            intending(allOf(hasExtra(
                    recipeKey, recipe),
                    hasExtra(ingredientListKey, ingredientList),
                    hasExtra(DetailRecipeFragment.stepListKey, stepList)))
                    .respondWith(result);
        }
    }
}