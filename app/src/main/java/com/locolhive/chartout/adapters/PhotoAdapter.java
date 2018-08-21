package com.locolhive.chartout.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.bumptech.glide.request.RequestOptions;
import com.locolhive.chartout.R;
import com.locolhive.chartout.helpers.GlideApp;
import com.locolhive.chartout.helpers.PhotoFullDialog;
import com.locolhive.chartout.helpers.Utils;
import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoVH> {

  private static final String TAG = PhotoAdapter.class.getSimpleName() + " YOYO";

  private Context context;
  private ArrayList<Uri> list;
  private ArrayList<Boolean> progress;
  private OnDeleteListener onDeleteListener;

  public PhotoAdapter(ArrayList<Uri> list, Context context, Boolean progress) {
    this.context = context;
    this.list = list;
    if (progress) {
      this.progress = new ArrayList<>();
      for (int i = 0; i < list.size(); i++) {
        this.progress.add(true);
      }
    }
  }

  public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
    this.onDeleteListener = onDeleteListener;
  }

  public void add(Uri s) {
    list.add(s);
    notifyItemInserted(list.size() - 1);
    if (progress != null) {
      progress.add(true);
    }
  }

  private void remove(int position) {
    list.remove(position);
    if (progress != null) {
      progress.remove(position);
    }
    if (onDeleteListener != null) {
      onDeleteListener.onDelete(position);
    }
    notifyItemRemoved(position);
  }

  public void remove(Uri uri) {
    if (list.contains(uri)) {
      remove(list.indexOf(uri));
    }
  }

  public void finishX(Uri uri) {
    int i = list.indexOf(uri);
    progress.set(i, false);
    notifyItemChanged(i);
  }

  public ArrayList<Uri> getList() {
    return list;
  }

  @NonNull
  @Override
  public PhotoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.card_photo, parent, false);
    return new PhotoVH(view);
  }

  @Override
  public void onBindViewHolder(@NonNull PhotoVH holder, int position) {
    holder.setData(list.get(position), position);
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  public void refresh() {
    for (int i = 0; i < progress.size(); i++) {
      if (progress.get(i)) {
        notifyItemChanged(i);
      }
    }
  }

  public interface OnDeleteListener {
    void onDelete(int position);
  }

  class PhotoVH extends RecyclerView.ViewHolder {

    ImageView img;
    ImageButton btnDelete;
    ProgressBar progressBar;

    PhotoVH(View itemView) {
      super(itemView);
      img = itemView.findViewById(R.id.img);
      btnDelete = itemView.findViewById(R.id.ib_close);
      progressBar = itemView.findViewById(R.id.loading);
    }

    void setData(Uri s, final int position) {

      try {
        GlideApp.with(context)
            .load(s).apply(RequestOptions.centerCropTransform())
            .placeholder(Utils.getProgressDrawable(context)).into(img);
      } catch (Exception e) {
        Toast.makeText(context, "Permissions required", Toast.LENGTH_SHORT).show();
        remove(position);
      }

      btnDelete.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          remove(position);
        }
      });

      if (progress != null && progress.get(position)) {
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
          }
        });

        if (progress == null) {
          btnDelete.setVisibility(View.GONE);
        } else {
          btnDelete.setVisibility(View.VISIBLE);
        }

      }
    }

  }

}
