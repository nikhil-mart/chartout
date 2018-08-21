package com.locolhive.chartout.api;

import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.locolhive.chartout.interfaces.OnTaskCompleteListener;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;


public class EmptyResponseRequestBuilder {

  public static final int GET = Request.Method.GET;
  public static final int POST = Request.Method.POST;
  public static final int PUT = Request.Method.PUT;
  private static final String TAG = EmptyResponseRequestBuilder.class.getSimpleName() + " YOYO";
  private String url;
  private Object requestBody;
  private HashMap<String, String> urlParams;
  private HashMap<String, String> bodyParams;
  private int method;
  private String token;

  private OnTaskCompleteListener<String> listener;

  public EmptyResponseRequestBuilder() {
  }

  //region Setters

  public EmptyResponseRequestBuilder setToken(String token) {
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

  public EmptyResponseRequestBuilder setUrl(String url) {
    this.url = url;
    return this;
  }

  public EmptyResponseRequestBuilder setRequestBody(Object requestBody) {
    this.requestBody = requestBody;
    return this;
  }

  public EmptyResponseRequestBuilder setUrlParams(HashMap<String, String> urlParams) {
    this.urlParams = urlParams;
    return this;
  }

  public EmptyResponseRequestBuilder addUrlParams(String key, String value) {
    if (this.urlParams == null) {
      this.urlParams = new HashMap<>();
    }
    this.urlParams.put(key, value);
    return this;
  }

  public EmptyResponseRequestBuilder setBodyParams(HashMap<String, String> bodyParams) {
    this.bodyParams = bodyParams;
    return this;
  }

  public EmptyResponseRequestBuilder addBodyParams(String key, String value) {
    if (this.bodyParams == null) {
      this.bodyParams = new HashMap<>();
    }
    this.bodyParams.put(key, value);
    return this;
  }

  public EmptyResponseRequestBuilder setMethod(int method) {
    this.method = method;
    return this;
  }

  public EmptyResponseRequestBuilder setListener(OnTaskCompleteListener<String> listener) {
    this.listener = listener;
    return this;
  }

  //endregion

  public Request<String> createRequest() {

    if (url == null) {
      throw new IllegalStateException("URL can not be empty!");
    } else if (listener == null) {
      throw new IllegalStateException("listener can not be empty!");
    } else {

      final Response.Listener<String> successListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
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
                err = "code: " + error.networkResponse.statusCode + " body: " + err;
              } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
              }
            }
          }
          listener.onFailure(err);
        }
      };

      return new Request<String>(method, createUrl(), errorListener) {

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
        protected void deliverResponse(String response) {
          successListener.onResponse(response);
        }

        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
          if (response.statusCode == 200) {
            return Response.success(String.valueOf(response.statusCode),
                HttpHeaderParser.parseCacheHeaders(response));
          } else {
            return Response.error(new VolleyError(response));
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