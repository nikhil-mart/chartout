package com.locolhive.chartout.create;

import static com.locolhive.chartout.create.CreateActivity.GET_SinglePic_CODE;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.locolhive.chartout.R;
import com.locolhive.chartout.api.ImageRequestBuilder;
import com.locolhive.chartout.api.ImageRequestBuilder.MultiPartRequest;
import com.locolhive.chartout.classes.CustomLatLng;
import com.locolhive.chartout.classes.Post;
import com.locolhive.chartout.databinding.FragmentBasicBinding;
import com.locolhive.chartout.helpers.DialogProgress;
import com.locolhive.chartout.helpers.GlideApp;
import com.locolhive.chartout.helpers.ImageUtils;
import com.locolhive.chartout.helpers.Utils;
import com.locolhive.chartout.home.GlobalData;
import com.locolhive.chartout.interfaces.ErrorListener;
import com.locolhive.chartout.interfaces.OnResultListener;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.model.Place;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragBasicDetails extends Fragment {

  private static final String TAG = FragBasicDetails.class.getSimpleName() + " YOYO";

  FragmentBasicBinding binding;
  CreateActivity activity;

  public static FragBasicDetails newInstance() {

    Bundle args = new Bundle();

    FragBasicDetails fragment = new FragBasicDetails();
    fragment.setArguments(args);
    return fragment;
  }

  public FragBasicDetails() {
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_basic, container, false);

    binding.spType.setAdapter(Utils.getSpinnerAdapter(getContext(), "Select type of post", new ArrayList<String>()));
    binding.spCat.setAdapter(Utils.getSpinnerAdapter(getContext(), "Select category of activity", new ArrayList<String>()));

    activity = (CreateActivity) getActivity();

    binding.nxt.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        activity.nxt();
      }
    });

    if(activity.request.getPrimaryImage()!=null){
      binding.img.setVisibility(View.VISIBLE);
      binding.btnDelete.setVisibility(View.VISIBLE);
      GlideApp.with(activity)
          .load(ImageUtils.getUri(activity.request.getPrimaryImage()))
          .placeholder(Utils.getProgressDrawable(activity))
          .centerCrop()
          .into(binding.img);
    }

    binding.btnDelete.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        GlideApp.with(activity)
            .load(R.drawable.placeholder)
            .centerCrop()
            .into(binding.img);
        binding.img.setVisibility(View.GONE);
        binding.btnDelete.setVisibility(View.GONE);
      }
    });

    binding.uploadCover.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {

        activity.photoListener = new OnResultListener<ArrayList<Uri>>() {
          @Override
          public void OnResult(ArrayList<Uri> result) {
            final DialogProgress progress = DialogProgress.newInstance();
            progress.display(activity);

            MultiPartRequest request =
                new ImageRequestBuilder(activity, new Listener<String>() {
                  @Override
                  public void onResponse(String response) {
                    progress.done();
                    activity.request.setPrimaryImage(response);
                    binding.img.setVisibility(View.VISIBLE);
                    binding.btnDelete.setVisibility(View.VISIBLE);
                    GlideApp.with(activity)
                        .load(ImageUtils.getUri(activity.request.getPrimaryImage()))
                        .placeholder(Utils.getProgressDrawable(activity))
                        .centerCrop()
                        .into(binding.img);
                  }
                }, new ErrorListener() {
                  @Override
                  public void onError(String err) {
                    progress.done();
                    Toast.makeText(activity, "Error uploading image: " + err, Toast.LENGTH_LONG).show();
                  }
                })
                    .setToken(((GlobalData) activity.getApplication()).getToken())
                    .addUri(result.get(0))
                    .build();

            Volley.newRequestQueue(activity).add(request);
          }
        };

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), GET_SinglePic_CODE);

      }
    });

    //region Location
    if (activity.request.getDestination() != null) {
      binding.placesAutocomplete.setText(activity.request.getDestination());
      binding.map.setVisibility(View.VISIBLE);
      GlideApp.with(activity)
          .load(Utils.getMapUsingLatLong(activity.request.getMainLocation()))
          .centerCrop()
          .placeholder(Utils.getBlackProgressDrawable(getContext()))
          .into(binding.map);
    }
    binding.placesAutocomplete.setCountry("IN");
    binding.placesAutocomplete.setLanguageCode("en");
    binding.placesAutocomplete.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
      @Override
      public void onPlaceSelected(@NonNull Place place) {
        Log.i(TAG, "onPlaceSelected: " + place);

        InputMethodManager inputManager = (InputMethodManager)
            activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        assert inputManager != null;
        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
            InputMethodManager.HIDE_NOT_ALWAYS);

        final DialogProgress dialogProgress = DialogProgress.newInstance();
        dialogProgress.display(getContext());

        Places.GeoDataApi.getPlaceById((activity).mGoogleApiClient, place.place_id)
            .setResultCallback(new ResultCallback<PlaceBuffer>() {
              @Override
              public void onResult(@NonNull PlaceBuffer places) {
                if (places.getStatus().isSuccess()) {
                  dialogProgress.done();
                  com.google.android.gms.location.places.Place myPlace = places.get(0);
                  if (myPlace != null) {
                    LatLng queriedLocation = myPlace.getLatLng();
                    Log.v("Latitude is", "" + queriedLocation.latitude);
                    Log.v("Longitude is", "" + queriedLocation.longitude);

                    String s = (String) myPlace.getName();
                    if (s == null || s.equals("")) {
                      s = (String) myPlace.getAddress();
                    }
                    if (s != null && !s.equals("")) {
                      activity.request.setDestination(s);
                      activity.request.setMainLocation(new CustomLatLng(queriedLocation.latitude, queriedLocation.longitude));
                    }

                    binding.map.setVisibility(View.VISIBLE);
                    GlideApp.with(activity)
                        .load(Utils.getMapUsingLatLong(new CustomLatLng(queriedLocation.latitude, queriedLocation.longitude)))
                        .centerCrop()
                        .placeholder(Utils.getBlackProgressDrawable(getContext()))
                        .into(binding.map);
                  } else {
                    Toast.makeText(activity, "Error getting map", Toast.LENGTH_SHORT).show();
                  }
                } else {
                  dialogProgress.done();
                  Toast.makeText(activity, "Error getting map", Toast.LENGTH_SHORT).show();
                }
                places.release();
              }
            });
      }
    });
    //endregion

    return binding.getRoot();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }
}
