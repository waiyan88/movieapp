package wy.chris.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class ViewMovieActivity extends AppCompatActivity {

    public static String videolink;
    ProgressBar progressBar;
    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_movie);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        exoPlayerView=findViewById(R.id.exoplayer);
        progressBar=findViewById(R.id.progress_bar);
        TrackSelector trackSelector=new DefaultTrackSelector();
        exoPlayer= ExoPlayerFactory.newSimpleInstance(getApplicationContext(),trackSelector);
        exoPlayerView.setPlayer(exoPlayer);

        Uri uri=Uri.parse(videolink);
        DefaultHttpDataSourceFactory defaultHttpDataSourceFactory=new DefaultHttpDataSourceFactory("exo player");
        ExtractorsFactory extractorsFactory=new DefaultExtractorsFactory();
        MediaSource mediaSource=new ExtractorMediaSource(
                uri,
                defaultHttpDataSourceFactory,

                extractorsFactory,
                null,
                null
        );

        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);
        SimpleExoPlayer.EventListener eventListener=new SimpleExoPlayer.EventListener(){
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

                if(playbackState==SimpleExoPlayer.STATE_BUFFERING)
                {
                    progressBar.setVisibility(View.VISIBLE);
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity() {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }
        };
        exoPlayer.addListener(eventListener);

    }

    @Override
    public void onBackPressed() {
        exoPlayer.stop();
        super.onBackPressed();
    }
}
