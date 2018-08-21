package com.locolhive.chartout.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.locolhive.chartout.R;
import com.locolhive.chartout.adapters.PhotoAdapter;
import com.locolhive.chartout.api.ListingApi;
import com.locolhive.chartout.classes.HostPublicProfile;
import com.locolhive.chartout.classes.Post;
import com.locolhive.chartout.classes.SkillSet;
import com.locolhive.chartout.helpers.DataOpeners;
import com.locolhive.chartout.helpers.GlideApp;
import com.locolhive.chartout.helpers.ImageUtils;
import com.locolhive.chartout.helpers.PhotoFullDialog;
import com.locolhive.chartout.helpers.Utils;
import com.locolhive.chartout.home.GlobalData;
import com.locolhive.chartout.interfaces.OnResultListener;
import com.locolhive.chartout.interfaces.OnTaskCompleteListener;
import com.locolhive.chartout.statics.Hobby;
import java.util.ArrayList;

public class MyProfileActivity extends AppCompatActivity {

  public static final String KEY_Profile = "KEY_Profile";
  private static final String TAG = MyProfileActivity.class.getSimpleName() + " YOYO";

//  private static final int ANIMATION_DURATION = 300;
//
//  private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
//  private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
//  private static final int ALPHA_ANIMATIONS_DURATION              = 200;
//
//  private boolean mIsTheTitleVisible          = false;
//  private boolean mIsTheTitleContainerVisible = true;
//
//  private LinearLayout mTitleContainer;
//  private TextView mTitle;
//  private AppBarLayout mAppBarLayout;
//  private Toolbar mToolbar;

  ImageView hostImage;
  AppCompatRatingBar ratingBar;
  TextView rating;
  TextView jobLocation;
  TextView shortDesc;
  TextView hobbies;
  RecyclerView created;
  TextView tv_created;
  Button viewAll;
  DataOpeners openers;
  TextView tv_pics;
  RecyclerView pics;
  TextView tv_reviewsRV;
  RecyclerView reviews;

  Toolbar toolbar;

  Boolean isMine;

  UserProfile user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_profile);

    isMine = true;

    Intent intent = getIntent();
    if (intent != null) {
      Bundle extras = intent.getExtras();
      if (extras != null && extras.containsKey(KEY_Profile)) {
        isMine = false;
        HostPublicProfile profile = extras.getParcelable(KEY_Profile);
        assert profile != null;
        user = new UserProfile(profile);
      }
    }

    if (user == null) {
      ((GlobalData) getApplication()).getUser(MyProfileActivity.this, new OnResultListener<UserProfile>() {
        @Override
        public void OnResult(UserProfile result) {
          user = result;
          initVariables();
          setUpToolbar();
          setOpeners();
          setData();
        }
      });
    } else {
      initVariables();
      setUpToolbar();
      setOpeners();
      setData();
    }

  }

  void initVariables() {
    hostImage = findViewById(R.id.hostImage);
    ratingBar = findViewById(R.id.rating);
    rating = findViewById(R.id.tv_rating);
    jobLocation = findViewById(R.id.tv_job_location);
    shortDesc = findViewById(R.id.tv_aboutme);
    hobbies = findViewById(R.id.tv_interests);

    created = findViewById(R.id.created_RV);
    tv_created = findViewById(R.id.tv_created);
    viewAll = findViewById(R.id.viewAll);

    openers = new DataOpeners(MyProfileActivity.this);
    tv_pics = findViewById(R.id.tv_pics);
    pics = findViewById(R.id.hostPics);
    tv_reviewsRV = findViewById(R.id.tv_reviewsRV);
    reviews = findViewById(R.id.reviewsRV);
  }

  void setUpToolbar() {
    toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//    BottomNavigationViewEx bottomNavigationView = findViewById(R.id.bottomNav);
//    bottomNavigationView.setCurrentItem(4);
//    Navigation.setUpBottomNav(MyProfileActivity.this, bottomNavigationView);
  }

  @SuppressLint("CheckResult")
  void setData() {

    if (user != null) {

      if (user.getProfilePicId() != null) {

        final ImageView blur = findViewById(R.id.blur);

        hostImage.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            PhotoFullDialog.newInstance(MyProfileActivity.this, hostImage, ImageUtils.getUri(user.getProfilePicId())).display();
          }
        });

        GlideApp.with(this)
            .asBitmap()
            .load(ImageUtils.getUri(user.getProfilePicId())) // TODO: 12-Jun-18
            .apply(RequestOptions.centerCropTransform())
            .listener(new RequestListener<Bitmap>() {
              @Override
              public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                return true;
              }

              @Override
              public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                hostImage.setImageBitmap(resource);
                blur.setBackground(
                    new BitmapDrawable(getResources(), ImageUtils.fastblur(Bitmap.createScaledBitmap(resource, 50, 50, true))));
                return true;
              }
            })
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(hostImage);
      }

      toolbar.setTitle(user.getFullName());
