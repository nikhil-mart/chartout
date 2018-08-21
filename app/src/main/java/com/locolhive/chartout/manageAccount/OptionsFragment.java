package com.locolhive.chartout.manageAccount;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.locolhive.chartout.R;
import com.locolhive.chartout.adapters.OptionsAdapter;
import com.locolhive.chartout.home.BlankActivity;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OptionsFragment extends Fragment {

  private static final String TAG = OptionsFragment.class.getSimpleName() + " YOYO";

  View view;
  Context context;

  ArrayList<String> list;
  ArrayList<View.OnClickListener> listeners;
  String title;

  public OptionsFragment() {
    // Required empty public constructor
  }

  public static OptionsFragment newInstance(ArrayList<String> list,
      ArrayList<View.OnClickListener> listeners, String title) {
    OptionsFragment fragment = new OptionsFragment();
    fragment.list = list;
    fragment.listeners = listeners;
    Bundle bundle = new Bundle();
    bundle.putString("title", title);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment

    if (getArguments() != null) {
      title = getArguments().getString("title", title);
    }

    view = inflater.inflate(R.layout.fragment_options, container, false);
    context = view.getContext();
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    /*((BlankActivity)context).listener = new OnBackPressListener() {
        @Override
        public void onBackPress() {
            ((BlankActivity)context).setToolbarTitle(null);
        }
    };*/
    ((BlankActivity) context).setToolbarTitle(title);
    setUpRecycler();
  }

  @Override
  public void onPause() {
    super.onPause();
  }

  private void setUpRecycler() {
    RecyclerView recyclerView = view.findViewById(R.id.optionsRecycler);
    recyclerView.setAdapter(new OptionsAdapter(list, listeners, getContext()));
    recyclerView.setLayoutManager(
        new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
  }

}
