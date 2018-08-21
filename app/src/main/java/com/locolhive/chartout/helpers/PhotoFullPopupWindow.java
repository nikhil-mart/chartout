package com.locolhive.chartout.helpers;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.locolhive.chartout.R;


public class PhotoFullPopupWindow extends PopupWindow {

  private View view;
  private Context mContext;
  private PhotoView photoView;
  private ProgressBar loading;
  private ViewGroup parent;


  public PhotoFullPopupWindow(Context ctx, View v, Uri imageUrl, Bitmap bitmap) {

    super(((LayoutInflater) ctx.getSystemService(LAYOUT_INFLATER_SERVICE))
            .inflate(R.layout.popup_photo_full, null), ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT);

    if (VERSION.SDK_INT >= 21) {
      setElevation(5.0f);
    }

    this.mContext = ctx;
    this.view = getContentView();

    ImageButton closeButton = this.view.findViewById(R.id.ib_close);
    setOutsideTouchable(true);

    setFocusable(true);
    // Set a click listener for the popup window close button
    closeButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dismiss();
      }
    });

    photoView = view.findViewById(R.id.image);
    loading = view.findViewById(R.id.loading);
    photoView.setMaximumScale(6);
    parent = (ViewGroup) photoView.getParent();

    // ImageUtils.setZoomable(imageView);
    //----------------------------

    if (bitmap != null) {

      loading.setVisibility(View.GONE);
      if (VERSION.SDK_INT >= 16) {
        parent.setBackground(new BitmapDrawable(mContext.getResources(), ImageUtils.fastblur(Bitmap.createScaledBitmap(bitmap, 50, 50, true))));// ));
      } else {
        onPalette(Palette.from(bitmap).generate());
      }
      photoView.setImageBitmap(bitmap);
    } else {
      loading.setIndeterminate(true);
      loading.setVisibility(View.VISIBLE);
      GlideApp.with(ctx).asBitmap()
          .load(imageUrl)
          .error(R.drawable.ic_error_outline)
          .listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
              loading.setIndeterminate(false);
              loading.setBackgroundColor(Color.LTGRAY);
              return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model,
                Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
              Log.i("YOYO", "onResourceReady: ");

              if (VERSION.SDK_INT >= 16) {
                parent.setBackground(
                    new BitmapDrawable(mContext.getResources(), ImageUtils.fastblur(Bitmap.createScaledBitmap(resource, 50, 50, true))));// ));
              } else {
                onPalette(Palette.from(resource).generate());

              }
              photoView.setImageBitmap(resource);

              loading.setVisibility(View.GONE);
              return false;
            }
          })
          .diskCacheStrategy(DiskCacheStrategy.ALL)
          .into(photoView);

      showAtLocation(v, Gravity.CENTER, 0, 0);
    }
    //------------------------------

  }

  private void onPalette(Palette palette) {
    if (null != palette) {
      ViewGroup parent = (ViewGroup) photoView.getParent().getParent();
      parent.setBackgroundColor(palette.getDarkVibrantColor(Color.GRAY));
    }
  }

}