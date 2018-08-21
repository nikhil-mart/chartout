package com.locolhive.chartout.helpers.ImageViewPager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.locolhive.chartout.R;
import com.locolhive.chartout.helpers.ImageUtils;
import com.locolhive.chartout.helpers.PhotoFullDialog;
import java.util.ArrayList;

public class SimpleViewPagerAdapter extends PagerAdapter {

  private static final int MODE_URLS = 0;
  private static final int MODE_DRAWABLES = 1;
  private static final int MODE_IDS = 2;
  private static final String TAG = SimpleViewPagerAdapter.class.getSimpleName() + " YOYO";
  ArrayList<ImageView> imageViews;
  private int mode;
  private Context context;
  private String[] imageUrls;
  private Drawable[] drawables;
  private int[] resourceIds = null;
  private ImageURLLoader imageURLLoader;
  private ImageResourceLoader imageResourceLoader;
  private ImageView.ScaleType scaleType;

  @SuppressWarnings("WeakerAccess")
  public SimpleViewPagerAdapter(Context context, String[] imageUrls, ImageURLLoader imageURLLoader, ImageView.ScaleType scaleType) {
    mode = MODE_URLS;
    this.context = context;
    this.imageUrls = imageUrls;
    this.imageURLLoader = imageURLLoader;
    this.scaleType = scaleType;
    imageViews = new ArrayList<>();

    if (scaleType == null) {
      this.scaleType = ImageView.ScaleType.CENTER_CROP;
    }
  }

  public SimpleViewPagerAdapter(Context context, Drawable[] drawables, ImageView.ScaleType scaleType) {
    mode = MODE_DRAWABLES;
    this.context = context;
    this.drawables = drawables;
    this.scaleType = scaleType;
    imageViews = new ArrayList<>();

    if (scaleType == null) {
      this.scaleType = ImageView.ScaleType.CENTER_CROP;
    }
  }

  @SuppressWarnings("WeakerAccess")
  public SimpleViewPagerAdapter(Context context, int[] resourceIds, ImageResourceLoader imageResourceLoader, ImageView.ScaleType scaleType) {
    mode = MODE_IDS;
    this.context = context;
    this.resourceIds = resourceIds;
    this.imageResourceLoader = imageResourceLoader;
    this.scaleType = scaleType;
    imageViews = new ArrayList<>();

    if (scaleType == null) {
      this.scaleType = ImageView.ScaleType.CENTER_CROP;
    }
  }

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, final int position) {

    final ImageView imageView = new ImageView(context);

    if (mode == MODE_URLS) {
      imageView.setImageResource(R.drawable.placeholder);
    }

    imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT));
    imageView.setAdjustViewBounds(true);

    if (scaleType != null) {
      imageView.setScaleType(scaleType);
    } else {
      imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    imageView.setPadding(0, 0, 0, 0);

    switch (mode) {
      case MODE_URLS:
        container.addView(imageView);
        imageURLLoader.loadImage(imageView, imageUrls[position], position);
        break;
      case MODE_DRAWABLES:
        imageView.setImageDrawable(drawables[position]);
        container.addView(imageView);
        break;
      case MODE_IDS:
        container.addView(imageView);
        imageResourceLoader.loadImageResource(imageView, resourceIds[position], position);
        break;
      default:
        //unused
    }

    imageView.setClickable(true);
    imageView.setFocusable(true);
    imageView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {

//        String[] urlList = new String[imageUrls.length];
//        for (int i=0;i<imageUrls.length;i++) {
//          urlList[i] = ImageUtils.getUri(imageUrls[i]).toString();
//        }

        PhotoFullDialog.newInstance(context, imageView, ImageUtils.getUri(imageUrls[position])).display();
//        new PhotoFullPopupWindow(context, imageView, ImageUtils.getUri(imageUrls[position]), null);

//        new ImageViewer.Builder<>(context, urlList)
//            .allowZooming(true)
//            .setStartPosition(position)
//            .setBackgroundColorRes(R.color.black_trans180)
//            .show();
      }
    });

    if (imageViews.size() > position && imageViews.get(position) != null) {

    } else {
      imageViews.add(position, imageView);
    }

    return imageView;
  }

  @Override
  public int getCount() {
    switch (mode) {
      case MODE_URLS:
        return imageUrls.length;
      case MODE_DRAWABLES:
        return drawables.length;
      case MODE_IDS:
        return resourceIds.length;
      default:
        return 0;
    }
  }

  @Override
  public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
    return view == object;
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    if (object instanceof ImageView) {
      container.removeView((ImageView) object);
    }
  }
}