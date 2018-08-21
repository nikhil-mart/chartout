package com.locolhive.chartout.profile;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.support.annotation.Keep;
import com.android.databinding.library.baseAdapters.BR;

@Keep
public class BankAccountDetails extends BaseObservable implements android.os.Parcelable {

  public static final Creator<BankAccountDetails> CREATOR = new Creator<BankAccountDetails>() {
    @Override
    public BankAccountDetails createFromParcel(Parcel source) {
      return new BankAccountDetails(source);
    }

    @Override
    public BankAccountDetails[] newArray(int size) {
      return new BankAccountDetails[size];
    }
  };
  private String beneficiaryName;
  private String currentAccountNumber;
  private String ifscCode;
  private String cancelledCheckFile;

  public BankAccountDetails() {
  }

  public BankAccountDetails(String beneficiaryName, String currentAccountNumber, String ifscCode,
      String cancelledCheckFile) {
    this.beneficiaryName = beneficiaryName;
    this.currentAccountNumber = currentAccountNumber;
    this.ifscCode = ifscCode;
    this.cancelledCheckFile = cancelledCheckFile;
  }

  protected BankAccountDetails(Parcel in) {
    this.beneficiaryName = in.readString();
    this.currentAccountNumber = in.readString();
    this.ifscCode = in.readString();
    this.cancelledCheckFile = in.readString();
  }

  //region Getters and setters
  @Bindable
  public String getBeneficiaryName() {
    return beneficiaryName;
  }

  public void setBeneficiaryName(String beneficiaryName) {
    this.beneficiaryName = beneficiaryName;
    notifyPropertyChanged(BR.beneficiaryName);
  }

  @Bindable
  public String getCurrentAccountNumber() {
    return currentAccountNumber;
  }

  public void setCurrentAccountNumber(String currentAccountNumber) {
    this.currentAccountNumber = currentAccountNumber;
    notifyPropertyChanged(BR.currentAccountNumber);
  }

  @Bindable
  public String getIfscCode() {
    return ifscCode;
  }

  public void setIfscCode(String ifscCode) {
    this.ifscCode = ifscCode;
    notifyPropertyChanged(BR.ifscCode);
  }
  //endregion

  public String getCancelledCheckFile() {
    return cancelledCheckFile;
  }

  public void setCancelledCheckFile(String cancelledCheckFile) {
    this.cancelledCheckFile = cancelledCheckFile;
  }

  //region Parcelable
  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.beneficiaryName);
    dest.writeString(this.currentAccountNumber);
    dest.writeString(this.ifscCode);
    dest.writeString(this.cancelledCheckFile);
  }
  //endregion

}
