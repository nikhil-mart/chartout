package com.locolhive.chartout.helpers;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.locolhive.chartout.classes.CustomLatLng;
import com.locolhive.chartout.home.GlobalData;
import com.locolhive.chartout.home.MainActivity;
import com.locolhive.chartout.home.SelectLocation;
import com.locolhive.chartout.interfaces.GetLocationListener;
import com.locolhive.chartout.interfaces.OnTaskCompleteListener;
import com.locolhive.chartout.interfaces.PendingTask;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

@SuppressWarnings({"unchecked", "WeakerAccess"})
@SuppressLint({"RestrictedApi", "MissingPermission"})
public class LocationHelper {

  public static final int TYPE_MAIN = 0;
  public static final int TYPE_SELECT = 1;
  public static final int PERMISSION_REQUEST_CODE = 12;
  public static final int REQUEST_CHECK_SETTINGS = 24;
  public static final int TASK_LastLocation = 0;
  public static final int TASK_Address = 2;
  private static final String TAG = LocationHelper.class.getSimpleName() + " YOYO";
  public boolean isGranted, isGps;
  PendingTask pendingTask;
  private Context context;
  private GlobalData globalData;
  private FusedLocationProviderClient client;
  private int type;
  private LocationRequest mLocationRequest;

  public LocationHelper(Context context, int type) {

    this.context = context;
    globalData = (GlobalData) context.getApplicationContext();
    isGranted = false;
    isGps = false;
//    checkAndExecute();
    this.type = type;
    client = new FusedLocationProviderClient(context);

    client.getLocationAvailability().addOnSuccessListener(new OnSuccessListener<LocationAvailability>() {
      @Override
      public void onSuccess(LocationAvailability locationAvailability) {
        Log.i(TAG, "onSuccess: getLocationAvailability\n+locationAvailability.isLocationAvailable(): " + locationAvailability.isLocationAvailable());
      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Log.i(TAG, "getLocationAvailability onFailure: ");
      }
    });

    mLocationRequest = new LocationRequest();
    mLocationRequest.setInterval(10 * 1000);
    mLocationRequest.setFastestInterval(1000);
    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

  }

  private void getLastLocation(final OnTaskCompleteListener<Location> listener) {

    if (isGranted) {

      Task<Location> lastLocation = client.getLastLocation();
      lastLocation.addOnSuccessListener(
          new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
              listener.onSuccess(location);
            }
          })
          .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              Log.e(TAG, "onFailure()", e);
              listener.onFailure(e.getMessage());
            }
          })
          .addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
              Log.d(TAG, "onCanceled: ");
              listener.onFailure("Cancelled");
            }
          });

    } else {

      pendingTask = new PendingTask() {
        @Override
        public void task() {
          getLastLocation(listener);
        }
      };
      checkAndExecute();

    }
  }

  public void getAddress(final GetLocationListener listener) {

    if (isGranted && isGps) {

      Task<Location> task = client.getLastLocation();
      task.addOnSuccessListener(
          new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
              if (location != null) {
                Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
                try {
                  List<Address> address = geoCoder
                      .getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                  listener.onSuccess(location, Utils.addressToString(address.get(0)));

                } catch (IOException e) {
                  Log.e(TAG, "getAddress: ", e);
                  listener.onFailure(e.getMessage());
                }
              } else {
                isGps = false;
                Log.e(TAG, "Location service disabled.");
                listener.onFailure("Location service disabled");
              }
            }
          })
          .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              Log.e(TAG, "onFailure()", e);
              listener.onFailure(e.getMessage());
            }
          })
          .addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
              Log.d(TAG, "onCanceled: ");
              listener.onFailure("Cancelled");
            }
          });

    } else {
      pendingTask = new PendingTask() {
        @Override
        public void task() {
          Log.i(TAG, "task: getAddress added");
          getAddress(listener);
          pendingTask = null;
        }
      };
      checkAndExecute();
    }
  }

  public void getAddressFromLatLng(CustomLatLng customLatLng, final OnTaskCompleteListener<String> listener) {
    Geocoder geoCoder = new Geocoder(context, Locale.getDefault());

    try {
      List<Address> address = geoCoder
          .getFromLocation(customLatLng.lat, customLatLng.lng, 1);
//      globalData.saveCurrentFilter(location, Utils.addressToString(address.get(0)));
      listener.onSuccess(Utils.addressToString(address.get(0)));

    } catch (IOException e) {
      Log.e(TAG, "getAddress: ", e);
      listener.onFailure(e.getMessage());
    }
  }

  @SuppressWarnings("UnusedReturnValue")
  public boolean checkAndExecute() {

    if (ActivityCompat.checkSelfPermission(context, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(context, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

      isGranted = false;
      Log.i(TAG, "checkPermission: requesting");

      if (type == TYPE_MAIN) {
        ActivityCompat.requestPermissions((MainActivity) context,
            new String[]{permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
      } else if (type == TYPE_SELECT) {
        ActivityCompat.requestPermissions((SelectLocation) context,
            new String[]{permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
      }
      return false;

    } else {
      Log.i(TAG, "checkPermission: granted");
      isGranted = true;
      if (isGps) {
        if (pendingTask != null) {
          pendingTask.task();
          pendingTask = null;
        }
      } else {
        checkGps();
      }
      return true;
    }
  }

  public void checkGps() {

    if (isGranted) {

      final LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
          .addLocationRequest(mLocationRequest);
      SettingsClient client = LocationServices.getSettingsClient(context);

      Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

      task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
        @Override
        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
          // All location settings are satisfied. The client can initialize
          // location requests here.
          // ...
          if (locationSettingsResponse.getLocationSettingsStates().isLocationUsable()) {
            Log.i(TAG, "All location settings are satisfied");
            isGps = true;
            if (pendingTask != null) {
              pendingTask.task();
              pendingTask = null;
            }
          } else {
            AlertDialog dialog = Utils
                .createOkDialog(context, "Location Required", "The app needs your location to display activites near you",
                    new OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                        Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent1);
                      }
                    });
            dialog.show();
          }
        }
      });

      task.addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          if (e instanceof ResolvableApiException) {
            // Location settings are not satisfied, but this can be fixed
            // by showing the user a dialog.
            try {
              Log.i(TAG, "onFailure: Show the dialog");
              ResolvableApiException resolvable = (ResolvableApiException) e;
              resolvable.startResolutionForResult((MainActivity) context,
                  REQUEST_CHECK_SETTINGS);
            } catch (IntentSender.SendIntentException sendEx) {
              // Ignore the error.
            }
          }
        }
      });

    } else {
      checkAndExecute();
    }

  }

  //region Other Stuff

