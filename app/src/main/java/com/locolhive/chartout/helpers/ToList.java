package com.locolhive.chartout.helpers;

import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;

public class ToList<T> {

  private T[] arr;

  public ToList(@Nullable T[] arr) {
    this.arr = arr;
  }

  public ArrayList<T> getList() {
    ArrayList<T> list;
    if (arr == null) {
      list = new ArrayList<T>();
    } else {
      list = new ArrayList<>(Arrays.asList(arr));
    }
    return list;
  }

}
