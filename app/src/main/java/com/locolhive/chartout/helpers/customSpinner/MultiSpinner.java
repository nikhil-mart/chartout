package com.locolhive.chartout.helpers.customSpinner;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.DialogInterface.OnShowListener;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Button;
import com.locolhive.chartout.R;
import com.locolhive.chartout.helpers.Utils;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MultiSpinner extends AppCompatSpinner implements OnMultiChoiceClickListener,
    OnCancelListener {

  private List<String> items;
  private boolean[] selected;
  private String defaultText = "Select";
  private String spinnerTitle = "";
  private MultiSpinnerListener listener;

  public MultiSpinner(Context context) {
    super(context);
  }

  public MultiSpinner(Context arg0, AttributeSet arg1) {
    super(arg0, arg1);
    TypedArray a = arg0.obtainStyledAttributes(arg1, R.styleable.MultiSpinnerSearch);
    final int N = a.getIndexCount();
    for (int i = 0; i < N; ++i) {
      int attr = a.getIndex(i);
      if (attr == R.styleable.MultiSpinnerSearch_hintText) {
        spinnerTitle = a.getString(attr);
      }
    }
    a.recycle();
  }

  public MultiSpinner(Context arg0, AttributeSet arg1, int arg2) {
    super(arg0, arg1, arg2);
  }

  @Override
  public void onClick(DialogInterface dialog, int which, boolean isChecked) {
    selected[which] = isChecked;
  }

  @Override
  public void onCancel(DialogInterface dialog) {
    refresh();
  }

  private void refresh() {
    // refresh text on spinner
    StringBuilder spinnerBuffer = new StringBuilder();
    for (int i = 0; i < items.size(); i++) {
      if (selected[i]) {
        spinnerBuffer.append(items.get(i));
        spinnerBuffer.append(", ");
      }
    }

    String spinnerText = spinnerBuffer.toString();
    if (spinnerText.length() > 2) {
      spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
    } else {
      spinnerText = defaultText;
    }

    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item,
        new String[]{spinnerText});
    setAdapter(adapter);
    if (selected.length > 0) {
      listener.onItemsSelected(selected);
    }
  }

  @Override
  public boolean performClick() {
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

    builder.setTitle(spinnerTitle);
    builder.setMultiChoiceItems(
        items.toArray(new CharSequence[items.size()]), selected, this);

    builder.setPositiveButton(android.R.string.ok,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
          }
        });
    builder.setOnCancelListener(this);

    AlertDialog alertDialog = builder.create();
    alertDialog.setOnShowListener(new OnShowListener() {
      @Override
      public void onShow(DialogInterface dialog) {

        int _10dp = Utils.dpToPx(10, getContext());
        int _32dp = Utils.dpToPx(32, getContext());

        AlertDialog alertDialog = (AlertDialog) dialog;
        Button ok = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        ok.setMinimumHeight(0);
        ok.setMinHeight(0);
        ok.setTextSize(COMPLEX_UNIT_SP, 16);
        ok.setBackground(getResources().getDrawable(R.drawable.bg_rect_green));
        ok.setTextColor(getResources().getColor(R.color.white));
        ok.setPadding(_32dp, _10dp, _32dp, _10dp);

      }
    });
    alertDialog.show();
    return true;
  }

  /**
   * Sets items to this spinner.
   *
   * @param items A TreeMap where the keys are the values to display in the spinner and the value the initial selected state of the key.
   * @param listener A MultiSpinnerListener.
   */
  public void setItems(LinkedHashMap<String, Boolean> items, MultiSpinnerListener listener) {
    this.items = new ArrayList<>(items.keySet());
    this.listener = listener;

    List<Boolean> values = new ArrayList<>(items.values());
    selected = new boolean[values.size()];
    for (int i = 0; i < items.size(); i++) {
      selected[i] = values.get(i);
    }

    // all text on the spinner
    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item,
        new String[]{defaultText});
    setAdapter(adapter);

    // Set Spinner Text
    onCancel(null);
  }

}
