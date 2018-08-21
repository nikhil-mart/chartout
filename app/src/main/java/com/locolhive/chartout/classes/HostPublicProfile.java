package com.locolhive.chartout.classes;

import android.os.Parcel;
import android.os.Parcelable;
import com.locolhive.chartout.profile.UserProfile;

public class HostPublicProfile implements Parcelable {

  private String fullName;
  private String shortDescription;
  private String doingForLiving;
  private SkillSet[] skillSet;
  private String profilePicId;
  private String reasonToShareSkills;
  private String reasonToPeopleChooseYou;
  private String[] photos;
  private String isFacebookLinked;
  private String isGoogleLinked;

  public HostPublicProfile() {

  }

  public HostPublicProfile(UserProfile  profile) {
    fullName = profile.getFullName();
    shortDescription = profile.getShortDescription();
    doingForLiving = profile.getDoingForLiving();
    if(profile.getSkillSet()!=null)
      skillSet = profile.getSkillSet().toArray(new SkillSet[profile.getSkillSet().size()]);
    profilePicId = profile.getProfilePicId();
    reasonToShareSkills = profile.getReasonToShareSkills();
    reasonToPeopleChooseYou = profile.getReasonToPeopleChooseYou();
    if(profile.getOtherPhotoIds()!=null)
      photos = profile.getOtherPhotoIds().toArray(new String[profile.getOtherPhotoIds().size()]);
    isFacebookLinked = profile.getFacebookProfile();
    isGoogleLinked = profile.getGoogleProfile();
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public String getDoingForLiving() {
    return doingForLiving;
  }

  public void setDoingForLiving(String doingForLiving) {
    this.doingForLiving = doingForLiving;
  }

  public SkillSet[] getSkillSet() {
    return skillSet;
  }

  public void setSkillSet(SkillSet[] skillSet) {
    this.skillSet = skillSet;
  }

  public String getProfilePicId() {
    return profilePicId;
  }

  public void setProfilePicId(String profilePicId) {
    this.profilePicId = profilePicId;
  }

  public String getReasonToShareSkills() {
    return reasonToShareSkills;
  }

  public void setReasonToShareSkills(String reasonToShareSkills) {
    this.reasonToShareSkills = reasonToShareSkills;
  }

  public String getReasonToPeopleChooseYou() {
    return reasonToPeopleChooseYou;
  }

  public void setReasonToPeopleChooseYou(String reasonToPeopleChooseYou) {
    this.reasonToPeopleChooseYou = reasonToPeopleChooseYou;
  }

  public String[] getPhotos() {
    return photos;
  }

  public void setPhotos(String[] photos) {
    this.photos = photos;
  }

  public String getIsFacebookLinked() {
    return isFacebookLinked;
  }

  public void setIsFacebookLinked(String isFacebookLinked) {
    this.isFacebookLinked = isFacebookLinked;
  }

  public String getIsGoogleLinked() {
    return isGoogleLinked;
  }

  public void setIsGoogleLinked(String isGoogleLinked) {
    this.isGoogleLinked = isGoogleLinked;
  }


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.fullName);
    dest.writeString(this.shortDescription);
    dest.writeString(this.doingForLiving);
    dest.writeTypedArray(this.skillSet, flags);
    dest.writeString(this.profilePicId);
    dest.writeString(this.reasonToShareSkills);
    dest.writeString(this.reasonToPeopleChooseYou);
    dest.writeStringArray(this.photos);
    dest.writeString(this.isFacebookLinked);
    dest.writeString(this.isGoogleLinked);
  }

  protected HostPublicProfile(Parcel in) {
    this.fullName = in.readString();
    this.shortDescription = in.readString();
    this.doingForLiving = in.readString();
    this.skillSet = in.createTypedArray(SkillSet.CREATOR);
    this.profilePicId = in.readString();
    this.reasonToShareSkills = in.readString();
    this.reasonToPeopleChooseYou = in.readString();
    this.photos = in.createStringArray();
    this.isFacebookLinked = in.readString();
    this.isGoogleLinked = in.readString();
  }

  public static final Creator<HostPublicProfile> CREATOR = new Creator<HostPublicProfile>() {
    @Override
    public HostPublicProfile createFromParcel(Parcel source) {
      return new HostPublicProfile(source);
    }

    @Override
    public HostPublicProfile[] newArray(int size) {
      return new HostPublicProfile[size];
    }
  };
}
