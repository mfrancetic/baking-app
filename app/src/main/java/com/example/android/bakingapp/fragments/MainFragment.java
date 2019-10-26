package com.example.android.bakingapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activities.MainActivity;
import com.example.android.bakingapp.adapters.MainAdapter;
import com.example.android.bakingapp.idlingResource.SimpleIdlingResource;
import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Step;
import com.example.android.bakingapp.utils.JsonBakingApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.QueryMap;


public class MainFragment extends Fragment {

    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    private List<Recipe> recipeList;

    private MainAdapter mainAdapter;

    @BindView(R.id.main_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;

    @BindView(R.id.empty_text_view)
    TextView emptyTextView;

    @BindView(R.id.main_scroll_view)
    @Nullable
    NestedScrollView mainScrollView;

    private static final String SCROLL_POSITION_X = "scrollPositionX";

    private static final String SCROLL_POSITION_Y = "scrollPositionY";

    private int scrollX;

    private int scrollY;

    private final static String recipeListKey = "recipeList";

    @BindView(R.id.constraint_layout_main_tablet_mode)
    @Nullable
    ConstraintLayout constraintLayoutTabletMode;

    private SimpleIdlingResource idlingResource;

    private JsonBakingApi jsonBakingApi;

    public static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public MainFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /* Create a new recipeList */
        recipeList = new ArrayList<>();

        /* Inflate the rootView */
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        /* Bind the views with their ID's using the Butterknife library */
        ButterKnife.bind(this, rootView);

        /* Get the context from the rootView */
        Context context = rootView.getContext();

        /* Get the fragmentActivity */
        final FragmentActivity fragmentActivity = getActivity();

        /* Get the idlingResource */
        getIdlingResource();

        Gson gson = new GsonBuilder().serializeNulls().create();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL + "/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        jsonBakingApi = retrofit.create(JsonBakingApi.class);

        LinearLayoutManager layoutManager;
        if (constraintLayoutTabletMode != null) {
            int spanCount = 3;
            layoutManager = new GridLayoutManager(context, spanCount);
        } else {
            layoutManager = new LinearLayoutManager(fragmentActivity);
        }

        /* Create a new MainAdapter, set the mainAdapter and the LayoutManager to the recyclerView */
        mainAdapter = new MainAdapter(recipeList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mainAdapter);

        /* Make detailRecyclerView not focusable and request focus from the detailScrollView */
        recyclerView.setFocusable(false);
        if (mainScrollView != null) {
            mainScrollView.requestFocus();
        }

        /* Set an OnClickListener to the recyclerView */
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        if (savedInstanceState != null) {
            /* If the savedInstanceState exists, get the values under their keys */
            scrollX = savedInstanceState.getInt(SCROLL_POSITION_X);
            scrollY = savedInstanceState.getInt(SCROLL_POSITION_Y);
            recipeList = savedInstanceState.getParcelableArrayList(recipeListKey);
        } else {
            /* If the savedInstanceState doesn't exist, execute a new RecipeAsyncTask */
            getRecipes();
            mainAdapter.notifyDataSetChanged();
        }
        return rootView;
    }

    /**
     * Get IdlingResource from the MainActivity
     */
    @VisibleForTesting
    private void getIdlingResource() {
        if (getActivity() != null) {
            idlingResource = (SimpleIdlingResource) ((MainActivity) getActivity()).getIdlingResource();
            idlingResource.setIdleState(false);
        }
    }

    /**
     * Populate the recipes with the list of recipes
     */
    private void populateRecipes(List<Recipe> recipes) {
        this.recipeList = recipes;
        mainAdapter.setRecipes(recipeList);
        mainAdapter.notifyDataSetChanged();
        if (mainScrollView != null) {
            /* Scroll to the X and Y position of the mainScrollView*/
            mainScrollView.scrollTo(scrollX, scrollY);
        }
    }

    /**
     * Store the values under their keys to the savedInstanceState bundle
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(recipeListKey, (ArrayList<? extends Parcelable>) recipeList);
        if (mainScrollView != null) {
            scrollX = mainScrollView.getScrollX();
            scrollY = mainScrollView.getScrollY();
        }
        savedInstanceState.putInt(SCROLL_POSITION_X, scrollX);
        savedInstanceState.putInt(SCROLL_POSITION_Y, scrollY);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void getRecipes() {
        Call<List<Recipe>> call = jsonBakingApi.getRecipes(BASE_URL);
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (!response.isSuccessful()) {
                    emptyTextView.setVisibility(View.VISIBLE);
                    String responseCode = getString(R.string.code) + " " + response.code();
                    emptyTextView.setText(responseCode);
                    return;
                }

                List<Recipe> recipes = response.body();
                if (recipes != null && !recipes.isEmpty()) {
                    /* If there are recipes available, hide the emptyTextView and loadingIndicator and
                     * populate the recipes */
                    emptyTextView.setVisibility(View.GONE);
                    loadingIndicator.setVisibility(View.GONE);
                    populateRecipes(recipes);
                } else {
                    /* If there are no recipes, show the emptyTextView and loadingIndicator */
                    emptyTextView.setVisibility(View.VISIBLE);
                    loadingIndicator.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                emptyTextView.setVisibility(View.VISIBLE);
                emptyTextView.setText(t.getMessage());
            }
        });
        idlingResource.setIdleState(true);
    }
}