package com.locolhive.chartout.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.locolhive.chartout.R;
import com.locolhive.chartout.classes.Content;
import com.locolhive.chartout.classes.CustomLatLng;
import com.locolhive.chartout.classes.Post;
import com.locolhive.chartout.databinding.ViewCardMainBinding;
import com.locolhive.chartout.helpers.GlideApp;
import com.locolhive.chartout.helpers.ImageUtils;
import com.locolhive.chartout.helpers.ImageViewPager.ImageURLLoader;
import com.locolhive.chartout.helpers.ImageViewPager.SimpleViewPager;
import com.locolhive.chartout.helpers.Utils;
import com.locolhive.chartout.statics.BANGALORE;
import com.locolhive.chartout.statics.ContentType;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.PlayerUIController;
import java.util.ArrayList;

public class ViewContentAdapter extends BaseMultiItemQuickAdapter<Content, BaseViewHolder> {

  private RecyclerView recyclerView;
  private AppCompatActivity activity;
  private Post post;

  public static ViewContentAdapter newInstance(Post post, final AppCompatActivity activity) {

    ViewContentAdapter adapter = new ViewContentAdapter();
    adapter.post = post;

    ArrayList<Content> list = new ArrayList<>();

    list.add(new Content(ContentType.Card_Main));
    if (post.getContent() != null) {
      list.addAll(post.getContent());
    }

    //Adding fake data

    Content content = new Content(ContentType.TITLE);
    content.setText("Day 1");
    list.add(content);

    content = new Content(ContentType.TEXT);
    content.setText("Very funsa asdkjadsjd dfs fds fd fds  fds fds ds f sd fsdfsdfds f ds f ds f ds  fsd f s df ss d s dsfdsfdfs fds sd sf ds d ");
    list.add(content);

    content = new Content(ContentType.LOCATION);
    content.setText("Bangalore");
    content.setLatLng(new CustomLatLng(BANGALORE.lat+0.002, BANGALORE.lng+0.001));
    list.add(content);

    content = new Content(ContentType.VIDEO_LINK);
    content.setText("TYgRF4pzO_M");
    list.add(content);

    //

    list.add(new Content(ContentType.Card_Avail));
    list.add(new Content(ContentType.Card_Msg));
    list.add(new Content(ContentType.Card_Review));

    adapter.setNewData(list);
    adapter.setEnableLoadMore(false);
    adapter.activity = activity;

    return adapter;
  }

  private ViewContentAdapter() {
    super(null);
    addItemType(ContentType.TITLE.ordinal(), R.layout.view_card_h1);
    addItemType(ContentType.TEXT.ordinal(), R.layout.view_card_content);
    addItemType(ContentType.LOCATION.ordinal(), R.layout.view_card_location);
    addItemType(ContentType.IMAGE.ordinal(), R.layout.card_carousal);
    addItemType(ContentType.VIDEO_LINK.ordinal(), R.layout.card_video);
    addItemType(ContentType.Card_Main.ordinal(), R.layout.view_card_main);
    addItemType(ContentType.Card_Avail.ordinal(), R.layout.view_card_avail);
    addItemType(ContentType.Card_Msg.ordinal(), R.layout.view_card_msg);
    addItemType(ContentType.Card_Review.ordinal(), R.layout.view_card_reviews);
  }

  @Override
  public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
    this.recyclerView = recyclerView;
  }

  @Override
  protected void convert(final BaseViewHolder viewHolder, final Content item) {
    switch (ContentType.values()[viewHolder.getItemViewType()]) {
      case TITLE:
      case TEXT:
        if (item.getText() != null) {
          ((TextView) viewHolder.getView(R.id.et)).setText(item.getText());
        }
        break;
      case LOCATION:
        //region Location
        if (item.getText() != null) {
          ((TextView) viewHolder.getView(R.id.et)).setText(item.getText());
          viewHolder.getView(R.id.map).setVisibility(View.VISIBLE);
          GlideApp.with(mContext)
              .load(Utils.getMapUsingLatLong(item.getLatLng()))
              .centerCrop()
              .placeholder(Utils.getBlackProgressDrawable(mContext))
              .into(((ImageView) viewHolder.getView(R.id.map)));
        }
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
                  initializedYouTubePlayer.cueVideo(item.getText(), 0);
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
      case Card_Main:
        ViewCardMainBinding binding = DataBindingUtil.bind(viewHolder.itemView);
        assert binding != null;
        binding.setPost(post);
        GlideApp.with(mContext)
            .load(Utils.getMapUsingLatLong(post.getMainLocation()))
            .centerCrop()
            .into(binding.map);
        if(post.getHost()!=null && post.getHost().getProfilePicId()!=null){
          GlideApp.with(mContext)
              .load(ImageUtils.getUri(post.getHost().getProfilePicId()))
              .centerCrop()
              .into(binding.hostImage);
        }
        break;
      case Card_Avail:
        if(post.getAvailableToBook()!=null && post.getAvailableToBook())
          ((TextView)viewHolder.getView(R.id.avail)).setText(mContext.getString(R.string.available_to_book_with_me_1_s, "Yes"));
        else
          ((TextView)viewHolder.getView(R.id.avail)).setText(mContext.getString(R.string.available_to_book_with_me_1_s, "No"));
        if(post.getListedOnLocol()!=null && post.getListedOnLocol())
          ((TextView)viewHolder.getView(R.id.locol)).setText(mContext.getString(R.string.listed_locol, "Yes"));
        else
          ((TextView)viewHolder.getView(R.id.locol)).setText(mContext.getString(R.string.listed_locol, "No"));
        break;
      case Card_Msg:
        viewHolder.getView(R.id.btnReview).setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            Toast.makeText(activity, "Review", Toast.LENGTH_SHORT).show();
          }
        });
        viewHolder.getView(R.id.btnMsg).setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            Toast.makeText(activity, "Message", Toast.LENGTH_SHORT).show();
          }
        });
        break;
      case Card_Review:
        RecyclerView rv = viewHolder.getView(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(new ReviewAdapter(null, mContext));
        break;
    }

  }

}