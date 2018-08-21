package com.locolhive.chartout.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import com.locolhive.chartout.R;
import com.locolhive.chartout.chat.Frag_ChatList;
import com.locolhive.chartout.helpers.BottomNavigationViewEx;
import com.locolhive.chartout.helpers.Navigation;
import com.locolhive.chartout.interfaces.OnBackPressListener;
import com.locolhive.chartout.interfaces.OnResultListener;
import com.locolhive.chartout.manageAccount.AccountFrag;
import com.locolhive.chartout.manageAccount.FavoritesFrag;
import com.locolhive.chartout.manageAccount.ManagePostsFrag;

public class BlankActivity extends AppCompatActivity {

  public static final String ARG_TYPE = "ARG_TYPE";
  public static final String FRAG_ACCOUNT = "FRAG_ACCOUNT";
  public static final String FRAG_Favorites = "FRAG_Favorites";
  public static final String FRAG_Exps = "FRAG_Exps";
  public static final String FRAG_CHAT = "FRAG_CHAT";
  public static final int ImageRC = 68;
  private static final String TAG = BlankActivity.class.getSimpleName() + " YOYO";
  public OnBackPressListener listener;
  public FragmentManager fragmentManager;
  public boolean isBackEnabled;
  public OnResultListener<Uri> imgUriResult;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_blank);

    fragmentManager = getSupportFragmentManager();
    isBackEnabled = true;
    listener = null;

    Bundle extras = getIntent().getExtras();
    if (extras != null && extras.getString(ARG_TYPE) != null) {
      String type = extras.getString(ARG_TYPE);
      assert type != null;
      switchFrag(type);
    }

  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    Log.i(TAG, "onNewIntent: ");
    Bundle extras = intent.getExtras();
    if (extras != null && extras.getString(ARG_TYPE) != null) {
      String type = extras.getString(ARG_TYPE);
      assert type != null;
      switchFrag(type);
    }
  }

  public void setToolbarTitle(String title) {

    if (title == null) {
      getSupportActionBar().hide();
    } else {
      getSupportActionBar().show();
      getSupportActionBar().setTitle(title);
    }

  }

  void setUpToolbars(int k) {
    Toolbar toolbar = findViewById(R.id.toolbar);
    toolbar.setNavigationOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().hide();

    BottomNavigationViewEx bottomNavigationView = findViewById(R.id.bottomNav);
    bottomNavigationView.setCurrentItem(k);
    Navigation.setUpBottomNav(BlankActivity.this, bottomNavigationView);
  }

  @Override
  public boolean onSupportNavigateUp() {
    Log.i(TAG, "onSupportNavigateUp: ");
    onBackPressed();
    return super.onSupportNavigateUp();
  }

  public void changeFrag(Fragment fragment) {
    changeFrag(fragment, true, null);
  }

  public void changeFrag(Fragment fragment, Boolean addToBack, String TAG) {

    FragmentTransaction transaction = fragmentManager.beginTransaction()
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

    if (TAG != null) {
      transaction.replace(R.id.container, fragment, TAG);
    } else {
      transaction.replace(R.id.container, fragment);
    }

    if (addToBack) {
      transaction.addToBackStack(null);
    }

    transaction.commit();
  }

  public void switchFrag(String type) {
    switch (type) {
      case FRAG_ACCOUNT:
        setUpToolbars(4);
        AccountFrag accountFrag = AccountFrag.newInstance();
        fragmentManager.beginTransaction()
            .replace(R.id.container, accountFrag)
//            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit();
        break;
      case FRAG_CHAT:
        setUpToolbars(3);
        Frag_ChatList fragChatMain = Frag_ChatList.newInstance();
        fragmentManager.beginTransaction()
            .replace(R.id.container, fragChatMain)
//            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit();
        break;
      case FRAG_Exps:
        setUpToolbars(4);
        setToolbarTitle("Manage Posts");
        fragmentManager.beginTransaction()
            .replace(R.id.container, ManagePostsFrag.newInstance())
//            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit();
        break;
      case FRAG_Favorites:
        setUpToolbars(2);
        setToolbarTitle("Favorites");
        fragmentManager.beginTransaction()
            .replace(R.id.container, FavoritesFrag.newInstance())
            .commit();

    }
  }

  @Override
  public void onBackPressed() {
    Log.i(TAG, "onBackPressed: ");
    if (listener != null) {
      Log.i(TAG, "onBackPressed: listener");
      listener.onBackPress();
    } else {
      super.onBackPressed();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == ImageRC) {
      if (imgUriResult != null) {
        if (data != null) {
          Uri uri = data.getData();
          if (imgUriResult != null) {
            imgUriResult.OnResult(uri);
          }
        }
      }
    }
  }
}
