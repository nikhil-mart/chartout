package com.locolhive.chartout.create;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.locolhive.chartout.R;
import com.locolhive.chartout.api.UploadVideoApi;
import com.locolhive.chartout.api.UploadVideoApi.ProgressListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.PlayerUIController;

public class UploadActivity extends AppCompatActivity { //implements OnInitializedListener

  private static final String TAG = UploadActivity.class.getSimpleName() + " YOYO";
  private static final int RECOVERY_DIALOG_REQUEST = 1;

  Context context;
  UploadVideoApi api;
//  YouTubePlayer player;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_upload);
    context = getApplicationContext();

//    YouTubePlayerSupportFragment fragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtubeFragment);
//    fragment.initialize(getString(R.string.google_api_key), this);
//
//    YouTubePlayerSupportFragment fragment2 = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtubeFragment2);
//    fragment.initialize(getString(R.string.google_api_key), this);

    YouTubePlayerView youtubePlayerView = findViewById(R.id.youtube_player_view);
    getLifecycle().addObserver(youtubePlayerView);
    PlayerUIController playerUIController = youtubePlayerView.getPlayerUIController();
    playerUIController.showFullscreenButton(false);
    playerUIController.showCurrentTime(false);

    youtubePlayerView.initialize(new YouTubePlayerInitListener() {
      @Override
      public void onInitSuccess(@NonNull final YouTubePlayer initializedYouTubePlayer) {
        initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
          @Override
          public void onReady() {
            String videoId = "9Oo0TlprwAQ";
            initializedYouTubePlayer.cueVideo(videoId, 0);
//            initializedYouTubePlayer.play();
//            initializedYouTubePlayer.pause();
//            initializedYouTubePlayer.loadVideo(, 0);
          }
        });
      }
    }, true);

    api = new UploadVideoApi("tempaccnt0001@gmail.com", UploadActivity.this, new ProgressListener() {
      @Override
      public void onComplete(String id) {
//        if (player != null) {
//          player.cueVideo(id);
//        }
//        Toast.makeText(UploadActivity.this, "ID: " + id, Toast.LENGTH_LONG).show();
      }

      @Override
      public void onFailure(String msg) {
        Toast.makeText(UploadActivity.this, msg, Toast.LENGTH_SHORT).show();
      }
    });

  }

  @Override
  protected void onResume() {
    super.onResume();
    findViewById(R.id.upload).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (api != null) {
          api.begin();
        }
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == RECOVERY_DIALOG_REQUEST) {
      // Retry initialization if user performed a recovery action
//      youTubePlayerFragment.initialize(getString(R.string.google_api_key), this);
    } else {
      api.onActivityResult(requestCode, resultCode, data);
    }

  }

//  @Override
//  public void onInitializationSuccess(Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
//    player = youTubePlayer;
//    Log.i(TAG, "onInitializationSuccess: ");
//    if (!wasRestored) {
//      player.setPlayerStyle(PlayerStyle.DEFAULT);
//      player.cueVideo("ZenHpWCoJac");
//      player.cueVideo("Lz-5hKJ2BAg");
//    } else {
//      Log.i(TAG, "onInitializationSuccess: wasRestored");
//    }
//  }
//
//  @Override
//  public void onInitializationFailure(Provider provider, YouTubeInitializationResult errorReason) {
//    if (errorReason.isUserRecoverableError()) {
//      errorReason.getErrorDialog(UploadActivity.this, RECOVERY_DIALOG_REQUEST).show();
//    } else {
//      String errorMessage = String.format(getString(R.string.error_player), errorReason.toString());
//      Toast.makeText(UploadActivity.this, errorMessage, Toast.LENGTH_LONG).show();
//    }
//  }
}
