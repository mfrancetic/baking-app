package com.example.android.bakingapp.utils;

import com.example.android.bakingapp.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface JsonBakingApi {

    @GET
    Call<List<Recipe>> getRecipes(@Url String url);
}