//      ratingBar.setNumStars(user.get);
      rating.setText(String.format(getString(R.string.profile_rating), "3", "15"));

      if (user.getCurrentCity() != null) {
        jobLocation.setText(user.getCurrentCity());
      } else {
        jobLocation.setText("Bangalore, India");
      }

      shortDesc.setText(user.getShortDescription());
      ArrayList<Hobby> list = user.getHobbies();
      String s2 = "";
      if (list != null) {
        for (Hobby hobby : list) {
          s2 = s2 + hobby.name() + ", ";
        }
      }
      if (user.getOtherHobbie() != null) {
        s2 = s2 + user.getOtherHobbie();
      }
      if (s2.equals("")) {
        findViewById(R.id.tv_interestsH).setVisibility(View.GONE);
        hobbies.setVisibility(View.GONE);
      } else {
        hobbies.setText(s2);
      }

      //region SkillSets
      StringBuilder s;
      ArrayList<SkillSet> skillSet = user.getSkillSet();
      if (skillSet != null && skillSet.size() > 0) {
        s = new StringBuilder();
        for (SkillSet set : skillSet) {
          s.append(set.toString()).append("\n");
        }
        openers.getData(R.id.skillsets_opener).setText(s.toString());
      }
      openers.getData(R.id.hostBecause_opener).setText(user.getReasonToShareSkills());
      getWidth();

      //endregion

      //region Host Pics
      ArrayList<Uri> picsList = new ArrayList<>();
      if (user.getProfilePicId() != null) {
//        picsList.add(ImageUtils.getUri(user.getProfilePicId()));
      }
      if (user.getOtherPhotoIds() != null) {
        for (String s1 : user.getOtherPhotoIds()) {
          picsList.add(ImageUtils.getUri(s1));
        }
      }
      if (picsList.size() != 0) {
        tv_pics.setVisibility(View.VISIBLE);
        pics.setVisibility(View.VISIBLE);
        PhotoAdapter adpater = new PhotoAdapter(picsList, this, false);
        pics.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        pics.setAdapter(adpater);
      } else {
        tv_pics.setVisibility(View.GONE);
        pics.setVisibility(View.GONE);
      }
      //endregion

      //region Get Created

//      if (isMine) {
//        final Drawable defBg = created.getBackground();
//        created.setBackground(Utils.getProgressDrawable(this));
//        new ListingApi(this).getMyPosts(new OnTaskCompleteListener<Post[]>() {
//          @Override
//          public void onSuccess(Post[] response) {
//            Log.i(TAG, "getCreated onSuccess: ");
//            created.setBackground(defBg);
////            setUpCreated(response);
//          }
//
//          @Override
//          public void onFailure(String error) {
//            Log.e(TAG, "getCreated onFailure: " + error);
//            created.setBackground(defBg);
//          }
//        });
//      } else {
////        setUpCreated(null);
//      }

      //endregion

      tv_reviewsRV.setError("Need Api");
      reviews.setVisibility(View.GONE);
    }
  }

