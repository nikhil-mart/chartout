package com.locolhive.chartout.classes;


import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateRange implements Parcelable {

  @SerializedName(value = "startDate", alternate = "startTime")
  private String startTime;

  @SerializedName(value = "endDate", alternate = "endTime")
  private String endTime;

  public DateRange() {
  }

  public DateRange(Date s, Date e) {
    String myFormat;
    myFormat = "dd-MM-yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    startTime = sdf.format(s);
    endTime = sdf.format(e);
  }

  public CalendarDay[] getCalendarDay(){
    String myFormat = "dd-MM-yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    try {

      CalendarDay[] days = new CalendarDay[2];
      days[0] = CalendarDay.from(sdf.parse(startTime));
      days[1] = CalendarDay.from(sdf.parse(endTime));
      return days;

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

  protected DateRange(Parcel in) {
    this.startTime = in.readString();
    this.endTime = in.readString();
  }

  public static final Creator<DateRange> CREATOR = new Creator<DateRange>() {
    @Override
    public DateRange createFromParcel(Parcel source) {
      return new DateRange(source);
    }

    @Override
    public DateRange[] newArray(int size) {
      return new DateRange[size];
    }
  };
  //endregion
}
