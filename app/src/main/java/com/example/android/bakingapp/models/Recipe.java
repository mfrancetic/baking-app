package com.example.android.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {

    private int id;

    private String name;

    private List<Ingredient> ingredientList;

    private List<Step> stepList;

    private int servings;

    private String image;

    public Recipe(int id, String name, List<Ingredient> ingredientList, List<Step> stepList,
                  int servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredientList = ingredientList;
        this.stepList = stepList;
        this.servings = servings;
        this.image = image;
    }

    private Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        ingredientList = new ArrayList<>();
        in.readList(ingredientList, Ingredient.class.getClassLoader());
        stepList = new ArrayList<>();
        in.readList(stepList, Step.class.getClassLoader());
        servings = in.readInt();
        image = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeList(ingredientList);
        parcel.writeList(stepList);
        parcel.writeInt(servings);
        parcel.writeString(image);
    }

    /**
     * Creates and returns a new Recipe object, as well as a new Recipe Array
     */
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    /**
     * Returns the id of the recipe
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the name of the recipe
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the ingredient list of the recipe
     */
    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    /**
     * Returns the stepList of the recipe
     */
    public List<Step> getStepList() {
        return stepList;
    }

    /**
     * Returns the image of the recipe
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets the id of the recipe
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the name of the recipe
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the ingredientList of the recipe
     */
    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    /**
     * Sets the stepList of the recipe
     */
    public void setStepList(List<Step> stepList) {
        this.stepList = stepList;
    }

    /**
     * Sets the servings of the recipe
     */
    public void setServings(int servings) {
        this.servings = servings;
    }

    /**
     * Sets the image of the recipe
     */
    public void setImage(String image) {
        this.image = image;
    }
}
