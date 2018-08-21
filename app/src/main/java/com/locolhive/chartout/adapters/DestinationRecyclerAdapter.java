package com.locolhive.chartout.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.locolhive.chartout.R;
import com.locolhive.chartout.adapters.DestinationRecyclerAdapter.DestViewHolder;
import com.locolhive.chartout.classes.Category;
import com.locolhive.chartout.helpers.GlideApp;
import com.locolhive.chartout.helpers.ImageUtils;
import com.locolhive.chartout.home.MainActivity;
import com.locolhive.chartout.home.SingleCategoryFragment;
import java.util.ArrayList;

public class DestinationRecyclerAdapter extends
    RecyclerView.Adapter<DestViewHolder> {

  private static final String TAG = DestinationRecyclerAdapter.class.getSimpleName() + " YOYO";
  private Context context;
  private ArrayList<Category> cats;

  public DestinationRecyclerAdapter(Context context, ArrayList<Category> cat) {
    this.context = context;
    cats = cat;
  }

  public void updateData(ArrayList<Category> cat) {
    this.cats = cat;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public DestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    View view = LayoutInflater.from(context).inflate(R.layout.card_category_big, parent, false);

    return new DestViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull DestViewHolder holder, int position) {
//    holder.setData(cats.get(position));
    holder.setListeners(null);
  }

  @Override
  public int getItemCount() {
//    return cats.size();
    return 4;
  }

  class DestViewHolder extends RecyclerView.ViewHolder {

    CardView card;
    TextView title;
    ImageView img;

    DestViewHolder(View itemView) {
      super(itemView);
      card = itemView.findViewById(R.id.card);
      title = itemView.findViewById(R.id.category);
      img = itemView.findViewById(R.id.bg);
    }

    public void setData(final Category cat) {
      title.setText(cat.getCategory());
      if (cat.getImageId() != null && !cat.getImageId().equals("")) {
        GlideApp.with(context).load(ImageUtils.getUri(cat.getImageId())).into(img);
      } else {
        GlideApp.with(context).load(context.getResources().getDrawable(android.R.color.transparent)).into(img);
      }
    }

    public void setListeners(final Category cat) {
      card.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          SingleCategoryFragment fragment = SingleCategoryFragment.newInstance(cat);
          MainActivity mainActivity = (MainActivity) context;
          mainActivity.switchContent(fragment);
        }
      });
    }

  }
}
