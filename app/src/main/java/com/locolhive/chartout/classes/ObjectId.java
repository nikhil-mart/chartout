package com.locolhive.chartout.classes;

@SuppressWarnings("ALL")
public class ObjectId implements android.os.Parcelable {

  private Long timestamp;
  private Long machineIdentifier;
  private Long processIdentifier;
  private Long counter;
  private Long time;
  private Long date;
  private Long timeSecond;

  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  public Long getMachineIdentifier() {
    return machineIdentifier;
  }

  public void setMachineIdentifier(Long machineIdentifier) {
    this.machineIdentifier = machineIdentifier;
  }

  public Long getProcessIdentifier() {
    return processIdentifier;
  }

  public void setProcessIdentifier(Long processIdentifier) {
    this.processIdentifier = processIdentifier;
  }

  public Long getCounter() {
    return counter;
  }

  public void setCounter(Long counter) {
    this.counter = counter;
  }

  public Long getTime() {
    return time;
  }

  public void setTime(Long time) {
    this.time = time;
  }

  public Long getDate() {
    return date;
  }

  public void setDate(Long date) {
    this.date = date;
  }

  public Long getTimeSecond() {
    return timeSecond;
  }

  public void setTimeSecond(Long timeSecond) {
    this.timeSecond = timeSecond;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj.getClass() == ObjectId.class) {
      ObjectId sender = (ObjectId) obj;
      return getMachineIdentifier().equals(sender.getMachineIdentifier())
          && getProcessIdentifier().equals(sender.getProcessIdentifier())
          && getCounter().equals(sender.getCounter());
    } else {
      return false;
    }
  }


  //region Parcelable
  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(android.os.Parcel dest, int flags) {
    dest.writeValue(this.timestamp);
    dest.writeValue(this.machineIdentifier);
    dest.writeValue(this.processIdentifier);
    dest.writeValue(this.counter);
    dest.writeValue(this.time);
    dest.writeValue(this.date);
    dest.writeValue(this.timeSecond);
  }

  public ObjectId() {
  }

  protected ObjectId(android.os.Parcel in) {
    this.timestamp = (Long) in.readValue(Long.class.getClassLoader());
    this.machineIdentifier = (Long) in.readValue(Long.class.getClassLoader());
    this.processIdentifier = (Long) in.readValue(Long.class.getClassLoader());
    this.counter = (Long) in.readValue(Long.class.getClassLoader());
    this.time = (Long) in.readValue(Long.class.getClassLoader());
    this.date = (Long) in.readValue(Long.class.getClassLoader());
    this.timeSecond = (Long) in.readValue(Long.class.getClassLoader());
  }

  public static final Creator<ObjectId> CREATOR = new Creator<ObjectId>() {
    @Override
    public ObjectId createFromParcel(android.os.Parcel source) {
      return new ObjectId(source);
    }

    @Override
    public ObjectId[] newArray(int size) {
      return new ObjectId[size];
    }
  };
  //endregion
}
