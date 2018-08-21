package com.locolhive.chartout.classes;

public class Review {

  private Reviewer reviewer;
  private String postId;
  private Integer ratingValue;
  private String reviewText;

  public Reviewer getReviewer() {
    return reviewer;
  }

  public void setReviewer(Reviewer reviewer) {
    this.reviewer = reviewer;
  }

  public String getPostId() {
    return postId;
  }

  public void setPostId(String postId) {
    this.postId = postId;
  }

  public Integer getRatingValue() {
    return ratingValue;
  }

  public void setRatingValue(Integer ratingValue) {
    this.ratingValue = ratingValue;
  }

  public String getReviewText() {
    return reviewText;
  }

  public void setReviewText(String reviewText) {
    this.reviewText = reviewText;
  }

  public class Reviewer {

    private String id;
    private String fullName;
    private String profilePictureId;
    private String tagLine;
    private String description;
    private String currentCity;
    private String preferredLanguage;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getFullName() {
      return fullName;
    }

    public void setFullName(String fullName) {
      this.fullName = fullName;
    }

    public String getProfilePictureId() {
      return profilePictureId;
    }

    public void setProfilePictureId(String profilePictureId) {
      this.profilePictureId = profilePictureId;
    }

    public String getTagLine() {
      return tagLine;
    }

    public void setTagLine(String tagLine) {
      this.tagLine = tagLine;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
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
  }

}
