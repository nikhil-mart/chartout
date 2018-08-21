package com.locolhive.chartout.settings;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.locolhive.chartout.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CancelAccFrag extends Fragment {

  private static final String TAG = CancelAccFrag.class.getSimpleName() + " YOYO";

  View view;
  Context context;

  public CancelAccFrag() {
    // Required empty public constructor
  }

  public static CancelAccFrag newInstance() {

    Bundle args = new Bundle();

    CancelAccFrag fragment = new CancelAccFrag();
    fragment.setArguments(args);
    return fragment;
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view = inflater.inflate(R.layout.changemob_frag, container, false);
    context = view.getContext();
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();

  }
}
