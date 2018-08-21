package com.locolhive.chartout.manageAccount;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
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
import com.locolhive.chartout.adapters.ManageListingAdapter;
import com.locolhive.chartout.api.ListingApi;
import com.locolhive.chartout.classes.Post;
import com.locolhive.chartout.helpers.Utils;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManagePostsFrag extends Fragment {

  private static final String TAG = ManagePostsFrag.class.getSimpleName() + " YOYO";

  View view;
  Context context;
  SwipeRefreshLayout swipe;
  ArrayList<Post> listings;
  Spinner spinner;
  RecyclerView recyclerView;

  ListingApi api;

  public ManagePostsFrag() {
    // Required empty public constructor
  }

  public static ManagePostsFrag newInstance() {

    Bundle args = new Bundle();
    ManagePostsFrag fragment = new ManagePostsFrag();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view = inflater.inflate(R.layout.manage_exp_frag, container, false);
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

    populateSpinner();
    getData();

  }

  void populateSpinner() {

    spinner = view.findViewById(R.id.manageSpinner);

    ArrayAdapter<String> spinnerAdapter = Utils.getSpinnerAdapter(context, null, new ArrayList<>(Arrays.asList(
        context.getResources().getStringArray(R.array.manage_exp_spinner))));

    spinner.setAdapter(spinnerAdapter);

    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemSelected: ");
        if (position == 0) {
          if (recyclerView != null && recyclerView.getAdapter() != null) {
            ((ManageListingAdapter) recyclerView.getAdapter()).updateData(listings);
          } else {
            setUpRecycler(listings);
          }
        } else {
          recyclerView.setVisibility(View.INVISIBLE);
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
      }
    });

    spinner.setSelection(0);

    spinner.setVisibility(View.GONE);

  }

  /**
   * getData()
   */
  private void getData() {
//    api.getMyPosts(new OnTaskCompleteListener<Post[]>() {
//      @Override
//      public void onSuccess(Post[] response) {
//        Log.i(TAG, "getData onSuccess: ");
//        if (response == null) {
//          response = new Post[0];
//        }
//        listings = new ArrayList<>(Arrays.asList(response));
//        setUpRecycler(listings);
//      }
//
//      @Override
//      public void onFailure(String error) {
//        Log.e(TAG, "getData onFailure: " + error);
//        if (swipe.isRefreshing()) {
//          swipe.setRefreshing(false);
//        }
//      }
//    });
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

  /*
   * Filter according to Post Status
   * @param requests
   */
//  private void filter() {
//
//    filtered = new ArrayList<>();
//
//    switch (spinner.getSelectedItemPosition()) {
//      case 0: //Pending
//        for (Post listing : listings) {
//          if (listing.getPostStatus() != null && listing.getPostStatus().equals(PostStatus.WAITING_APPROVAL)) {
//            filtered.add(listing);
//          }
//        }
//        break;
//      case 1: //Published
//        for (Post listing : listings) {
//          if (listing.getPostStatus() == null || listing.getPostStatus().equals(PostStatus.ACTIVE)) {
//            filtered.add(listing);
//          }
//        }
//        break;
//      case 2: //Saved
//        for (Post listing : listings) {
//          if (listing.getPostStatus() != null && listing.getPostStatus().equals(PostStatus.SAVED)) {
//            filtered.add(listing);
//          }
//        }
//        break;
//      case 3: //Closed
//        for (Post listing : listings) {
//          if (listing.getPostStatus() != null && listing.getPostStatus().equals(PostStatus.DELETED)) {
//            filtered.add(listing);
//          }
//        }
//        break;
//    }
//
//    if (filtered.size() == 0) {
//      String s = "No Listings ";
//      String s1 = context.getResources().getStringArray(R.array.manage_exp_spinner)[spinner.getSelectedItemPosition()];
//      ((TextView) view.findViewById(R.id.greyOut)).setText(s + s1);
//      view.findViewById(R.id.greyOut).setVisibility(View.VISIBLE);
//    } else {
//      view.findViewById(R.id.greyOut).setVisibility(View.GONE);
//    }
//
//    if (recyclerView != null && recyclerView.getAdapter() != null) {
//      ((ManageListingAdapter) recyclerView.getAdapter()).updateData(filtered);
//    } else {
//      setUpRecycler(filtered);
//    }
//
//    if (swipe.isRefreshing()) {
//      swipe.setRefreshing(false);
//    }
//
//  }

  void setUpRecycler(ArrayList<Post> requests) {
    if (requests != null && requests.size() > 0) {
      view.findViewById(R.id.greyOut).setVisibility(View.GONE);
      recyclerView = view.findViewById(R.id.recycler);
//      recyclerView
//          .setLayoutManager(new GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false));
      recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
      ManageListingAdapter adapter = new ManageListingAdapter(context, requests);
      recyclerView.setAdapter(adapter);
    } else {
      String s = "No Posts";
      String s1 = context.getResources().getStringArray(R.array.manage_exp_spinner)[spinner.getSelectedItemPosition()];
      ((TextView) view.findViewById(R.id.greyOut)).setText(s + s1);
      view.findViewById(R.id.greyOut).setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void onPause() {
    super.onPause();
  }

}
