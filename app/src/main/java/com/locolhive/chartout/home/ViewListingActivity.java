package com.locolhive.chartout.home;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.FileProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.ShareActionProvider.OnShareTargetSelectedListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import com.locolhive.chartout.R;
import com.locolhive.chartout.adapters.ViewContentAdapter;
import com.locolhive.chartout.api.ListingApi;
import com.locolhive.chartout.classes.Content;
import com.locolhive.chartout.classes.Post;
import com.locolhive.chartout.interfaces.OnResultListener;
import com.locolhive.chartout.profile.UserProfile;
import com.locolhive.chartout.statics.ContentType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ViewListingActivity extends AppCompatActivity {

  public static final String KEY_Preview = "KEY_Preview";
  public static final String KEY_Listing = "KEY_Listing";
  private static final String TAG = ViewListingActivity.class.getSimpleName() + " YOYO";
  private Context context;
  private ShareActionProvider actionProvider;
  private Intent intent;

  private Boolean isPreview;
  //  SimpleViewPager backDrop;
  private MenuItem bookmark;
  private Boolean isBookmarked;
  private Post listing;
  private UserProfile user;
  private ListingApi api;

  private SectionsPagerAdapter mSectionsPagerAdapter;
  private ViewPager mViewPager;
  private TabLayout tabLayout;

  ArrayList<String> allPics;
  String firstVideo;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_listing);

    isPreview = false;
    Intent i = getIntent();
    Bundle extras = i.getExtras();
    if (extras != null) {
      if (extras.containsKey(KEY_Listing)) {
        listing = (Post) extras.get(KEY_Listing);
      }

      if (extras.containsKey(KEY_Preview)) {
        isPreview = extras.getBoolean(KEY_Preview);
      }
    }

    initVariables();

    setUpToolbar();

    setData();

  }

  @SuppressLint("UseSparseArrays")
  void initVariables() {

    context = ViewListingActivity.this;

//    backDrop = findViewById(R.id.backDrop);
    isBookmarked = false;

    api = new ListingApi(context);

    ((GlobalData) context.getApplicationContext()).getUser(context, new OnResultListener<UserProfile>() {
      @Override
      public void OnResult(UserProfile result) {
        user = result;
      }
    });
  }

  void setUpToolbar() {

    Toolbar toolbar = findViewById(R.id.toolbar);
    toolbar.setNavigationOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.ctl_Toolbar);
    collapsingToolbarLayout.setTitleEnabled(false);
    collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.mast));

  }

  void setData() {

    //region Images
    allPics = new ArrayList<>();

    if (listing != null) {
      if (listing.getPrimaryImage() != null && !listing.getPrimaryImage().equals("")) {
        allPics.add(listing.getPrimaryImage());
      }
      if(listing.getContent()!=null){
        for (Content content : listing.getContent()) {
          if(content.getType().equals(ContentType.IMAGE)){
            allPics.addAll(content.getPics());
          }
          if(allPics.size()>16) //MAX PICS
            break;
        }
      }
    }

    if (allPics.size() == 0) {
      allPics.add("fcdd4edd-44c0-4cb4-aafa-5147a12291fd");
      allPics.add("fcdd4edd-44c0-4cb4-aafa-5147a12291fd");
    }

    setUpPager();
    //endregion

//      api.isFavorite(listing.getId(), new OnTaskCompleteListener<BooleanObject>() {
//        @Override
//        public void onSuccess(BooleanObject response) {
//          isBookmarked = response.isFavourite;
//          if (bookmark != null) {
//            bookmark.setChecked(isBookmarked);
//            if (isBookmarked) {
//              bookmark.setIcon(R.drawable.ic_heart_fill);
//            } else {
//              bookmark.setIcon(R.drawable.ic_heart);
//            }
//          }
//        }
//
//        @Override
//        public void onFailure(String error) {
//
//        }
//      });

//      api.getReviews(listing.getId(), new OnTaskCompleteListener<Review[]>() {
//        @Override
//        public void onSuccess(Review[] response) {
//          if (response != null && response.length > 0) {
//            ReviewAdapter adapter = new ReviewAdapter(new ArrayList<>(Arrays.asList(response)), context);
//            review_RV.setAdapter(adapter);
//            review_RV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
//          } else {
//            tvReview.setVisibility(View.GONE);
//            review_RV.setVisibility(View.GONE);
//          }
//        }
//
//        @Override
//        public void onFailure(String error) {
//          tvReview.setVisibility(View.GONE);
//          review_RV.setVisibility(View.GONE);
//        }
//      });

    RecyclerView recyclerView = findViewById(R.id.content);
    recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    recyclerView.setAdapter(ViewContentAdapter.newInstance(new Post(), ViewListingActivity.this));

  }

  void setUpPager() {
    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

    mViewPager = findViewById(R.id.pager);
    mViewPager.setAdapter(mSectionsPagerAdapter);

    tabLayout = findViewById(R.id.tabs);

    mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_view_listing, menu);

