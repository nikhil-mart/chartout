package com.locolhive.chartout.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.request.RequestOptions;
import com.locolhive.chartout.R;
import com.locolhive.chartout.classes.Post;
import com.locolhive.chartout.helpers.GlideApp;
import com.locolhive.chartout.helpers.ImageUtils;
import com.locolhive.chartout.helpers.Utils;
import com.locolhive.chartout.home.ViewListingActivity;
import java.util.ArrayList;

public class ManageListingAdapter extends
    RecyclerView.Adapter<ManageListingAdapter.PostViewHolder> {

  private Context context;

  private ArrayList<Post> posts;

  public ManageListingAdapter(Context context, ArrayList<Post> listing) {
    this.context = context;
    this.posts = listing;
  }

  public void updateData(ArrayList<Post> listings) {
    this.posts = listings;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    View newview = LayoutInflater.from(context)
        .inflate(R.layout.card_manage_listing, parent, false);

    return new PostViewHolder(newview);
  }

  @Override
  public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {

    if (posts != null) {
//      holder.setData(posts.get(position), position);
    }

  }

  @Override
  public int getItemCount() {
    if (posts != null) {
      return posts.size();
    } else {
      return 4;
    }
  }

  class PostViewHolder extends RecyclerView.ViewHolder {

    ConstraintLayout container;
    TextView address;
    TextView title;
    ImageView image;
    Button publish, edit, delete;

    public PostViewHolder(View itemView) {

      super(itemView);

      container = itemView.findViewById(R.id.post_Container);
      image = itemView.findViewById(R.id.post_image);
      address = itemView.findViewById(R.id.address);
      title = itemView.findViewById(R.id.title);
      publish = itemView.findViewById(R.id.btn_Publish);
      edit = itemView.findViewById(R.id.btn_Edit);
      delete = itemView.findViewById(R.id.btn_Delete);

    }

    public void setData(final Post listing, final int pos) {

      title.setText(listing.getTitle());
      address.setText(listing.getDestination());

      if (listing.getPrimaryImage() != null) {
        if (!listing.getPrimaryImage().equals("")) {
          GlideApp.with(context)
              .load(ImageUtils.getUri(listing.getPrimaryImage())).apply(RequestOptions.centerCropTransform())
              .placeholder(Utils.getProgressDrawable(context)).into(image);
        } else {
          GlideApp.with(context)
              .load(R.drawable.placeholder)
              .into(image);
        }
      } else {
        GlideApp.with(context)
            .load(R.drawable.placeholder)
            .into(image);
      }

//      publish.setText("Published");

    }

    public void setListeners(final Post listing, final int pos){
      container.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent = new Intent(context, ViewListingActivity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
          intent.putExtra(ViewListingActivity.KEY_Preview, true);
          intent.putExtra(ViewListingActivity.KEY_Listing, listing);
          context.startActivity(intent);
        }
      });

      edit.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show();
        }
      });

      delete.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          notifyItemRemoved(pos);
        }
      });

    }

  }

}
