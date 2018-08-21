package com.locolhive.chartout.home;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.locolhive.chartout.R;
import com.locolhive.chartout.helpers.LocationHelper;
import com.locolhive.chartout.interfaces.GetLocationListener;

public class SelectLocation extends AppCompatActivity {

  private static final String TAG = SelectLocation.class.getSimpleName() + " YOYO";

  LocationHelper locationHelper;
  TextView tv_currentLocation;
  TextView useCurrentLocation;
  ProgressBar circle;

  Location currentLocation;
  Integer currentRadius;
  String name;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_select_loction);

    tv_currentLocation = findViewById(R.id.tv_selectedLocation);
    useCurrentLocation = findViewById(R.id.tv1);
    circle = findViewById(R.id.progressCircle);

    currentLocation = ((GlobalData) getApplication()).currentLocation;
    name = ((GlobalData) getApplication()).currentAddress;
    currentRadius = ((GlobalData) getApplication()).currentRadius;

    if (currentRadius == null) {
      currentRadius = getResources().getInteger(R.integer.DefaultRadius);
    }

//    ((BubbleSeekBar) findViewById(R.id.seekbar)).setProgress(currentRadius);
    tv_currentLocation.setText(name);

    setUpToolbar();
    setUpPlacePicker();
    setUpListeners();

  }

  void setUpToolbar() {

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  void setUpPlacePicker() {

    PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
        getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
        .setCountry("IN")
        .build();

    autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
      @Override
      public void onPlaceSelected(Place place) {

        name = place.getName().toString();

        tv_currentLocation.setText(name);

        currentLocation = new Location("");
        currentLocation.setLongitude(place.getLatLng().longitude);
        currentLocation.setLatitude(place.getLatLng().latitude);

      }

      @Override
      public void onError(Status status) {
        Log.e(TAG, "onError: " + status.toString());
      }
    });
    autocompleteFragment.setFilter(typeFilter);
  }

  public void updateCurrentLocation() {
    circle.setVisibility(View.VISIBLE);
    locationHelper = new LocationHelper(SelectLocation.this, LocationHelper.TYPE_SELECT);
    locationHelper.getAddress(new GetLocationListener() {
      @Override
      public void onSuccess(Location location, String address) {
        circle.setVisibility(View.GONE);
        currentLocation = location;
        name = address;
        tv_currentLocation.setText(address);
      }

      @Override
      public void onFailure(String error) {
        Log.e(TAG, "onFailure: " + error);
        circle.setVisibility(View.GONE);
        Toast.makeText(SelectLocation.this, "Error getting location", Toast.LENGTH_SHORT).show();
      }
    });
  }

  void setUpListeners() {

//    ((BubbleSeekBar) findViewById(R.id.seekbar))
//        .setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
//          @Override
//          public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress,
//              float progressFloat, boolean fromUser) {
//          }
//
//          @Override
//          public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress,
//              float progressFloat) {
//            Log.i(TAG, "getProgressOnActionUp: ");
//          }
//
//          @Override
//          public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress,
//              float progressFloat, boolean fromUser) {
//            Log.i(TAG, "getProgressOnFinally: ");
//            currentRadius = progress;
//          }
//        });

    findViewById(R.id.btn_Confirm).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {

        Intent intent = new Intent();
        GlobalData application = (GlobalData) getApplication();
        application.currentRadius = currentRadius;
        application.saveCurrentFilter(currentLocation, name);
        setResult(RESULT_OK, intent);
        finish();

      }
    });

    useCurrentLocation.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        updateCurrentLocation();
      }
    });

  }

  @Override
  public boolean onNavigateUp() {
    Log.i(TAG, "onNavigateUp: ");
    finish();
    return true;
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (locationHelper != null) {
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
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == LocationHelper.REQUEST_CHECK_SETTINGS) {
      locationHelper.checkAndExecute();
    }
  }
}
