package com.locolhive.chartout.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {

  private String categoryId;
  private String category;
  private String description;
  private String imageId;

  public Category() {
    categoryId = "";
    category = "";
    description = "";
    imageId = "";
  }

  public Category(String name){
    categoryId = "";
    category = name;
    description = "";
    imageId = "";
  }

  //region Getters And Setters

  public String getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(String categoryId) {
    this.categoryId = categoryId;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getImageId() {
    return imageId;
  }

  public void setImageId(String imageId) {
    this.imageId = imageId;
  }
  //endregion

  //region Parcelable
  @Override
  public boolean equals(Object o) {

    // If the object is compared with itself then return true
    if (o == this) {
      return true;
    }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
    if (!(o instanceof Category)) {
      return false;
    }

    // typecast o to Complex so that we can compare data members
    Category c = (Category) o;
    return c.getCategoryId().equals(this.categoryId);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.categoryId);
    dest.writeString(this.category);
    dest.writeString(this.description);
    dest.writeString(this.imageId);
  }

  protected Category(Parcel in) {
    this.categoryId = in.readString();
    this.category = in.readString();
    this.description = in.readString();
    this.imageId = in.readString();
  }

  public static final Creator<Category> CREATOR = new Creator<Category>() {
    @Override
    public Category createFromParcel(Parcel source) {
      return new Category(source);
    }

    @Override
    public Category[] newArray(int size) {
      return new Category[size];
    }
  };
  //endregion
}
