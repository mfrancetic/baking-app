package com.example.android.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Ingredient implements Parcelable {

    @SerializedName("quantity")
    private final Double ingredientQuantity;

    @SerializedName("measure")
    private final String ingredientMeasure;

    @SerializedName("ingredient")
    private final String ingredientName;

    public Ingredient(Double ingredientQuantity, String ingredientMeasure, String ingredientName) {
        this.ingredientQuantity = ingredientQuantity;
        this.ingredientMeasure = ingredientMeasure;
        this.ingredientName = ingredientName;
    }

    private Ingredient(Parcel in) {
        ingredientQuantity = in.readDouble();
        ingredientMeasure = in.readString();
        ingredientName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeDouble(ingredientQuantity);
        parcel.writeString(ingredientMeasure);
        parcel.writeString(ingredientName);
    }

    /**
     * Creates and returns a new Ingredient object, as well as a new Ingredient Array
     */
    public static final Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    /**
     * Returns the quantity of the ingredient
     */
    public Double getIngredientQuantity() {
        return ingredientQuantity;
    }

    /**
     * Returns the measure of the ingredient
     */
    public String getIngredientMeasure() {
        return ingredientMeasure;
    }

    /**
     * Returns the name of the ingredient
     */
    public String getIngredientName() {
        return ingredientName;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}