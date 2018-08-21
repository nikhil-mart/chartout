package com.locolhive.chartout.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.locolhive.chartout.R;
import com.locolhive.chartout.classes.Content;
import com.locolhive.chartout.classes.CustomLatLng;
import com.locolhive.chartout.create.CreateActivity;
import com.locolhive.chartout.create.FragSections.MediaHandler;
import com.locolhive.chartout.helpers.DialogProgress;
import com.locolhive.chartout.helpers.GlideApp;
import com.locolhive.chartout.helpers.ImageUtils;
import com.locolhive.chartout.helpers.ImageViewPager.ImageURLLoader;
import com.locolhive.chartout.helpers.ImageViewPager.SimpleViewPager;
import com.locolhive.chartout.helpers.Utils;
import com.locolhive.chartout.statics.ContentType;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.PlayerUIController;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.Place;
import java.util.ArrayList;
import java.util.List;

public class ContentAdapter extends BaseMultiItemQuickAdapter<Content, BaseViewHolder> {


  private RecyclerView recyclerView;
  private AppCompatActivity activity;
  private MediaHandler mediaHandler;
  private SparseArray<YouTubePlayer> intitialisedPlayers;

  public static ContentAdapter newInstance(final AppCompatActivity activity, MediaHandler hh) {

    ArrayList<Content> list = new ArrayList<>();
    list.add(new Content(ContentType.TITLE));
    list.add(new Content(ContentType.TEXT));
    list.add(new Content(ContentType.AddMedia));
    list.add(new Content(ContentType.AddContent2));
    list.add(new Content(ContentType.LOCATION));

    return newInstance(activity, hh, list);
  }

  public static ContentAdapter newInstance(final AppCompatActivity activity, MediaHandler hh, ArrayList<Content> list) {
    ContentAdapter adapter = new ContentAdapter();
    adapter.addData(list);
    adapter.setEnableLoadMore(false);
    adapter.activity = activity;
    adapter.mediaHandler = hh;
    adapter.intitialisedPlayers = new SparseArray<>();
    return adapter;
  }

  private ContentAdapter() {
    super(null);
    addItemType(ContentType.TITLE.ordinal(), R.layout.card_h1);
    addItemType(ContentType.TEXT.ordinal(), R.layout.card_content);
    addItemType(ContentType.AddContent2.ordinal(), R.layout.card_content2);
    addItemType(ContentType.AddMedia.ordinal(), R.layout.card_add_media);
    addItemType(ContentType.LOCATION.ordinal(), R.layout.card_location);
    addItemType(ContentType.IMAGE.ordinal(), R.layout.card_carousal);
    addItemType(ContentType.VIDEO_LINK.ordinal(), R.layout.card_video);
  }

  public void add(Content content) {
    addData(content);
    recyclerView.smoothScrollToPosition(getItemCount() - 1);
  }

