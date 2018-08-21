package com.locolhive.chartout.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.locolhive.chartout.R;
import com.locolhive.chartout.api.UserApi;
import com.locolhive.chartout.classes.Token;
import com.locolhive.chartout.home.GlobalData;
import com.locolhive.chartout.home.MainActivity;
import com.locolhive.chartout.interfaces.OnTaskCompleteListener;
import com.locolhive.chartout.profile.UserProfile;
import io.paperdb.Paper;

public class SplashActivity extends Activity {

  private static final String TAG = SplashActivity.class.getSimpleName() + " YOYO";
  int SPLASH_TIME_OUT = 1500;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.splash_activity);

    if (!Paper.book().contains(GlobalData.KEY_FistTime)) {
      new Handler().postDelayed(new Runnable() {

        @Override
        public void run() {
          Intent i = new Intent(SplashActivity.this, Onboarding2.class);
          startActivity(i);
          finish();
        }
      }, SPLASH_TIME_OUT);
    } else {

      if (Paper.book().contains(GlobalData.TOKEN_KEY)) {
        ((GlobalData) getApplication()).setToken((Token) Paper.book().read(GlobalData.TOKEN_KEY));

        UserApi api = new UserApi(getApplicationContext());
        api.getUserProfile(new OnTaskCompleteListener<UserProfile>() {
          @Override
          public void onSuccess(UserProfile response) {
            Log.i(TAG, "getUserProfile onSuccess: ");
            ((GlobalData) getApplication()).setUser(response);
            new Handler().postDelayed(new Runnable() {

              @Override
              public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
              }
            }, SPLASH_TIME_OUT - 800);
          }

          @Override
          public void onFailure(String error) {
            Log.e(TAG, "Login Failure: " + error);
            if (error != null) {
              if (error.contains("401")) {
                Toast.makeText(SplashActivity.this, "Invalid Session. Please login again.", Toast.LENGTH_LONG).show();
                ((GlobalData) getApplication()).nullify();
              }
            } else {
              Toast.makeText(SplashActivity.this, "Could not login", Toast.LENGTH_LONG).show();
            }
            new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                Log.i(TAG, "promptLogin: ");
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
              }
            }, SPLASH_TIME_OUT - 800);
          }
        });
      } else {
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            finish();
          }
        }, SPLASH_TIME_OUT);
      }
    }
  }
}
