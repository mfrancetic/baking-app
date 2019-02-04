package com.example.android.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;

import com.example.android.bakingapp.activities.DetailActivity;
import com.example.android.bakingapp.activities.MainActivity;
import com.example.android.bakingapp.fragments.MainFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class MainActivityIntentTest {

    private static final String RECIPE_NAME = "Brownies";

    private MainFragment mainFragment;

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule =
            new IntentsTestRule<>(MainActivity.class);

    private IdlingResource idlingResource;

    private String recipeName = "Brownies";


    @Before
    public void StubAllExternalIntents() {
//        mainFragment = new MainFragment();
//        intentsTestRule.getActivity().getSupportFragmentManager().beginTransaction().add(R.id.main_fragment_container, mainFragment, null).commit();
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));

    }


    @Before
    public void registerIdlingResource() {
//        idlingResource = intentsTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Test
    public void clickRecipeName_OpensDetailActivity() {



//         Intents.init();


//        Intent intent = new Intent(context, DetailActivity.class);
//        intent.putExtra(DetailRecipeFragment.recipeNameKey, RECIPE_NAME);
//        intent.putExtra(MainAdapter.recipeKey, MainAdapter.recipe);
//        intent.putParcelableArrayListExtra(MainAdapter.ingredientListKey, (ArrayList<?extends Parcelable>) MainAdapter.ingredients);
//        intent.putParcelableArrayListExtra(stepListKey, (ArrayList<?extends Parcelable>)MainAdapter.steps);

//        Intent intent = new Intent();
//        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, null);

//        onView(withId(R.id.main_recycler_view))
//                .perform(RecyclerViewActions.actionOnItem(
//                        hasDescendant(withText(RECIPE_NAME)), click()));

//        try {
//            //Delay to have list available for test
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        mainFragment.getView().findViewById(R.id.main_recycler_view);
//        onView(mainFragment.getView().findViewById(R.id.main_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Context targetContext = getInstrumentation().getTargetContext();

        getInstrumentation().waitForIdleSync();

        onView(ViewMatchers.withId(R.id.main_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        getInstrumentation().waitForIdleSync();

        intended(hasComponent(DetailActivity.class.getName()));

//        try {
//            //Delay to have list available for test
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        intended(hasComponent(DetailActivity.class.getName()));

//        Intents.intended(IntentMatchers.hasComponent(new ComponentName(getTargetContext(), DetailActivity.class)));


//        intended(allOf(
//                hasExtra("recipeName", "Brownies"),
//                hasExtra("recipeId", 1),
//                hasExtra("introStepId", 100)
//        ));

//        Instrumentation.ActivityResult activityResult = new Instrumentation.ActivityResult(
//                Activity.RESULT_OK, new Intent()
//        );
//
//        Intents.init();

//        intended(toPackage("com.example.android.bakingapp.activities.detailactivity")
//        );


//        intended(
////                hasExtra("recipeName", recipeName),
//                hasComponent(" com.example.android.bakingapp.activities.DetailActivity")
//                );


//        onView(withId(R.id.main_recycler_view)).perform(RecyclerViewActions.actionO
// nItem
//                (hasDescendant(withText(RECIPE_NAME)), click()));

//        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        String detailActivityClassName = DetailActivity.class.getName();

//        activityTestRule.launchActivity(new Intent());
//        Intents.init();
//        intended((hasComponent((detailActivityClassName))));

//        intended(allOf(
//                hasExtra("recipeName", recipeName)));


//        intended(hasComponent(new ComponentName(getTargetContext(), DetailActivity.class)));
//        Intents.release();
        //        intended(hasExtraWithKey(MainAdapter.recipeKey));

//        Intents.init();
//        Matcher<Intent> intent = hasAction(Intent.
//        activityTestRule.launchActivity(new Intent());


//        intended(hasComponent(hasClassName(DetailActivity.class.getName())));

//        intended(intent.);
//        activityTestRule.launchActivity(intent);

//        String detailActivity = DetailActivity.class.getName();
//
//        intended(hasComponent(detailActivity));
//        intended(hasComponent(new ComponentName(getTargetContext(), DetailActivity.class)));
//        Intents.release();

//        onView(withId(R.id.detail_recycler_view)).check(matches(isDisplayed()));
//        onView(withId(R.id.ingredients_text_view)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

//    @After
//    public void tearDown() throws Exception{
//        Intents.release();
//    }
}