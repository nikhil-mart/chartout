package com.locolhive.chartout.interfaces;

/**
 * Created by Raghav on 19-Mar-18.
 */

public interface OnTaskCompleteListener<T> {

  void onSuccess(T response);

  void onFailure(String error);
}