//  public void onResult(int requestCode, int resultCode, Intent data) {
//    if (requestCode == PLAY_SERVICES_REQUEST) {
//      if (resultCode == Activity.RESULT_OK) {
//        Log.i(TAG, "onResult: PLAY_SERVICES_REQUEST ");
//        if (pendingTask != null) {
//          pendingTask.task();
//        }
//      }
//    }
//  }

//  @SuppressLint("RestrictedApi")
//  public void getCurrentPlace() {
//
//    if (isGranted) {
//      // Get the likely places - that is, the businesses and other points of interest that
//      // are the best match for the device's current location.
//      Log.i(TAG, "getCurrentPlace: ");
//
//      if (pendingTask != null) {
//        pendingTask = null;
//      }
//
//      @SuppressWarnings("MissingPermission") final Task<PlaceLikelihoodBufferResponse> placeResult =
//          mPlaceDetectionClient.getCurrentPlace(null);
//      placeResult.addOnCompleteListener
//          (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
//            @Override
//            public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
//              if (task.isSuccessful() && task.getResult() != null) {
//                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
//
//                // Set the count, handling cases where less than 5 entries are returned.
//                int count;
//                if (likelyPlaces.getCount() < M_MAX_ENTRIES) {
//                  count = likelyPlaces.getCount();
//                } else {
//                  count = M_MAX_ENTRIES;
//                }
//
//                int i = 0;
//                mLikelyPlaceNames = new String[count];
//                mLikelyPlaceAddresses = new String[count];
//                mLikelyPlaceAttributions = new String[count];
//                mLikelyPlaceLatLngs = new CustomLatLng[count];
//
//                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
//                  // Build a list of likely places to show the user.
//                  mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
//                  mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace()
//                      .getAddress();
//                  mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace()
//                      .getAttributions();
//                  mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();
//
//                  i++;
//                  if (i > (count - 1)) {
//                    break;
//                  }
//                }
//
//                // Release the place likelihood buffer, to avoid memory leaks.
//                likelyPlaces.release();
//
//              } else {
//                Log.e(TAG, "Exception: %s", task.getException());
//              }
//            }
//          });
//    } else {
//      // The user has not granted permission.
//      Log.i(TAG, "The user did not grant location permission.");
//
//      // Prompt the user for permission.
//      checkAndExecute();
//      pendingTask = new PendingTask() {
//        @Override
//        public void task() {
//          getCurrentPlace();
//        }
//      };
//    }
//  }

//  private boolean checkPlayServices() {
//
//    GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
//    int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);
//
//    if (resultCode != ConnectionResult.SUCCESS) {
//      if (googleApiAvailability.isUserResolvableError(resultCode)) {
//        googleApiAvailability.getErrorDialog((MainActivity) context, resultCode,
//            PLAY_SERVICES_REQUEST).show();
//      } else {
//        Toast.makeText(context,
//            "This device is not supported.", Toast.LENGTH_LONG)
//            .show();
//      }
//      return false;
//    }
//    return true;
//  }

//  void setUpGoogleApis() {
//
//        mGoogleApiClient = new GoogleApiClient
//                .Builder(this)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener(){
//                    @Override
//                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//                        Log.e(TAG, "onConnectionFailed: "+ connectionResult.getErrorMessage());
//                    }
//                }).build();*//*
//
//        mGeoDataClient = Places.getGeoDataClient(this, null);
//        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
//        @SuppressLint("MissingPermission") Task<PlaceLikelihoodBufferResponse> placeResult = mPlaceDetectionClient.getCurrentPlace(null);
//        placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
//            @SuppressLint("RestrictedApi")
//            @Override
//            public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
//                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
//                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
//                    Log.i(TAG, String.format("Place '%s' has likelihood: %g",
//                            placeLikelihood.getPlace().getName(),
//                            placeLikelihood.getLikelihood()));
//                }
//                likelyPlaces.release();
//            }
//        });
//
//
//        @SuppressLint("MissingPermission") PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
//                .getCurrentPlace(mGoogleApiClient, null);
//        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
//            @Override
//            public void onPermissionResult(@NonNull PlaceLikelihoodBuffer likelyPlaces) {
//                Log.v("App", "1. likelyPlaces.getCount() : " + likelyPlaces.getCount());
//                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
//                    for(int i : placeLikelihood.getPlace().getPlaceTypes()){
//                        Log.i("App", String.format("Place '%s' with " +
//                                        "likelihood: %g "+"type : %d",
//                                placeLikelihood.getPlace().getName(),
//                                placeLikelihood.getLikelihood(), i));
//                    }
//                }
//                likelyPlaces.release();
//            }
//        });*//*
//
//   FusedLocationProviderClient mFusedLocationClient;
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//
//            }
//        });
//
//  }

  //endregion


}
