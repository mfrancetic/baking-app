package com.example.android.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;

import static com.example.android.bakingapp.adapters.MainAdapter.ingredients;
import static com.example.android.bakingapp.adapters.MainAdapter.stepListKey;
import static com.example.android.bakingapp.adapters.MainAdapter.steps;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.ingredientListKey;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.ingredientsString;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.recipeKey;
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
import com.example.android.bakingapp.activities.MainActivity;
import com.example.android.bakingapp.adapters.MainAdapter;
import com.example.android.bakingapp.fragments.DetailRecipeFragment;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class MainActivityIntentTest {

    private IdlingResource idlingResource;

    private Instrumentation.ActivityResult result;

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule =
            new IntentsTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        idlingResource = intentsTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);

        result = new Instrumentation.ActivityResult(Activity.RESULT_OK, null);
    }

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

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }
}