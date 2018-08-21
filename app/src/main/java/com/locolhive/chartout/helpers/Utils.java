package com.locolhive.chartout.helpers;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.google.gson.GsonBuilder;
import com.locolhive.chartout.R;
import com.locolhive.chartout.api.API_KEY;
import com.locolhive.chartout.classes.CustomLatLng;
import com.locolhive.chartout.classes.DateRange;
import com.locolhive.chartout.classes.StringArrayObject;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Utils {

  private static final String TAG = Utils.class.getSimpleName() + " YOYO";

  public static ArrayAdapter<String> getSpinnerAdapter(Context context, String first,
      ArrayList<String> dataList) {

    if (first != null) {
      dataList.add(0, first);
      ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_item,
          dataList) {

        @Override
        public boolean isEnabled(int position) {
          return position != 0;
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {

          View view = super.getDropDownView(position, convertView, parent);
          TextView tv = (TextView) view;
          if (position == 0) {
            tv.setTextColor(Color.GRAY);
          } else {
            tv.setTextColor(Color.BLACK);
          }
          return view;
        }

      };
      adapter.setDropDownViewResource(R.layout.spinner_item);
      return adapter;
    } else {
      ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, dataList);
      adapter.setDropDownViewResource(R.layout.spinner_item);
      return adapter;
    }
  }

  public static CircularProgressDrawable getProgressDrawable(Context context) {
    CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
    circularProgressDrawable.setStrokeWidth(6f);
    circularProgressDrawable.setCenterRadius(30f);
    circularProgressDrawable.setColorSchemeColors(context.getResources().getIntArray(R.array.gplus_colors));
//    circularProgressDrawable.sets
//        .sweepSpeed(mSpeed)
//        .rotationSpeed(mSpeed)
    circularProgressDrawable.start();
    return circularProgressDrawable;
  }

  public static CircularProgressDrawable getBlackProgressDrawable(Context context) {
    CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
    circularProgressDrawable.setStrokeWidth(6f);
    circularProgressDrawable.setCenterRadius(30f);
    circularProgressDrawable.setColorSchemeColors(context.getResources().getColor(R.color.white));
    circularProgressDrawable.start();
    return circularProgressDrawable;
  }

  public static AlertDialog createOkDialog(final Context context, String title, String msg,
      DialogInterface.OnClickListener listener) {

    AlertDialog alertDialog = new Builder(context)
        .setCancelable(true)
        .setTitle(title)
        .setMessage(msg)
        .setPositiveButton("OK", listener)
        .create();

    alertDialog.setOnShowListener(new OnShowListener() {
      @Override
      public void onShow(DialogInterface dialog) {

        int _10dp = Utils.dpToPx(10, context);
        int _32dp = Utils.dpToPx(32, context);

        AlertDialog alertDialog = (AlertDialog) dialog;
        Button ok = alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
        ok.setMinimumHeight(0);
        ok.setMinHeight(0);
        ok.setTextSize(COMPLEX_UNIT_SP, 16);
        ok.setBackground(context.getResources().getDrawable(R.drawable.bg_rect_green));
        ok.setTextColor(context.getResources().getColor(R.color.white));
        ok.setPadding(_32dp, _10dp, _32dp, _10dp);

      }
    });
    return alertDialog;
  }

  public static ArrayList<CalendarDay> getValidRange(DateRange[] date) {

    ArrayList<CalendarDay> list = new ArrayList<>();

    for (DateRange dateRange : date) {
      CalendarDay[] calendarDay = dateRange.getCalendarDay();
      if (calendarDay != null) {
        CalendarDay start = calendarDay[0];
        if (start.isAfter(CalendarDay.today())) {
          list.add(start);
        }
      }
    }

    return list;
  }

  public static void tryCustomDeserializer() {

    String json = "    { \"naturalElements\": [" +
        "        \"Pets present at the Place\"," +
        "        \"Flowers, Trees and Greenery\"," +
        "        \"Parks available nearby\"," +
        "        \"Play Area present\"" +
        "    ] }";

    GsonBuilder gsonBldr = new GsonBuilder();
    gsonBldr.registerTypeAdapter(StringArrayObject.class, new CustomGsonDeserializer());
    StringArrayObject targetObject = gsonBldr.create().fromJson(json, StringArrayObject.class);

  }

  public static Uri getMapUsingAddress(String address) throws UnsupportedEncodingException {
    String encode = URLEncoder.encode(address, "UTF-8");

    String url = "https://maps.googleapis.com/maps/api/staticmap?center=" + encode
        + "&region=IN&zoom=16&size=800x500&key=AIzaSyDTThyA58xwR_uLDWvIx0Gb9G8PoD8k1Gc&markers=" + encode;

    return Uri.parse(url);
  }

  public static Uri getMapUsingLatLong(CustomLatLng location) {
    String encode = location.lat + "," + location.lng;

    String url = "https://maps.googleapis.com/maps/api/staticmap?center=" + encode
        + "&region=IN&zoom=16&size=800x500&key=" + API_KEY.KEY + "&markers=" + encode;

    return Uri.parse(url);
  }

  public static Uri getCircleMapUsingAddress(String address) throws UnsupportedEncodingException {
    String encode = URLEncoder.encode(address, "UTF-8");

    //https://maps.googleapis.com/maps/api/staticmap?center=Sector+15+Noida+
    // Uttar+Pradesh&zoom=14&size=600x300&key=AIzaSyDTThyA58xwR_uLDWvIx0Gb9G8PoD8k1Gc
    // &markers=icon:https://iiti-blackbox.000webhostapp.com/circle.png|size:mid|Sector+15+Noida+Uttar+Pradesh

    String url = "https://maps.googleapis.com/maps/api/staticmap?center=" + encode
        + "&region=IN&zoom=14&size=800x300&key=" + API_KEY.KEY + "&markers=" +
        "icon:https://iiti-blackbox.000webhostapp.com/circle.png|" + encode;

    return Uri.parse(url);
  }

  public static int dpToPx(int dp, Context context) {
    float scale = context.getResources().getDisplayMetrics().density;
    return ((int) (dp * scale + 0.5f));
  }

  public static String formatDate(String format, Date date) {
    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
    return sdf.format(date);
  }

  public static byte[] encrypt(Context context, byte[] b) throws Exception {
    byte[] keyStart = context.getString(R.string.ipsem).getBytes();
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    sr.setSeed(keyStart);
    kgen.init(128, sr);
    SecretKey skey = kgen.generateKey();
    byte[] key = skey.getEncoded();
    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    return cipher.doFinal(b);
  }

  public static byte[] decrypt(Context context, byte[] b) throws Exception {

    byte[] keyStart = context.getString(R.string.ipsem).getBytes();
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    sr.setSeed(keyStart);
    kgen.init(128, sr);
    SecretKey skey = kgen.generateKey();
    byte[] key = skey.getEncoded();
    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    return cipher.doFinal(b);

  }

  public static String addressToString(Address address) {
    String name = "";
    if (address.getLocality() != null) {
      name = address.getLocality();
    } else if (address.getAdminArea() != null) {
      name = address.getAdminArea();
    } else if (address.getPostalCode() != null) {
      name = address.getPostalCode();
    } else {
      name = "Unknown";
    }
    return name;
  }

  public static void TintMenu(int color, Menu menu) {

    for (int i = 0, size = menu.size(); i < size; i++) {
      MenuItem item = menu.getItem(i);

      Drawable drawable = item.getIcon();
      if (drawable != null) {
        // If we don't mutate the drawable, then all drawables with this id will have the ColorFilter
        drawable.mutate();
//        drawable.setTint();
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
      }
    }

  }

  public static String extractYTId(String ytUrl) {
    String vId = null;
    Pattern pattern = Pattern.compile(
        "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
        Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(ytUrl);
    if (matcher.matches()){
      vId = matcher.group(1);
    }
    return vId;
  }

  public static void launchYoutube(AppCompatActivity activity){
    PackageManager packageManager = activity.getPackageManager();
    Intent i = null;
    try{
      i = packageManager.getLaunchIntentForPackage("com.google.android.youtube");
    } catch (Exception e){
      e.printStackTrace();
    }
    if(i!=null){
      activity.startActivity(i);
    }else {
      Intent webIntent = new Intent(Intent.ACTION_VIEW,
          Uri.parse("http://www.youtube.com/"));
      activity.startActivity(webIntent);
    }
//    if(YouTubeIntents.canResolveSearchIntent(activity)){
//      Intent searchIntent = YouTubeIntents.createSearchIntent(activity, "chartout");
//      try {
//        activity.startActivity(searchIntent);
//      } catch (ActivityNotFoundException e) {
//        Log.e(TAG, "watchYoutubeVideo: ", e);
//      }
//    }
  }
}

