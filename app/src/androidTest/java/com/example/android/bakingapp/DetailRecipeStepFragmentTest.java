package com.example.android.bakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.activities.DetailStepActivity;

import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DetailRecipeStepFragmentTest {

    @Rule
    ActivityTestRule<DetailStepActivity> activityTestRule =
            new ActivityTestRule<>(DetailStepActivity.class);


}
