package com.locolhive.chartout.classes;

import com.google.gson.annotations.SerializedName;

public class StringObject {

  @SerializedName(value = "placeId", alternate = "description")
  private String string;

  public String getString() {
    return string;
  }

  public void setString(String string) {
    this.string = string;
  }

}
