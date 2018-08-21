package com.locolhive.chartout.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class NearBy implements Parcelable {

  private String value;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }


  public NearBy() {

  }

  public NearBy(String s) {
    value = s;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.value);
  }

  protected NearBy(Parcel in) {
    this.value = in.readString();
  }

  public static final Creator<NearBy> CREATOR = new Creator<NearBy>() {
    @Override
    public NearBy createFromParcel(Parcel source) {
      return new NearBy(source);
    }

    @Override
    public NearBy[] newArray(int size) {
      return new NearBy[size];
    }
  };
}
