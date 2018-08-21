package com.locolhive.chartout.helpers;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.locolhive.chartout.R;
import com.locolhive.chartout.helpers.TokenAutoComplete.TokenCompleteTextView;

public class ChipsView extends TokenCompleteTextView<String> {

  public ChipsView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected View getViewForObject(String object) {
    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    assert inflater != null;
    TextView view = (TextView) inflater.inflate(R.layout.card_chip, (ViewGroup) getParent(), false);
    view.setText(object);
    return view;
  }

  @Override
  protected String defaultObject(String completionText) {
    //Stupid simple example of guessing if we have an email or not
    return "";
  }
}
