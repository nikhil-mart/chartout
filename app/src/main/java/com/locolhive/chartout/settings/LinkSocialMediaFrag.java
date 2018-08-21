package com.locolhive.chartout.settings;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.locolhive.chartout.R;
import com.locolhive.chartout.home.BlankActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LinkSocialMediaFrag extends Fragment {

  private static final String TAG = LinkSocialMediaFrag.class.getSimpleName() + " YOYO";

  View view;
  Context context;

  public LinkSocialMediaFrag() {
    // Required empty public constructor
  }

  public static LinkSocialMediaFrag newInstance() {

    Bundle args = new Bundle();

    LinkSocialMediaFrag fragment = new LinkSocialMediaFrag();
    fragment.setArguments(args);
    return fragment;
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view = inflater.inflate(R.layout.link_frag, container, false);
    context = view.getContext();
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    ((BlankActivity) context).setToolbarTitle("Link Accounts");
  }
}
