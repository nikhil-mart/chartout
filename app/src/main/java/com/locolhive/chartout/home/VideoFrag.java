package com.locolhive.chartout.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.locolhive.chartout.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.PlayerUIController;

public class VideoFrag extends Fragment {

  private static final String TAG = VideoFrag.class.getSimpleName() + " YOYO";

  String id;

  public static VideoFrag newInstance(String id) {
    VideoFrag photoFrag = new VideoFrag();
    Bundle bundle = new Bundle();
    bundle.putString("id", id);
    photoFrag.setArguments(bundle);
    return photoFrag;
  }

  public VideoFrag() {
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.view_top_video, container, false);

    Bundle arguments = getArguments();
    if (arguments != null) {
      id = arguments.getString("images");
    }

    YouTubePlayerView youtubePlayerView = view.findViewById(R.id.youtube_player_view);
    getActivity().getLifecycle().addObserver(youtubePlayerView);
    PlayerUIController playerUIController = youtubePlayerView.getPlayerUIController();
    playerUIController.showFullscreenButton(false);
    playerUIController.showCurrentTime(false);

    if (id == null) {
      id = "TYgRF4pzO_M";
    }

    youtubePlayerView.initialize(new YouTubePlayerInitListener() {
      @Override
      public void onInitSuccess(@NonNull final YouTubePlayer initializedYouTubePlayer) {
        initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
          @Override
          public void onReady() {
            initializedYouTubePlayer.cueVideo(id, 0);
          }
        });
      }
    }, true);

    return view;
  }

}
