package com.locolhive.chartout.helpers;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.TransitionInflater;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.locolhive.chartout.R;


public class PhotoFullDialog extends DialogFragment {

  private Context mContext;
  private PhotoView photoView;
  private ProgressBar loading;
  private ViewGroup parent;

  View v;
  Uri imageUrl;

  public static PhotoFullDialog newInstance(Context ctx, View imageView, Uri imageUrl) {

    PhotoFullDialog fragment = new PhotoFullDialog();

    fragment.mContext = ctx;
    fragment.imageUrl = imageUrl;
    fragment.v = imageView;

    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.fade));
      setExitTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.fade));
    }
  }

  @SuppressLint("CheckResult")
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.PicDialogStyle);

    View view = getActivity().getLayoutInflater().inflate(R.layout.popup_photo_full, null);

//    if (VERSION.SDK_INT >= 21) {
//      setElevation(5.0f);
//    }

    ImageButton closeButton = view.findViewById(R.id.ib_close);

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

    loading.setIndeterminate(true);
    loading.setVisibility(View.VISIBLE);
    GlideApp.with(mContext).asBitmap()
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

//            parent.setBackground(
//                new BitmapDrawable(mContext.getResources(),
//                    ImageUtils.fastblur(Bitmap.createScaledBitmap(resource, 50, 50, true))));
//            photoView.setImageBitmap(resource);

            loading.setVisibility(View.GONE);
            return false;
          }
        })
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(photoView);

    builder.setView(view);

    return builder.create();

  }

  public void display() {
    show(((AppCompatActivity) mContext).getSupportFragmentManager(), "PhotoFullDialog");
  }

}