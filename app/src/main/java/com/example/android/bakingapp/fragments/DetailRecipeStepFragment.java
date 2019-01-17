package com.example.android.bakingapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.models.Step;

import java.util.List;

public class DetailRecipeStepFragment extends Fragment {

    private static final String LOG_TAG = DetailRecipeStepFragment.class.getSimpleName();

    private static final String stepKey = "step";

    private static final String stepListKey = "stepList";

    private List<Step> stepList;

    private Step step;

    OnDetailRecipeStepClickListener onDetailRecipeStepClickListener;

    public interface OnDetailRecipeStepClickListener {
        void onDetailRecipeStepSelected();
    }

    public DetailRecipeStepFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail_step, container, false);

        Intent intent = getActivity().getIntent();

        if (intent != null) {
            step = intent.getParcelableExtra(stepKey);
            stepList = intent.getParcelableArrayListExtra(stepListKey);
        }

        Context context = rootView.getContext();


        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onDetailRecipeStepClickListener = (OnDetailRecipeStepClickListener) context;
        } catch (ClassCastException e) {
            throw new RuntimeException(context.toString() + " must implement OnDetailRecipeStepClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onDetailRecipeStepClickListener = null;

    }
}
