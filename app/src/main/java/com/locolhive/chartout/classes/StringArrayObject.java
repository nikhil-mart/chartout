package com.locolhive.chartout.classes;

import com.google.gson.annotations.SerializedName;

public class StringArrayObject {

  @SerializedName(value = "uniqueness", alternate = {"naturalElements", "placeTypes",
      "noOfParticipant", "conveyanceOptions", "list", "priceType"})
  private String[] strings;

  public String[] getStrings() {
    return strings;
  }

  public void setStrings(String[] strings) {
    this.strings = strings;
  }

}
