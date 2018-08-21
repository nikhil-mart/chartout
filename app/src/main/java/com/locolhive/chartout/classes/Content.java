package com.locolhive.chartout.classes;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.locolhive.chartout.statics.ContentType;
import java.util.ArrayList;

public class Content implements MultiItemEntity {

  private ContentType type;
  private String text;
  private ArrayList<String> pics;
  private CustomLatLng latLng;

  public Content() {
    this.type = ContentType.TITLE;
    text = "title";
  }

  public Content(ContentType type) {
    this.type = type;
  }

  public void setType(ContentType type) {
    this.type = type;
  }

  public Content setText(String text) {
    this.text = text;
    return this;
  }

  public Content setPics(ArrayList<String> pics) {
    this.pics = pics;
    return this;
  }

  public Content setLatLng(CustomLatLng latLng) {
    this.latLng = latLng;
    return this;
  }

  public Content setLatLng(Double lat, Double lng) {
    this.latLng = new CustomLatLng(lat, lng);
    return this;
  }

  public ContentType getType() {
    return type;
  }

  public String getText() {
    return text;
  }

  public ArrayList<String> getPics() {
    return pics;
  }

  public CustomLatLng getLatLng() {
    return latLng;
  }

  @Override
  public int getItemType() {
    return type.ordinal();
  }

}
