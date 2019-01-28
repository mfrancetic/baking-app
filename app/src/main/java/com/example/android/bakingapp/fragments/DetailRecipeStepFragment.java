package com.example.android.bakingapp.fragments;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activities.DetailStepActivity;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.content.Context.NOTIFICATION_SERVICE;

public class DetailRecipeStepFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String LOG_TAG = DetailRecipeStepFragment.class.getSimpleName();


    private static final String stepListKey = "stepList";

    private static final String stepIdKey = "stepId";

    private List<Step> stepList;

    private String description;

    private Step step;

    private int stepId;

    private TextView instructionTextView;

    private SimpleExoPlayer exoPlayer;

    private String videoUrl;

    private SimpleExoPlayerView simpleExoPlayerView;

    private static Uri videoUri;

    private static MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;
    private NotificationManager notificationManager;

    private Button previousStepButton;

    private Button nextStepButton;

    private TextView emptyPlayerView;

    private static final String recipeNameKey = "recipeName";

    private static final String recipeKey = "recipe";


    public String recipeName;

    public Recipe recipe;


    private Context context;

    private boolean twoPane;

    OnDetailRecipeStepClickListener onDetailRecipeStepClickListener;

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    exoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    exoPlayer.getCurrentPosition(), 1f);
        }
        mediaSession.setPlaybackState(stateBuilder.build());
        showNotification(stateBuilder.build());
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) simpleExoPlayerView.getLayoutParams();
//            params.width = params.MATCH_PARENT;
//            params.height = params.MATCH_PARENT;
//            simpleExoPlayerView.setLayoutParams(params);
//            DetailRecipeStepFragment.this.getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) simpleExoPlayerView.getLayoutParams();
//            params.width = params.MATCH_PARENT;
//            params.height = 600;
//            simpleExoPlayerView.setLayoutParams(params);
//            DetailRecipeStepFragment.this.getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }
//    }


    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

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
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail_step, container, false);

        if (rootView.findViewById(R.id.previous_step_button) == null) {
            twoPane = true;
        } else {
            twoPane = false;
            nextStepButton = rootView.findViewById(R.id.next_step_button);
            previousStepButton = rootView.findViewById(R.id.previous_step_button);
        }

        simpleExoPlayerView = rootView.findViewById(R.id.player_view);

        emptyPlayerView = rootView.findViewById(R.id.empty_exo_player_view);

        instructionTextView = rootView.findViewById(R.id.recipe_step_instructions);

        context = instructionTextView.getContext();

        if (savedInstanceState != null) {
            savedInstanceState.getInt(stepIdKey, stepId);
            savedInstanceState.getParcelableArrayList(stepListKey);
            savedInstanceState.getString(recipeNameKey);
            savedInstanceState.getParcelable(recipeKey);
        }

        if (getActivity() != null) {
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

        if (stepList == null) {
            stepList = recipe.getStepList();
        }

        if (recipeName == null) {
            recipeName = recipe.getName();
        }

        if (getActivity() != null) {
            getActivity().setTitle(recipeName);
        }

        generateView();

        if (!twoPane) {
            checkIfFirstOrLastButton();
            generateButtons();
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(stepIdKey, stepId);
        outState.putParcelableArrayList(stepListKey, (ArrayList<? extends Parcelable>) stepList);
        outState.putString(recipeNameKey, recipeName);
        outState.putParcelable(recipeKey, recipe);
    }

    void generateView() {
        if (!twoPane) {
            checkIfFirstOrLastButton();
        }

        releasePlayer();

        videoUrl = stepList.get(stepId).getStepVideoUrl();

        emptyPlayerView.setVisibility(View.GONE);
        initializeMediaSession();
        videoUri = Uri.parse(videoUrl);
        initializePlayer(videoUri);

        description = stepList.get(stepId).getStepDescription();

        instructionTextView.setText(description);
    }

    void checkIfFirstOrLastButton() {
        if (!twoPane) {
            if (stepId == 0) {
                previousStepButton.setEnabled(false);
                previousStepButton.setBackgroundColor(getResources().getColor(R.color.colorDivider));
            } else {
                previousStepButton.setEnabled(true);
                previousStepButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
            if (stepId == (stepList.size() - 1)) {
                nextStepButton.setEnabled(false);
                nextStepButton.setBackgroundColor(getResources().getColor(R.color.colorDivider));
            } else {
                nextStepButton.setEnabled(true);
                nextStepButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
        }
    }


    private void generateButtons() {

        if (!twoPane) {
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
        } else {
            emptyPlayerView.setVisibility(View.GONE);
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            if (exoPlayer == null) {
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
                simpleExoPlayerView.setPlayer(exoPlayer);

                exoPlayer.addListener(this);

                String userAgent = Util.getUserAgent(context, getString(R.string.app_name));

                MediaSource mediaSource = new ExtractorMediaSource(recipeStepUri, new DefaultDataSourceFactory
                        (context, userAgent), new DefaultExtractorsFactory(), null, null);

                exoPlayer.prepare(mediaSource);

                exoPlayer.setPlayWhenReady(true);
            }
        }
    }


    private void showNotification(PlaybackStateCompat state) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        int icon;
        String play_pause;
        if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {
            icon = R.drawable.exo_controls_pause;
            play_pause = getString(R.string.pause);
        } else {
            icon = R.drawable.exo_controls_play;
            play_pause = getString(R.string.play);
        }

//        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(
//                icon, play_pause, MediaButtonReceiver.buildMediaButtonPendingIntent(context,
//                PlaybackStateCompat.ACTION_PLAY_PAUSE));
//
//        PendingIntent contentPendingIntent = PendingIntent.getActivity(
//                // ckeck Class!
//                context, 0, new Intent(context, DetailStepActivity.class), 0);
//
//        builder.setContentTitle(getString(R.string.recipe_instructions))
//                .setContentText(getString(R.string.notification_text))
//                .setContentIntent(contentPendingIntent)
//                .setSmallIcon(R.drawable.exo_controls_play)
//                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                .addAction(playPauseAction)
//                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
//                        .setMediaSession(mediaSession.getSessionToken())
//                        .setShowActionsInCompactView(0, 1));
//
//        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0, builder.build());

    }

    private void releasePlayer() {
//        notificationManager.cancelAll();
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

        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            playVideoFullScreen();
        }
    }

    void playVideoFullScreen() {
        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
//        simpleExoPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
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


}