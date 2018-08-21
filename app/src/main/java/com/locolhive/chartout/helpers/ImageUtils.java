package com.locolhive.chartout.helpers;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Base64;
import android.util.Log;
import com.locolhive.chartout.api.SERVER;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class ImageUtils {

  public static final String Get_Img_Path = "/locolapi/v2/get/image/";
  private static final float BITMAP_SCALE = 0.4f;
  private static final int BLUR_RADIUS = 8;
  private static String TAG = ImageUtils.class.getSimpleName() + " YOYO";

  public static Uri getUri(String imageID) {
    return Uri.parse(SERVER.IP + Get_Img_Path + imageID + "/");
  }

  public static String getIdFromUri(Uri uri) {
    String s = uri.toString();
    s = s.replace(SERVER.IP + Get_Img_Path, "");
    s = s.replace("/", "");
    return s;
  }

  public static Bitmap fastblur(Bitmap sentBitmap) {
    float scale = BITMAP_SCALE;
    int radius = BLUR_RADIUS;
    int width = Math.round(sentBitmap.getWidth() * scale);
    int height = Math.round(sentBitmap.getHeight() * scale);
    sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);

    Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

    if (radius < 1) {
      return (null);
    }

    int w = bitmap.getWidth();
    int h = bitmap.getHeight();

    int[] pix = new int[w * h];
    Log.e("pix", w + " " + h + " " + pix.length);
    bitmap.getPixels(pix, 0, w, 0, 0, w, h);

    int wm = w - 1;
    int hm = h - 1;
    int wh = w * h;
    int div = radius + radius + 1;

    int r[] = new int[wh];
    int g[] = new int[wh];
    int b[] = new int[wh];
    int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
    int vmin[] = new int[Math.max(w, h)];

    int divsum = (div + 1) >> 1;
    divsum *= divsum;
    int dv[] = new int[256 * divsum];
    for (i = 0; i < 256 * divsum; i++) {
      dv[i] = (i / divsum);
    }

    yw = yi = 0;

    int[][] stack = new int[div][3];
    int stackpointer;
    int stackstart;
    int[] sir;
    int rbs;
    int r1 = radius + 1;
    int routsum, goutsum, boutsum;
    int rinsum, ginsum, binsum;

    for (y = 0; y < h; y++) {
      rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
      for (i = -radius; i <= radius; i++) {
        p = pix[yi + Math.min(wm, Math.max(i, 0))];
        sir = stack[i + radius];
        sir[0] = (p & 0xff0000) >> 16;
        sir[1] = (p & 0x00ff00) >> 8;
        sir[2] = (p & 0x0000ff);
        rbs = r1 - Math.abs(i);
        rsum += sir[0] * rbs;
        gsum += sir[1] * rbs;
        bsum += sir[2] * rbs;
        if (i > 0) {
          rinsum += sir[0];
          ginsum += sir[1];
          binsum += sir[2];
        } else {
          routsum += sir[0];
          goutsum += sir[1];
          boutsum += sir[2];
        }
      }
      stackpointer = radius;

      for (x = 0; x < w; x++) {

        r[yi] = dv[rsum];
        g[yi] = dv[gsum];
        b[yi] = dv[bsum];

        rsum -= routsum;
        gsum -= goutsum;
        bsum -= boutsum;

        stackstart = stackpointer - radius + div;
        sir = stack[stackstart % div];

        routsum -= sir[0];
        goutsum -= sir[1];
        boutsum -= sir[2];

        if (y == 0) {
          vmin[x] = Math.min(x + radius + 1, wm);
        }
        p = pix[yw + vmin[x]];

        sir[0] = (p & 0xff0000) >> 16;
        sir[1] = (p & 0x00ff00) >> 8;
        sir[2] = (p & 0x0000ff);

        rinsum += sir[0];
        ginsum += sir[1];
        binsum += sir[2];

        rsum += rinsum;
        gsum += ginsum;
        bsum += binsum;

        stackpointer = (stackpointer + 1) % div;
        sir = stack[(stackpointer) % div];

        routsum += sir[0];
        goutsum += sir[1];
        boutsum += sir[2];

        rinsum -= sir[0];
        ginsum -= sir[1];
        binsum -= sir[2];

        yi++;
      }
      yw += w;
    }
    for (x = 0; x < w; x++) {
      rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
      yp = -radius * w;
      for (i = -radius; i <= radius; i++) {
        yi = Math.max(0, yp) + x;

        sir = stack[i + radius];

        sir[0] = r[yi];
        sir[1] = g[yi];
        sir[2] = b[yi];

        rbs = r1 - Math.abs(i);

        rsum += r[yi] * rbs;
        gsum += g[yi] * rbs;
        bsum += b[yi] * rbs;

        if (i > 0) {
          rinsum += sir[0];
          ginsum += sir[1];
          binsum += sir[2];
        } else {
          routsum += sir[0];
          goutsum += sir[1];
          boutsum += sir[2];
        }

        if (i < hm) {
          yp += w;
        }
      }
      yi = x;
      stackpointer = radius;
      for (y = 0; y < h; y++) {
        // Preserve alpha channel: ( 0xff000000 & pix[yi] )
        pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

        rsum -= routsum;
        gsum -= goutsum;
        bsum -= boutsum;

        stackstart = stackpointer - radius + div;
        sir = stack[stackstart % div];

        routsum -= sir[0];
        goutsum -= sir[1];
        boutsum -= sir[2];

        if (x == 0) {
          vmin[y] = Math.min(y + r1, hm) * w;
        }
        p = x + vmin[y];

        sir[0] = r[p];
        sir[1] = g[p];
        sir[2] = b[p];

        rinsum += sir[0];
        ginsum += sir[1];
        binsum += sir[2];

        rsum += rinsum;
        gsum += ginsum;
        bsum += binsum;

        stackpointer = (stackpointer + 1) % div;
        sir = stack[stackpointer];

        routsum += sir[0];
        goutsum += sir[1];
        boutsum += sir[2];

        rinsum -= sir[0];
        ginsum -= sir[1];
        binsum -= sir[2];

        yi += w;
      }
    }

    Log.e("pix", w + " " + h + " " + pix.length);
    bitmap.setPixels(pix, 0, w, 0, 0, w, h);

    return (bitmap);

  }

  public static Uri getUriToResource(@NonNull Context context,
      @AnyRes int resId)
      throws Resources.NotFoundException {
    /** Return a Resources instance for your application's package. */
    Resources res = context.getResources();
    /**
     * Creates a Uri which parses the given encoded URI string.
     * @param uriString an RFC 2396-compliant, encoded URI
     * @throws NullPointerException if uriString is null
     * @return Uri for this given uri string
     */
    Uri resUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
        "://" + res.getResourcePackageName(resId)
        + '/' + res.getResourceTypeName(resId)
        + '/' + res.getResourceEntryName(resId));
    /** return uri */
    return resUri;
  }

  public static Drawable tintMyDrawable(Drawable drawable, int color) {
    drawable = DrawableCompat.wrap(drawable);
    DrawableCompat.setTint(drawable, color);
    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
    return drawable;
  }

  public static Uri getImageUri(Context context, Bitmap photo) {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    photo.compress(Bitmap.CompressFormat.PNG, 80, bytes);
    String path = MediaStore.Images.Media
        .insertImage(context.getContentResolver(), photo, "Title", null);
    return Uri.parse(path);
  }

  public static Bitmap StringToBitMap(String encodedString) {
    try {
      byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
      return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
    } catch (Exception e) {
      //Log.e(TAG,"StringToBitMap error: ",e);
      return null;
    }
  }

  public static String BitMapToString(Bitmap bitmap, int quality) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.WEBP, quality, baos);
    byte[] b = baos.toByteArray();
    return Base64.encodeToString(b, Base64.DEFAULT);
  }

  /**
   * Check Image Exist (or) Not
   */
  public static boolean checkimage(String file_name, String file_path) {
    boolean flag;
    File path = new File(file_path);

    File file = new File(path, file_name);
    //Log.i("file", "exists");
//Log.i("file", "not exist");
    flag = file.exists();

    return flag;
  }

  /**
   * Get Image from the given path
   */
  public static Bitmap getImage(String file_name, String file_path) {

    File path;
    path = new File(file_path);
    File file = new File(path, file_name);

    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
    options.inSampleSize = 2;
    options.inTempStorage = new byte[16 * 1024];

    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

    if (bitmap != null) {
      return bitmap;
    } else {
      return null;
    }
  }

  public boolean isDeviceSupportCamera(Context context) {
    // this device has a camera
// no camera on this device
    return context.getPackageManager().hasSystemFeature(
        PackageManager.FEATURE_CAMERA);
  }

}
