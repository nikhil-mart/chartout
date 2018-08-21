package com.locolhive.chartout.profile;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import com.android.databinding.library.baseAdapters.BR;
import com.locolhive.chartout.classes.Address;

public class BusinessDetails extends BaseObservable implements android.os.Parcelable {

  private String companyName;
  private String companyDisplayName;
  private Address companyAddress;
  private String addressProofFileId;
  private String panNumber;
  private String panCardFileId;
  private String gstNumber;
  private String gstProofFile;
  private String aadharNumber;
  private String aadharProofFile;
  private BankAccountDetails accountDetails;

  public BusinessDetails() {
    companyAddress = new Address();
    accountDetails = new BankAccountDetails();
  }

  protected BusinessDetails(Parcel in) {
    this.companyName = in.readString();
    this.companyDisplayName = in.readString();
    this.companyAddress = in.readParcelable(Address.class.getClassLoader());
    this.addressProofFileId = in.readString();
    this.panNumber = in.readString();
    this.panCardFileId = in.readString();
    this.gstNumber = in.readString();
    this.gstProofFile = in.readString();
    this.aadharNumber = in.readString();
    this.aadharProofFile = in.readString();
    this.accountDetails = in.readParcelable(BankAccountDetails.class.getClassLoader());
  }

  //region Getters and setters

  @Bindable
  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
    notifyPropertyChanged(BR.companyName);
  }

  @Bindable
  public String getCompanyDisplayName() {
    return companyDisplayName;
  }

  public void setCompanyDisplayName(String companyDisplayName) {
    this.companyDisplayName = companyDisplayName;
    notifyPropertyChanged(BR.companyDisplayName);
  }

  @Bindable
  public Address getCompanyAddress() {
    return companyAddress;
  }

  public void setCompanyAddress(Address companyAddress) {
    this.companyAddress = companyAddress;
    notifyPropertyChanged(BR.companyAddress);
  }

  public String getAddressProofFileId() {
    return addressProofFileId;
  }

  public void setAddressProofFileId(String addressProofFileId) {
    this.addressProofFileId = addressProofFileId;
  }

  @Bindable
  public String getPanNumber() {
    return panNumber;
  }

  public void setPanNumber(String panNumber) {
    this.panNumber = panNumber;
    notifyPropertyChanged(BR.panNumber);
  }

  public String getPanCardFileId() {
    return panCardFileId;
  }

  public void setPanCardFileId(String panCardFileId) {
    this.panCardFileId = panCardFileId;
  }

  @Bindable
  public String getGstNumber() {
    return gstNumber;
  }

  public void setGstNumber(String gstNumber) {
    this.gstNumber = gstNumber;
    notifyPropertyChanged(BR.gstNumber);
  }

  public String getGstProofFile() {
    return gstProofFile;
  }

  public void setGstProofFile(String gstProofFile) {
    this.gstProofFile = gstProofFile;
  }

  @Bindable
  public String getAadharNumber() {
    return aadharNumber;
  }

  public void setAadharNumber(String aadharNumber) {
    this.aadharNumber = aadharNumber;
    notifyPropertyChanged(BR.aadharNumber);
  }

  public String getAadharProofFile() {
    return aadharProofFile;
  }

  public void setAadharProofFile(String aadharProofFile) {
    this.aadharProofFile = aadharProofFile;
  }

  @Bindable
  public BankAccountDetails getAccountDetails() {
    return accountDetails;
  }

  public void setAccountDetails(BankAccountDetails accountDetails) {
    this.accountDetails = accountDetails;
    notifyPropertyChanged(BR.accountDetails);
  }

  //endregion

  //region Parcelable

  public static final Creator<BusinessDetails> CREATOR = new Creator<BusinessDetails>() {
    @Override
    public BusinessDetails createFromParcel(Parcel source) {
      return new BusinessDetails(source);
    }

    @Override
    public BusinessDetails[] newArray(int size) {
      return new BusinessDetails[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.companyName);
    dest.writeString(this.companyDisplayName);
    dest.writeParcelable(this.companyAddress, flags);
    dest.writeString(this.addressProofFileId);
    dest.writeString(this.panNumber);
    dest.writeString(this.panCardFileId);
    dest.writeString(this.gstNumber);
    dest.writeString(this.gstProofFile);
    dest.writeString(this.aadharNumber);
    dest.writeString(this.aadharProofFile);
    dest.writeParcelable(this.accountDetails, flags);
  }
  //endregion

}

