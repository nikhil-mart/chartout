package com.locolhive.chartout.classes;

import android.os.Parcel;
import com.google.android.gms.maps.model.LatLng;

public class CustomLatLng implements android.os.Parcelable {
  public Double lat, lng;

  public CustomLatLng(double lat, double lng) {
    this.lat = lat;
    this.lng = lng;
  }

  public CustomLatLng(LatLng latLng){
    lat = latLng.latitude;
    lng = latLng.longitude;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.lat);
    dest.writeValue(this.lng);
  }

  protected CustomLatLng(Parcel in) {
    this.lat = (Double) in.readValue(Double.class.getClassLoader());
    this.lng = (Double) in.readValue(Double.class.getClassLoader());
  }

  public static final Creator<CustomLatLng> CREATOR = new Creator<CustomLatLng>() {
    @Override
    public CustomLatLng createFromParcel(Parcel source) {
      return new CustomLatLng(source);
    }

    @Override
    public CustomLatLng[] newArray(int size) {
      return new CustomLatLng[size];
    }
  };

}
