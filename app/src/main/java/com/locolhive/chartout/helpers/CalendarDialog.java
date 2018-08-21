package com.locolhive.chartout.helpers;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import com.locolhive.chartout.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import java.util.Calendar;
import java.util.List;

public class CalendarDialog extends DialogFragment {

  public static final int TYPE_Week = R.layout.week_picker_layout;
  public static final int TYPE_Month = R.layout.date_picker_layout;
  public static final int SELECTION_MODE_SINGLE = MaterialCalendarView.SELECTION_MODE_SINGLE;
  public static final int SELECTION_MODE_MULTIPLE = MaterialCalendarView.SELECTION_MODE_MULTIPLE;
  private static final String TAG = CalendarDialog.class.getSimpleName() + " YOYO";
  public OnDialogResultListener listener;
  private MaterialCalendarView calendarView;
  private Integer selectionMode;
  private int type;
  private boolean before;

  public static CalendarDialog newInstance(int type, OnDialogResultListener listener) {
    CalendarDialog fragment = new CalendarDialog();
    fragment.listener = listener;
    fragment.type = type;
    fragment.before = false;
    return fragment;
  }

  public void setBefore() {
    before = true;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    View view = getActivity().getLayoutInflater().inflate(type, null);
    calendarView = view.findViewById(R.id.calendarView);

    Calendar today = Calendar.getInstance();

    Calendar date2 = Calendar.getInstance();

    if (before) {
      date2.add(Calendar.YEAR, -18);
      Calendar dd = Calendar.getInstance();
      dd.add(Calendar.YEAR, -118);
      calendarView.state().edit()
          .setMinimumDate(dd)
          .setMaximumDate(date2)
          .commit();
      calendarView.setCurrentDate(date2);
    } else {
      date2.add(Calendar.YEAR, 1);
      calendarView.state().edit()
          .setMinimumDate(today)
          .setMaximumDate(date2)
          .commit();
      calendarView.setCurrentDate(today.getTime());
    }

    if (selectionMode == null) {
      calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_RANGE);
    } else {
      calendarView.setSelectionMode(selectionMode);
    }

    calendarView.setCurrentDate(today.getTime());

    view.findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        CalendarDialog.this.dismiss();
        listener.onResult(calendarView.getSelectedDates());
      }
    });

    view.findViewById(R.id.btn_Cancel).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        CalendarDialog.this.dismiss();
      }
    });

    view.findViewById(R.id.btn_Clear).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        calendarView.clearSelection();
      }
    });

    builder.setView(view);

//    dialog.setOnShowListener(new OnShowListener() {
//      @Override
//      public void onShow(DialogInterface dialog) {
//
//        int _10dp = Utils.dpToPx(10, getActivity());
//        int _32dp = Utils.dpToPx(32, getActivity());
//
//        AlertDialog alertDialog = (AlertDialog) dialog;
//
//        Button ok = alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
//        ok.setMinimumHeight(0);
//        ok.setMinHeight(0);
//        ok.setTextSize(COMPLEX_UNIT_SP, 16);
//        ok.setBackground(getResources().getDrawable(R.drawable.bg_rect_green));
//        ok.setTextColor(getResources().getColor(R.color.white));
//        ok.setPadding(_32dp, _10dp, _32dp, _10dp);
//
//        Button cancel = alertDialog.getButton(android.app.AlertDialog.BUTTON_NEUTRAL);
//        cancel.setMinimumHeight(0);
//        cancel.setMinHeight(0);
//        cancel.setTextSize(COMPLEX_UNIT_SP, 16);
//        cancel.setBackground(getResources().getDrawable(R.drawable.bg_rect_trans));
//        cancel.setTextColor(getResources().getColor(R.color.Grey800));
//        cancel.setPadding(_32dp, _10dp, _32dp, _10dp);
//
//        Button clear = alertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE);
//        clear.setMinimumHeight(0);
//        clear.setMinHeight(0);
//        clear.setTextSize(COMPLEX_UNIT_SP, 16);
//        clear.setBackground(getResources().getDrawable(R.drawable.bg_rect_trans));
//        clear.setTextColor(getResources().getColor(R.color.Grey800));
//        clear.setPadding(_32dp, _10dp, _32dp, _10dp);
//
//      }
//    });

    return builder.create();
  }

  public void setSelectionMode(int selectionMode) {
    this.selectionMode = selectionMode;
  }

  public interface OnDialogResultListener {

    void onResult(List<CalendarDay> dates);
  }

}
