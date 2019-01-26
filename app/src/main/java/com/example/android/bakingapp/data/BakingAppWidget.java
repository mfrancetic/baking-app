package com.example.android.bakingapp.data;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activities.DetailActivity;
import com.example.android.bakingapp.activities.MainActivity;
import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Recipe;

import java.util.List;

import static com.example.android.bakingapp.fragments.DetailRecipeFragment.ingredientsString;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.recipe;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.recipeName;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    private static final String recipeNameKey = "recipeName";

    private static final String ingredientsKey = "ingredients";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String recipeName, String ingredientsString) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

//        remoteViews.setTextViewText(R.id.widget_recipe_name, recipeName);
//        remoteViews.setTextViewText(R.id.widget_recipe_ingredients, ingredientsString);

//        remoteViews.setTextViewText(R.id.widget_select_recipe, context.getString(R.string.widget_select_recipe);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        remoteViews.setOnClickPendingIntent(R.id.widget_recipe_image, pendingIntent);

//        StringBuilder stringBuilder = new StringBuilder();
//
//        for (int i = 0; i < ingredientList.size(); i++) {
//            int quantityInt = ingredientList.get(i).getIngredientQuantity();
//            String quantity = String.valueOf(quantityInt);
//            String measure = ingredientList.get(i).getIngredientMeasure();
//            String ingredient = ingredientList.get(i).getIngredientName();
//            String ingredientLine = quantity + " " + measure + " " + ingredient + "\n";
//            stringBuilder.append(ingredientLine);
//        }
//
//        ingredientsString = stringBuilder.toString();
//
//        RemoteViews ingredientsRemoteView = new RemoteViews(context.getPackageName(),
//                R.layout.widget_grid_view_item);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

//        recipeName = recipe.getName();

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipeName, ingredientsString);
        }
    }

    static RemoteViews getRemoteView(Context context, String recipeName, String ingredientsString) {

        Intent intent;

        if (recipeName == null) {
            intent = new Intent(context, MainActivity.class);
        } else {
            intent = new Intent(context, DetailActivity.class);
            intent.putExtra(recipeNameKey, recipeName);
            intent.putExtra(ingredientsKey, ingredientsString);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        remoteViews.setTextViewText(R.id.widget_recipe_name, recipeName);
        remoteViews.setTextViewText(R.id.widget_recipe_ingredients, ingredientsString);

        remoteViews.setOnClickPendingIntent(R.id.widget_recipe_image, pendingIntent);

        return remoteViews;
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

