package com.locolhive.chartout.classes;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.locolhive.chartout.statics.BANGALORE;
import java.util.ArrayList;
import lombok.Data;

@Data
public class Post implements android.os.Parcelable {

  @SerializedName(value = "id", alternate = "postId")
  private String id;

  private Category category;
  private String title;
  private String description;
  private HostPublicProfile host;
  private String destination;
  private CustomLatLng mainLocation;
  private ArrayList<Content> content;
  private String primaryImage;
  private Boolean availableToBook;
  private Boolean listedOnLocol;
  private Integer views;
  private Integer shares;
  private Integer chats;
  private Integer hearts;

  public Post() {
    category = new Category("Trekking");
    host = new HostPublicProfile();
    title = "Main Title";
    description = "dadada";
    destination = "Langkawi";
    mainLocation = new CustomLatLng(BANGALORE.lat, BANGALORE.lng);
    content = new ArrayList<>();
    availableToBook = true;
    listedOnLocol = true;
    views = 123;
    shares = 32;
    chats = 23;
    hearts = 100;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(android.os.Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeParcelable(this.category, flags);
    dest.writeString(this.title);
    dest.writeString(this.description);
    dest.writeParcelable(this.host, flags);
    dest.writeString(this.destination);
    dest.writeParcelable(this.mainLocation, flags);
    dest.writeList(this.content);
    dest.writeString(this.primaryImage);
    dest.writeValue(this.availableToBook);
    dest.writeValue(this.listedOnLocol);
    dest.writeValue(this.views);
    dest.writeValue(this.shares);
    dest.writeValue(this.chats);
    dest.writeValue(this.hearts);
  }

  protected Post(android.os.Parcel in) {
    this.id = in.readString();
    this.category = in.readParcelable(Category.class.getClassLoader());
    this.title = in.readString();
    this.description = in.readString();
    this.host = in.readParcelable(HostPublicProfile.class.getClassLoader());
    this.destination = in.readString();
    this.mainLocation = in.readParcelable(LatLng.class.getClassLoader());
    this.content = new ArrayList<Content>();
    in.readList(this.content, Content.class.getClassLoader());
    this.primaryImage = in.readString();
    this.availableToBook = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.listedOnLocol = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.views = (Integer) in.readValue(Integer.class.getClassLoader());
    this.shares = (Integer) in.readValue(Integer.class.getClassLoader());
    this.chats = (Integer) in.readValue(Integer.class.getClassLoader());
    this.hearts = (Integer) in.readValue(Integer.class.getClassLoader());
  }

  public static final Creator<Post> CREATOR = new Creator<Post>() {
    @Override
    public Post createFromParcel(android.os.Parcel source) {
      return new Post(source);
    }

    @Override
    public Post[] newArray(int size) {
      return new Post[size];
    }
  };

}
