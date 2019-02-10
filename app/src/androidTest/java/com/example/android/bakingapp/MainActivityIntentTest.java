package com.example.android.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;

import static com.example.android.bakingapp.adapters.MainAdapter.ingredients;
import static com.example.android.bakingapp.adapters.MainAdapter.steps;
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

import com.example.android.bakingapp.activities.MainActivity;
import com.example.android.bakingapp.adapters.MainAdapter;
import com.example.android.bakingapp.fragments.DetailRecipeFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for navigating from the MainActivity to the DetailActivity
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityIntentTest {

    private IdlingResource idlingResource;

    private Instrumentation.ActivityResult result;

    /**
     * IntentsTestRule for the MainActivity
     */
    @Rule
    public final IntentsTestRule<MainActivity> intentsTestRule =
            new IntentsTestRule<>(MainActivity.class);

    /**
     * Register the idling resources before the test
     */
    @Before
    public void registerIdlingResource() {
        idlingResource = intentsTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
        result = new Instrumentation.ActivityResult(Activity.RESULT_OK, null);
    }

    /**
     * Test if clicking on the first item in the RecyclerView triggers the intent to open the
     * DetailActivity that has all the necessary data
     */
    @Test
    public void clickRecyclerViewItem_displaysDetailActivity() {
        onView(withId(R.id.main_recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(0, click()));
        intending(allOf(hasExtra(
                recipeKey, MainAdapter.recipe),
                hasExtra(ingredientListKey, ingredients),
                hasExtra(DetailRecipeFragment.stepListKey, steps)))
                .respondWith(result);
    }

    /**
     * Unregister the idling resources after the test
     */
    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }
}