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
import android.view.View;
import android.widget.RemoteViews;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activities.DetailActivity;
import com.example.android.bakingapp.activities.MainActivity;
import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.DESedeKeySpec;

import static com.example.android.bakingapp.fragments.DetailRecipeFragment.ingredientsString;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.preferenceIngredients;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.preferenceName;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.preferences;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.recipe;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.recipeKey;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.recipeName;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.stepList;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.stepListKey;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    private static SharedPreferences sharedPreferences;

    private static final String recipeNameKey = "recipeName";

    private static final String ingredientsKey = "ingredients";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        sharedPreferences = context.getSharedPreferences(preferences, Context.MODE_PRIVATE);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        if (sharedPreferences != null) {
            Intent intent = new Intent(context, DetailActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            recipeName = sharedPreferences.getString(preferenceName, null);
            ingredientsString = sharedPreferences.getString(preferenceIngredients, null);
            remoteViews.setTextViewText(R.id.widget_recipe_name, recipeName);
            remoteViews.setTextViewText(R.id.widget_recipe_ingredients, ingredientsString);
            remoteViews.setViewVisibility(R.id.widget_recipe_image, View.GONE);
            remoteViews.setViewVisibility(R.id.widget_recipe_name, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.widget_recipe_ingredients, View.VISIBLE);

            intent.putExtra(recipeNameKey, recipeName);
            intent.putExtra(recipeKey, recipe);
            intent.putParcelableArrayListExtra(stepListKey, (ArrayList<? extends Parcelable>) stepList);
            intent.putExtra(ingredientsKey, ingredientsString);
            remoteViews.setOnClickPendingIntent(R.id.widget_recipe_name, pendingIntent);
        } else {
            remoteViews.setViewVisibility(R.id.widget_recipe_image, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.widget_recipe_ingredients, View.GONE);
            remoteViews.setViewVisibility(R.id.widget_recipe_name, View.GONE);
//            Intent intent = new Intent(context, MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//
//            remoteViews.setViewVisibility(R.id.widget_recipe_image, View.VISIBLE);
//            remoteViews.setViewVisibility(R.id.widget_select_recipe, View.VISIBLE);
//            remoteViews.setViewVisibility(R.id.widget_recipe_name, View.GONE);
//            remoteViews.setViewVisibility(R.id.widget_recipe_ingredients, View.GONE);
//
//            remoteViews.setOnClickPendingIntent(R.id.widget_recipe_image, pendingIntent);
//
//            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

//            detailActivityIntent.addCategory(Intent.ACTION_MAIN);
//            detailActivityIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//            detailActivityIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//            PendingIntent getRecipeDetailsPendingIntent = PendingIntent.getActivity(context, 0, detailActivityIntent,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//            remoteViews.setPendingIntentTemplate(R.id.widget_recipe_name, getRecipeDetailsPendingIntent);

//            Intent getRecipeDetailsServiceIntent = new Intent(context, WidgetIntentService.class);
//            remoteViews.setRemoteAdapter(R.id.widget_recipe_name, getRecipeDetailsServiceIntent);
        }
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

//            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

//            if (sharedPreferences != null) {
//                remoteViews.setTextViewText(R.id.widget_recipe_name, recipeName);
//                remoteViews.setTextViewText(R.id.widget_recipe_ingredients, ingredientsString);
//
//                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_recipe_name);
//                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_recipe_ingredients);
//            } else {
//                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_recipe_image);
//                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_select_recipe);
//            }
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

//        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

//    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
//                                           int[] appWidgetIds, String recipeName, String ingredients) {
//
//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
//        }
//    }

//    static RemoteViews getRemoteView(Context context, String recipeName, String ingredientsString) {
//
//        Intent intent;
//
//        if (recipeName == null) {
//            intent = new Intent(context, MainActivity.class);
//        } else {
//            intent = new Intent(context, DetailActivity.class);
//            intent.putExtra(recipeNameKey, recipeName);
//            intent.putExtra(ingredientsKey, ingredientsString);
//        }
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
//        remoteViews.setTextViewText(R.id.widget_recipe_name, recipeName);
//        remoteViews.setTextViewText(R.id.widget_recipe_ingredients, ingredientsString);
//
//        remoteViews.setOnClickPendingIntent(R.id.widget_recipe_image, pendingIntent);
//
//        return remoteViews;
//    }

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
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_recipe_name);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_recipe_ingredients);
            }
        }
    }
}