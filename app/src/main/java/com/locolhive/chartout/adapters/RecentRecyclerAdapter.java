package com.locolhive.chartout.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.locolhive.chartout.R;
import com.locolhive.chartout.classes.Category;
import com.locolhive.chartout.classes.Post;
import com.locolhive.chartout.helpers.GlideApp;
import com.locolhive.chartout.helpers.ImageUtils;
import com.locolhive.chartout.home.ViewListingActivity;
import java.util.ArrayList;

public class RecentRecyclerAdapter extends
    RecyclerView.Adapter<RecentRecyclerAdapter.CatViewHolder> {

  private static final String TAG = RecentRecyclerAdapter.class.getSimpleName() + " YOYO";
  private Context context;
  private ArrayList<Category> cats;

  public RecentRecyclerAdapter(Context context, ArrayList<Category> cat) {
    this.context = context;
    cats = cat;
  }

  public void updateData(ArrayList<Category> cat) {
    this.cats = cat;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    View view = LayoutInflater.from(context).inflate(R.layout.card_category_small, parent, false);

    return new CatViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull CatViewHolder holder, int position) {
//    holder.setData(cats.get(position));
    holder.setListeners(null);
  }

  @Override
  public int getItemCount() {
//    return cats.size();
    return 5;
  }

  class CatViewHolder extends RecyclerView.ViewHolder {

    ConstraintLayout card;
    TextView cat, tagline;
    ImageView img;

    CatViewHolder(View itemView) {
      super(itemView);
      card = itemView.findViewById(R.id.card);
      cat = itemView.findViewById(R.id.category);
      tagline = itemView.findViewById(R.id.tagline);
      img = itemView.findViewById(R.id.bg);
    }

    public void setData(final Category cat) {
      this.cat.setText(cat.getCategory());
      if (cat.getImageId() != null && !cat.getImageId().equals("")) {
        GlideApp.with(context).load(ImageUtils.getUri(cat.getImageId())).into(img);
      } else {
        GlideApp.with(context).load(context.getResources().getDrawable(android.R.color.transparent)).into(img);
      }
    }

    public void setListeners(@Nullable final Post listing){
      card.setOnClickListener(new View.OnClickListener() {
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
