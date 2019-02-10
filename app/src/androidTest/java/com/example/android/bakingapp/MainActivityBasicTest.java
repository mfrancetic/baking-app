package com.example.android.bakingapp;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import com.example.android.bakingapp.activities.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for opening and displaying the MainActivity
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityBasicTest {

    private IdlingResource idlingResource;

    /**
     * ActivityTestRule for the MailActivity
     */
    @Rule
    public final ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    /**
     * Register the idling resources
     */
    @Before
    public void registerIdlingResource() {
        idlingResource = activityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
        getInstrumentation().waitForIdleSync();
    }

    /**
     * Test if opening the MainActivity displays the recipe list
     */
    @Test
    public void openMainActivity_displaysRecipeList() {
        onView(withId(R.id.main_recycler_view)).check(matches(isDisplayed()));
        String recipeName = "Nutella Pie";
        onView(withText(recipeName)).check(matches(isDisplayed()));
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