package com.locolhive.chartout.api;

import android.content.Context;
import android.support.annotation.Nullable;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.locolhive.chartout.classes.BooleanObject;
import com.locolhive.chartout.classes.Category;
import com.locolhive.chartout.classes.Occasion;
import com.locolhive.chartout.classes.Post;
import com.locolhive.chartout.classes.Review;
import com.locolhive.chartout.classes.StringArrayObject;
import com.locolhive.chartout.classes.Tag;
import com.locolhive.chartout.create.PostRequest;
import com.locolhive.chartout.home.GlobalData;
import com.locolhive.chartout.interfaces.OnTaskCompleteListener;
import java.util.HashMap;

@SuppressWarnings("ALL")
public class ListingApi {

  public static final String URL_GetTags = SERVER.IP + "/locolapi/v2/tag/all";
  public static final String URL_ActivityTypes = SERVER.IP + "/locolapi/v2/post/place-type";
  public static final String URL_GetUniquenessOp = SERVER.IP + "/locolapi/v2/post/uniqueness-options";
  public static final String URL_GetNatElement = SERVER.IP + "/locolapi/v2/post/natural-element";
  public static final String URL_ActPriceTypes = SERVER.IP + "/locolapi/v2/post/price-type";
  public static final String URL_ActNum = SERVER.IP + "/locolapi/v2/post/participant-size";
  public static final String URL_PlaceTypes = SERVER.IP + "/locolapi/v2/place/kind";
  public static final String URL_PlaceOcc = SERVER.IP + "/locolapi/v2/place/occasions/all";
  public static final String URL_ConvOp = SERVER.IP + "/locolapi/v2/place/conveyance-options";
  public static final String URL_PlaceNum = SERVER.IP + "/locolapi/v2/place/guest-per-slot";
  public static final String URL_PlacePriceTypes = SERVER.IP + "/locolapi/v2/place/price-type-options";
  public static final String URL_IsFav = SERVER.IP + "/locolapi/v2/post/isfavourite/";
  public static final String URL_MarkFav = SERVER.IP + "/locolapi/v2/post/favourite/";
  public static final String URL_Reviews = SERVER.IP + "/locolapi/v1/rating/";
  static final String TAG = ListingApi.class.getSimpleName() + " YOYO";
  static final String URL_ActivitySearch = SERVER.IP + "/locolapi/v2/post/radius_search";
  static final String URL_AllActivity = SERVER.IP + "/locolapi/v2/post/all_category";
  static final String URL_CreateActivity = SERVER.IP + "/locolapi/v1/post";
  static final String URL_GetActCat = SERVER.IP + "/locolapi/v2/category";
  static final String URL_PlaceSearch = SERVER.IP + "/locolapi/v2/place/radius_search";
  static final String URL_CreatePlace = SERVER.IP + "/locolapi/v2/place";
  static final String URL_PlaceCat = SERVER.IP + "/locolapi/v2/place/category";
  private static final String URL_MyListings = SERVER.IP + "/locolapi/v1/post/my_post";
  private Context context;
  private RequestQueue queue;

  public ListingApi(Context context) {
    this.context = context;
    queue = Volley.newRequestQueue(context);
  }


  public void getAllActivities(@Nullable Integer limit, @Nullable String cat,
      final OnTaskCompleteListener<Post[]> listener) {

    double latitude, longitude;
    GlobalData globalData = (GlobalData) context.getApplicationContext();
    if (globalData.currentLocation != null) {
      latitude = globalData.currentLocation.getLatitude();
      longitude = globalData.currentLocation.getLongitude();
    } else {
      latitude = 12.9126701;
      longitude = 77.6387760;
    }

    if (limit == null) {
      limit = 4;
    }

    if (cat == null) {
      cat = "9b9df4cf-129f-42ac-9c3c-e0acf4d25015";
    }

    getActivitiesForCategory(latitude, longitude, globalData.currentRadius * 1000, limit, cat, listener);

  }

