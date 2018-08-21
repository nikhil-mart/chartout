package com.locolhive.chartout.adapters;

import static com.locolhive.chartout.api.UserApi.URL_EditUser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.RequestOptions;
import com.locolhive.chartout.adapters.AddImageAdapter.PhotoVH;
import com.locolhive.chartout.R;
import com.locolhive.chartout.api.ImageRequestBuilder;
import com.locolhive.chartout.api.ImageRequestBuilder.MultiPartRequest;
import com.locolhive.chartout.api.RequestBuilder;
import com.locolhive.chartout.classes.StringArrayHolder;
import com.locolhive.chartout.helpers.GlideApp;
import com.locolhive.chartout.helpers.ImageUtils;
import com.locolhive.chartout.helpers.PhotoFullDialog;
import com.locolhive.chartout.helpers.Utils;
import com.locolhive.chartout.home.BlankActivity;
import com.locolhive.chartout.home.GlobalData;
import com.locolhive.chartout.interfaces.ErrorListener;
import com.locolhive.chartout.interfaces.OnResultListener;
import com.locolhive.chartout.interfaces.OnTaskCompleteListener;
import com.locolhive.chartout.profile.ProfileUpdateRequest;
import com.locolhive.chartout.profile.UserProfile;
import java.util.ArrayList;

public class AddImageAdapter extends RecyclerView.Adapter<PhotoVH> {

  private static final String TAG = AddImageAdapter.class.getSimpleName() + " YOYO";
  private Context context;

  private int num;
  private ArrayList<Uri> list;
  private ArrayList<Boolean> showProgress;
  private ArrayList<Boolean> isNew;

  private RequestQueue queue;