//  private void setUpCreated(Post[] listings) {
//
//    ArrayList<Post> filtered = new ArrayList<>();
//
//    if (listings != null && listings.length > 0) {
//      for (Post listing : listings) {
//        if (listing.getPostStatus() == null || listing.getPostStatus().equals(PostStatus.ACTIVE)) {
//          filtered.add(listing);
//        }
//      }
//    }
//
//    if (filtered.size() > 0) {
//      created.setLayoutManager(new GridLayoutManager(MyProfileActivity.this,
//          2, LinearLayoutManager.VERTICAL, false) {
//        @Override
//        public boolean canScrollVertically() {
//          return false;
//        }
//      });
//      ListingRecyclerAdapter adapter;
//      if (filtered.size() > 4) {
//        filtered = new ArrayList<>(filtered.subList(0, 4));
//        adapter = new ListingRecyclerAdapter(this, filtered);
//        viewAll.setOnClickListener(new OnClickListener() {
//          @Override
//          public void onClick(View v) {
//            Intent i = new Intent(MyProfileActivity.this, BlankActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            i.putExtra(BlankActivity.ARG_TYPE, BlankActivity.FRAG_Exps);
//            startActivity(i);
//          }
//        });
//      } else {
//        adapter = new ListingRecyclerAdapter(this, filtered);
//        viewAll.setVisibility(View.GONE);
//      }
//      created.setAdapter(adapter);
//
//    } else {
//      created.setVisibility(View.GONE);
//      tv_created.setVisibility(View.GONE);
//      viewAll.setVisibility(View.GONE);
//    }
//
//  }

  void setOpeners() {
    openers.add(R.id.skillsets_opener, R.id.tv_skilsets, R.id.skillsets_openIcon);
    openers.add(R.id.hostBecause_opener, R.id.tv_hostBecause, R.id.hostBecause_openIcon);
    openers.done();
  }

  private void getWidth() {
    final TextView textView = findViewById(R.id.tv_skilsets);
    ViewTreeObserver viewTreeObserver = textView.getViewTreeObserver();
    viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

      @SuppressWarnings("deprecation")
      @Override
      public void onGlobalLayout() {
        openers.setWidth(textView.getWidth());

        if (!isMine) {
          openers.toggle(R.id.skillsets_opener);
          openers.toggle(R.id.hostBecause_opener);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
          textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        } else {
          textView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
      }

    });
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

//  private void bindActivity() {
//    mToolbar        = (Toolbar) findViewById(R.id.main_toolbar);
//    mTitle          = (TextView) findViewById(R.id.main_textview_title);
//    mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
//    mAppBarLayout   = (AppBarLayout) findViewById(R.id.main_appbar);
//  }
//
//  @Override
//  public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
//    int maxScroll = appBarLayout.getTotalScrollRange();
//    float percentage = (float) Math.abs(offset) / (float) maxScroll;
//
//    handleAlphaOnTitle(percentage);
//    handleToolbarTitleVisibility(percentage);
//  }
//
//  private void handleToolbarTitleVisibility(float percentage) {
//    if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
//
//      if(!mIsTheTitleVisible) {
//        startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
//        mIsTheTitleVisible = true;
//      }
//
//    } else {
//
//      if (mIsTheTitleVisible) {
//        startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
//        mIsTheTitleVisible = false;
//      }
//    }
//  }
//
//  private void handleAlphaOnTitle(float percentage) {
//    if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
//      if(mIsTheTitleContainerVisible) {
//        startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
//        mIsTheTitleContainerVisible = false;
//      }
//
//    } else {
//
//      if (!mIsTheTitleContainerVisible) {
//        startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
//        mIsTheTitleContainerVisible = true;
//      }
//    }
//  }
//
//  public static void startAlphaAnimation (View v, long duration, int visibility) {
//    AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
//        ? new AlphaAnimation(0f, 1f)
//        : new AlphaAnimation(1f, 0f);
//
//    alphaAnimation.setDuration(duration);
//    alphaAnimation.setFillAfter(true);
//    v.startAnimation(alphaAnimation);
//  }

}
