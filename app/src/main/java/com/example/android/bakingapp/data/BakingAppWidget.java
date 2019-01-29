package com.example.android.bakingapp.data;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activities.DetailActivity;
import com.example.android.bakingapp.activities.MainActivity;
import com.example.android.bakingapp.fragments.DetailRecipeFragment;
import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.DESedeKeySpec;

import static com.example.android.bakingapp.fragments.DetailRecipeFragment.ingredientsString;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.preferenceId;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.preferenceIngredients;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.preferenceName;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.preferences;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.recipe;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.recipeKey;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.recipeName;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.sharedPreferences;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.stepList;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.stepListKey;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    private static final String recipeNameKey = "recipeName";

    private static final String ingredientsKey = "ingredients";

    private static RemoteViews remoteViews;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        if (sharedPreferences!= null) {
            Log.i("prefId -widgetUpdate", String.valueOf(sharedPreferences.getInt(preferenceId, 0)));
            Log.i("prefName-widgetUpdate", String.valueOf(sharedPreferences.getString(preferenceName, "default")));
            Log.i("prefIngred-widgetUpdate", String.valueOf(sharedPreferences.getString(preferenceIngredients, "default")));
        }

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        if (sharedPreferences != null) {
            Intent intent = new Intent(context, DetailActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            String recipeName = sharedPreferences.getString(preferenceName, null);
            String ingredientsString = sharedPreferences.getString(preferenceIngredients, null);
            remoteViews.setTextViewText(R.id.widget_recipe_name, recipeName);
            remoteViews.setTextViewText(R.id.widget_recipe_ingredients, ingredientsString);
            remoteViews.setViewVisibility(R.id.widget_recipe_image, View.GONE);
            remoteViews.setViewVisibility(R.id.widget_recipe_name, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.widget_recipe_ingredients, View.VISIBLE);

            intent.putExtra(recipeNameKey, recipeName);
            intent.putExtra(recipeKey, recipe);
            intent.putParcelableArrayListExtra(stepListKey, (ArrayList<? extends Parcelable>) stepList);
            intent.putExtra(ingredientsKey, ingredientsString);

            remoteViews.setOnClickPendingIntent(R.id.widget_relative_layout, pendingIntent);
        } else {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            remoteViews.setViewVisibility(R.id.widget_recipe_image, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.widget_recipe_ingredients, View.GONE);
            remoteViews.setViewVisibility(R.id.widget_recipe_name, View.GONE);

            remoteViews.setOnClickPendingIntent(R.id.widget_relative_layout, pendingIntent);
        }
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction() != null) {
            if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                ComponentName appWidget = new ComponentName(context.getPackageName(), BakingAppWidget.class.getName());
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(appWidget);

                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_recipe_image);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_recipe_name);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_recipe_ingredients);
//                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_relative_layout);
//                }
            }
        }
    }
}