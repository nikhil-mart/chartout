package com.locolhive.chartout.api;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import com.locolhive.chartout.R;
import com.locolhive.chartout.helpers.DialogProgress;
import com.locolhive.chartout.helpers.Utils;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import io.paperdb.Paper;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UploadVideoApi {

  private static final String TAG = "YOYO";
  private static final int CAPTURE_RETURN = 1;
  private static final int GALLERY_RETURN = 2;
  private static final int REQUEST_AUTHORIZATION = 1001;
  private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;

  private static final String[] SCOPES = {YouTubeScopes.YOUTUBE, YouTubeScopes.YOUTUBE_UPLOAD, YouTubeScopes.YOUTUBEPARTNER,
      YouTubeScopes.YOUTUBE_FORCE_SSL};

  private AppCompatActivity activity;
  private ProgressListener listener;
  private DialogProgress dialogProgress;
  private String accnt;
  private GoogleAccountCredential cred;

  private static String ACCNT_KEY;

  public UploadVideoApi(AppCompatActivity activity, ProgressListener listener) {
    this.activity = activity;
    this.listener = listener;
    dialogProgress = DialogProgress.newInstance();
  }

  public UploadVideoApi(String s, AppCompatActivity uploadActivity, ProgressListener listener) {
    accnt = s;
    activity = uploadActivity;
    this.listener = listener;
    dialogProgress = DialogProgress.newInstance();

    ACCNT_KEY = accnt.substring(0, accnt.indexOf('@'));
    if (Paper.book().contains(ACCNT_KEY)) {
      accnt = Paper.book().read(ACCNT_KEY);
    }

  }

  public void begin() {
    Permissions.check(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
        "Need permissionsGet Accounts Storage permissions are required ", new Permissions.Options()
            .setSettingsDialogTitle("Warning!").setRationaleDialogTitle("Info"),
        new PermissionHandler() {
          @Override
          public void onGranted() {
            checkGoogle();
          }
        });
  }

  public void onActivityResult(
      int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case REQUEST_GOOGLE_PLAY_SERVICES:
        if (resultCode != RESULT_OK) {
          listener.onFailure("This app requires Google Play Services. Please install " +
                  "Google Play Services on your device and try again.");
        } else {
          checkGoogle();
        }
        break;
      case REQUEST_AUTHORIZATION:
        if (resultCode == RESULT_OK) {
          checkGoogle();
        }else {
          listener.onFailure("Google SignIn Cancelled");
        }
        break;

      case CAPTURE_RETURN:
      case GALLERY_RETURN:
        if (resultCode == RESULT_OK) {
          if (cred != null) {
            new UploadVideoAsync(activity, data.getData(), cred).execute();
          } else {
            listener.onFailure("Invalid Account");
          }
//          new GetCredentials(activity,data,accnt).execute();
        }else {
          listener.onFailure("Video Picker Cancelled");
        }
        break;

    }
  }

  private static File getFileFromUri(Uri uri, Activity activity) {

    try {
      String filePath = null;

      String[] proj = {MediaStore.Video.VideoColumns.DATA};

      Cursor cursor = activity.getContentResolver().query(uri, proj, null, null, null);

      if (cursor.moveToFirst()) {
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATA);
        filePath = cursor.getString(column_index);
      }

      cursor.close();

      File file = new File(filePath);
      cursor.close();
      return file;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  private void initVideoPicker() {

    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_PICK);
    intent.setType("video/*");

    List<ResolveInfo> list = activity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
    if (list.size() <= 0) {
      Log.d(TAG, "no video picker intent on activity hardware");
      listener.onFailure("No video picker found on device");
      return;
    }
    activity.startActivityForResult(intent, GALLERY_RETURN);

  }

  @SuppressLint("StaticFieldLeak")
  private class GetCredentials extends AsyncTask<Void, Void, String> {

    GoogleAccountCredential mCredential;
    final ThreadLocal<Activity> activity = new ThreadLocal<Activity>();
    //    Intent data;
    Intent error;

    GetCredentials(Activity activity, String accnt) {
//      this.data = data;
      this.activity.set(activity);
      showProgress();
      mCredential = GoogleAccountCredential.usingOAuth2(
          activity, Arrays.asList(SCOPES))
          .setBackOff(new ExponentialBackOff());
      Log.i(TAG, "GetCredentials: email: " + accnt);
      mCredential.setSelectedAccountName(accnt);
//      String selectedAccountName = mCredential.getSelectedAccountName();

    }

    @Override
    protected String doInBackground(Void... voids) {
      String token = "";
      try {
        token = mCredential.getToken();
      } catch (UserRecoverableAuthException e) {
        error = e.getIntent();
      } catch (IOException | GoogleAuthException | IllegalArgumentException e) {
        e.printStackTrace();
      }
      return token;
    }

    @Override
    protected void onPostExecute(String s) {
      hideProgress();
      if(!s.equals("")) {

        cred = mCredential;
        Paper.book().write(ACCNT_KEY, cred.getSelectedAccountName());

        initVideoPicker();
//        new UploadVideoAsync(activity.get(), data.getData(), mCredential).execute();
      }
      else {
        if (error != null) {
          activity.get().startActivityForResult(error, REQUEST_AUTHORIZATION);
        } else {
          accnt = null;
          checkGoogle();
        }

      }
      super.onPostExecute(s);
    }
  }

  @SuppressLint("StaticFieldLeak")
  private class UploadVideoAsync extends AsyncTask<Void, MediaHttpUploader, String> {

    private String PRIVACY_STATUS = "unlisted";       // or public,private

    Uri data;
    GoogleAccountCredential cred;
    ProgressDialog progressDialog;
    @SuppressLint("StaticFieldLeak")
    Context context;

    public UploadVideoAsync(Context context, Uri data, GoogleAccountCredential mCredential) {
      this.data = data;
      this.context = context;
      cred = mCredential;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      progressDialog = new ProgressDialog(context);
      progressDialog.setMessage("Preparing video for upload");
      progressDialog.setProgressDrawable(Utils.getProgressDrawable(activity));
      progressDialog.setIndeterminate(true);
      progressDialog.setCancelable(false);
      progressDialog.show();
    }

    @Override
    protected String doInBackground(Void... voids) {
      return uploadYoutube(data, cred, context);
    }

    @Override
    protected void onProgressUpdate(MediaHttpUploader... values) {
      super.onProgressUpdate(values);
      if(values.length>0) {
        Log.d(TAG, "progressChanged: " + values[0].getUploadState());
        try {
          double progress = values[0].getProgress();
          Log.i(TAG, "progress: " + progress*100);
          if(progress>0) {
            if(progressDialog.isShowing() && progressDialog.isIndeterminate()) {
              progressDialog.dismiss();
              progressDialog = new ProgressDialog(context);
              progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
              progressDialog.setTitle("Upload in progress...");
              progressDialog.setIndeterminate(false);
              progressDialog.setCancelable(false);
              progressDialog.show();
            }
            progressDialog.setProgress((int)(progress*100));
          }
        } catch (IOException e) {
//              e.printStackTrace();
        }
        switch (values[0].getUploadState()) {
          case INITIATION_STARTED:
            break;
          case INITIATION_COMPLETE:
            break;
          case MEDIA_IN_PROGRESS:
            break;
          case MEDIA_COMPLETE:
          case NOT_STARTED:
            Log.d(TAG, "progressChanged: upload_not_started");
            break;
        }
      }
    }

    @Override
    protected void onPostExecute(String s) {
      super.onPostExecute(s);
      Toast.makeText(context, "VideoId is " + s, Toast.LENGTH_SHORT).show();
      Log.i("VideoId", "" + s);
      progressDialog.dismiss();
      if(s!=null && !s.equals("")){
        listener.onComplete(s);
      }else {
        listener.onFailure("Unknown error");
      }
    }

    private String uploadYoutube(Uri data, final GoogleAccountCredential mCredential, Context context) {

      HttpTransport transport = AndroidHttp.newCompatibleTransport();
      JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

      HttpRequestInitializer initializer = new HttpRequestInitializer() {
        @Override
        public void initialize(HttpRequest request) {
          mCredential.initialize(request);
          request.setLoggingEnabled(true);
        }
      };


      YouTube youtube = new YouTube.Builder(transport, jsonFactory, initializer)
          .setApplicationName("Chartout")
          .build();

      String PARTS = "snippet,status,contentDetails";

      String videoId = null;
      try {

        Video metadata = new Video();
        metadata.setStatus(new VideoStatus().setPrivacyStatus(PRIVACY_STATUS));

        VideoSnippet videoDetails = new VideoSnippet();
        videoDetails.setTitle("Chartout Listing Video " + System.currentTimeMillis());
        videoDetails.setDescription("Video uploaded by Chartout");
        videoDetails.setTags(Arrays.asList("Chartout","Experiences","New","Fun","Near","Me"));
        metadata.setSnippet(videoDetails);

        YouTube.Videos.Insert videoInsert = youtube.videos().insert(
            PARTS,
            metadata,
            getMediaContent(getFileFromUri(data, (Activity) context)))
            .setOauthToken(mCredential.getToken());

        MediaHttpUploader uploader = videoInsert.getMediaHttpUploader();
        uploader.setDirectUploadEnabled(false);
        MediaHttpUploaderProgressListener progressListener =
            new MediaHttpUploaderProgressListener() {
              public void progressChanged(MediaHttpUploader uploader) {
                publishProgress(uploader);
              }
            };

        uploader.setProgressListener(progressListener);

        Log.d(TAG, "Uploading..");
        Video returnedVideo = videoInsert.execute();
        Log.d(TAG, "Video upload completed");
        videoId = returnedVideo.getId();
        Log.d(TAG, String.format("videoId = [%s]", videoId));

      } catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
        Log.e(TAG, "GooglePlayServicesAvailabilityIOException", availabilityException);
        listener.onFailure("Google Play Services not available");
      } catch (UserRecoverableAuthIOException userRecoverableException) {
        Log.i(TAG, String.format("UserRecoverableAuthIOException: %s",
            userRecoverableException.getMessage()));
        listener.onFailure("Google Play Services not available");
      } catch (IOException e) {
        Log.e(TAG, "IOException", e);
        listener.onFailure("Google Play Services not available");
      } catch (GoogleAuthException e) {
        e.printStackTrace();
        listener.onFailure("Error accessing account");
      }

      return videoId;

    }

    private AbstractInputStreamContent getMediaContent(File file) throws FileNotFoundException {
      InputStreamContent mediaContent = new InputStreamContent(
          "video/*",
          new BufferedInputStream(new FileInputStream(file)));
      mediaContent.setLength(file.length());
      return mediaContent;
    }

  }

  /**
   * Attempt to call the API, after verifying that all the preconditions are satisfied. The preconditions are: Google Play Services installed, an
   * account was selected and the device currently has online access. If any of the preconditions are not satisfied, the app will prompt the user as
   * appropriate.
   */
  private void checkGoogle() {

    if (!isGooglePlayServicesAvailable()) {
      acquireGooglePlayServices();
    }else if (!isDeviceOnline()) {
      listener.onFailure("Network Error");
    } else {

      Builder builder = new Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
          .requestEmail()
          .requestProfile()
          .requestIdToken(activity.getString(R.string.default_web_client_id))
          .requestScopes(
              new Scope(SCOPES[0]),
              new Scope(SCOPES[1]),
              new Scope(SCOPES[2]),
              new Scope(SCOPES[3]));
//      builder.setAccountName("tempaccnt0001@gmail.com");
      if (accnt != null) {
        builder.setAccountName(accnt);
      }

      final GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(activity, builder.build());

      Task<GoogleSignInAccount> task = googleSignInClient.silentSignIn();
      if (task.isSuccessful()) {
        GoogleSignInAccount signInAccount = task.getResult();
        accnt = signInAccount.getEmail();
        new GetCredentials(activity, accnt).execute();
//        initVideoPicker();

      } else {
        showProgress();
        task.addOnCompleteListener(new OnCompleteListener<GoogleSignInAccount>() {
          @Override
          public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
            hideProgress();

            try {
              GoogleSignInAccount signInAccount = task.getResult(ApiException.class);
              accnt = signInAccount.getEmail();
//              initVideoPicker();
              new GetCredentials(activity, accnt).execute();

            } catch (ApiException apiException) {
              switch (apiException.getStatusCode()) {
                case GoogleSignInStatusCodes.NETWORK_ERROR:
                  listener.onFailure("Network Error");
                  break;
                case GoogleSignInStatusCodes.CANCELED:
                case GoogleSignInStatusCodes.INTERRUPTED:
                  listener.onFailure("SignIn Cancelled");
                  break;
                case GoogleSignInStatusCodes.SIGN_IN_REQUIRED:

                  Intent signInIntent = googleSignInClient.getSignInIntent();
                  activity.startActivityForResult(signInIntent, REQUEST_AUTHORIZATION);

                  break;
                case GoogleSignInStatusCodes.INVALID_ACCOUNT:
                  listener.onFailure("Invalid Account");
                  break;
                default:
                  listener.onFailure("Google SignIn Error: "+apiException.getMessage());
              }
            } catch (Throwable throwable) {
              throwable.printStackTrace();
              listener.onFailure("Unknown Error");
            }
          }
        });
      }
    }

  }

  private void showProgress() {
    if(dialogProgress==null)
      dialogProgress = DialogProgress.newInstance();
    dialogProgress.display(activity);
  }

  private void hideProgress() {
    dialogProgress.done();
  }


  /**
   * Checks whether the device currently has a network connection.
   *
   * @return true if the device has a network connection, false otherwise.
   */
  private boolean isDeviceOnline() {
    ConnectivityManager connMgr =
        (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
    assert connMgr != null;
    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    return (networkInfo != null && networkInfo.isConnected());
  }

  /**
   * Check that Google Play services APK is installed and up to date.
   *
   * @return true if Google Play Services is available and up to date on activity device; false otherwise.
   */
  private boolean isGooglePlayServicesAvailable() {
    GoogleApiAvailability apiAvailability =
        GoogleApiAvailability.getInstance();
    final int connectionStatusCode =
        apiAvailability.isGooglePlayServicesAvailable(activity);
    return connectionStatusCode == ConnectionResult.SUCCESS;
  }

  /**
   * Attempt to resolve a missing, out-of-date, invalid or disabled Google Play Services installation via a user dialog, if possible.
   */
  private void acquireGooglePlayServices() {
    GoogleApiAvailability apiAvailability =
        GoogleApiAvailability.getInstance();
    final int connectionStatusCode =
        apiAvailability.isGooglePlayServicesAvailable(activity);
    if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
      showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
    }
  }

  /**
   * Display an error dialog showing that Google Play Services is missing or out of date.
   *
   * @param connectionStatusCode code describing the presence (or lack of) Google Play Services on activity device.
   */
  private void showGooglePlayServicesAvailabilityErrorDialog(
      final int connectionStatusCode) {
    GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
    Dialog dialog = apiAvailability.getErrorDialog(
        activity,
        connectionStatusCode,
        REQUEST_GOOGLE_PLAY_SERVICES);
    dialog.show();
  }

  public interface ProgressListener{
    void onComplete(String id);
    void onFailure(String msg);
  }

}
