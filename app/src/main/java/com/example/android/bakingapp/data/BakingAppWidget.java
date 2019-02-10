package com.example.android.bakingapp.data;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.View;
import android.widget.RemoteViews;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activities.DetailActivity;
import com.example.android.bakingapp.activities.MainActivity;

import java.util.ArrayList;

import static com.example.android.bakingapp.fragments.DetailRecipeFragment.preferenceIngredients;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.preferenceName;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.preferenceStepId;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.recipe;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.recipeKey;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.sharedPreferences;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.stepList;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.stepListKey;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    private static final String recipeNameKey = "recipeName";

    private static final String ingredientsKey = "ingredients";

    private static final String stepIdKey = "stepId";

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {

        /* Create a new RemoteViews variable with the baking_app_widget layout */
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        if (sharedPreferences != null) {
            /* If there are sharedPreferences available, create an intent to launch the detail activity */
            Intent intent = new Intent(context, DetailActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            /* Get the recipe name, ingredients and step id from the sharedPreferences */
            String recipeName = sharedPreferences.getString(preferenceName, null);
            String ingredientsString = sharedPreferences.getString(preferenceIngredients, null);
            int stepId = sharedPreferences.getInt(preferenceStepId, 0);

            /* Set the recipe name and ingredients to the appropriate TextViews and hide the recipe
            * image */
            remoteViews.setTextViewText(R.id.widget_recipe_name, recipeName);
            remoteViews.setTextViewText(R.id.widget_recipe_ingredients, ingredientsString);
            remoteViews.setViewVisibility(R.id.widget_recipe_image, View.GONE);
            remoteViews.setViewVisibility(R.id.widget_recipe_name, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.widget_recipe_ingredients, View.VISIBLE);

            /* Put the recipe, recipe name, step list, step id and ingredients to the intent */
            intent.putExtra(recipeNameKey, recipeName);
            intent.putExtra(recipeKey, recipe);
            intent.putParcelableArrayListExtra(stepListKey, (ArrayList<? extends Parcelable>) stepList);
            intent.putExtra(stepIdKey, stepId);
            intent.putExtra(ingredientsKey, ingredientsString);

            /* Set an OnClickPendingIntent to the widget */
            remoteViews.setOnClickPendingIntent(R.id.widget_relative_layout, pendingIntent);
        } else {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            remoteViews.setViewVisibility(R.id.widget_recipe_image, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.widget_recipe_ingredients, View.GONE);
            remoteViews.setViewVisibility(R.id.widget_recipe_name, View.GONE);

            remoteViews.setOnClickPendingIntent(R.id.widget_recipe_image, pendingIntent);
        }
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        /* In case there are multiple widgets active, update all of them */
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        /* If there is an action, update the app widget and notify it that the data has changed */
        if (intent.getAction() != null) {
            if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                ComponentName appWidget = new ComponentName(context.getPackageName(), BakingAppWidget.class.getName());
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(appWidget);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_recipe_image);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_recipe_name);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_recipe_ingredients);
            }
        }
    }
}