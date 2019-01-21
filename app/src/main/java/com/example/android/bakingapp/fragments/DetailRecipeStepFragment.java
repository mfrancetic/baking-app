package com.example.android.bakingapp.fragments;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activities.DetailStepActivity;
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
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

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

    private Uri recipeStepUri;

    private static MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;
    private NotificationManager notificationManager;


    private static final String recipeNameKey = "recipeName";

    private String recipeName;

    private Context context;

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


        simpleExoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.player_view);

        instructionTextView = rootView.findViewById(R.id.recipe_step_instructions);

        context = instructionTextView.getContext();

        Intent intent = getActivity().getIntent();

        if (intent != null) {
            stepId = intent.getIntExtra(stepIdKey, 0);
            stepList = intent.getParcelableArrayListExtra(stepListKey);
//            recipeName = intent.getStringExtra(recipeNameKey);
        }

        videoUrl = stepList.get(stepId).getStepVideoUrl();

        initializeMediaSession();

        Uri videoUri = Uri.parse(videoUrl);

//        getActivity().setTitle(recipeName);


        Context context = rootView.getContext();

        description = stepList.get(stepId).getStepDescription();

        instructionTextView.setText(description);

        initializePlayer(videoUri);

        return rootView;
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

//    @Override
//    public void onPause() {
//        super.onPause();
//        releasePlayer();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        releasePlayer();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        initializePlayer(recipeStepUri);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        initializePlayer(recipeStepUri);
//    }



    public static class MediaReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mediaSession, intent);
        }
    }


}
