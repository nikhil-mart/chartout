package com.locolhive.chartout.create;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.locolhive.chartout.R;
import com.locolhive.chartout.api.UploadVideoApi;
import com.locolhive.chartout.classes.Post;
import com.locolhive.chartout.helpers.CustomViewPager;
import com.locolhive.chartout.interfaces.OnResultListener;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.FabSpeedDial.MenuListener;
import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("FieldCanBeLocal")
public class CreateActivity extends AppCompatActivity {

  private static final String TAG = CreateActivity.class.getSimpleName() + " YOYO";
  public static final int GET_MultiplePics_CODE = 1;
  public static final int GET_SinglePic_CODE = 5;

  public static final String KEY_EditMode = "KEY_EditMode";
  public static final String KEY_Post = "KEY_Post";

  private SectionsPagerAdapter mSectionsPagerAdapter;
  public CustomViewPager mViewPager;
  private TabLayout tabLayout;
  public FabSpeedDial fab;
  public GoogleApiClient mGoogleApiClient;
  public UploadVideoApi api;
  public OnResultListener<ArrayList<Uri>> photoListener;
  boolean editmode = false;
  public PostRequest request;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create);

    Toolbar toolbar = findViewById(R.id.toolbar);
    toolbar.setNavigationOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    setSupportActionBar(toolbar);
    Log.i(TAG, "onCreate: ");
    fab = findViewById(R.id.speed);
    setUpTabs();

//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//      getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//    }

    request = new PostRequest();
    Bundle extras = getIntent().getExtras();
    if(extras!=null){
      if(extras.containsKey(KEY_EditMode)){
        editmode = true;
        Post post = extras.getParcelable(KEY_Post);
        request = new PostRequest(post);
      }
    }

    mGoogleApiClient = new GoogleApiClient
        .Builder(CreateActivity.this)
        .addApi(Places.GEO_DATA_API)
        .enableAutoManage(CreateActivity.this, new GoogleApiClient.OnConnectionFailedListener() {
          @Override
          public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Log.e(TAG, "onConnectionFailed: " + connectionResult.getErrorMessage());
          }
        }).build();

  }

  public void nxt(){
    if(mViewPager.getCurrentItem()<2){
      mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1, true);
    }
  }

  private void setUpTabs() {
    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

    mViewPager = findViewById(R.id.container);
    mViewPager.setAdapter(mSectionsPagerAdapter);

    tabLayout = findViewById(R.id.tabs);

    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if ((position == 1 && positionOffset < 0.1f) || (position == 0 && positionOffset > 0.9f)) {
          fab.show();
        } else {
          fab.hide();
        }
      }

      @Override
      public void onPageSelected(int position) {
//        if (position == 1) {
//          fab.setLayoutParams(new CoordinatorLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//        } else {
//          LayoutParams layoutParams = fab.getLayoutParams();
//          layoutParams.height = 0;
//          layoutParams.width = 0;
//          fab.setLayoutParams(layoutParams);
//        }
//        if(position==1){
//          fab.show();
//        }else {
//          fab.hide();
//        }
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });

    mViewPager.setPagingDisabled(true);

  }

  public class SectionsPagerAdapter extends FragmentPagerAdapter {

    SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return FragBasicDetails.newInstance();
        case 1:
          return FragSections.newInstance();
        case 2:
          return FragFinalize.newInstance();
      }
      return null;
    }

    @Override
    public int getCount() {
      return 3;
    }

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == GET_MultiplePics_CODE) {
      // Make sure the request was successful
      if (resultCode == RESULT_OK && data != null) {
        ClipData clipData = data.getClipData();
        if (photoListener != null) {
          if (clipData != null) {
            ArrayList<Uri> list = new ArrayList<>();
            for (int i = 0; i < clipData.getItemCount(); i++) {
              list.add(clipData.getItemAt(i).getUri());
            }
            photoListener.OnResult(list);
          } else if (data.getData() != null) {
            photoListener.OnResult(new ArrayList<>(Collections.singleton(data.getData())));
          }
          photoListener = null;
        }
      }
    } else if (requestCode == GET_SinglePic_CODE) {
      // Make sure the request was successful
      if (resultCode == RESULT_OK && data != null) {
        if (photoListener != null) {
          if (data.getData() != null) {
            photoListener.OnResult(new ArrayList<>(Collections.singleton(data.getData())));
            photoListener = null;
          }
        }
      }
    } else {
      api.onActivityResult(requestCode, resultCode, data);
    }
  }

}
