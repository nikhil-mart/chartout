package com.locolhive.chartout.profile;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import com.android.databinding.library.baseAdapters.BR;
import com.locolhive.chartout.classes.HostPublicProfile;
import com.locolhive.chartout.classes.ObjectId;
import com.locolhive.chartout.classes.SkillSet;
import com.locolhive.chartout.statics.Gender;
import com.locolhive.chartout.statics.Hobby;
import java.util.ArrayList;
import java.util.Arrays;

public class UserProfile extends BaseObservable implements android.os.Parcelable {

  public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
    @Override
    public UserProfile createFromParcel(Parcel source) {
      return new UserProfile(source);
    }

    @Override
    public UserProfile[] newArray(int size) {
      return new UserProfile[size];
    }
  };
  private ObjectId _id;
  private String fullName;
  private Long dob;
  private String emailId;
  private String mobileNumber;
  private String profilePicId;
  private Gender gender;
  private Long createdTs;
  private Long lastModifiedTs;
  private String tagLine;
  private String shortDescription;
  private ArrayList<Hobby> hobbies;
  private String otherHobbie;
  private String verficationIdName;
  private String verficationIdNumber;
  private String currentCity;
  private String preferredLanguage;
  private String doingForLiving;
  private ArrayList<SkillSet> skillSet;
  private String reasonToShareSkills;
  private String reasonToPeopleChooseYou;
  private BusinessDetails businessDetails;
  private ArrayList<String> otherPhotoIds;
  private String facebookProfile;
  private String googleProfile;

  //region Getters and Setters

  public UserProfile(HostPublicProfile profile) {
    fullName = profile.getFullName();
    shortDescription = profile.getShortDescription();
    doingForLiving = profile.getDoingForLiving();
    if (profile.getSkillSet() != null) {
      skillSet = new ArrayList<>();
      skillSet.addAll(Arrays.asList(profile.getSkillSet()));
    }
    profilePicId = profile.getProfilePicId();
    reasonToShareSkills = profile.getReasonToShareSkills();
    reasonToPeopleChooseYou = profile.getReasonToPeopleChooseYou();
    if (profile.getPhotos() != null) {
      otherPhotoIds = new ArrayList<>();
      otherPhotoIds.addAll(Arrays.asList(profile.getPhotos()));
    }
    facebookProfile = profile.getIsFacebookLinked();
    googleProfile = profile.getIsGoogleLinked();
  }

  protected UserProfile(Parcel in) {
    this._id = in.readParcelable(ObjectId.class.getClassLoader());
    this.fullName = in.readString();
    this.dob = (Long) in.readValue(Long.class.getClassLoader());
    this.emailId = in.readString();
    this.mobileNumber = in.readString();
    this.profilePicId = in.readString();
    int tmpGender = in.readInt();
    this.gender = tmpGender == -1 ? null : Gender.values()[tmpGender];
    this.createdTs = (Long) in.readValue(Long.class.getClassLoader());
    this.lastModifiedTs = (Long) in.readValue(Long.class.getClassLoader());
    this.tagLine = in.readString();
    this.shortDescription = in.readString();
    this.hobbies = new ArrayList<Hobby>();
    in.readList(this.hobbies, Hobby.class.getClassLoader());
    this.otherHobbie = in.readString();
    this.verficationIdName = in.readString();
    this.verficationIdNumber = in.readString();
    this.currentCity = in.readString();
    this.preferredLanguage = in.readString();
    this.doingForLiving = in.readString();
    this.skillSet = in.createTypedArrayList(SkillSet.CREATOR);
    this.reasonToShareSkills = in.readString();
    this.reasonToPeopleChooseYou = in.readString();
    this.businessDetails = in.readParcelable(BusinessDetails.class.getClassLoader());
    this.otherPhotoIds = in.createStringArrayList();
    this.facebookProfile = in.readString();
    this.googleProfile = in.readString();
  }

  public ObjectId get_id() {
    return _id;
  }

  public void set_id(ObjectId _id) {
    this._id = _id;
  }

  @Bindable
  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public Long getDob() {
    return dob;
  }

  public void setDob(Long dob) {
    this.dob = dob;
  }

  public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  @Bindable
  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public String getProfilePicId() {
    return profilePicId;
  }

  public void setProfilePicId(String profilePicId) {
    this.profilePicId = profilePicId;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public Long getCreatedTs() {
    return createdTs;
  }

  public void setCreatedTs(Long createdTs) {
    this.createdTs = createdTs;
  }

  public Long getLastModifiedTs() {
    return lastModifiedTs;
  }

  public void setLastModifiedTs(Long lastModifiedTs) {
    this.lastModifiedTs = lastModifiedTs;
  }

  @Bindable
  public String getTagLine() {
    return tagLine;
  }

  public void setTagLine(String tagLine) {
    this.tagLine = tagLine;
  }

  @Bindable
  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public ArrayList<Hobby> getHobbies() {
    return hobbies;
  }

  public void setHobbies(ArrayList<Hobby> hobbies) {
    this.hobbies = hobbies;
  }

  public String getOtherHobbie() {
    return otherHobbie;
  }

  public void setOtherHobbie(String otherHobbie) {
    this.otherHobbie = otherHobbie;
  }

  public String getVerficationIdName() {
    return verficationIdName;
  }

  public void setVerficationIdName(String verficationIdName) {
    this.verficationIdName = verficationIdName;
  }

  public String getVerficationIdNumber() {
    return verficationIdNumber;
  }

  public void setVerficationIdNumber(String verficationIdNumber) {
    this.verficationIdNumber = verficationIdNumber;
  }

  public String getCurrentCity() {
    return currentCity;
  }

  public void setCurrentCity(String currentCity) {
    this.currentCity = currentCity;
  }

  public String getPreferredLanguage() {
    return preferredLanguage;
  }

  public void setPreferredLanguage(String preferredLanguage) {
    this.preferredLanguage = preferredLanguage;
  }

  public String getDoingForLiving() {
    return doingForLiving;
  }

  public void setDoingForLiving(String doingForLiving) {
    this.doingForLiving = doingForLiving;
  }

  public ArrayList<SkillSet> getSkillSet() {
    return skillSet;
  }

  public void setSkillSet(ArrayList<SkillSet> skillSet) {
    this.skillSet = skillSet;
  }

  @Bindable
  public String getReasonToShareSkills() {
    return reasonToShareSkills;
  }

  public void setReasonToShareSkills(String reasonToShareSkills) {
    this.reasonToShareSkills = reasonToShareSkills;
    notifyPropertyChanged(BR.reasonToShareSkills);
  }

  @Bindable
  public String getReasonToPeopleChooseYou() {
    return reasonToPeopleChooseYou;
  }

  public void setReasonToPeopleChooseYou(String reasonToPeopleChooseYou) {
    this.reasonToPeopleChooseYou = reasonToPeopleChooseYou;
    notifyPropertyChanged(BR.reasonToPeopleChooseYou);
  }

  public BusinessDetails getBusinessDetails() {
    return businessDetails;
  }

  public void setBusinessDetails(BusinessDetails businessDetails) {
    this.businessDetails = businessDetails;
  }

  public ArrayList<String> getOtherPhotoIds() {
    return otherPhotoIds;
  }

  public void setOtherPhotoIds(ArrayList<String> otherPhotoIds) {
    this.otherPhotoIds = otherPhotoIds;
  }

  public String getFacebookProfile() {
    return facebookProfile;
  }

  public void setFacebookProfile(String facebookProfile) {
    this.facebookProfile = facebookProfile;
  }

  //endregion

  public String getGoogleProfile() {
    return googleProfile;
  }

  public void setGoogleProfile(String googleProfile) {
    this.googleProfile = googleProfile;
  }

  //region Parcelable
  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this._id, flags);
    dest.writeString(this.fullName);
    dest.writeValue(this.dob);
    dest.writeString(this.emailId);
    dest.writeString(this.mobileNumber);
    dest.writeString(this.profilePicId);
    dest.writeInt(this.gender == null ? -1 : this.gender.ordinal());
    dest.writeValue(this.createdTs);
    dest.writeValue(this.lastModifiedTs);
    dest.writeString(this.tagLine);
    dest.writeString(this.shortDescription);
    dest.writeList(this.hobbies);
    dest.writeString(this.otherHobbie);
    dest.writeString(this.verficationIdName);
    dest.writeString(this.verficationIdNumber);
    dest.writeString(this.currentCity);
    dest.writeString(this.preferredLanguage);
    dest.writeString(this.doingForLiving);
    dest.writeTypedList(this.skillSet);
    dest.writeString(this.reasonToShareSkills);
    dest.writeString(this.reasonToPeopleChooseYou);
    dest.writeParcelable(this.businessDetails, flags);
    dest.writeStringList(this.otherPhotoIds);
    dest.writeString(this.facebookProfile);
    dest.writeString(this.googleProfile);
  }
  //endregion

}
