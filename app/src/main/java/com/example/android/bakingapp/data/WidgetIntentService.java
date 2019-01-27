//package com.example.android.bakingapp.data;
//
//import android.app.IntentService;
//import android.content.Context;
//import android.content.Intent;
//
//public class WidgetIntentService extends IntentService {
//
////    public static final String ACTION_GET_RECIPE_NAME = "com.example.android.bakingapp.action.get_recipe_name";
//
//    public static final String ACTION_GET_RECIPE_DETAILS = "com.example.android.bakingapp.action.get_recipe_details";
//
//    private static final String recipeNameKey = "recipeName";
//
//    private static final String ingredientsKey = "ingredients";
//
////    public static final String ACTION_GET_RECIPE_INGREDIENTS = "com.example.android.bakingapp.action.get_recipe_ingredients";
//
//    /**
//     * Creates an IntentService.  Invoked by your subclass's constructor.
//     */
//    public WidgetIntentService() {
//        super("WidgetIntentService");
//    }
//
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
////        if (intent != null) {
////            final String action = intent.getAction();
////            if (ACTION_GET_RECIPE_NAME.equals(action)) {
////                handleActionGetRecipeName();
////            } else if (ACTION_GET_RECIPE_INGREDIENTS.equals(action)) {
////                handleActionGetIngredients();
////            }
////        }
//        String recipeName = intent.getExtras().getString(recipeNameKey);
//        String ingredients = intent.getExtras().getString(ingredientsKey);
//        handleActionGetRecipeDetails(recipeName, ingredients);
//
//    }
//
////    public static void startActionGetRecipeName(Context context, String recipeName) {
////        Intent intent = new Intent(context, WidgetIntentService.class);
////        intent.setAction(ACTION_GET_RECIPE_NAME);
////        intent.putExtra(recipeNameKey, recipeName);
////        context.startService(intent);
////    }
//
//    public static void startActionGetRecipeDetails(Context context, String recipeName, String ingredients) {
//        Intent intent = new Intent(context, WidgetIntentService.class);
////        intent.setAction(ACTION_GET_RECIPE_INGREDIENTS);
//        intent.putExtra(recipeNameKey, recipeName);
//        intent.putExtra(ingredientsKey, ingredients);
//        context.startService(intent);
//    }
//
//    private void handleActionGetRecipeDetails(String recipeName, String ingredients) {
//        Intent intent = new Intent(ACTION_GET_RECIPE_DETAILS);
//        intent.setAction(ACTION_GET_RECIPE_DETAILS);
//        intent.putExtra(ingredientsKey, ingredients);
//        intent.putExtra(recipeNameKey, recipeName);
//        sendBroadcast(intent);
//    }
//
////    private void handleActionGetIngredients(String ingredients) {
////        Intent intent = new Intent(ACTION_GET_RECIPE_INGREDIENTS);
////        intent.setAction(ACTION_GET_RECIPE_INGREDIENTS);
////        intent.putExtra(ingredientsKey, ingredients);
////        sendBroadcast(intent);
////    }
//
//}
