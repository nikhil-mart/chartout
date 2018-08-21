package com.locolhive.chartout.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.locolhive.chartout.R;
import com.locolhive.chartout.helpers.GlideApp;
import com.locolhive.chartout.helpers.ImageUtils;
import com.locolhive.chartout.helpers.ImageViewPager.ImageURLLoader;
import com.locolhive.chartout.helpers.ImageViewPager.SimpleViewPager;
import com.locolhive.chartout.helpers.Utils;
import java.util.ArrayList;

public class PhotoFrag extends Fragment {

  private static final String TAG = PhotoFrag.class.getSimpleName() + " YOYO";

  ArrayList<String> images;

  public static PhotoFrag newInstance(ArrayList<String> images) {
    PhotoFrag photoFrag = new PhotoFrag();
    Bundle bundle = new Bundle();
    bundle.putStringArrayList("images", images);
    photoFrag.setArguments(bundle);
    return photoFrag;
  }
  
  public PhotoFrag(){

  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.view_top_carousal, container, false);

    Bundle arguments = getArguments();
    if(arguments!=null){
      images = arguments.getStringArrayList("images");
    }

    SimpleViewPager backDrop = view.findViewById(R.id.images);

    backDrop.setActivity(getActivity());
    backDrop.setImageUrls(images.toArray(new String[images.size()]), new ImageURLLoader() {
      @Override
      public void loadImage(final ImageView imageView, String s, int pos) {
        if (pos == 0) {
          GlideApp
              .with(getActivity())
              .asDrawable()
              .load(ImageUtils.getUri(s))
              .listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                  Log.i(TAG, "onLoadFailed: ");
                  return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource,
                    boolean isFirstResource) {
                  Log.i(TAG, "onResourceReady: ");
//                  setShareIntent(imageView);
                  imageView.setImageDrawable(resource);
                  return true;
                }
              })
              .apply(RequestOptions.centerCropTransform())
              .placeholder(Utils.getProgressDrawable(getActivity()))
              .into(imageView);
        } else {
          GlideApp
              .with(getActivity()).load(ImageUtils.getUri(s))
              .apply(RequestOptions.centerCropTransform())
              .placeholder(Utils.getProgressDrawable(getActivity()))
              .into(imageView);
        }
      }
    });

    int indicatorColor = getActivity().getResources().getColor(R.color.white);
    int selectedIndicatorColor = getActivity().getResources().getColor(R.color.grey600);
    backDrop.showIndicator(indicatorColor, selectedIndicatorColor);

    return view;
  }

}
