package com.example.android.bakingapp.fragments;

import android.app.Dialog;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.media.session.MediaButtonReceiver;

import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activities.MainActivity;
import com.example.android.bakingapp.data.BakingAppWidget;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.bakingapp.fragments.DetailRecipeFragment.ingredientsString;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.preferenceId;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.preferenceIngredients;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.preferenceName;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.preferenceStepId;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.preferences;

public class DetailRecipeStepFragment extends Fragment implements Player.EventListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = DetailRecipeStepFragment.class.getSimpleName();


    private static final String stepListKey = "stepList";

    private static final String stepIdKey = "stepId";

    private List<Step> stepList;

    private String description;

    private Step step;

    private int stepId;

    @BindView(R.id.recipe_step_instructions)
    TextView instructionTextView;

    SimpleExoPlayer exoPlayer;

    @BindView(R.id.player_view_frame)
    FrameLayout playerViewFrame;

    private String videoUrl;

    private String thumbnailUrl;

    private PlayerView simpleExoPlayerView;

    private static Uri videoUri;

    private static MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;
    private NotificationManager notificationManager;

    @BindView(R.id.previous_step_button)
    @Nullable
    Button previousStepButton;

    @BindView(R.id.next_step_button)
    @Nullable
    Button nextStepButton;

    @BindColor(R.color.colorAccent)
    int colorAccent;

    @BindColor(R.color.colorDivider)
    int colorDivider;

    @BindView(R.id.empty_exo_player_view)
    TextView emptyPlayerView;

    private static final String recipeNameKey = "recipeName";

    private static final String recipeKey = "recipe";

    @BindString(R.string.app_name)
    String appName;


    public String recipeName;

    public Recipe recipe;

    @BindView(R.id.thumbnail_image_view)
    ImageView thumbnailImageView;

    public static SharedPreferences sharedPreferences;


    private boolean exoPlayerIsFullScreen = false;


    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";

    private View rootView;

    private Context context;

    private boolean twoPane;

    private Dialog fullScreenDialog;

    @BindView(R.id.exo_fullscreen_icon)
    ImageView fullScreenIcon;

    @BindView(R.id.exo_fullscreen_button)
    FrameLayout fullScreenButton;

    private int resumeWindow;

    private long resumePosition;


    OnDetailRecipeStepClickListener onDetailRecipeStepClickListener;

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == Player.STATE_READY) && playWhenReady) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    exoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == Player.STATE_READY)) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    exoPlayer.getCurrentPosition(), 1f);
        }
        mediaSession.setPlaybackState(stateBuilder.build());
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    public interface OnDetailRecipeStepClickListener {
        void onDetailRecipeStepSelected();
    }

    public DetailRecipeStepFragment() {
        super();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ButterKnife.bind(this, rootView);

        if (previousStepButton == null) {
            twoPane = true;
        } else {
            twoPane = false;
        }

        if (getContext() != null) {
            sharedPreferences = getContext().getSharedPreferences(preferences, Context.MODE_PRIVATE);
        }

        if (savedInstanceState != null) {
            savedInstanceState.getInt(stepIdKey, stepId);
            savedInstanceState.getParcelableArrayList(stepListKey);
            savedInstanceState.getString(recipeNameKey);
            savedInstanceState.getParcelable(recipeKey);
        } else if (getActivity() != null) {
            Intent intent = getActivity().getIntent();
            if (intent != null) {
                recipe = intent.getParcelableExtra(recipeKey);
                if (!twoPane) {
                    stepId = intent.getIntExtra(stepIdKey, 0);
                }
                stepList = intent.getParcelableArrayListExtra(stepListKey);
                recipeName = intent.getStringExtra(recipeNameKey);
            }
        }
//
//        if (recipe == null) {
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.clear().apply();
//
//            editor.putString(preferenceName, recipeName);
//            editor.putString(preferenceIngredients, ingredientsString);
//            editor.putInt(preferenceStepId, stepId);
//            editor.apply();
//
//            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
//
//            Intent widgetIntent = new Intent(getContext(), BakingAppWidget.class);
//            widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//
//            int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(getContext().getPackageName(), BakingAppWidget.class.getName()));
//
//            widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
//            getContext().sendBroadcast(widgetIntent);
//
//            Intent intent = new Intent(getContext(), MainActivity.class);
//            startActivity(intent);
//        }

        rootView = inflater.inflate(R.layout.fragment_recipe_detail_step, container, false);


        simpleExoPlayerView = (PlayerView) rootView.findViewById(R.id.player_view);

        thumbnailImageView.setVisibility(View.GONE);

        context = instructionTextView.getContext();


        if (stepList == null) {
            stepList = recipe.getStepList();
        }

        if (recipeName == null) {
            recipeName = recipe.getName();
        }

        if (getActivity() != null) {
            getActivity().setTitle(recipeName);
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();

        editor.putString(preferenceName, recipeName);
        editor.putString(preferenceIngredients, ingredientsString);
        editor.putInt(preferenceStepId, stepId);
        editor.apply();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        Intent widgetIntent = new Intent(context, BakingAppWidget.class);
        widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context.getPackageName(), BakingAppWidget.class.getName()));

        widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(widgetIntent);


        generateView();

        if (!twoPane) {
            checkIfFirstOrLastButton();
            generateButtons();
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();

        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(stepIdKey, stepId);
        outState.putParcelableArrayList(stepListKey, (ArrayList<? extends Parcelable>) stepList);
        outState.putString(recipeNameKey, recipeName);
        outState.putParcelable(recipeKey, recipe);
    }

    private void generateView() {
        if (!twoPane) {
            checkIfFirstOrLastButton();
        }

        releasePlayer();

        videoUrl = stepList.get(stepId).getStepVideoUrl();
        if (videoUrl.isEmpty()) {
            thumbnailUrl = stepList.get(stepId).getStepThumbnailUrl();
        }

        if (thumbnailUrl != null) {
            if (thumbnailUrl.contains(".mp4")) {
                videoUrl = thumbnailUrl;
            } else if (thumbnailUrl.contains(".jpeg") || thumbnailUrl.contains(".jpg") || thumbnailUrl.contains(".png")) {
                Uri thumbnailUri = Uri.parse(thumbnailUrl);

                com.squareup.picasso.Picasso
                        .get()
                        .load(thumbnailUri)
                        .into(thumbnailImageView);
                thumbnailImageView.setVisibility(View.VISIBLE);
            }
        }

        emptyPlayerView.setVisibility(View.GONE);
        initializeMediaSession();
        videoUri = Uri.parse(videoUrl);
        initializePlayer(videoUri);

        description = stepList.get(stepId).getStepDescription();

        instructionTextView.setText(description);
    }

    private void checkIfFirstOrLastButton() {
        if (!twoPane && previousStepButton != null && nextStepButton != null) {
            if (stepId == 0) {
                previousStepButton.setEnabled(false);
                previousStepButton.setBackgroundColor(colorDivider);
            } else {
                previousStepButton.setEnabled(true);
                previousStepButton.setBackgroundColor(colorAccent);
            }
            if (stepId == (stepList.size() - 1)) {
                nextStepButton.setEnabled(false);
                nextStepButton.setBackgroundColor(colorDivider);
            } else {
                nextStepButton.setEnabled(true);
                nextStepButton.setBackgroundColor(colorAccent);
            }
        }
    }


    private void generateButtons() {
        if (!twoPane && nextStepButton != null && previousStepButton != null) {
            nextStepButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (stepId < stepList.size()) {
                        stepId++;
                        generateView();
                    }
                }
            });

            previousStepButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (stepId > 0) {
                        stepId--;
                        generateView();
                    }
                }
            });
        }
    }

    private void initializeMediaSession() {

        mediaSession = new MediaSessionCompat(context, LOG_TAG);

        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                        | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mediaSession.setMediaButtonReceiver(null);

        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE
                                | PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSession.setPlaybackState(stateBuilder.build());

        mediaSession.setCallback(new SessionCallback());

        mediaSession.setActive(true);

    }

    private void initializePlayer(Uri recipeStepUri) {

        if (videoUrl.isEmpty()) {
            emptyPlayerView.setVisibility(View.VISIBLE);
            simpleExoPlayerView.setVisibility(View.INVISIBLE);
            thumbnailImageView.setVisibility(View.VISIBLE);
        } else {
            emptyPlayerView.setVisibility(View.GONE);
            thumbnailImageView.setVisibility(View.GONE);
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            if (exoPlayer == null) {
                if (!twoPane) {
                    initFullscreenDialog();
                    initFullscreenButton();
                    int currentOrientation = getResources().getConfiguration().orientation;
                    if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                        openFullscreenDialog();
                    } else {
                        closeFullscreenDialog();
                    }
                }
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
                simpleExoPlayerView.setPlayer(exoPlayer);

                exoPlayer.addListener(this);

                String userAgent = Util.getUserAgent(context, appName);

                MediaSource mediaSource = new ExtractorMediaSource(recipeStepUri, new DefaultDataSourceFactory
                        (context, userAgent), new DefaultExtractorsFactory(), null, null);

                exoPlayer.prepare(mediaSource);

                exoPlayer.setPlayWhenReady(true);
            }
        }
    }


    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mediaSession.setActive(false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
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

    private class SessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            exoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            exoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer(videoUri);
    }

    @Override
    public void onStart() {
        super.onStart();
        initializePlayer(videoUri);
    }


    public static class MediaReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mediaSession, intent);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (!twoPane && !videoUrl.isEmpty()) {
            int currentOrientation = getResources().getConfiguration().orientation;
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                openFullscreenDialog();
            } else {
                closeFullscreenDialog();
            }
        }
    }

    public void setStepId(int index) {
        stepId = index;
    }

    public void setRecipeName(String name) {
        recipeName = name;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setStepList(List<Step> stepList) {
        this.stepList = stepList;
    }

    private void initFullscreenDialog() {

        fullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (exoPlayerIsFullScreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }

    private void openFullscreenDialog() {

        ((ViewGroup) simpleExoPlayerView.getParent()).removeView(simpleExoPlayerView);
        fullScreenDialog.addContentView(simpleExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        fullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen_exit));
        exoPlayerIsFullScreen = true;
        fullScreenDialog.show();
    }

    private void closeFullscreenDialog() {
        ((ViewGroup) simpleExoPlayerView.getParent()).removeView(simpleExoPlayerView);
        playerViewFrame.addView(simpleExoPlayerView);
        exoPlayerIsFullScreen = false;
        fullScreenDialog.dismiss();
        fullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen_expand));
    }

    private void initFullscreenButton() {
        PlayerControlView controlView = simpleExoPlayerView.findViewById(R.id.exo_controller);
        fullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        fullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        fullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!exoPlayerIsFullScreen)
                    openFullscreenDialog();
                else
                    closeFullscreenDialog();
            }
        });
    }
}
