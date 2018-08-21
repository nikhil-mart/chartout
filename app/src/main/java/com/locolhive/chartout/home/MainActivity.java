package com.locolhive.chartout.home;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.AsyncLayoutInflater;
import android.support.v4.view.AsyncLayoutInflater.OnInflateFinishedListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.locolhive.chartout.R;
import com.locolhive.chartout.classes.Post;
import com.locolhive.chartout.helpers.BottomNavigationViewEx;
import com.locolhive.chartout.helpers.LocationHelper;
import com.locolhive.chartout.helpers.Navigation;
import com.locolhive.chartout.interfaces.GetLocationListener;
import com.locolhive.chartout.interfaces.OnBackPressListener;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName() + " YOYO";
  private static final int SELECT_LOCATION = 56;
  public OnBackPressListener backListener;
  public boolean newIntent;
  TextView tv_currentLocation;
  ConstraintLayout locationContainer;
  BottomNavigationViewEx bottomNavigationView;
  FragmentManager fragmentManager;
  LocationHelper locationHelper;
  OnRefreshListener listener;
  GlobalData globalData;

  @SuppressLint("StaticFieldLeak")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.i(TAG, "onCreate: ");
//    setContentView(R.layout.activity_main);
    new AsyncLayoutInflater(MainActivity.this)
        .inflate(R.layout.activity_main, null, new OnInflateFinishedListener() {
          @Override
          public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {

            setContentView(view);

            initVariables();
            newIntent = false;
            setUpToolbars();
            setUpListeners();

            bottomNavigationView.setCurrentItem(0);

            HomeFragment frag_main = HomeFragment.newInstance();
//    frag_main.setRetainInstance(true);

            fragmentManager.beginTransaction()
                .replace(R.id.home_container, frag_main, HomeFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
          }
        });

  }

  @Override
  protected void onNewIntent(Intent intent) {
    Log.i(TAG, "newIntent: ");
    newIntent = true;
    bottomNavigationView.setCurrentItem(0);
    super.onNewIntent(intent);
  }

  @Override
  protected void onResume() {
    Log.i(TAG, "onResume: ");
//    bottomNavigationView.setCurrentItem(0);
//    if (newIntent) {
//      goToHome();
//    }
    super.onResume();
  }

  @Override
  public View onCreateView(String name, Context context, AttributeSet attrs) {
    Log.i(TAG, "onCreateView: ");
    return super.onCreateView(name, context, attrs);
  }

  @Override
  public void onContentChanged() {
    Log.i(TAG, "onContentChanged: ");
    super.onContentChanged();
  }

  void initVariables() {

    tv_currentLocation = findViewById(R.id.tv_location);
    fragmentManager = getSupportFragmentManager();
    locationContainer = findViewById(R.id.location_container);
    globalData = ((GlobalData) getApplication());

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        locationHelper = new LocationHelper(MainActivity.this, LocationHelper.TYPE_MAIN);
        globalData.currentRadius = getResources().getInteger(R.integer.DefaultRadius);

        String address = globalData.currentAddress;
        Location location = globalData.currentLocation;
        if (address != null && location != null) {
          updateUI();
        } else if (!globalData.getSavedLocation()) {

          locationHelper.getAddress(new GetLocationListener() {
            @Override
            public void onSuccess(Location location, String response) {
              Log.i(TAG, "getAddress onSuccess: ");
              globalData.saveCurrentFilter(location, response);
              updateUI();
            }

            @Override
            public void onFailure(String error) {
              Log.e(TAG, "getAddress onFailure: " + error);
              globalData.currentLocation = new Location("");
              globalData.currentLocation.setLatitude(12.9126701);
              globalData.currentLocation.setLongitude(77.6387760);
              globalData.currentAddress = "Bangalore";
              updateUI();
            }
          });
        } else {
          updateUI();
        }

      }
    }, 1000);

    final AppBarLayout mAppBarLayout = findViewById(R.id.appbar);
    final FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mAppBarLayout.setExpanded(true, true);
      }
    });
    mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

      int scrollRange = -1;

      @Override
      public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if (scrollRange == -1) {
          scrollRange = appBarLayout.getTotalScrollRange();
        }

        Log.i(TAG, "onOffsetChanged: " + (scrollRange + verticalOffset));

        if (scrollRange + verticalOffset == 0) {
          fab.setVisibility(View.VISIBLE);
        } else {
          fab.setVisibility(View.INVISIBLE);
        }

      }
    });

  }

  void updateUI() {

    Log.i(TAG, "updateUI: ");

    String address = globalData.currentAddress;
    if (address != null) {
      tv_currentLocation.setText(address);
    }

    if (listener != null) {
      listener.onRefresh();
    }

  }

  public void setUpListeners() {

    locationContainer.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.i(TAG, "onClick: ");
        startActivityForResult(new Intent(MainActivity.this, SelectLocation.class),
            SELECT_LOCATION);
      }
    });

  }

  //region Navigation

  void setUpToolbars() {
    Log.i(TAG, "setUpToolbars: ");
    Toolbar toolbar = findViewById(R.id.toolbar);
    toolbar.setNavigationOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });
    setSupportActionBar(toolbar);
    bottomNavigationView = findViewById(R.id.bottomNav);
    bottomNavigationView.setCurrentItem(0);
    Navigation.setUpBottomNav(MainActivity.this, bottomNavigationView);
  }

  void goToHome() {
    HomeFragment fragmentByTag = (HomeFragment) fragmentManager.findFragmentByTag(HomeFragment.TAG);
    if (fragmentByTag != null) {
      if (!fragmentByTag.isVisible()) {
        fragmentManager.beginTransaction()
            .replace(R.id.home_container, fragmentByTag, HomeFragment.TAG)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commitAllowingStateLoss();
      } else {

      }
    } else {
      HomeFragment frag_main = HomeFragment.newInstance();
      frag_main.setRetainInstance(true);
      fragmentManager.beginTransaction()
          .replace(R.id.home_container, frag_main, HomeFragment.TAG)
          .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
          .commit();
    }
  }

  public void switchContent(SingleCategoryFragment fragment) {
    fragmentManager.beginTransaction()
        .replace(R.id.home_container, fragment)
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        .addToBackStack(null)
        .commit();
  }

  public void goToViewListing(Post listing, Boolean isPlace) {
//    Intent intent = new Intent(MainActivity.this, ViewListingActivity.class);
//
//    intent.putExtra(ViewListingActivity.KEY_IsPlace, isPlace);
//    intent.putExtra(ViewListingActivity.KEY_Listing, listing);
//
//    startActivity(intent);
  }

  //endregion

  //region Callbacks

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {

    if (requestCode == LocationHelper.PERMISSION_REQUEST_CODE) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        Log.i(TAG, "onPermissionResult: granted");
        locationHelper.isGranted = true;
        locationHelper.checkAndExecute();
      } else {
        Log.i(TAG, "onPermissionResult: not granted");
        locationHelper.isGranted = false;
      }
    } else if (requestCode == LocationHelper.REQUEST_CHECK_SETTINGS) {
      locationHelper.checkAndExecute();
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    Log.i(TAG, "onActivityResult: ");
    if (requestCode == SELECT_LOCATION) {
      if (resultCode == RESULT_OK) {
        updateUI();
      }
    } else if (requestCode == LocationHelper.REQUEST_CHECK_SETTINGS) {
      locationHelper.checkAndExecute();
    } else {

    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public void onBackPressed() {
    if (backListener != null) {
      backListener.onBackPress();
    } else {
      super.onBackPressed();
    }
  }

  @Override
  protected void onDestroy() {
    Log.i(TAG, "onDestroy: ");
    super.onDestroy();
  }

  @Override
  protected void onPause() {
    Log.i(TAG, "onPause: ");
    super.onPause();
  }

  public interface OnRefreshListener {

    void onRefresh();
  }

  //endregion


}
