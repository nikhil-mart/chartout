package com.locolhive.chartout.helpers;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.widget.Toast;
import com.locolhive.chartout.R;
import com.locolhive.chartout.home.BlankActivity;
import com.locolhive.chartout.home.MainActivity;
import com.locolhive.chartout.map.MapActivity;

public class Navigation {

  private static final String TAG = "BottomNav YOYO";

  public static void setUpBottomNav(final Context context, final BottomNavigationViewEx bottomNavigationView) {

    bottomNavigationView.enableAnimation(false);
    bottomNavigationView.enableShiftingMode(false);
    bottomNavigationView.enableItemShiftingMode(true);
//    bottomNavigationView.setOnNavigationItemSelectedListener();

    bottomNavigationView.setOnNavigationItemSelectedListener(
        new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent i;
            switch (item.getItemId()) {
              case R.id.action_home:
                i = new Intent(context, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
                break;
              case R.id.action_heart:
                i = new Intent(context, BlankActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra(BlankActivity.ARG_TYPE, BlankActivity.FRAG_Favorites);
                context.startActivity(i);
                break;
              case R.id.action_chat:
                i = new Intent(context, BlankActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra(BlankActivity.ARG_TYPE, BlankActivity.FRAG_CHAT);
                context.startActivity(i);
                break;
              case R.id.action_settings:
                i = new Intent(context, BlankActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra(BlankActivity.ARG_TYPE, BlankActivity.FRAG_ACCOUNT);
                context.startActivity(i);
                break;
              case R.id.action_filter:
                i = new Intent(context, MapActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NO_HISTORY);
                context.startActivity(i);
                break;
            }
            return true;
          }
        });
  }

  public static void goToMain(Context context) {
    Intent i;
    i = new Intent(context, MainActivity.class);
    i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    context.startActivity(i);
  }

}
