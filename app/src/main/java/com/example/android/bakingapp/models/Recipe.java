package com.example.android.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Recipe implements Parcelable {

    private int id;

    private String name;

    private List<Ingredient> ingredientList;

    private List<Step> stepList;

    private int servings;

    private String image;

    Recipe(int id, String name, List<Ingredient> ingredientList, List<Step> stepList,
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
        ingredientList = in.readArrayList(ClassLoader.getSystemClassLoader());
        stepList = in.readArrayList(ClassLoader.getSystemClassLoader());
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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public List<Step> getStepList() {
        return stepList;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public void setStepList(List<Step> stepList) {
        this.stepList = stepList;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
