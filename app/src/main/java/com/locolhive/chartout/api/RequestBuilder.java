package com.locolhive.chartout.api;

import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.locolhive.chartout.interfaces.OnTaskCompleteListener;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.json.JSONObject;


public class RequestBuilder<T> {

  public static final int GET = Request.Method.GET;
  public static final int POST = Request.Method.POST;
  public static final int PUT = Request.Method.PUT;
  private static final String TAG = RequestBuilder.class.getSimpleName() + " YOYO";
  private String url;
  private Class<T> responseBodyClass;
  private Object requestBody;
  private HashMap<String, String> urlParams;
  private HashMap<String, String> bodyParams;
  private int method;
  private String token;

  private OnTaskCompleteListener<T> listener;

  public RequestBuilder() {
  }

  //region Setters

  public RequestBuilder setToken(String token) {
    if (token == null) {
      try {
        throw new Exception("Null token");
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      this.token = token;
    }
    return this;
  }

  public RequestBuilder setUrl(String url) {
    this.url = url;
    return this;
  }

  public RequestBuilder setResponseBodyClass(Class<T> responseBodyClass) {
    this.responseBodyClass = responseBodyClass;
    return this;
  }

  public RequestBuilder setRequestBody(Object requestBody) {
    this.requestBody = requestBody;
    return this;
  }

  public RequestBuilder setUrlParams(HashMap<String, String> urlParams) {
    this.urlParams = urlParams;
    return this;
  }

  public RequestBuilder addUrlParams(String key, String value) {
    if (this.urlParams == null) {
      this.urlParams = new HashMap<>();
    }
    this.urlParams.put(key, value);
    return this;
  }

  public RequestBuilder setBodyParams(HashMap<String, String> bodyParams) {
    this.bodyParams = bodyParams;
    return this;
  }

  public RequestBuilder addBodyParams(String key, String value) {
    if (this.bodyParams == null) {
      this.bodyParams = new HashMap<>();
    }
    this.bodyParams.put(key, value);
    return this;
  }

  public RequestBuilder setMethod(int method) {
    this.method = method;
    return this;
  }

  public RequestBuilder setListener(OnTaskCompleteListener<T> listener) {
    this.listener = listener;
    return this;
  }

  //endregion

  public Request<T> createRequest() {

    if (url == null) {
      throw new IllegalStateException("URL can not be empty!");
    } else if (responseBodyClass == null) {
      throw new IllegalStateException("responseBodyClass can not be empty!");
    } else if (listener == null) {
      throw new IllegalStateException("listener can not be empty!");
    } else {

      final Response.Listener<T> successListener = new Response.Listener<T>() {
        @Override
        public void onResponse(T response) {
          listener.onSuccess(response);
        }
      };

      Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
          String err = error.getMessage();
          if (error.networkResponse != null) {
            if (error.networkResponse.data != null) {

              try {

                err = new String(error.networkResponse.data, "UTF-8");
                try {
                  JsonElement parse = new JsonParser().parse(err);
                  if (!parse.isJsonNull()) {
                    if (parse.isJsonObject()) {
                      err = "";
                      JsonObject asJsonObject = parse.getAsJsonObject();
                      Set<Entry<String, JsonElement>> entries = asJsonObject.entrySet();
                      for (Entry<String, JsonElement> entry : entries) {
                        JsonElement value = entry.getValue();
                        if (!value.isJsonNull()) {
                          err = value.getAsString();
                        }
                      }
                    }
                  }
                } catch (Exception e) {
                  //Ignored
                }
                err = error.networkResponse.statusCode + ", " + err;

              } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
              }
            }
          }
          if (err == null) {
            err = "Unknown Error";
          }
          listener.onFailure(err);
        }
      };

      return new Request<T>(method, createUrl(), errorListener) {

        @Override
        public String getBodyContentType() {
          return "application/json";
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
          byte[] body = new byte[0];
          try {
            String s;
            if (requestBody == null) {
              if (bodyParams != null) {
                s = new JSONObject(bodyParams).toString();
              } else {
                return super.getBody();
              }
            } else {
              s = new Gson().toJson(requestBody);
            }
            Log.i(TAG, "RequestBody:\n" + s);
            body = s.getBytes("UTF-8");

          } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Unable to gets bytes from JSON", e.fillInStackTrace());
          }
          return body;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
          if (token != null) {
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Bearer " + token);
            return headers;
          } else {
            return super.getHeaders();
          }
        }

        @Override
        protected void deliverResponse(T response) {
          successListener.onResponse(response);
        }

        @Override
        protected Response<T> parseNetworkResponse(NetworkResponse response) {
          try {
            String json = new String(
                response.data,
                HttpHeaderParser.parseCharset(response.headers));
            Gson gson = new Gson();
            return Response.success(
                gson.fromJson(json, responseBodyClass),
                HttpHeaderParser.parseCacheHeaders(response));
          } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "UnsupportedEncodingException: ", e);
            return Response.error(new ParseError(e));
          } catch (JsonSyntaxException e) {
            Log.e(TAG, "JsonSyntaxException: ", e);
            return Response.error(new ParseError(e));
          }
        }

      };
    }
  }

  private String createUrl() {
    String urlf = "";
    if (urlParams != null) {
      if (!urlParams.isEmpty()) {
        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append("?");
        for (String s : urlParams.keySet()) {
          urlBuilder.append(s).append("=");
          urlBuilder.append(urlParams.get(s)).append("&");
        }
        urlBuilder.deleteCharAt(urlBuilder.lastIndexOf("&"));
        urlf = urlBuilder.toString();
        return urlf;
      }
      return url;
    } else {
      return url;
    }
  }

}