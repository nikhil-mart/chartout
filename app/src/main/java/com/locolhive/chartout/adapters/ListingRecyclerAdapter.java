package com.locolhive.chartout.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import com.bumptech.glide.request.RequestOptions;
import com.locolhive.chartout.R;
import com.locolhive.chartout.classes.Post;
import com.locolhive.chartout.databinding.CardSinglePostBinding;
import com.locolhive.chartout.helpers.GlideApp;
import com.locolhive.chartout.helpers.ImageUtils;
import com.locolhive.chartout.helpers.Utils;
import com.locolhive.chartout.home.ViewListingActivity;
import java.util.ArrayList;

public class ListingRecyclerAdapter extends
    RecyclerView.Adapter<ListingRecyclerAdapter.PostViewHolder> {

  private Context context;
  private Integer size;

  private ArrayList<Post> listings;
  Boolean big;

  public ListingRecyclerAdapter(Context context, ArrayList<Post> listing, Boolean isBig) {
    this.context = context;
    this.listings = listing;
    if (listing != null) {
      if (listing.size() > 0) {
        size = listings.size();
      } else {
        size = 2;
      }
    } else {
      size = 10;
    }
    big = false;
    if(isBig!=null){
      big = isBig;
    }
  }

  public void updateData(@NonNull ArrayList<Post> listings) {
    if (listings.size() > 0) {
      this.listings = listings;
      size = listings.size();
      notifyDataSetChanged();
    } else {
      this.listings = listings;
      size = 2;
      notifyDataSetChanged();
    }
  }

//  public void add(ArrayList<Post> list){
//    int bef = listings.size();
//    listings.addAll(list);
//    notifyItemRangeInserted(bef, list.size());
//  }

  @NonNull
  @Override
  public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    CardSinglePostBinding binding = DataBindingUtil.inflate(
          LayoutInflater.from(context),
          R.layout.card_single_post, parent, false);

    return new PostViewHolder(binding);

  }

  @Override
  public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {

    if(listings!=null){
      if (position < listings.size()) {
        Post listing = listings.get(position);
        holder.setData(listing);
      }
    }
    holder.setListeners(null);

  }

  @Override
  public int getItemCount() {
    return size;
  }

  class PostViewHolder extends RecyclerView.ViewHolder {

    CardSinglePostBinding binding;

    PostViewHolder(CardSinglePostBinding binding) {

      super(binding.getRoot());
      this.binding = binding;

    }

    public void setData(final Post listing) {

      binding.title.setText(listing.getTitle());
      binding.address.setText(listing.getDestination());
      if (listing.getPrimaryImage() != null && !listing.getPrimaryImage().equals("")) {
        GlideApp.with(context)
            .load(ImageUtils.getUri(listing.getPrimaryImage())).apply(RequestOptions.centerCropTransform())
            .placeholder(Utils.getProgressDrawable(context)).into(binding.postImage);
      } else {
        GlideApp.with(context)
            .load(R.drawable.placeholder).apply(RequestOptions.centerCropTransform())
            .into(binding.postImage);
      }

      //dot.setImageResource(listing.getPrimaryImageId());
    }

    public void setListeners(@Nullable final Post listing){
      if(big) {
        binding.postContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
      }
      binding.postContainer.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent = new Intent(context, ViewListingActivity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
          if(listing!=null)
            intent.putExtra(ViewListingActivity.KEY_Listing, listing);
          context.startActivity(intent);
        }
      });
    }

  }
}
