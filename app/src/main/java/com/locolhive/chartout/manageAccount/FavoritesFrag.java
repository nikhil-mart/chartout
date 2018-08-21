package com.locolhive.chartout.manageAccount;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.locolhive.chartout.R;
import com.locolhive.chartout.adapters.FavoritesAdapter;
import com.locolhive.chartout.adapters.ManageListingAdapter;
import com.locolhive.chartout.api.ListingApi;
import com.locolhive.chartout.classes.Post;
import com.locolhive.chartout.helpers.Utils;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFrag extends Fragment {

  private static final String TAG = FavoritesFrag.class.getSimpleName() + " YOYO";

  View view;
  Context context;
  SwipeRefreshLayout swipe;
  ArrayList<Post> listings;
  Spinner spinner;
  RecyclerView recyclerView;

  ListingApi api;

  public FavoritesFrag() {
    // Required empty public constructor
  }

  public static FavoritesFrag newInstance() {

    Bundle args = new Bundle();
    FavoritesFrag fragment = new FavoritesFrag();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view = inflater.inflate(R.layout.fav_frag, container, false);
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    context = getContext();

    api = new ListingApi(context);

    swipe = view.findViewById(R.id.swipe_refresh);
    swipe.setOnRefreshListener(new OnRefreshListener() {
      @Override
      public void onRefresh() {
        getData();
      }
    });
    swipe.setRefreshing(true);

    getData();

  }

  /**
   * getData()
   */
  private void getData() {

    listings = new ArrayList<>();
    listings.add(new Post());
    listings.add(new Post());
    listings.add(new Post());
    listings.add(new Post());
    listings.add(new Post());
    listings.add(new Post());
    setUpRecycler(listings);

    swipe.setRefreshing(false);
  }

  void setUpRecycler(ArrayList<Post> requests) {
    if (requests != null && requests.size() > 0) {
      view.findViewById(R.id.greyOut).setVisibility(View.GONE);
      recyclerView = view.findViewById(R.id.recycler);
      recyclerView
          .setLayoutManager(new GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false));
//      recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
      FavoritesAdapter adapter = new FavoritesAdapter(requests);
      recyclerView.setAdapter(adapter);
    } else {
      String s = "No Faovrites";
      ((TextView) view.findViewById(R.id.greyOut)).setText(s);
      view.findViewById(R.id.greyOut).setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void onPause() {
    super.onPause();
  }

}
