package com.locolhive.chartout.helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {

  public float mStartDragX;
  private boolean enabled;
  private OnSwipeOutListener mListener;

  public CustomViewPager(@NonNull Context context) {
    super(context);
    enabled = true;
  }

  public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    enabled = true;
  }

  public void setPagingDisabled(Boolean f) {
    enabled = !f;
  }

  public void setOnSwipeOutListener(OnSwipeOutListener listener) {
    mListener = listener;
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    float x = ev.getX();
    switch (ev.getAction()) {
      case MotionEvent.ACTION_DOWN:
        mStartDragX = x;
        break;
      case MotionEvent.ACTION_MOVE:
        if (mListener != null) {
          if (mStartDragX < x && getCurrentItem() == 0) {
            mListener.onSwipeOutAtStart();
          } else if (mStartDragX > x && getCurrentItem() == getAdapter().getCount() - 1) {
            mListener.onSwipeOutAtEnd();
          }
        }
        break;
    }
    return (enabled && super.onInterceptTouchEvent(ev));
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    return enabled && super.onTouchEvent(ev);
  }

  public interface OnSwipeOutListener {

    void onSwipeOutAtStart();

    void onSwipeOutAtEnd();
  }

}