  public AddImageAdapter(ArrayList<String> list, Context context) {

    this.context = context;
    this.list = new ArrayList<>();
    for (String s : list) {
      this.list.add(ImageUtils.getUri(s));
    }
    this.showProgress = new ArrayList<>();
    isNew = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      this.showProgress.add(false);
      isNew.add(false);
    }
    num = list.size() + 1;
    queue = Volley.newRequestQueue(context);

  }

  private void add(Uri s) {
    list.add(s);
    showProgress.add(true);
    isNew.add(true);
    num = num + 1;
    notifyItemInserted(num - 1);
    notifyItemChanged(num - 2);
  }

  private void remove(int position) {

    list.remove(position);
    showProgress.remove(position);
    isNew.remove(position);
    num--;

    ArrayList<String> finalStrings = new ArrayList<>();
    for (int k = 0; k < list.size(); k++) {
      if (!showProgress.get(k)) {
        if (!isNew.get(k)) {
          finalStrings.add(ImageUtils.getIdFromUri(list.get(k)));
        }
      }
    }
    updateList(finalStrings, new OnTaskCompleteListener<UserProfile>() {
      @Override
      public void onSuccess(UserProfile response) {
        Toast.makeText(context, "Image Removed!", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onFailure(String error) {
        Toast.makeText(context, "Error removing photo", Toast.LENGTH_SHORT).show();
      }
    });

    notifyItemRemoved(position);
    notifyItemChanged(num - 1);
  }

  private void finishX(Uri uri, final String s) {
    final int i = list.indexOf(uri);
    if (i >= 0 && i < list.size()) {
      showProgress.set(i, false);

      isNew.set(i, false);
      list.set(i, ImageUtils.getUri(s));

      ArrayList<String> finalStrings = new ArrayList<>();
      for (int k = 0; k < list.size(); k++) {
        if (!showProgress.get(k)) {
          if (!isNew.get(k)) {
            finalStrings.add(ImageUtils.getIdFromUri(list.get(k)));
          }
        }
      }

      updateList(finalStrings, new OnTaskCompleteListener<UserProfile>() {
        @Override
        public void onSuccess(UserProfile response) {
          Log.i(TAG, "onSuccess: image added to profile: " + s);
          notifyItemChanged(i);
          ((GlobalData) context.getApplicationContext()).setUser(response);
        }

        @Override
        public void onFailure(String error) {
          Log.e(TAG, "Error adding " + s + " to profile\nError: " + error);
        }
      });
    }

  }

  private void updateList(final ArrayList<String> finalStrings, final OnTaskCompleteListener<UserProfile> listener) {

    ((GlobalData) context.getApplicationContext()).getUser(context, new OnResultListener<UserProfile>() {
      @Override
      public void OnResult(UserProfile result) {

        ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest(result);
        profileUpdateRequest.setOtherPhotoIds(finalStrings);

        String request_Tag = "UpdatePics_REQUEST";

        StringArrayHolder ims = new StringArrayHolder();
        ims.otherPhotoIds = finalStrings;
        //noinspection unchecked
        Request request = new RequestBuilder<UserProfile>()
            .setMethod(RequestBuilder.PUT)
            .setListener(listener)
            .setUrl(URL_EditUser)
            .setRequestBody(ims)
            .setResponseBodyClass(UserProfile.class)
            .setToken(((GlobalData) context.getApplicationContext()).getToken())
            .createRequest();

        request.setTag(request_Tag);
        queue.add(request);

      }
    });
  }

  @NonNull
  @Override
  public PhotoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view;

    if (viewType == 0) {
      view = LayoutInflater.from(context).inflate(R.layout.card_photo_vert, parent, false);
    } else {
      view = LayoutInflater.from(context).inflate(R.layout.card_plus_big, parent, false);
    }

    return new PhotoVH(view);
  }

  @Override
  public int getItemViewType(int position) {
    if (position == num - 1) {
      return 1;
    } else {
      return 0;
    }
  }

  @Override
  public void onBindViewHolder(@NonNull PhotoVH holder, int position) {
    holder.setData(position);
  }

  @Override
  public int getItemCount() {
    return num;
  }

  class PhotoVH extends RecyclerView.ViewHolder {

    ImageView img;
    ImageButton btnDelete;
    ProgressBar progressBar;
    View itemView;

    PhotoVH(View itemView) {
      super(itemView);
      this.itemView = itemView;
      img = itemView.findViewById(R.id.img);
      btnDelete = itemView.findViewById(R.id.ib_close);
      progressBar = itemView.findViewById(R.id.loading);
    }

    void setPlusListener() {

      itemView.findViewById(R.id.btn).setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {

          Intent i = new Intent(Intent.ACTION_GET_CONTENT);
          i.setType("image/*");
          ((BlankActivity) context).imgUriResult = new OnResultListener<Uri>() {
            @Override
            public void OnResult(final Uri result) {
              if (result != null) {
                Log.i(TAG, "OnResult: " + result.toString());

                add(result);

                MultiPartRequest request = new ImageRequestBuilder(context,
                    new Listener<String>() {
                      @Override
                      public void onResponse(String imgId) {
                        Log.i(TAG, "onResponse: " + imgId);
                        finishX(result, imgId);
                      }
                    },
                    new ErrorListener() {
                      @Override
                      public void onError(String err) {
                        Log.e(TAG, "onError: " + err);
                      }
                    })
                    .addUri(result)
                    .setToken(((GlobalData) context.getApplicationContext()).getToken())
                    .build();

                queue.add(request);

              } else {
                Log.i(TAG, "Image picker cancelled");
              }
            }
          };
          ((BlankActivity) context).startActivityForResult(i, BlankActivity.ImageRC);
        }
      });
    }

    void setPhotoListeners(final int position) {
      Log.i(TAG, "setPhotoListeners: " + position);

      GlideApp.with(context).load(list.get(position))
          .apply(RequestOptions.centerCropTransform())
          .placeholder(Utils.getProgressDrawable(context))
          .into(img);

      if (showProgress.get(position)) {
        progressBar.setVisibility(View.VISIBLE);
        img.setFocusable(false);
        img.setClickable(false);
        img.setOnClickListener(null);
      } else {
        progressBar.setVisibility(View.GONE);
        img.setFocusable(true);
        img.setClickable(true);
        img.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {

            PhotoFullDialog.newInstance(context, img, list.get(position)).display();
//            new PhotoFullPopupWindow(context, img, list.get(position), null);

//            new ImageViewer.Builder<>(context, list)
//                .allowZooming(true)
//                .setStartPosition(position)
//                .setBackgroundColorRes(R.color.black_trans180)
//                .show();
          }
        });
      }

      btnDelete.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          Log.i(TAG, "btnDelete onClick: ");
          remove(position);
        }
      });

    }

    public void setData(int position) {
      Log.i(TAG, "setData: " + position);
      if (position == num - 1) {
        setPlusListener();
      } else {
        setPhotoListeners(position);
      }
    }
  }

}
