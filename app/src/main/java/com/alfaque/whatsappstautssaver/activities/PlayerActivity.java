package com.alfaque.whatsappstautssaver.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.alfaque.whatsappstautssaver.R;
import com.alfaque.whatsappstautssaver.databinding.ActivityPlayerBinding;
import com.alfaque.whatsappstautssaver.models.StatusModel;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;

public class PlayerActivity extends AppCompatActivity {
    StatusModel statusModel;
    Gson gson = new Gson();
    SimpleExoPlayer simpleExoPlayer;
    PlayerView playerView;
    ActivityPlayerBinding activityPlayerBinding;
    Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        activityPlayerBinding = DataBindingUtil.setContentView(this, R.layout.activity_player);
        getIntentData();
    }

    private void getIntentData() {

        if (getIntent() != null) {

            String model = getIntent().getStringExtra(MainActivity.STATUS_MODEL);
            statusModel = gson.fromJson(model, StatusModel.class);
            setUpPlayer();

        }

    }

    private void setUpPlayer() {

        videoUri = Uri.parse(statusModel.getFile().getAbsolutePath());
        simpleExoPlayer = new SimpleExoPlayer.Builder(this).build();

        DataSource.Factory factory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this,
                        "WhatsAppStatusSaver"));

        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(factory, extractorsFactory).createMediaSource(videoUri);
        activityPlayerBinding.exoplayer.setPlayer(simpleExoPlayer);
        activityPlayerBinding.exoplayer.setKeepScreenOn(true);
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);


    }

    @Override
    protected void onPause() {
        super.onPause();

        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.getPlaybackState();

    }
}