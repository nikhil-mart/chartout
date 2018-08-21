package com.locolhive.chartout.classes;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import com.android.databinding.library.baseAdapters.BR;

public class Address extends BaseObservable implements android.os.Parcelable {

  public static final Creator<Address> CREATOR = new Creator<Address>() {
    @Override
    public Address createFromParcel(Parcel source) {
      return new Address(source);
    }

    @Override
    public Address[] newArray(int size) {
      return new Address[size];
    }
  };
  private String pincode;
  private String city;
  private String state;

  public Address() {
  }

  public Address(String pincode, String city, String state) {
    this.pincode = pincode;
    this.city = city;
    this.state = state;
  }

  protected Address(Parcel in) {
    this.pincode = in.readString();
    this.city = in.readString();
    this.state = in.readString();
  }

  @Bindable
  public String getPincode() {
    return pincode;
  }

  public void setPincode(String pincode) {
    this.pincode = pincode;
    notifyPropertyChanged(BR.pincode);
  }

  @Bindable
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
    notifyPropertyChanged(BR.city);
  }

  @Bindable
  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
    notifyPropertyChanged(BR.state);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.pincode);
    dest.writeString(this.city);
    dest.writeString(this.state);
  }

}
