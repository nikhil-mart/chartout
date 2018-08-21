package com.locolhive.chartout.helpers;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.locolhive.chartout.R;
import com.locolhive.chartout.databinding.DialogOptionsBinding;
import com.locolhive.chartout.interfaces.OnResultListener;

public class DialogUploadOptions extends DialogFragment {

  public static final String TAG = DialogUploadOptions.class.getSimpleName() + " YOYO";

  Context context;
  OnResultListener<String> listener;

  public static DialogUploadOptions newInstance(OnResultListener<String> listener) {
    DialogUploadOptions fragment = new DialogUploadOptions();
    fragment.listener = listener;
    return fragment;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    context = getActivity();

    final Builder builder = new Builder(context);

    final DialogOptionsBinding binding = DataBindingUtil.inflate(
        ((AppCompatActivity) context).getLayoutInflater(),
        R.layout.dialog_options, null, false);

    binding.btnUrl.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        binding.options.setVisibility(View.GONE);
        binding.form.setVisibility(View.VISIBLE);
        Utils.launchYoutube((AppCompatActivity) getActivity());
      }
    });
    binding.btnUp.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        dismiss();
        listener.OnResult(null);
      }
    });
    binding.btnAdd.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        String string = binding.etUrl.getText().toString();
        String id = Utils.extractYTId(string);
        if (id == null) {
          binding.etUrl.setError("Invalid url");
          Toast.makeText(context, "Invalid url", Toast.LENGTH_SHORT).show();
        } else {
          dismiss();
          listener.OnResult(id);
        }
      }
    });

    builder
        .setCancelable(true)
        .setView(binding.getRoot());

    return builder.create();
  }

  public void display(Context context) {
    if (context != null) {
      if (!this.isShowing()) {
        show(((AppCompatActivity) context).getSupportFragmentManager(), null);
      }
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
