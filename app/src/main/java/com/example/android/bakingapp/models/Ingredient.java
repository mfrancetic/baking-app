package com.example.android.bakingapp.models;

public class Ingredient {

    private int ingredientQantity;

    private String ingredientMeasure;

    private String ingredientName;


    Ingredient(int ingredientQantity, String ingredientMeasure, String ingredientName) {
        this.ingredientQantity = ingredientQantity;
        this.ingredientMeasure = ingredientMeasure;
        this.ingredientName = ingredientName;
    }

    public int getIngredientQantity() {
        return ingredientQantity;
    }

    public String getIngredientMeasure() {
        return ingredientMeasure;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientMeasure(String ingredientMeasure) {
        this.ingredientMeasure = ingredientMeasure;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public void setIngredientQantity(int ingredientQantity) {
        this.ingredientQantity = ingredientQantity;
    }
}
