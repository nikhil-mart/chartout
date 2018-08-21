package com.locolhive.chartout.create;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.Keep;
import com.locolhive.chartout.classes.Category;
import com.locolhive.chartout.classes.Content;
import com.locolhive.chartout.classes.CustomLatLng;
import com.locolhive.chartout.classes.HostPublicProfile;
import com.locolhive.chartout.classes.Post;
import com.locolhive.chartout.statics.BANGALORE;
import java.util.ArrayList;
import lombok.Data;

@Keep
@Data
public class PostRequest extends BaseObservable implements Parcelable {

  private String title;
  private String description;
  private String primaryImage;
  private String categoryId;
  private ArrayList<String> tags; //?
  private ArrayList<Content> content;
  private String destination;
  private CustomLatLng mainLocation;
  private Boolean isProfilePublic;
  private Boolean hideExact;
  private Boolean messagesEnabled;
  private Boolean availableToBook;
  private Boolean listedOnLocol;

  public PostRequest(){
    title = "";
    description = "";
    categoryId = "";
    content = new ArrayList<>();
    destination = "Bangalore";
    mainLocation = new CustomLatLng(BANGALORE.lat, BANGALORE.lng);
    isProfilePublic = true;
    hideExact = false;
    messagesEnabled = true;
    availableToBook = false;
    listedOnLocol = false;
  }

  public PostRequest(Post post) {
    if(post!=null) {
      title = post.getTitle();
      description = post.getDescription();
      if (post.getCategory() != null)
        categoryId = post.getCategory().getCategoryId();
      content = post.getContent();
      destination = post.getDestination();
      mainLocation = post.getMainLocation();
      isProfilePublic = true;
      hideExact = false;
      messagesEnabled = true;
      availableToBook = post.getAvailableToBook();
      listedOnLocol = post.getListedOnLocol();
    }else {
      title = "";
      description = "";
      categoryId = "";
      content = new ArrayList<>();
      destination = "Bangalore";
      mainLocation = new CustomLatLng(BANGALORE.lat, BANGALORE.lng);
      isProfilePublic = true;
      hideExact = false;
      messagesEnabled = true;
      availableToBook = false;
      listedOnLocol = false;
    }
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.title);
    dest.writeString(this.description);
    dest.writeString(this.primaryImage);
    dest.writeString(this.categoryId);
    dest.writeStringList(this.tags);
    dest.writeList(this.content);
    dest.writeString(this.destination);
    dest.writeParcelable(this.mainLocation, flags);
    dest.writeValue(this.isProfilePublic);
    dest.writeValue(this.hideExact);
    dest.writeValue(this.messagesEnabled);
    dest.writeValue(this.availableToBook);
    dest.writeValue(this.listedOnLocol);
  }

  protected PostRequest(Parcel in) {
    this.title = in.readString();
    this.description = in.readString();
    this.primaryImage = in.readString();
    this.categoryId = in.readString();
    this.tags = in.createStringArrayList();
    this.content = new ArrayList<Content>();
    in.readList(this.content, Content.class.getClassLoader());
    this.destination = in.readString();
    this.mainLocation = in.readParcelable(CustomLatLng.class.getClassLoader());
    this.isProfilePublic = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.hideExact = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.messagesEnabled = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.availableToBook = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.listedOnLocol = (Boolean) in.readValue(Boolean.class.getClassLoader());
  }

  public static final Creator<PostRequest> CREATOR = new Creator<PostRequest>() {
    @Override
    public PostRequest createFromParcel(Parcel source) {
      return new PostRequest(source);
    }

    @Override
    public PostRequest[] newArray(int size) {
      return new PostRequest[size];
    }
  };

}
