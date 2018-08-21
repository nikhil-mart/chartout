package com.locolhive.chartout.profile;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.locolhive.chartout.R;
import com.locolhive.chartout.adapters.AddImageAdapter;
import com.locolhive.chartout.home.BlankActivity;
import com.locolhive.chartout.home.GlobalData;
import com.locolhive.chartout.interfaces.OnResultListener;
import java.util.ArrayList;

public class AddPhotosFrag extends Fragment {

  private static final String TAG = AddPhotosFrag.class.getSimpleName() + " YOYO";

  Context context;
  View view;

  RecyclerView recyclerView;


  public AddPhotosFrag() {
    // Required empty public constructor
  }

  public static AddPhotosFrag newInstance() {
    AddPhotosFrag fragment = new AddPhotosFrag();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {

    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    view = inflater.inflate(R.layout.fragment_add_photos, container, false);

    context = view.getContext();

    ((GlobalData) context.getApplicationContext()).getUser(context, new OnResultListener<UserProfile>() {
      @Override
      public void OnResult(UserProfile result) {
        ArrayList<String> otherPhotoIds = result.getOtherPhotoIds();
        if (otherPhotoIds == null) {
          otherPhotoIds = new ArrayList<>();
        }
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new AddImageAdapter(otherPhotoIds, context));
      }
    });

    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    context = getContext();

    ((BlankActivity) context).setToolbarTitle("Edit Seller Profile");
  }
}
