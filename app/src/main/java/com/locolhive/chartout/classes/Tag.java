package com.locolhive.chartout.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Tag implements Parcelable {

  public static final Creator<Tag> CREATOR = new Creator<Tag>() {
    @Override
    public Tag createFromParcel(Parcel source) {
      return new Tag(source);
    }

    @Override
    public Tag[] newArray(int size) {
      return new Tag[size];
    }
  };
  private String tagId;
  private String tag;
  private String description;

  public Tag() {

  }

  protected Tag(Parcel in) {
    this.tagId = in.readString();
    this.tag = in.readString();
    this.description = in.readString();
  }

  public String getTagId() {
    return tagId;
  }

  public void setTagId(String tagId) {
    this.tagId = tagId;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.tagId);
    dest.writeString(this.tag);
    dest.writeString(this.description);
  }
}
