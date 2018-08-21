package com.locolhive.chartout.chat;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.locolhive.chartout.R;
import com.locolhive.chartout.helpers.Navigation;
import com.locolhive.chartout.home.BlankActivity;
import com.locolhive.chartout.interfaces.OnBackPressListener;

public class Frag_ChatList extends Fragment {

  Context context;
  View view;
  RecyclerView recyclerView;

  public Frag_ChatList() {
    // Required empty public constructor
  }

  public static Frag_ChatList newInstance() {
    Frag_ChatList fragment = new Frag_ChatList();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view = inflater.inflate(R.layout.fragment_view_notifications, container, false);
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();

    context = getContext();
    ((BlankActivity) context).setToolbarTitle("Chat");
    ((BlankActivity) context).listener = new OnBackPressListener() {
      @Override
      public void onBackPress() {
        Navigation.goToMain(context);
      }
    };
    recyclerView = view.findViewById(R.id.recycler);
    recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    recyclerView.setAdapter(new ChatListAdapter(context));

  }

  @Override
  public void onDestroyView() {
    ((BlankActivity) context).listener = null;
    super.onDestroyView();
  }
}