  @Override
  public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
    this.recyclerView = recyclerView;
  }

  @Override
  protected void convert(final BaseViewHolder viewHolder, final Content item) {
    switch (ContentType.values()[viewHolder.getItemViewType()]) {
      case AddMedia:
        viewHolder.getView(R.id.btnPhoto).setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            mediaHandler.handleAddPhoto(viewHolder.getAdapterPosition());
          }
        });
        viewHolder.getView(R.id.btnVideo).setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            mediaHandler.handleAddVideo(viewHolder.getAdapterPosition());
          }
        });
        break;
      case TITLE:
      case TEXT:
      case AddContent2:
        if(item.getText()!=null)
          ((EditText) viewHolder.getView(R.id.et)).setText(item.getText());
        ((EditText) viewHolder.getView(R.id.et)).addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {

          }

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {

          }

          @Override
          public void afterTextChanged(Editable s) {
            Log.i(TAG, "afterTextChanged: " + s.toString());
            item.setText(s.toString());
          }
        });
        if(item.getType().equals(ContentType.AddContent2)){
          item.setType(ContentType.TEXT);
        }
        break;
      case LOCATION:
        //region Location
        if(item.getText()!=null) {
          ((PlacesAutocompleteTextView) viewHolder.getView(R.id.places_autocomplete)).setText(item.getText());
          viewHolder.getView(R.id.map).setVisibility(View.VISIBLE);
          GlideApp.with(mContext)
              .load(Utils.getMapUsingLatLong(item.getLatLng()))
              .centerCrop()
              .placeholder(Utils.getBlackProgressDrawable(mContext))
              .into(((ImageView) viewHolder.getView(R.id.map)));
        }
        ((PlacesAutocompleteTextView) viewHolder.getView(R.id.places_autocomplete)).setCountry("IN");
        ((PlacesAutocompleteTextView) viewHolder.getView(R.id.places_autocomplete)).setLanguageCode("en");
        ((PlacesAutocompleteTextView) viewHolder.getView(R.id.places_autocomplete)).setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
          @Override
          public void onPlaceSelected(@NonNull Place place) {
            Log.i(TAG, "onPlaceSelected: " + place);

            InputMethodManager inputManager = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            assert inputManager != null;
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

            final DialogProgress dialogProgress = DialogProgress.newInstance();
            dialogProgress.display(mContext);

            Places.GeoDataApi.getPlaceById(((CreateActivity) mContext).mGoogleApiClient, place.place_id)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                  @Override
                  public void onResult(@NonNull PlaceBuffer places) {
                    if (places.getStatus().isSuccess()) {
                      dialogProgress.done();
                      com.google.android.gms.location.places.Place myPlace = places.get(0);
                      if (myPlace != null) {
                        LatLng queriedLocation = myPlace.getLatLng();
                        Log.v("Latitude is", "" + queriedLocation.latitude);
                        Log.v("Longitude is", "" + queriedLocation.longitude);

                        String s = (String) myPlace.getName();
                        if(s==null || s.equals("")){
                          s = (String) myPlace.getAddress();
                        }
                        if(s!=null && !s.equals("")) {
                          item.setText(s);
                          item.setLatLng(queriedLocation.latitude, queriedLocation.longitude);
                        }

                        viewHolder.getView(R.id.map).setVisibility(View.VISIBLE);
                        GlideApp.with(mContext)
                            .load(Utils.getMapUsingLatLong(new CustomLatLng(queriedLocation.latitude, queriedLocation.longitude)))
                            .centerCrop()
                            .placeholder(Utils.getBlackProgressDrawable(mContext))
                            .into(((ImageView) viewHolder.getView(R.id.map)));
                      } else {
                        Toast.makeText(activity, "Error getting map", Toast.LENGTH_SHORT).show();
                      }
                    } else {
                      dialogProgress.done();
                      Toast.makeText(activity, "Error getting map", Toast.LENGTH_SHORT).show();
                    }
                    places.release();
                  }
                });
          }
        });
        //endregion
        break;
      case VIDEO_LINK:
        //region Video
        YouTubePlayerView youtubePlayerView = viewHolder.getView(R.id.youtube_player_view);
        activity.getLifecycle().addObserver(youtubePlayerView);
        PlayerUIController playerUIController = youtubePlayerView.getPlayerUIController();
        playerUIController.showFullscreenButton(false);
        playerUIController.showCurrentTime(false);

        if (item.getText() != null && !item.getText().equals("")) {
          youtubePlayerView.initialize(new YouTubePlayerInitListener() {
            @Override
            public void onInitSuccess(@NonNull final YouTubePlayer initializedYouTubePlayer) {
              initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady() {
                  new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                      initializedYouTubePlayer.cueVideo(item.getText(), 0);
                      intitialisedPlayers.append(viewHolder.getAdapterPosition(), initializedYouTubePlayer);
                    }
                  }, 20000);
                  Utils.createOkDialog(mContext, "Info", "Youtube is processing your video, it usually takes around 20 seconds",
                      new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                          dialog.dismiss();
                        }
                      }).show();
                }
              });
            }
          }, true);
        }
        //endregion
        break;
      case IMAGE:
        //region Carousal
        SimpleViewPager backDrop = viewHolder.getView(R.id.images);
        backDrop.setActivity(mContext);
        backDrop.setImageUrls(item.getPics().toArray(new String[item.getPics().size()]), new ImageURLLoader() {
          @Override
          public void loadImage(final ImageView imageView, String s, int pos) {
            GlideApp
                .with(mContext).load(ImageUtils.getUri(s))
                .apply(RequestOptions.centerCropTransform())
                .placeholder(Utils.getProgressDrawable(mContext))
                .into(imageView);
          }
        });

        int indicatorColor = mContext.getResources().getColor(R.color.white);
        int selectedIndicatorColor = mContext.getResources().getColor(R.color.grey600);
        backDrop.showIndicator(indicatorColor, selectedIndicatorColor);
        //endregion
        break;
    }
//    viewHolder.setText(R.id.tweetName, item.getUserName())
//        .setText(R.id.tweetText, item.getText())
//        .setText(R.id.tweetDate, item.getCreatedAt())
//        .setVisible(R.id.tweetRT, item.isRetweet())
//        .linkify(R.id.tweetText);
//    GlideApp.with(mContext).load(item.getUserAvatar()).crossFade().into((ImageView) viewHolder.getView(R.id.iv));
  }

  public void refreshVideos(){
    List<Content> data = getData();
    for (int i = 0; i < data.size(); i++) {
      if(data.get(i).getType().equals(ContentType.VIDEO_LINK)){
        YouTubePlayer youTubePlayer = intitialisedPlayers.get(i);
        if(youTubePlayer!=null){
          youTubePlayer.cueVideo(data.get(i).getText(), 0);
        }
      }
    }
  }

  public void addVideo(String id, Integer pos){
    if(pos!=null) {
      getData().get(pos).setText(id);
      getData().get(pos).setType(ContentType.VIDEO_LINK);
      notifyItemChanged(pos);
    }else {
      Content content = new Content(ContentType.VIDEO_LINK);
      content.setText(id);
      add(content);
    }
  }

  public void addPhotos(ArrayList<String> imgs, Integer pos){
    if(pos!=null) {
      getData().get(pos).setPics(imgs);
      getData().get(pos).setType(ContentType.IMAGE);
      notifyItemChanged(pos);
    }else {
      Content content = new Content(ContentType.IMAGE);
      content.setPics(imgs);
      add(content);
    }
  }

}