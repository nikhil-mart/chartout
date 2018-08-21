package com.locolhive.chartout.helpers;

import android.text.TextUtils;
import android.widget.EditText;

public class RequiredSet {

  EditText[] et;

  public RequiredSet(EditText[] ets) {
    et = ets;
  }

  public boolean required() {
    boolean f = true;
    for (EditText text : et) {
      f = f && !TextUtils.isEmpty(text.getText());
    }

    return f;
  }

  public boolean validate() {
    boolean f = !TextUtils.isEmpty(et[0].getText());
    for (int i = 0; i < et.length; i++) {
      EditText text = et[i];
      f = f == !TextUtils.isEmpty(text.getText());
    }
    return f;
  }

  EditText getFirst() {
    return et[0];
  }

}
