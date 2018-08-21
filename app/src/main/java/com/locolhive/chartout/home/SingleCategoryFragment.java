package com.locolhive.chartout.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.locolhive.chartout.R;
import com.locolhive.chartout.adapters.ListingRecyclerAdapter;
import com.locolhive.chartout.api.ListingApi;
import com.locolhive.chartout.classes.Category;

public class SingleCategoryFragment extends Fragment {

  private static final String TAG = SingleCategoryFragment.class.getSimpleName() + " YOYO";
  static int STEP_SIZE = 30;
  TextView tvCat;
  RecyclerView activityRV;
  Category category;
  SwipeRefreshLayout swipeRefreshLayout;
  int num;

  ListingApi pms;

  public SingleCategoryFragment() {
    // Required empty public constructor
  }

  public static SingleCategoryFragment newInstance(Category cat) {
    SingleCategoryFragment fragment = new SingleCategoryFragment();
    Bundle args = new Bundle();
    if(cat!=null)
      args.putParcelable("Category", cat);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onResume() {
    super.onResume();

    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        refreshActivity();
      }
    });

    swipeRefreshLayout.setRefreshing(true);
    refreshActivity();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    if (getArguments() != null) {
      Bundle bundle = getArguments();
      category = bundle.getParcelable("Category");
    }
    if(category==null){
      Log.e(TAG, "onCreateView: " + "Null Args");
      category = new Category();
      category.setCategoryId("e460d34e-5b08-4031-a028-4d35a5ca2391");
      category.setCategory("Random Category");
    }

    View view = inflater.inflate(R.layout.fragment_full_category, container, false);
    activityRV = view.findViewById(R.id.home_category_full_fragmentRV);
    tvCat = view.findViewById(R.id.category_full);
    tvCat.setText(category.getCategory());
    swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);

    num = STEP_SIZE;

    return view;
  }

  void refreshActivity() {

    if (pms == null) {
      pms = new ListingApi(getContext());
    }

    String id;
    if (category != null) {
      id = category.getCategoryId();
    } else {
      id = null;
    }

    activityRV.setAdapter(new ListingRecyclerAdapter(getActivity(), null, true));
    activityRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

    swipeRefreshLayout.setRefreshing(false);

//    Call Api Get Data
//    pms.getAllActivities(num, id, new OnTaskCompleteListener<Post[]>() {
//      @Override
//      public void onSuccess(Post[] response) {
//
//        ArrayList<Post> listings = new ArrayList<>(Arrays.asList(response));
//
//        if (activityRV.getAdapter() != null) {
//          ((ListingRecyclerAdapter) activityRV.getAdapter()).updateData(listings);
//        } else {
//          activityRV.setAdapter(new ListingRecyclerAdapter(getActivity(), listings, false));
//
//          activityRV.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//        }
//
//        swipeRefreshLayout.setRefreshing(false);
//
//      }
//
//      @Override
//      public void onFailure(String error) {
//        swipeRefreshLayout.setRefreshing(false);
//        Toast.makeText(getContext(), "Error getting posts", Toast.LENGTH_SHORT).show();
//      }
//    });

  }


}
