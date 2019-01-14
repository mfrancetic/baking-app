package com.example.android.bakingapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingapp.R;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    // @BindView (R.id.title_text_view) TextViw mTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

    }
}