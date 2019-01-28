package com.example.android.bakingapp.data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.models.Ingredient;

import java.util.List;

import static com.example.android.bakingapp.fragments.DetailRecipeFragment.ingredientsString;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.preferenceIngredients;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.preferenceName;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.preferences;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.recipe;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.recipeNameKey;

public class WidgetRemoteViewsService extends RemoteViewsService {

    SharedPreferences sharedPreferences;

    Context context;


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactory(this.getApplicationContext());
    }

    class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        Context context;

        String recipeName;

        String ingredients;


        public WidgetRemoteViewsFactory(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

            sharedPreferences = context.getSharedPreferences(preferences, Context.MODE_PRIVATE);

            if (sharedPreferences != null) {
                recipeName = sharedPreferences.getString(preferenceName, null);
                ingredientsString = sharedPreferences.getString(preferenceIngredients, null);

                remoteViews.setTextViewText(R.id.widget_recipe_name, recipeName);
                remoteViews.setTextViewText(R.id.widget_recipe_ingredients, ingredients);
                remoteViews.setViewVisibility(R.id.widget_recipe_image, View.GONE);
                remoteViews.setViewVisibility(R.id.widget_recipe_name, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.widget_recipe_ingredients, View.VISIBLE);

                Bundle extras = new Bundle();
                extras.putString(preferenceName, recipeName);
                extras.putString(preferenceIngredients, ingredientsString);

                Intent fillInIntent = new Intent();
                fillInIntent.putExtras(extras);
                remoteViews.setOnClickFillInIntent(R.id.widget_recipe_ingredients, fillInIntent);
                remoteViews.setOnClickFillInIntent(R.id.widget_recipe_name, fillInIntent);
            } else {
                remoteViews.setViewVisibility(R.id.widget_recipe_image, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.widget_recipe_name, View.GONE);
                remoteViews.setViewVisibility(R.id.widget_recipe_ingredients, View.GONE);
            }

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
//            if (recipeName == null) {
            return 0;
//            }
        }

        @Override
        public RemoteViews getViewAt(int position) {
//
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
//
//            sharedPreferences = context.getSharedPreferences(preferences, Context.MODE_PRIVATE);
//            recipeName = sharedPreferences.getString(preferenceName, null);
//            ingredientsString = sharedPreferences.getString(preferenceIngredients, null);
//
//            if (recipeName != null && ingredientsString != null) {
//                remoteViews.setTextViewText(R.id.widget_recipe_name, recipeName);
//                remoteViews.setTextViewText(R.id.widget_recipe_ingredients, ingredients);
//                remoteViews.setViewVisibility(R.id.widget_select_recipe, View.GONE);
//                remoteViews.setViewVisibility(R.id.widget_recipe_image, View.GONE);
//                remoteViews.setViewVisibility(R.id.widget_recipe_name, View.VISIBLE);
//                remoteViews.setViewVisibility(R.id.widget_recipe_ingredients, View.VISIBLE);
//
//                Bundle extras = new Bundle();
//                extras.putString(preferenceName, recipeName);
//                extras.putString(preferenceIngredients, ingredientsString);
//
//                Intent fillInIntent = new Intent();
//                fillInIntent.putExtras(extras);
//                remoteViews.setOnClickFillInIntent(R.id.widget_recipe_ingredients, fillInIntent);
//                remoteViews.setOnClickFillInIntent(R.id.widget_recipe_name, fillInIntent);
//            } else {
//                remoteViews.setViewVisibility(R.id.widget_select_recipe, View.VISIBLE);
//                remoteViews.setViewVisibility(R.id.widget_recipe_image, View.VISIBLE);
//                remoteViews.setViewVisibility(R.id.widget_recipe_name, View.GONE);
//                remoteViews.setViewVisibility(R.id.widget_recipe_ingredients, View.GONE);
//            }


            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}
