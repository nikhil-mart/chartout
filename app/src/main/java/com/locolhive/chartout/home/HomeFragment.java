package com.locolhive.chartout.home;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;
import com.locolhive.chartout.R;
import com.locolhive.chartout.adapters.DestinationRecyclerAdapter;
import com.locolhive.chartout.adapters.ListingRecyclerAdapter;
import com.locolhive.chartout.adapters.RecentRecyclerAdapter;
import com.locolhive.chartout.api.ListingApi;
import com.locolhive.chartout.classes.Category;
import com.locolhive.chartout.classes.Post;
import com.locolhive.chartout.databinding.FragmentHomeBinding;
import com.locolhive.chartout.helpers.DialogProgress;
import com.locolhive.chartout.home.MainActivity.OnRefreshListener;
import com.locolhive.chartout.interfaces.OnBackPressListener;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

  public static final String TAG = HomeFragment.class.getSimpleName() + " YOYO";

  private FragmentHomeBinding binding;
  private Context context;
  private FragmentManager fragmentManager;
  private ListingApi pms;
  private DialogProgress dialogProgress;
  private boolean firstTime = true;

  private boolean timer;

  public HomeFragment() {
    // Required empty public constructor
  }

  public static HomeFragment newInstance() {
    HomeFragment fragment = new HomeFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.i(TAG, "onCreate: ");
    fragmentManager = getFragmentManager();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
    return binding.getRoot();
  }

  @Override
  public void onResume() {
    super.onResume();
    Log.i(TAG, "onResume: ");
    context = getContext();
    setUpListeners();
    GlobalData globalData = (GlobalData) context.getApplicationContext();

    binding.rvRecent.requestFocus();

    if (!((MainActivity) context).newIntent) {
      if (globalData.currentAddress != null && globalData.currentLocation != null) {
        refresh();
      }
      ((MainActivity) context).newIntent = false;
    } else {
      if (binding.rvRecent.getAdapter() == null) {
        refresh();
      }
    }
    timer = false;

    ((MainActivity) context).backListener = new OnBackPressListener() {
      @Override
      public void onBackPress() {
        if (timer) {
          Intent a = new Intent(Intent.ACTION_MAIN);
          a.addCategory(Intent.CATEGORY_HOME);
          a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(a);
        } else {
          Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show();
          timer = true;
          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              timer = false;
            }
          }, 1500);
        }
      }
    };
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        try {
          fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } catch (IllegalStateException ignored) {
          // There's no way to avoid getting this if saveInstanceState has already been called.
        }
      }
    }, 500);
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
//    super.onSaveInstanceState(outState);
  }

  private void setUpListeners() {

    ((MainActivity) context).listener = new OnRefreshListener() {
      @Override
      public void onRefresh() {
        Log.i(TAG, "OnRefresh Listener: ");
        refresh();
      }
    };

//    view.findViewById(R.id.activity_viewAll_btn).setOnClickListener(new OnClickListener() {
//      @Override
//      public void onClick(View v) {
//
//        fragmentManager.beginTransaction()
//            .replace(R.id.home_container, ActivityByCategories.newInstance(false))
//            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//            .addToBackStack(null)
//            .commit();
//
//      }
//    });
//
//    view.findViewById(R.id.place_viewAll_btn).setOnClickListener(new OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        fragmentManager.beginTransaction()
//            .replace(R.id.home_container, ActivityByCategories.newInstance(true))
//            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//            .addToBackStack(null)
//            .commit();
//      }
//    });
//
//    view.findViewById(R.id.cat1_viewAll_btn).setOnClickListener(new OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        ActivityByCategories frag_main = ActivityByCategories.newInstance(false);
//        fragmentManager.beginTransaction()
//            .replace(R.id.home_container, frag_main)
//            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//            .addToBackStack(null)
//            .commit();
//      }
//    });
//
//    view.findViewById(R.id.cat2_viewAll_btn).setOnClickListener(new OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        ActivityByCategories frag_main = ActivityByCategories.newInstance(true);
//        fragmentManager.beginTransaction()
//            .replace(R.id.home_container, frag_main)
//            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//            .addToBackStack(null)
//            .commit();
//      }
//    });

  }

  private void setUpRecyclers() {

    binding.rvRecent.setHasFixedSize(true);
    binding.rvRecent.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    binding.rvRecent.setAdapter(new RecentRecyclerAdapter(context, new ArrayList<Category>()));
    binding.rvRecent.setNestedScrollingEnabled(false);

    binding.rvByDest.setHasFixedSize(true);
    binding.rvByDest.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    binding.rvByDest.setAdapter(new DestinationRecyclerAdapter(context, new ArrayList<Category>()));
    binding.rvByDest.setNestedScrollingEnabled(false);

    binding.rvTripIdeas.setHasFixedSize(true);
    binding.rvTripIdeas.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    binding.rvTripIdeas.setAdapter(new ListingRecyclerAdapter(context, new ArrayList<Post>(), null));
    binding.rvTripIdeas.setNestedScrollingEnabled(false);

    binding.rvVacationPlans.setHasFixedSize(true);
    binding.rvVacationPlans.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    binding.rvVacationPlans.setAdapter(new ListingRecyclerAdapter(context, new ArrayList<Post>(), null));
    binding.rvVacationPlans.setNestedScrollingEnabled(false);

    ViewTreeObserver vto = binding.rvVacationPlans.getViewTreeObserver();
    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        if (binding.rvVacationPlans.getChildCount() > 0 && firstTime){
          firstTime = false;
          dialogProgress.done();
          Toast.makeText(context, "DONE", Toast.LENGTH_SHORT).show();
        }
      }
    });

  }

  public void refresh() {
    Log.i(TAG, "refreshActivity: ");

    if (dialogProgress==null)
      dialogProgress = DialogProgress.newInstance();

//    dialogProgress.displayNow(context); todo

    if (pms == null) {
      pms = new ListingApi(getContext());
    }

    setUpRecyclers();

//    pms.getAllActivities(4, null, new OnTaskCompleteListener<Listing[]>() {
//      @Override
//      public void onSuccess(Listing[] response) {
//
//        if (response == null) {
//          response = new Listing[0];
//        }
//
////        if (response.length == 0) {
////          tvGrey2.setVisibility(View.VISIBLE);
////        } else {
////          tvGrey1.setVisibility(View.GONE);
////        }
////
////        ArrayList<Listing> listings = new ArrayList<>(Arrays.asList(response));
////
////        if (binding.rvRecent.getAdapter() != null) {
////          ((ListingRecyclerAdapter) binding.rvRecent.getAdapter()).updateData(listings);
////        } else {
////          ListingRecyclerAdapter postAdapter = new ListingRecyclerAdapter(getActivity(), listings, false);
////          binding.rvRecent.setAdapter(postAdapter);
////          binding.rvRecent.setLayoutManager(new GridLayoutManager(getActivity(), 2) {
////            @Override
////            public boolean canScrollVertically() {
////              return false;
////            }
////          });
////        }
//
//        if (binding.homeSwipe.isRefreshing()) {
//          binding.homeSwipe.setRefreshing(false);
//        }
//
//      }
//
//      @Override
//      public void onFailure(String error) {
//        if (binding.homeSwipe.isRefreshing()) {
//          binding.homeSwipe.setRefreshing(false);
//        }
//        Toast.makeText(getContext(), "Error getting data", Toast.LENGTH_SHORT).show();
//      }
//    });
//
//    pms.getActivityCategories(new OnTaskCompleteListener<Category[]>() {
//      @Override
//      public void onSuccess(Category[] response) {
//
//        if (response == null) {
//          response = new Category[0];
//        }
//
//        int x = 8;
//        if (response.length < 8) {
//          x = response.length;
//        }
//
////        ArrayList<Category> cats = new ArrayList<>(Arrays.asList(response).subList(0, x));
////
////        if (cat1RV.getAdapter() != null) {
////          ((CategoryRecyclerAdapter) cat1RV.getAdapter()).updateData(cats);
////        } else {
////          CategoryRecyclerAdapter adapter = new CategoryRecyclerAdapter(getActivity(), cats);
////
////          cat1RV.setAdapter(adapter);
////
////          cat1RV.setLayoutManager(new GridLayoutManager(getActivity(), 4) {
////            @Override
////            public boolean canScrollVertically() {
////              return false;
////            }
////          });
//
////          if (binding.homeSwipe.isRefreshing()) {
////            binding.homeSwipe.setRefreshing(false);
////          }
////        }
//      }
//
//      @Override
//      public void onFailure(String error) {
//        if (getContext() != null) {
//          //Toast.makeText(getContext(), "Error getting categories", Toast.LENGTH_SHORT).show();
//          Log.e(TAG, "onFailure getActCat: " + error);
//        }
//      }
//    });

  }

  @Override
  public void onDestroyView() {
    Log.i(TAG, "onDestroyView: ");
    if (context != null) {
      ((MainActivity) context).backListener = null;
    }
    super.onDestroyView();
  }

}
