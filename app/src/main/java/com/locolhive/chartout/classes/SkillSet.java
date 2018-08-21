package com.locolhive.chartout.classes;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import com.locolhive.chartout.BR;

public class SkillSet extends BaseObservable implements Parcelable {

  private String skillName;
  private String description;
  private String duration;

  @Bindable
  public String getSkillName() {
    return skillName;
  }

  public void setSkillName(String skillName) {
    this.skillName = skillName;
    notifyPropertyChanged(BR.skillName);
  }

  @Bindable
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
    notifyPropertyChanged(BR.description);
  }

  @Bindable
  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
    notifyPropertyChanged(BR.duration);
  }

  @Override
  public String toString() {
    return
        skillName + '\n' +
        description + "\n" +
        "Duration: " + duration + " years";
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.skillName);
    dest.writeString(this.description);
    dest.writeString(this.duration);
  }

  public SkillSet() {
  }

  protected SkillSet(Parcel in) {
    this.skillName = in.readString();
    this.description = in.readString();
    this.duration = in.readString();
  }

  public static final Creator<SkillSet> CREATOR = new Creator<SkillSet>() {
    @Override
    public SkillSet createFromParcel(Parcel source) {
      return new SkillSet(source);
    }

    @Override
    public SkillSet[] newArray(int size) {
      return new SkillSet[size];
    }
  };
}
