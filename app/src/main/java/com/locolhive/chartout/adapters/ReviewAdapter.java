package com.locolhive.chartout.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.request.RequestOptions;
import com.locolhive.chartout.R;
import com.locolhive.chartout.adapters.ReviewAdapter.ReviewVH;
import com.locolhive.chartout.classes.Review;
import com.locolhive.chartout.helpers.GlideApp;
import com.locolhive.chartout.helpers.ImageUtils;
import com.locolhive.chartout.helpers.Utils;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewVH> {

  private Context context;
  private ArrayList<Review> list;

  public ReviewAdapter(ArrayList<Review> list, Context context) {
    this.context = context;
    this.list = list;
  }

  @NonNull
  @Override
  public ReviewVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.card_review, parent, false);
    return new ReviewVH(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ReviewVH holder, int position) {
    if(list!=null)
      holder.setData(list.get(position));
  }

  @Override
  public int getItemCount() {
    if(list!=null)
      return list.size();
    else
      return 4;
  }

  class ReviewVH extends RecyclerView.ViewHolder {

    CircleImageView imageView;
    TextView reviewer, date, rev;

    public ReviewVH(View itemView) {
      super(itemView);
      imageView = itemView.findViewById(R.id.hostImage);
      reviewer = itemView.findViewById(R.id.reviewerName);
      date = itemView.findViewById(R.id.date);
      rev = itemView.findViewById(R.id.review);
    }

    void setData(Review s) {
      if (s.getReviewer().getProfilePictureId() != null) {
        GlideApp.with(context).load(ImageUtils.getUri(s.getReviewer().getProfilePictureId()))
            .apply(new RequestOptions().centerCrop())
            .placeholder(Utils.getProgressDrawable(context)).into(imageView);
      }
      reviewer.setText(s.getReviewer().getFullName());
//      date.setText(s.get); todo: api required
      rev.setText(s.getReviewText());
    }

  }
}