  public void getActivitiesForCategory(double latitude, double longitude, long near_by, int limit, String cat,
      final OnTaskCompleteListener<Post[]> listener) {

    final String request_Tag = "POSTS_REQUEST";

    HashMap<String, String> hashMap = new HashMap<>();
    hashMap.put("latitude", String.valueOf(latitude));
    hashMap.put("longitude", String.valueOf(longitude));
    hashMap.put("near_by", String.valueOf(near_by));
    hashMap.put("limit", String.valueOf(limit));
    hashMap.put("category_id", cat);

    Request request1 = new RequestBuilder<Post[]>()
        .setMethod(RequestBuilder.GET)
        .setUrl(URL_ActivitySearch)
        .setUrlParams(hashMap)
        .setResponseBodyClass(Post[].class)
        .setListener(listener)
        .createRequest().setTag(request_Tag);

    queue.add(request1);

  }

  public void getActivitiesForAllCategory(double latitude, double longitude, long near_by,
      final OnTaskCompleteListener<Post[]> listener) {
//    "/locolapi/v2/post/all_category?latitude=12.9126701&longitude=77.6387760&near_by=700000&limit=5";

  }


  public void getPlaces(int limit, String catId, final OnTaskCompleteListener<Post[]> listener) {
    double latitude, longitude;
    GlobalData globalData = (GlobalData) context.getApplicationContext();
    if (globalData.currentLocation != null) {
      latitude = globalData.currentLocation.getLatitude();
      longitude = globalData.currentLocation.getLongitude();
    } else {
      latitude = 12.9126701;
      longitude = 77.6387760;
    }
    if (catId == null) {
      catId = "e460d34e-5b08-4031-a028-4d35a5ca2391";
    }

    getAllPlaces(latitude, longitude, globalData.currentRadius * 1000, limit, catId, listener);
  }

  public void getAllPlaces(double latitude, double longitude, long radius, int limit, String catId,
      final OnTaskCompleteListener<Post[]> listener) {

    final String request_Tag = "Places_REQUEST";

    HashMap<String, String> hashMap = new HashMap<>();
    hashMap.put("latitude", String.valueOf(latitude));
    hashMap.put("longitude", String.valueOf(longitude));
    hashMap.put("nearByRadius", String.valueOf(radius));
    hashMap.put("limit", String.valueOf(limit));
    hashMap.put("offset", String.valueOf(0));
    hashMap.put("categoryId", catId);

    Request request1 = new RequestBuilder<Post[]>()
        .setMethod(RequestBuilder.POST)
        .setUrl(URL_PlaceSearch)
        .setBodyParams(hashMap)
        .setResponseBodyClass(Post[].class)
        .setListener(listener)
        .createRequest().setTag(request_Tag);

    queue.add(request1);
  }

  public void getActivityCategories(final OnTaskCompleteListener<Category[]> listener) {
    getCategories(URL_GetActCat, listener);
  }

  public void getPlaceCategories(final OnTaskCompleteListener<Category[]> listener) {
    getCategories(URL_PlaceCat, listener);
  }

  private void getCategories(String url, final OnTaskCompleteListener<Category[]> listener) {

    final String request_Tag = "CAT_REQUEST";

    HashMap<String, String> hashMap = new HashMap<>();

    Request request = new RequestBuilder<Category[]>()
        .setUrl(url)
        .setMethod(RequestBuilder.GET)
        .setResponseBodyClass(Category[].class)
        .setListener(listener)
        .createRequest();

    request.setTag(request_Tag);
    queue.add(request);

  }


  public void getActivityTags(final OnTaskCompleteListener<Tag[]> listener) {
    final String reqTag = "TAG_request";

    Request request = new RequestBuilder<Tag[]>()
        .setMethod(RequestBuilder.GET)
        .setUrl(URL_GetTags)
        .setResponseBodyClass(Tag[].class)
        .setListener(listener)
        .createRequest();

    request.setTag(reqTag);
    queue.add(request);
  }

  public void getPlaceOcc(final OnTaskCompleteListener<Occasion[]> listener) {
    Request request = new RequestBuilder<Occasion[]>()
        .setMethod(RequestBuilder.GET)
        .setResponseBodyClass(Occasion[].class)
        .setUrl(URL_PlaceOcc)
        .setListener(listener).createRequest();
    queue.add(request);
  }

