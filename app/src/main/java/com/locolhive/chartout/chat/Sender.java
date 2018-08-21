package com.locolhive.chartout.chat;

import com.locolhive.chartout.classes.ObjectId;

public class Sender {

  private ObjectId userID;
  private String name;
  private String imageID;

  public ObjectId getUserID() {
    return userID;
  }

  public void setUserID(ObjectId userID) {
    this.userID = userID;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getImageID() {
    return imageID;
  }

  public void setImageID(String imageID) {
    this.imageID = imageID;
  }


}
