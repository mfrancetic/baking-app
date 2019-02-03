//package com.example.android.bakingapp;
//
//import android.support.test.espresso.Espresso;
//import android.support.test.espresso.IdlingResource;
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//
//import android.support.test.espresso.IdlingResource;
//
//import com.example.android.bakingapp.activities.MainActivity;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.runner.RunWith;
//
//@RunWith(AndroidJUnit4.class)
//public class IdlingResourceTest {
//
//    @Rule
//    public ActivityTestRule<MainActivity> activityTestRule =
//            new ActivityTestRule<>(MainActivity.class);
//
//    private IdlingResource idlingResource;
//
//    @Before
//    public void registerIdlingResource() {
////        idlingResource = intentsTestRule.getActivity().getIdlingResource();
//        Espresso.registerIdlingResources(idlingResource);
//    }
//
//
//    @After
//    public void unregisterIdlingResource() {
//        if (idlingResource != null) {
//            Espresso.unregisterIdlingResources(idlingResource);
//        }
//    }
//
//
//}