  public void getTypes(String url, final OnTaskCompleteListener<StringArrayObject> listener) {
    Request request = new RequestBuilder<StringArrayObject>()
        .setMethod(RequestBuilder.GET)
        .setUrl(url)
        .setResponseBodyClass(StringArrayObject.class)
        .setListener(listener).createRequest();
    queue.add(request);
  }


  public void createActivity(PostRequest listing, final OnTaskCompleteListener<String> listener) {

    String create_tag = "CreateActivity";

    Request request = new RequestBuilder<String>()
        .setMethod(RequestBuilder.POST)
        .setUrl(URL_CreateActivity)
        .setRequestBody(listing)
        .setResponseBodyClass(String.class)
        .setToken(((GlobalData) context.getApplicationContext()).getToken())
        .setListener(listener).createRequest();

    request.setTag(create_tag);
    queue.add(request);

  }


  public void isFavorite(String id, OnTaskCompleteListener<BooleanObject> listener) {
    Request request = new RequestBuilder<BooleanObject>()
        .setMethod(RequestBuilder.GET)
        .setUrl(URL_IsFav + id)
        .setListener(listener)
        .setToken(((GlobalData) context.getApplicationContext()).getToken())
        .setResponseBodyClass(BooleanObject.class)
        .createRequest();
    queue.add(request);
  }

  public void markFavorite(String id, boolean b, final OnTaskCompleteListener<String> listener) {
    BooleanObject object = new BooleanObject();
    object.isFavourite = b;

    Request request = new EmptyResponseRequestBuilder()
        .setMethod(RequestBuilder.PUT)
        .setUrl(URL_MarkFav + id)
        .setToken(((GlobalData) context.getApplicationContext()).getToken())
        .setListener(listener)
        .setRequestBody(object)
        .createRequest();
    queue.add(request);
  }

  public void getReviews(String id, OnTaskCompleteListener<Review[]> listener) {
    //http://13.126.76.247:9090/locolapi/v1/rating/5a463080508418156c9a76b3?skip=0&limit=1
    Review review = new Review();
    Request request = new RequestBuilder<Review[]>()
        .setMethod(RequestBuilder.GET)
        .setUrl(URL_Reviews + id)
        .addUrlParams("skip", "0")
        .addUrlParams("limit", "4")
        .setResponseBodyClass(Review[].class)
        .setListener(listener)
        .createRequest();
    queue.add(request);
  }

  public void postReview(String id, int val, String text, OnTaskCompleteListener<Review[]> listener) {
//    http://13.126.76.247:9090/locolapi/v1/rating/5a463080508418156c9a76b3
    Review review = new Review();
    Request request = new RequestBuilder<Review[]>()
        .setMethod(RequestBuilder.POST)
        .setUrl(URL_Reviews + id)
        .addBodyParams("ratingValue", String.valueOf(val))
        .addBodyParams("reviewText", text)
        .setToken(((GlobalData) context.getApplicationContext()).getToken())
        .setResponseBodyClass(Review[].class)
        .setListener(listener)
        .createRequest();
    queue.add(request);
  }

  public void updateReview(String id, int val, String text, OnTaskCompleteListener<Review[]> listener) {
//    http://13.126.76.247:9090/locolapi/v1/rating/5a463080508418156c9a76b3
    Review review = new Review();
    Request request = new RequestBuilder<Review[]>()
        .setMethod(RequestBuilder.PUT)
        .setUrl(URL_Reviews + id)
        .addBodyParams("ratingValue", String.valueOf(val))
        .addBodyParams("reviewText", text)
        .setToken(((GlobalData) context.getApplicationContext()).getToken())
        .setResponseBodyClass(Review[].class)
        .setListener(listener)
        .createRequest();
    queue.add(request);
  }

  public void getMyPosts(OnTaskCompleteListener<Post[]> listener) {

    Request request = new RequestBuilder<Post[]>()
        .setUrl(URL_MyListings)
        .setMethod(RequestBuilder.GET)
        .setResponseBodyClass(Post[].class)
        .setToken(((GlobalData) context.getApplicationContext()).getToken())
        .setListener(listener).createRequest();

    queue.add(request);

  }


}
