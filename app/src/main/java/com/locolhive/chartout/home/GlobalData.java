package com.locolhive.chartout.home;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Location;
import android.support.multidex.MultiDex;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.locolhive.chartout.api.UserApi;
import com.locolhive.chartout.classes.Token;
import com.locolhive.chartout.helpers.DialogProgress;
import com.locolhive.chartout.helpers.Utils;
import com.locolhive.chartout.interfaces.OnResultListener;
import com.locolhive.chartout.interfaces.OnSessionVerification;
import com.locolhive.chartout.interfaces.OnTaskCompleteListener;
import com.locolhive.chartout.login.LoginSignupActivity;
import com.locolhive.chartout.profile.UserProfile;
import io.paperdb.Paper;

//import android.support.multidex.MultiDex;

//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;

public class GlobalData extends Application {

  public static final String TOKEN_KEY = "token_key";
  public static final String KEY_FistTime = "firstTimeKey";
  private static final String TAG = GlobalData.class.getSimpleName() + " YOYO";

  public String currentAddress;
  public Location currentLocation;
  public Integer currentRadius;
  private Token token;
  private UserProfile user;

  public boolean getSavedLocation() {
    Log.i(TAG, "getSavedLocation: ");
    if (Paper.book().contains("currentLocation")) {
      currentAddress = Paper.book().read("currentAddress", null);
      currentLocation = Paper.book().read("currentLocation");
      return true;
    } else {
      return false;
    }
  }

  public String getToken() {
    if (token != null) {
      return token.getToken();
    } else {
      if (Paper.book().contains(GlobalData.TOKEN_KEY)) {
        token = Paper.book().read(GlobalData.TOKEN_KEY);
      }
      if (token != null) {
        return token.getToken();
      } else {
        return null;
      }
    }
  }

  public void setToken(Token token) {
    this.token = token;
    if (token != null) {
      Log.i(TAG, "setToken: " + token.getToken());
      Paper.book().write(TOKEN_KEY, token);
      UserApi api = new UserApi(getApplicationContext());
      api.getUserProfile(new OnTaskCompleteListener<UserProfile>() {
        @Override
        public void onSuccess(UserProfile response) {
          Log.i(TAG, "getUserProfile onSuccess: ");
          user = response;
        }

        @Override
        public void onFailure(String error) {
          Log.e(TAG, "getUserProfile onFailure: " + error);
          if (error.contains("401")) {
            GlobalData.this.token = null;
          }
        }
      });
    } else {
      Log.i(TAG, "setToken: null");
      Paper.book().delete(TOKEN_KEY);
    }
  }

  public void getUser(Context context, final OnResultListener<UserProfile> listener) {

    if (user != null) {
      listener.OnResult(user);
    } else if (Paper.book().contains(GlobalData.TOKEN_KEY)) {
      sessionCheck((AppCompatActivity) context
          , new OnSessionVerification() {
            @Override
            public void onVerified() {
              listener.OnResult(user);
            }
          });
    } else {
      listener.OnResult(null);
    }

  }

  public void setUser(UserProfile user) {
    this.user = user;
  }

  public void saveCurrentFilter(Location location, String address) {
    currentLocation = location;
    currentAddress = address;

    Paper.book().write("currentLocation", currentLocation);
    if (address != null) {
      Paper.book().write("currentAddress", currentAddress);
    } else {
      Paper.book().delete("currentAddress");
    }
  }

  public void logout(final AppCompatActivity activity) {

    AlertDialog dialog = Utils.createOkDialog(activity, "Logout", "Proceed?",
        new OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            Paper.book().delete(GlobalData.TOKEN_KEY);
            token = null;
            Intent i = new Intent(activity, LoginSignupActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            activity.finish();
          }
        });
    dialog.show();
  }

  public void nullify() {
    Paper.book().destroy();
    Paper.book().write(GlobalData.KEY_FistTime, false);
  }

  public void sessionCheck(final AppCompatActivity activity, final OnSessionVerification listener) {

    final DialogProgress dialogProgress = DialogProgress.newInstance();

    dialogProgress.display(activity);
    setToken((Token) Paper.book().read(GlobalData.TOKEN_KEY));

    UserApi api = new UserApi(getApplicationContext());
    api.getUserProfile(new OnTaskCompleteListener<UserProfile>() {
      @Override
      public void onSuccess(UserProfile response) {
        dialogProgress.done();
        setUser(response);
        listener.onVerified();
      }

      @Override
      public void onFailure(String error) {
        dialogProgress.done();
        Log.e(TAG, "Login Failure: " + error);
        if (error != null) {
          if (error.contains("401")) {
            error = "Invalid Session. Please login again.";
            nullify();
            Intent i = new Intent(activity, LoginSignupActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(i);
            activity.finish();
          }
        } else {
          error = "Network Unavailable";
        }
        Toast.makeText(activity, error, Toast.LENGTH_LONG).show();
      }
    });

//    if (Paper.book().contains(GlobalData.TOKEN_KEY)) {
//
//
//
//    } else {
//      Toast.makeText(activity, "Invalid Session. Please login again.", Toast.LENGTH_LONG).show();
//      nullify();
//      Intent i = new Intent(activity, LoginSignupActivity.class);
//      i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//      startActivity(i);
//      activity.finish();
//    }
    }

  @Override
  public void onCreate() {
    super.onCreate();
    Paper.init(getApplicationContext());

//    FacebookSdk.sdkInitialize(getApplicationContext()); todo
//    AppEventsLogger.activateApp(this);
  }

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(newBase);
    MultiDex.install(this);
  }

}
