package com.example.android.bakingapp.fragments;

import android.app.Dialog;
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

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.media.session.MediaButtonReceiver;

import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

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
import com.example.android.bakingapp.data.BakingAppWidget;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
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
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.bakingapp.fragments.DetailRecipeFragment.ingredientsString;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.preferenceIngredients;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.preferenceName;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.preferenceStepId;
import static com.example.android.bakingapp.fragments.DetailRecipeFragment.preferences;

public class DetailRecipeStepFragment extends Fragment implements Player.EventListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = DetailRecipeStepFragment.class.getSimpleName();

    private static final String stepListKey = "stepList";

    static final String stepIdKey = "stepId";

    private List<Step> stepList;

    private String description;

    private Step step;

    static int stepId;

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

    private String recipeName;

    public Recipe recipe;

    @BindView(R.id.thumbnail_image_view)
    ImageView thumbnailImageView;

    private static SharedPreferences sharedPreferences;

    private boolean exoPlayerIsFullScreen = false;

    private View rootView;

    private Context context;

    private boolean twoPane;

    private Dialog fullScreenDialog;

    @BindView(R.id.exo_fullscreen_icon)
    @Nullable
    ImageView fullScreenIcon;

    @BindView(R.id.exo_fullscreen_button)
    @Nullable
    FrameLayout fullScreenButton;

    private OnDetailRecipeStepClickListener onDetailRecipeStepClickListener;

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    /**
     * Set the player state to playing and paused
     */
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

        rootView = inflater.inflate(R.layout.fragment_recipe_detail_step, container, false);

        /* Bind the views with their ID's using the Butterknife library */
        ButterKnife.bind(this, rootView);

        /* If the previousStepButton exists, set the boolean twoPane to true (since it is the
         * tablet mode); otherwise, set it to false*/
        if (previousStepButton == null) {
            twoPane = true;
        } else {
            twoPane = false;
        }

        if (getContext() != null) {
            sharedPreferences = getContext().getSharedPreferences(preferences, Context.MODE_PRIVATE);
        }

        /* If the savedInstanceState exists, get the saved values under their keys */
        if (savedInstanceState != null) {
            savedInstanceState.getInt(stepIdKey, stepId);
            savedInstanceState.getParcelableArrayList(stepListKey);
            savedInstanceState.getString(recipeNameKey);
            savedInstanceState.getParcelable(recipeKey);
            /* If the savedInstanceState doesn't exist, get the values from the intent */
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

        /* If there is no recipe, get the values from the sharedPreferences */
        if (recipe == null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            recipeName = sharedPreferences.getString(preferenceName, null);
            ingredientsString = sharedPreferences.getString(preferenceIngredients, null);
            recipe = DetailRecipeFragment.recipe;
            editor.apply();
        }

        /* Find the playerView and hide the thumbnailImageView */
        simpleExoPlayerView = rootView.findViewById(R.id.player_view);
        thumbnailImageView.setVisibility(View.GONE);

        /* Get the context from the instructionTextView */
        context = instructionTextView.getContext();

        /* Get the stepList and recipeName */
        if (stepList == null) {
            stepList = recipe.getStepList();
        }
        if (recipeName == null) {
            recipeName = recipe.getName();
        }

        /* Set the title of the activity to the recipeName */
        if (getActivity() != null) {
            getActivity().setTitle(recipeName);
        }

        /* Register an OnSharedPreferenceChangeListener */
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        /* Clear the sharedPreferences and add the values under their keys */
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
        editor.putString(preferenceName, recipeName);
        editor.putString(preferenceIngredients, ingredientsString);
        editor.putInt(preferenceStepId, stepId);
        editor.apply();

        /* Update the app widget */
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        Intent widgetIntent = new Intent(context, BakingAppWidget.class);
        widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        /* Send the broadcast to update all the app widget id's */
        int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context.getPackageName(), BakingAppWidget.class.getName()));
        widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(widgetIntent);

        /* Generate the view */
        generateView();

        /* In phone mode, check if one of the buttons is a first or last step, and generate the
         * buttons */
        if (!twoPane) {
            checkIfFirstOrLastButton();
            generateButtons();
        }
        return rootView;
    }

    /**
     * OnDestroyView, clear the sharedPreferences and unregister the onSharedPreferenceChangeListener
     */
    @Override
    public void onDestroyView() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroyView();
    }

    /**
     * Store the values under their keys to the savedInstanceState bundle
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(stepIdKey, stepId);
        outState.putParcelableArrayList(stepListKey, (ArrayList<? extends Parcelable>) stepList);
        outState.putString(recipeNameKey, recipeName);
        outState.putParcelable(recipeKey, recipe);
    }

    /**
     * Generate the view for both the tablet and phone mode
     */
    private void generateView() {
        /* In phone mode, check if one of the buttons is a first or last step */
        if (!twoPane) {
            checkIfFirstOrLastButton();
        }

        releasePlayer();

        /* Get the videoUrl. If it is empty, get the thumbnailUrl */
        videoUrl = stepList.get(stepId).getStepVideoUrl();
        if (videoUrl.isEmpty()) {
            thumbnailUrl = stepList.get(stepId).getStepThumbnailUrl();
        }

        /* Check if the thumbnailUrl exists */
        if (thumbnailUrl != null) {
            /* If the thumbnailUrl contains a video, add the URL string to the videoUrl variable.
             * Otherwise, if it contains an image, parse it and display the image using the Picasso
             * library */
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

        /* Hide the emptyPlayerView and initialize the media sessio */
        emptyPlayerView.setVisibility(View.GONE);
        initializeMediaSession();

        /* Parse the videoUri and initialize the player using that Uri */
        videoUri = Uri.parse(videoUrl);
        initializePlayer(videoUri);

        /* Set the step description to the instructionTextView */
        description = stepList.get(stepId).getStepDescription();
        instructionTextView.setText(description);
    }

    /**
     * In phone mode, check if one of the steps is a first or last one. In that case,
     * disable the previous or next button
     */
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

    /**
     * In phone mode, generate the previous and last button
     */
    private void generateButtons() {
        if (!twoPane && nextStepButton != null && previousStepButton != null) {
            /* Set an OnClickListener to the nextStepButton */
            nextStepButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (stepId < stepList.size()) {
                        stepId++;
                        generateView();
                    }
                }
            });

            /* Set an OnClickListener to the previousStepButton */
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

    /**
     * Initialize a new MediaSession
     */
    private void initializeMediaSession() {
        mediaSession = new MediaSessionCompat(context, LOG_TAG);
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                        | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setMediaButtonReceiver(null);

        /* Create a new stateBuilder with the play, pause and play_pause actions */
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE
                                | PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(new SessionCallback());
        mediaSession.setActive(true);
    }

    /**
     * Initialize the player using the recipeStepUri
     */
    private void initializePlayer(Uri recipeStepUri) {
        if (videoUrl.isEmpty()) {
            /* If there is no videoUrl, hide the simpleExoPlayerView and show the emptyPlayerView and
             * thumbnailImageView */
            emptyPlayerView.setVisibility(View.VISIBLE);
            simpleExoPlayerView.setVisibility(View.INVISIBLE);
            thumbnailImageView.setVisibility(View.VISIBLE);
        } else {
            /* If there is a videoUrl, hide the emptyPlayerView and thumbnailImageView and show the
             * simpleExoPlayerView */
            emptyPlayerView.setVisibility(View.GONE);
            thumbnailImageView.setVisibility(View.GONE);
            simpleExoPlayerView.setVisibility(View.VISIBLE);

            /* Check if the exoPlayer exists */
            if (exoPlayer == null) {
                if (!twoPane) {
                    /* In phone mode, initialize a fullScreenDialog and fullScreenButton*/
                    initFullscreenDialog();
                    initFullscreenButton();
                    int currentOrientation = getResources().getConfiguration().orientation;
                    /* If the phone is in landscape mode, open the fullScreenDialog; otherwise,
                     * close the fullScreenDialog */
                    if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                        openFullscreenDialog();
                    } else {
                        closeFullscreenDialog();
                    }
                }

                /* Create a new DefaultTrackSelector and loadControl*/
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();

                /* Create a new exoPlayer and set it to the simpleExoPlayerView */
                exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
                simpleExoPlayerView.setPlayer(exoPlayer);

                /* Add the listener to the exoPlayer */
                exoPlayer.addListener(this);

                String userAgent = Util.getUserAgent(context, appName);

                /* Create a new mediaSource, and prepare the exoPlayer, setting it to play when
                 * ready */
                MediaSource mediaSource = new ExtractorMediaSource(recipeStepUri, new DefaultDataSourceFactory
                        (context, userAgent), new DefaultExtractorsFactory(), null, null);
                exoPlayer.prepare(mediaSource);
                exoPlayer.setPlayWhenReady(true);
            }
        }
    }

    /**
     * Release the player if it exists
     */
    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    /**
     * OnDestroy, release the player and set the mediaSession to not active
     */
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
            /* Set playWhenReady to true */
            exoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            /* Set playWhenReady to false */
            exoPlayer.setPlayWhenReady(false);
        }
    }

    /**
     * Release the player onPause
     */
    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    /**
     * Release the player OnStop
     */
    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    /**
     * Initialize the player onResume
     */
    @Override
    public void onResume() {
        super.onResume();
        initializePlayer(videoUri);
    }

    /**
     * Initialize the player onStart
     */
    @Override
    public void onStart() {
        super.onStart();
        initializePlayer(videoUri);
    }

    /**
     * Handle the intent onReceive with the MediaReceiver
     */
    public static class MediaReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mediaSession, intent);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (!twoPane && !videoUrl.isEmpty()) {
            int currentOrientation = getResources().getConfiguration().orientation;
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                /* In phone landscape mode, open the fullScreenDialog */
                openFullscreenDialog();
            } else {
                /* In phone portrait mode, close the fullScreenDialog */
                closeFullscreenDialog();
            }
        }
    }

    /**
     * Set the stepId
     */
    public void setStepId(int index) {
        stepId = index;
    }

    /**
     * Set the recipeName
     */
    public void setRecipeName(String name) {
        recipeName = name;
    }

    /**
     * Set the recipe
     */
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    /**
     * Set the stepList
     */
    public void setStepList(List<Step> stepList) {
        this.stepList = stepList;
    }

    /**
     * Initialize the fullScreenDialog
     */
    private void initFullscreenDialog() {
        if (getContext() != null) {
            /* Create a new Dialog */
            fullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
                public void onBackPressed() {
                    /* OnBackPressed, if the exoPlayer is in full screen, close the fullScreenDialog */
                    if (exoPlayerIsFullScreen)
                        closeFullscreenDialog();
                    super.onBackPressed();
                }
            };
        }
    }

    /**
     * Open the fullScreenDialog
     */
    private void openFullscreenDialog() {
        ((ViewGroup) simpleExoPlayerView.getParent()).removeView(simpleExoPlayerView);
        fullScreenDialog.addContentView(simpleExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (fullScreenIcon != null && getContext() != null) {
            /* Set the imageDrawable to the ic_fullscreen_exit */
            fullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen_exit));
        }
        exoPlayerIsFullScreen = true;
        fullScreenDialog.show();
    }

    /**
     * Close the fullScreenDialog
     */
    private void closeFullscreenDialog() {
        ((ViewGroup) simpleExoPlayerView.getParent()).removeView(simpleExoPlayerView);
        playerViewFrame.addView(simpleExoPlayerView);
        exoPlayerIsFullScreen = false;
        fullScreenDialog.dismiss();
        if (fullScreenIcon != null && getContext() != null) {
            /* Set the imageDrawable to the ic_fullscreen_expand */
            fullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen_expand));
        }
    }

    /**
     * Initialize the fullScreenButton
     */
    private void initFullscreenButton() {
        PlayerControlView controlView = simpleExoPlayerView.findViewById(R.id.exo_controller);
        fullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        fullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        if (fullScreenButton != null) {
            /* Set an OnClickListener to the fullScreenButton */
            fullScreenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!exoPlayerIsFullScreen)
                        /* If the exoPlayer is not in the fullScreen, open the fullScreenDialog */
                        openFullscreenDialog();
                    else
                        /* If the exoPlayer is in the fullScreen, close the fullScreenDialog */
                        closeFullscreenDialog();
                }
            });
        }
    }
}