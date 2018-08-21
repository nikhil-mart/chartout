package com.locolhive.chartout.interfaces;

import android.location.Location;

/**
 * Created by Raghav on 19-Mar-18.
 */

public interface GetLocationListener {

  void onSuccess(Location location, String address);

  void onFailure(String error);
}