//    Utils.TintMenu(getResources().getColor(R.color.white), menu);

    MenuItem item = menu.findItem(R.id.action_share);
    if (listing != null) {
      Log.i(TAG, "onCreateOptionsMenu: ");
      actionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
      if (intent != null) {
        actionProvider.setShareIntent(intent);
      }
    }

    bookmark = menu.findItem(R.id.action_heart);
    bookmark.setCheckable(true);
    bookmark.setEnabled(true);

    bookmark.setChecked(isBookmarked);
    bookmark.setOnMenuItemClickListener(new OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
        if (isBookmarked) {
          bookmark.setIcon(R.drawable.ic_heart);
          isBookmarked = false;
        } else {
          bookmark.setIcon(R.drawable.ic_heart_fill);
          isBookmarked = true;
        }
//        api.markFavorite(listing.getId(), isBookmarked, new OnTaskCompleteListener<String>() {
//          @Override
//          public void onSuccess(String response) {
//            Toast.makeText(ViewListingActivity.this, "Added to Favorites!", Toast.LENGTH_SHORT).show();
//          }
//
//          @Override
//          public void onFailure(String error) {
//            if (isBookmarked) {
//              bookmark.setIcon(R.drawable.ic_heart);
//              isBookmarked = false;
//            } else {
//              bookmark.setIcon(R.drawable.ic_heart_fill);
//              isBookmarked = true;
//            }
//            Toast.makeText(ViewListingActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();
//          }
//        });
        return true;
      }
    });

    return true;
  }

  void setShareIntent(ImageView imageView) {

    Intent sendIntent = new Intent(Intent.ACTION_SEND);

    if (!isPreview) {

      final String id = listing.getId();
      sendIntent.putExtra(Intent.EXTRA_TEXT, listing.getTitle() + "\nhttp://www.locolhive.com/post/" + id);
      actionProvider.setOnShareTargetSelectedListener(new OnShareTargetSelectedListener() {
        @Override
        public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
          ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
          ClipData clip;
//              clip = ClipData.newPlainText("label", "Text to copy");
          clip = ClipData.newRawUri(listing.getTitle(), Uri.parse("http://www.locolhive.com/post/" + id));
          assert clipboard != null;
          clipboard.setPrimaryClip(clip);

          Toast.makeText(context, "Post link added to clipboard", Toast.LENGTH_LONG).show();
          return false;
        }
      });

      if (listing.getPrimaryImage() != null) {

        imageView.setDrawingCacheEnabled(true);
        imageView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        imageView.layout(0, 0,
            imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
        imageView.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
        imageView.setDrawingCacheEnabled(false);

//    Bitmap bitmap = ((android.graphics.drawable.BitmapDrawable) imageView.getDrawable()).getBitmap();

        if (bitmap != null) {

          File file = new File(this.getExternalCacheDir(), "image.jpeg");
          FileOutputStream fOut = null;
          try {
            fOut = new FileOutputStream(file);
            bitmap.compress(CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);

            Uri uri;
            if (VERSION.SDK_INT >= VERSION_CODES.N) {
              uri = FileProvider.getUriForFile(
                  context,
                  context.getApplicationContext()
                      .getPackageName() + ".provider", file);
            } else {
              uri = Uri.fromFile(file);
            }
            sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
            sendIntent.setType("image/*");
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (actionProvider != null) {
              actionProvider.setShareIntent(sendIntent);
            } else {
              intent = sendIntent;
            }

          } catch (FileNotFoundException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      } else {
        sendIntent.setType("text/*");
        if (actionProvider != null) {
          actionProvider.setShareIntent(sendIntent);
        } else {
          intent = sendIntent;
        }
      }
    }

    //      if(listing.getPrimaryImageId()!=null) {
//        Uri uri = ImageUtils.getUri(listing.getPrimaryImageId());
//        ArrayList<Uri> list = new ArrayList<>();
//        list.add(uri);
//        sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, list);
////        sendIntent.putExtra(Intent.EXTRA_STREAM, list);
//        sendIntent.setType("image/*");
//      }else {
//        sendIntent.setType("text/plain");
//      }
//
//      sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
////        sendIntent.putExtra(Intent.EXTRA_TEXT, "https://www.locolhive.com/post/"+id);
//
//      actionProvider.setShareIntent(sendIntent);
  }

  public class SectionsPagerAdapter extends FragmentPagerAdapter {

    SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return PhotoFrag.newInstance(allPics);
        case 1:
          return VideoFrag.newInstance(firstVideo);
      }
      return null;
    }

    @Override
    public int getCount() {
      return 2;
    }

  }

}
