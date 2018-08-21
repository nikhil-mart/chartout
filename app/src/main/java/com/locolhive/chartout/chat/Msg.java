package com.locolhive.chartout.chat;

import com.locolhive.chartout.classes.ObjectId;
import java.util.Calendar;

public class Msg {

  private String message;
  private Sender sender;
  private long createdAt;

  public Msg(String message, String name, ObjectId id) {
    this.message = message;
    createdAt = Calendar.getInstance().getTimeInMillis();
    Sender sender1 = new Sender();
    sender1.setName(name);
    sender1.setUserID(id);
    sender1.setName(name);
    sender = sender1;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Sender getSender() {
    return sender;
  }

  public void setSender(Sender sender) {
    this.sender = sender;
  }

  public long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(long createdAt) {
    this.createdAt = createdAt;
  }
}
