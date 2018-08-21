package com.locolhive.chartout.helpers;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import com.locolhive.chartout.R;
import com.locolhive.chartout.home.BlankActivity;
import com.locolhive.chartout.home.MainActivity;

public class DialogSuccess extends DialogFragment {

  public static final String TAG = DialogSuccess.class.getSimpleName() + " YOYO";

  Context context;

  public static DialogSuccess newInstance() {
    DialogSuccess fragment = new DialogSuccess();
    return fragment;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    context = getActivity();

    Builder builder = new Builder(context);

    View view = ((AppCompatActivity) context).getLayoutInflater().inflate(R.layout.dialog_success, null);

    view.findViewById(R.id.btn_neutral).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(context, BlankActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra(BlankActivity.ARG_TYPE, BlankActivity.FRAG_Exps);
        context.startActivity(i);
        ((AppCompatActivity) context).finishAffinity();
      }
    });

    view.findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        ((AppCompatActivity) context).finishAffinity();
      }
    });

    return builder
        .setCancelable(false)
        .setView(view)
        .create();

  }

  public void display(Context context) {
    if (!this.isShowing()) {
      show(((AppCompatActivity) context).getSupportFragmentManager(), null);
    }
  }

  public void displayNow(Context context) {
    if (!this.isShowing()) {
      showNow(((AppCompatActivity) context).getSupportFragmentManager(), null);
    }
  }

  public boolean isShowing() {
    if (getDialog() != null) {
      return getDialog().isShowing();
    } else {
      return false;
    }
  }

  public void done() {
    if (isShowing()) {
      dismiss();
    }
  }

}
