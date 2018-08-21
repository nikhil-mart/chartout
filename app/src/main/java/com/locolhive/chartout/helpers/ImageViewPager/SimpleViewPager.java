package com.locolhive.chartout.helpers.ImageViewPager;

import static com.locolhive.chartout.R.drawable.circle;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.locolhive.chartout.R;

@SuppressWarnings("unused")
public class SimpleViewPager extends RelativeLayout {

  private static final String TAG = "SimpleViewPager";
  int currentIndicator = 0;
  private SimpleViewPagerAdapter adapter;
  private Context context;
  private BiViewPager viewPager;
  //Circle indicators
  private LinearLayout circleLayout = null;
  private Drawable selectedCircle = null;
  private Drawable unselectedCircle = null;
  //Xml attributes
  private boolean forceSquare = false;
  private boolean vertical = false;
  private boolean useIndicator = false;
  private int circlesPaddingTop = 0;
  private int circlesPaddingBottom = 20;
  private int scaleType = -1;


  public SimpleViewPager(Context context) {
    super(context);
    this.context = context;
    setupViewPager();
  }

  public SimpleViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;

    TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SimpleViewPager, 0, 0);

    try {
      forceSquare = a.getBoolean(R.styleable.SimpleViewPager_forceSquare, false);
      vertical = a.getBoolean(R.styleable.SimpleViewPager_vertical, false);
      circlesPaddingTop = a.getDimensionPixelSize(R.styleable.SimpleViewPager_circlesPaddingTop, 0);
      circlesPaddingBottom = a.getDimensionPixelSize(R.styleable.SimpleViewPager_circlesPaddingBottom, 20);

      scaleType = a.getInteger(R.styleable.SimpleViewPager_scaleType, -1);
    } finally {
      a.recycle();
    }

    setupViewPager();
  }

  public static int dpToPx(int dp) {
    return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
  }

  private void setupViewPager() {
    viewPager = new BiViewPager(context, vertical);
    viewPager.setId(R.id.programmatic_viewpager);
    addView(viewPager);
  }

  public void setActivity(Context context) {
    this.context = context;
  }

  public void setImageUrls(String[] imageUrls, ImageURLLoader imageURLLoader) {
    adapter = new SimpleViewPagerAdapter(context, imageUrls, imageURLLoader, convertScaleTypeAttribute());
    viewPager.setAdapter(adapter);
  }

  public void setImageIds(int[] resourceIds, ImageResourceLoader imageResourceLoader) {
    adapter = new SimpleViewPagerAdapter(context, resourceIds, imageResourceLoader, convertScaleTypeAttribute());
    viewPager.setAdapter(adapter);
  }

  public void showIndicator(int indicatorColor, int selectedIndicatorColor) {
    setupIndicator(indicatorColor, selectedIndicatorColor);
  }

  public ImageView getImageViewAt(int position) {
    return adapter.imageViews.get(position);
  }

  private ImageView.ScaleType convertScaleTypeAttribute() {
    switch (scaleType) {
      case 0:
        return ImageView.ScaleType.CENTER;
      case 1:
      case -1:
        return ImageView.ScaleType.CENTER_CROP;
      case 2:
        return ImageView.ScaleType.CENTER_INSIDE;
      case 3:
        return ImageView.ScaleType.FIT_CENTER;
      case 4:
        return ImageView.ScaleType.FIT_END;
      case 5:
        return ImageView.ScaleType.FIT_START;
      case 6:
        return ImageView.ScaleType.FIT_XY;
      case 7:
        return ImageView.ScaleType.MATRIX;
      default:
        return ImageView.ScaleType.CENTER_CROP;
    }
  }

  public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
    viewPager.addOnPageChangeListener(onPageChangeListener);
  }

  public void clearListeners() {
    viewPager.clearOnPageChangeListeners();
  }

  public void cancelIndicator() {
    useIndicator = false;
    circleLayout.removeAllViews();
  }

  public void setupIndicator(int unselectedColor, int selectedColor) {

    useIndicator = true;
    selectedCircle = ContextCompat.getDrawable(context, circle);
    selectedCircle.setColorFilter(new PorterDuffColorFilter(selectedColor, PorterDuff.Mode.MULTIPLY));

    unselectedCircle = ContextCompat.getDrawable(context, circle);
    unselectedCircle.setColorFilter(new PorterDuffColorFilter(unselectedColor, PorterDuff.Mode.MULTIPLY));

    float scale = getResources().getDisplayMetrics().density;
    int padding = (int) (2 * scale + 0.5f);

    circleLayout = new LinearLayout(context);

    LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

    if (vertical) {
      circleLayout.setOrientation(LinearLayout.VERTICAL);
      int leftPadding = dpToPx(20);
      circleLayout.setPadding(leftPadding, circlesPaddingTop, 0, circlesPaddingBottom);
      params.addRule(RelativeLayout.ALIGN_LEFT);
      params.addRule(RelativeLayout.CENTER_VERTICAL);
    } else {
      circleLayout.setPadding(0, 0, 0, circlesPaddingBottom);
      circleLayout.setOrientation(LinearLayout.HORIZONTAL);
      params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      params.addRule(RelativeLayout.CENTER_HORIZONTAL);
    }

    params.bottomMargin = 0;
    circleLayout.setLayoutParams(params);

    addView(circleLayout);

    for (int i = 0; i < adapter.getCount(); i++) {
      ImageView circle = new ImageView(context);
      circle.setImageDrawable(unselectedCircle);
      circle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
      circle.setAdjustViewBounds(true);
      if (vertical) {
        circle.setPadding(0, padding, 0, padding);
      } else {
        circle.setPadding(padding, 0, padding, 0);
      }

      circleLayout.addView(circle);
    }

    setIndicator(0);

    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //unused
      }

      @Override
      public void onPageSelected(int position) {
        setIndicator(position);
      }

      @Override
      public void onPageScrollStateChanged(int state) {
        //unused
      }
    });
  }

  public void setCurrentItem(int index) {
    setCurrentItem(index, true);
  }

  public void setCurrentItem(int index, boolean smoothScroll) {
    resetIndicator();
    viewPager.setCurrentItem(index, smoothScroll);
  }

  private void resetIndicator() {
    if (!useIndicator) {
      return;
    }
    if (circleLayout == null) {
      return;
    }

    for (int i = 0; i < circleLayout.getChildCount(); i++) {
      ((ImageView) circleLayout.getChildAt(i)).setImageDrawable(unselectedCircle);
    }
  }

  private void setIndicator(int index) {
    if (!useIndicator) {
      return;
    }
    currentIndicator = index;
    int imageCount = adapter.getCount();

    if (index >= imageCount) {
      return;
    }

    ((ImageView) circleLayout.getChildAt(index)).setImageDrawable(selectedCircle);

    if (index > 0) {
      ((ImageView) circleLayout.getChildAt(index - 1)).setImageDrawable(unselectedCircle);
    }

    if (index < imageCount - 1) {
      ((ImageView) circleLayout.getChildAt(index + 1)).setImageDrawable(unselectedCircle);
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    if (forceSquare) {
      //noinspection SuspiciousNameCombination
      super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    } else {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);

    if (adapter != null) {
      resetIndicator();
      setIndicator(currentIndicator);
    }
  }
}
