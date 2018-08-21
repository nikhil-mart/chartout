package com.locolhive.chartout.api;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.locolhive.chartout.classes.StringObject;
import com.locolhive.chartout.classes.Token;
import com.locolhive.chartout.classes.UserCred;
import com.locolhive.chartout.home.GlobalData;
import com.locolhive.chartout.interfaces.OnTaskCompleteListener;
import com.locolhive.chartout.profile.ProfileUpdateRequest;
import com.locolhive.chartout.profile.UserProfile;
import java.util.HashMap;

@SuppressWarnings("unchecked")
public class UserApi {

  public static final String URL_EditUser = SERVER.IP + "/locolapi/v1/user/update";
  private static final String TAG = UserApi.class.getSimpleName() + " YOYO";
  private static final String URL_Register = SERVER.IP + "/locolapi/v2/registor";
  private static final String URL_Login = SERVER.IP + "/locolapi/v2/login";
  private static final String URL_GetUserDetails = SERVER.IP + "/locolapi/v2/user/info";

  private RequestQueue requestQueue;
  private Context context;

  public UserApi(Context context) {
    this.requestQueue = Volley.newRequestQueue(context);
    this.context = context;
  }

  public void register(UserCred user, final OnTaskCompleteListener<StringObject> listener) {

    final String request_Tag = "REGISTER_REQUEST";

    HashMap<String, String> hashMap = new HashMap<>();
    hashMap.put("password", user.getpwd());
    hashMap.put("fullName", user.getName());
    hashMap.put("emailId", user.getEmail());
    hashMap.put("mobileNo", user.getMobile());

    Request request = new RequestBuilder<StringObject>()
        .setMethod(RequestBuilder.POST)
        .setUrl(URL_Register)
        .setResponseBodyClass(StringObject.class)
        .setBodyParams(hashMap)
        .setListener(listener)
        .createRequest();

    request.setTag(request_Tag);
    requestQueue.add(request);

  }

  public void login(UserCred user, final OnTaskCompleteListener<Token> listener) {

    String request_Tag = "LOGIN_REQUEST";

    HashMap<String, String> hashMap = new HashMap<>();
    hashMap.put("username", user.getEmail());
    hashMap.put("password", user.getpwd());

    Request request = new RequestBuilder<Token>()
        .setMethod(RequestBuilder.POST)
        .setUrl(URL_Login)
        .setResponseBodyClass(Token.class)
        .setBodyParams(hashMap)
        .setListener(listener)
        .createRequest();

    request.setTag(request_Tag);
    requestQueue.add(request);

  }

  public void editProfile(ProfileUpdateRequest updated,
      final OnTaskCompleteListener<UserProfile> listener) {

    String request_Tag = "EditUser_REQUEST";

    Request request = new RequestBuilder<UserProfile>()
        .setMethod(RequestBuilder.PUT)
        .setListener(listener)
        .setUrl(URL_EditUser)
        .setRequestBody(updated)
        .setResponseBodyClass(UserProfile.class)
        .setToken(((GlobalData) context.getApplicationContext()).getToken())
        .createRequest();

    request.setTag(request_Tag);
    requestQueue.add(request);

  }

  public void getUserProfile(final OnTaskCompleteListener<UserProfile> listener) {

    final String request_Tag = "GetUserDetails_Request";

    Request request = new RequestBuilder<UserProfile>()
        .setToken(((GlobalData) context.getApplicationContext()).getToken())
        .setMethod(RequestBuilder.GET)
        .setResponseBodyClass(UserProfile.class)
        .setUrl(URL_GetUserDetails)
        .setListener(listener)
        .createRequest();
    request.setTag(request_Tag);

    requestQueue.add(request);

  }

  public void setUserPic(String id, final OnTaskCompleteListener<UserProfile> listener) {

    String request_Tag = "AddPic_REQUEST";

    Request request = new RequestBuilder<UserProfile>()
        .setMethod(RequestBuilder.PUT)
        .setListener(listener)
        .setUrl(URL_EditUser)
        .addBodyParams("photoId", id)
        .setResponseBodyClass(UserProfile.class)
        .setToken(((GlobalData) context.getApplicationContext()).getToken())
        .createRequest();

    request.setTag(request_Tag);

    requestQueue.add(request);
  }


}
