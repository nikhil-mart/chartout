package com.locolhive.chartout.create;

import static com.locolhive.chartout.create.CreateActivity.GET_MultiplePics_CODE;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenu;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.locolhive.chartout.R;
import com.locolhive.chartout.adapters.ContentAdapter;
import com.locolhive.chartout.api.ImageRequestBuilder;
import com.locolhive.chartout.api.ImageRequestBuilder.MultiPartRequest;
import com.locolhive.chartout.api.UploadVideoApi;
import com.locolhive.chartout.api.UploadVideoApi.ProgressListener;
import com.locolhive.chartout.classes.Content;
import com.locolhive.chartout.databinding.FragmentSectionsBinding;
import com.locolhive.chartout.helpers.DialogProgress;
import com.locolhive.chartout.helpers.DialogUploadOptions;
import com.locolhive.chartout.home.GlobalData;
import com.locolhive.chartout.interfaces.ErrorListener;
import com.locolhive.chartout.interfaces.OnResultListener;
import com.locolhive.chartout.interfaces.OnTaskCompleteListener;
import com.locolhive.chartout.statics.ContentType;
import io.github.yavski.fabspeeddial.FabSpeedDial.MenuListener;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragSections extends Fragment {

  private static final String TAG = FragSections.class.getSimpleName() + " YOYO";
  FragmentSectionsBinding binding;
  CreateActivity activity;
  MediaHandler hh;
  @Nullable
  Integer videoPos = null;
  RequestQueue queue;
  Integer num = 0;

  public static FragSections newInstance() {

    Bundle args = new Bundle();

    FragSections fragment = new FragSections();
    fragment.setArguments(args);
    return fragment;
  }

  public FragSections() {
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sections, container, false);

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    binding.rv.setLayoutManager(linearLayoutManager);
    activity = (CreateActivity) getActivity();

    assert activity != null;

    queue = Volley.newRequestQueue(activity);

    String userEmail = "tempaccnt0001@gmail.com";

    activity.api = new UploadVideoApi(userEmail, activity, new ProgressListener() {
      @Override
      public void onComplete(String id) {
        ((ContentAdapter) binding.rv.getAdapter()).addVideo(id, videoPos);
        videoPos = null;
      }

      @Override
      public void onFailure(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
      }
    });

    hh = new MediaHandler() {
      @Override
      public void handleAddPhoto(@Nullable final Integer x) {
        activity.photoListener = new OnResultListener<ArrayList<Uri>>() {
          @Override
          public void OnResult(ArrayList<Uri> result) {
            uploadPics(result, new OnTaskCompleteListener<ArrayList<String>>() {
              @Override
              public void onSuccess(ArrayList<String> response) {
                ((ContentAdapter) binding.rv.getAdapter()).addPhotos(response, x);
              }

              @Override
              public void onFailure(String error) {
                Toast.makeText(activity, "Error: " + error, Toast.LENGTH_SHORT).show();
              }
            });
          }
        };
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), GET_MultiplePics_CODE);
      }

      @Override
      public void handleAddVideo(@Nullable Integer x) {
        videoPos = x;
        DialogUploadOptions.newInstance(new OnResultListener<String>() {
          @Override
          public void OnResult(String result) {
            if (result == null) {
              uploadVideo();
            } else {
              ((ContentAdapter) binding.rv.getAdapter()).addVideo(result, videoPos);
              videoPos = null;
            }
          }
        }).display(getContext());
      }
    };

    if(activity.editmode && activity.request.getContent()!=null && activity.request.getContent().size()>0){
      binding.rv.setAdapter(ContentAdapter.newInstance(activity, hh, activity.request.getContent()));
    }else {
      binding.rv.setAdapter(ContentAdapter.newInstance(activity, hh));
    }

    activity.mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        if(position==2){
          activity.request.setContent(new ArrayList<>(((ContentAdapter) binding.rv.getAdapter()).getData()));
        }
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });

    binding.swipeRefresh.setOnRefreshListener(new OnRefreshListener() {
      @Override
      public void onRefresh() {
        ((ContentAdapter) binding.rv.getAdapter()).refreshVideos();
        binding.swipeRefresh.setRefreshing(false);
      }
    });

    addSwipeToDelete();

    return binding.getRoot();
  }

  void uploadPics(final ArrayList<Uri> list, final OnTaskCompleteListener<ArrayList<String>> listener) {
    if (list != null && list.size() > 0) {

      num = 0;
      final ArrayList<String> ids = new ArrayList<>();

      final DialogProgress dialogProgress = DialogProgress.newInstance("Uploading Images");
      dialogProgress.display(activity);

      for (int i = 0; i < list.size(); i++) {
        final int finalI = i;
        MultiPartRequest request =
            new ImageRequestBuilder(activity, new Listener<String>() {
              @Override
              public void onResponse(String response) {
                num = num + 1;
                if (finalI == 0) {
                  ids.add(0, response);
                } else {
                  ids.add(response);
                }
                if (num == list.size()) {
                  dialogProgress.done();
                  listener.onSuccess(ids);
                }
              }
            }, new ErrorListener() {
              @Override
              public void onError(String err) {
                num = num + 1;
                Toast.makeText(activity, "Error uploading image #" + finalI + ": " + err, Toast.LENGTH_LONG).show();
                if (num == list.size()) {
                  if (ids.size() > 0) {
                    dialogProgress.done();
                    listener.onSuccess(ids);
                  } else {
                    dialogProgress.done();
                    listener.onFailure(err);
                  }
                }
              }
            })
                .setToken(((GlobalData) activity.getApplication()).getToken())
                .addUri(list.get(i))
                .build();

        queue.add(request);
      }
    }
  }

  void uploadVideo() {
    activity.api.begin();
  }

  void addSwipeToDelete(){
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
      @Override
      public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
      }

      @Override
      public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // Row is swiped from recycler view
        // remove it from adapter
        Toast.makeText(activity, "Delete #"+viewHolder.getLayoutPosition(), Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        // view the background view
      }
    };

// attaching the touch helper to recycler view
    new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rv);
  }

  @Override
  public void onResume() {
    super.onResume();
    assert activity != null;
    activity.fab.setMenuListener(new MenuListener() {
      @Override
      public boolean onPrepareMenu(NavigationMenu navigationMenu) {
        return true;
      }

      @Override
      public boolean onMenuItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
          case R.id.action_h1:
            ((ContentAdapter) binding.rv.getAdapter()).add(new Content(ContentType.TITLE));
            return true;
          case R.id.action_details:
            ((ContentAdapter) binding.rv.getAdapter()).add(new Content(ContentType.TEXT));
            return true;
          case R.id.action_photo:
            hh.handleAddPhoto(null);
            return true;
          case R.id.action_video:
            hh.handleAddVideo(null);
            return true;
          case R.id.action_location:
            ((ContentAdapter) binding.rv.getAdapter()).add(new Content(ContentType.LOCATION));
            return true;
          default:
            return true;
        }
      }

      @Override
      public void onMenuClosed() {

      }
    });
  }

  public interface MediaHandler {

    void handleAddPhoto(@Nullable Integer x);

    void handleAddVideo(@Nullable Integer x);
  }

}
