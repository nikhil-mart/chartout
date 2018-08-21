package com.locolhive.chartout.create;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.locolhive.chartout.R;
import com.locolhive.chartout.databinding.FragmentFinalBinding;
import com.locolhive.chartout.helpers.DialogSuccess;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragFinalize extends Fragment {

  private static final String TAG = FragFinalize.class.getSimpleName() + " YOYO";
  FragmentFinalBinding binding;
  CreateActivity activity;

  public static FragFinalize newInstance() {

    Bundle args = new Bundle();

    FragFinalize fragment = new FragFinalize();
    fragment.setArguments(args);
    return fragment;
  }

  public FragFinalize() {
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_final, container, false);
    activity = (CreateActivity) getActivity();

    assert activity != null;
    binding.setPost(activity.request);

    binding.nxt.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        DialogSuccess dialogSuccess = DialogSuccess.newInstance();
        dialogSuccess.display(activity);
      }
    });

    return binding.getRoot();

  }

}
