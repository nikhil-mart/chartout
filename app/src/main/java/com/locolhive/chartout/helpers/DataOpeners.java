package com.locolhive.chartout.helpers;

import android.animation.ValueAnimator;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.LinkedHashMap;

@SuppressWarnings("FieldCanBeLocal")
public class DataOpeners {

  private static final String TAG = DataOpeners.class.getSimpleName() + " YOYO";
  private static int ANIMATION_DURATION = 300;

  private LinkedHashMap<Integer, Group> groupArrayList;
  private AppCompatActivity activity;

  public DataOpeners(AppCompatActivity activity) {
    this.activity = activity;
    groupArrayList = new LinkedHashMap<>();
  }

  public void add(Integer id, Integer textViewID, Integer ic_rightID) {
    groupArrayList.put(id, new Group((TextView) activity.findViewById(textViewID), (ImageView) activity.findViewById(ic_rightID)));
  }

  public TextView getData(Integer openerID) {
    return groupArrayList.get(openerID).data;
  }

  public void setWidth(int width) {
    for (Integer integer : groupArrayList.keySet()) {
      groupArrayList.get(integer).measure(width);
    }
  }

  public void done() {
    setListeners();
  }

  public void toggle(int id) {
    handleOpeners(id);
  }

  public void setOpened(int id) {
    groupArrayList.get(id).opened = true;
  }

  private void setListeners() {
    for (int i = 0; i < groupArrayList.size(); i++) {
      ConstraintLayout constraintLayout = activity.findViewById(
          (Integer) groupArrayList.keySet().toArray()[i]);
      constraintLayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          handleOpeners(v.getId());
        }
      });
    }
  }

  private void handleOpeners(final int id) {

    Log.i(TAG, "handleOpeners: " + id);

    if (!groupArrayList.get(id).opened) {
      ValueAnimator anim = ValueAnimator.ofInt(0, groupArrayList.get(id).actualSize);
      anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
          int val = (Integer) valueAnimator.getAnimatedValue();
          ViewGroup.LayoutParams layoutParams = groupArrayList.get(id).data.getLayoutParams();
          layoutParams.height = val;
          groupArrayList.get(id).data.setLayoutParams(layoutParams);
        }
      });
      anim.setDuration(ANIMATION_DURATION);
      ValueAnimator animator = ValueAnimator.ofFloat(0, 90);
      animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
          float val = (float) animation.getAnimatedValue();
          groupArrayList.get(id).ic_right.setRotation(val);
        }
      });
      animator.setDuration(ANIMATION_DURATION);
      animator.start();
      anim.start();
      groupArrayList.get(id).opened = true;
    } else {
      ValueAnimator anim = ValueAnimator.ofInt(groupArrayList.get(id).actualSize, 0);
      anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
          int val = (Integer) valueAnimator.getAnimatedValue();
          ViewGroup.LayoutParams layoutParams = groupArrayList.get(id).data.getLayoutParams();
          layoutParams.height = val;
          groupArrayList.get(id).data.setLayoutParams(layoutParams);
        }
      });
      anim.setDuration(ANIMATION_DURATION);
      ValueAnimator animator = ValueAnimator.ofFloat(90, 0);
      animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
          float val = (float) animation.getAnimatedValue();
          groupArrayList.get(id).ic_right.setRotation(val);
        }
      });
      animator.setDuration(ANIMATION_DURATION);
      animator.start();
      anim.start();
      groupArrayList.get(id).opened = false;
    }
  }

  public class Group {

    int actualSize;
    TextView data;
    ImageView ic_right;
    boolean opened;

    Group(TextView textView, ImageView ic_right) {
      opened = false;
      this.ic_right = ic_right;
      data = textView;
    }

    public void measure(int width) {
      data.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST),
          View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
      actualSize = data.getMeasuredHeight();
    }

  }

}
