package com.locolhive.chartout.classes;


import android.os.Parcel;
import android.os.Parcelable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeRange implements Parcelable {

  private String startTime;

  private String endTime;

  public TimeRange() {
  }

  public TimeRange(Date s, Date e) {
    String myFormat;
    myFormat = "HH:mm";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    startTime = sdf.format(s);
    endTime = sdf.format(e);
  }

  public Date getDate(){
    String myFormat = "HH:mm";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    try {
      return sdf.parse(startTime);

    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  //region Parcelable
  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.startTime);
    dest.writeString(this.endTime);
  }

  protected TimeRange(Parcel in) {
    this.startTime = in.readString();
    this.endTime = in.readString();
  }

  public static final Creator<TimeRange> CREATOR = new Creator<TimeRange>() {
    @Override
    public TimeRange createFromParcel(Parcel source) {
      return new TimeRange(source);
    }

    @Override
    public TimeRange[] newArray(int size) {
      return new TimeRange[size];
    }
  };
  //endregion
}
