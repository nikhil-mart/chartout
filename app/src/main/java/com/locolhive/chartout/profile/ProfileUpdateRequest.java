package com.locolhive.chartout.profile;

import com.locolhive.chartout.classes.SkillSet;
import com.locolhive.chartout.statics.Hobby;
import java.util.ArrayList;

public class ProfileUpdateRequest {

  private String fullName;
  private String photoId;
  private String tagLine;
  private String shortDescription;
  private ArrayList<Hobby> hobbies;
  private String otherHobbie;
  private String verficationIdName;
  private String verficationIdNumber;
  private String currentCity;
  private Long dateOfBirth;
  private String gender;
  private String mobileNumber;
  private String preferredLanguage;
  private String doingForLiving;
  private ArrayList<SkillSet> skillSet;
  private String reasonToShareSkills;
  private String reasonToPeopleChooseYou;
  private BusinessDetails businessDetails;
  private ArrayList<String> otherPhotoIds;
  private String facebookData;
  private String googleData;

  public ProfileUpdateRequest() {
  }

  public ProfileUpdateRequest(UserProfile profile) {
    fullName = profile.getFullName();
    photoId = profile.getProfilePicId();
    tagLine = profile.getTagLine();
    shortDescription = profile.getShortDescription();
    hobbies = profile.getHobbies();
    otherHobbie = profile.getOtherHobbie();
    verficationIdName = profile.getVerficationIdName();
    verficationIdNumber = profile.getVerficationIdNumber();
    currentCity = profile.getCurrentCity();
    dateOfBirth = profile.getDob();
    if (profile.getGender() != null) {
      gender = profile.getGender().name();
    }
    mobileNumber = profile.getMobileNumber();
    preferredLanguage = profile.getPreferredLanguage();
    doingForLiving = profile.getDoingForLiving();
    skillSet = profile.getSkillSet();
    reasonToShareSkills = profile.getReasonToShareSkills();
    reasonToPeopleChooseYou = profile.getReasonToPeopleChooseYou();
    businessDetails = profile.getBusinessDetails();
    otherPhotoIds = profile.getOtherPhotoIds();
    facebookData = profile.getFacebookProfile();
    googleData = profile.getGoogleProfile();
  }

  //region Getters and setters
  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getPhotoId() {
    return photoId;
  }

  public void setPhotoId(String photoId) {
    this.photoId = photoId;
  }

  public String getTagLine() {
    return tagLine;
  }

  public void setTagLine(String tagLine) {
    this.tagLine = tagLine;
  }

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

  public Long getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(Long dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
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

  public String getFacebookData() {
    return facebookData;
  }

  public void setFacebookData(String facebookData) {
    this.facebookData = facebookData;
  }

  public String getGoogleData() {
    return googleData;
  }

  public void setGoogleData(String googleData) {
    this.googleData = googleData;
  }
  //endregion

  @Override
  public String toString() {
    return "ProfileUpdateRequest{" +
        "fullName='" + fullName + '\'' +
        ", photoId='" + photoId + '\'' +
        ", tagLine='" + tagLine + '\'' +
        ", shortDescription='" + shortDescription + '\'' +
        ", hobbies=" + hobbies +
        ", otherHobbie='" + otherHobbie + '\'' +
        ", verficationIdName='" + verficationIdName + '\'' +
        ", verficationIdNumber='" + verficationIdNumber + '\'' +
        ", currentCity='" + currentCity + '\'' +
        ", dateOfBirth=" + dateOfBirth +
        ", gender='" + gender + '\'' +
        ", mobileNumber='" + mobileNumber + '\'' +
        ", preferredLanguage='" + preferredLanguage + '\'' +
        ", doingForLiving='" + doingForLiving + '\'' +
        ", skillSet=" + skillSet +
        ", reasonToShareSkills='" + reasonToShareSkills + '\'' +
        ", reasonToPeopleChooseYou='" + reasonToPeopleChooseYou + '\'' +
        ", businessDetails=" + businessDetails +
        ", otherPhotoIds=" + otherPhotoIds +
        ", facebookData='" + facebookData + '\'' +
        ", googleData='" + googleData + '\'' +
        '}';
  }

}
