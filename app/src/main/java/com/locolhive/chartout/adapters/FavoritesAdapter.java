package com.locolhive.chartout.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.locolhive.chartout.R;
import com.locolhive.chartout.classes.Post;
import com.locolhive.chartout.databinding.CardFavoriteBinding;
import com.locolhive.chartout.helpers.GlideApp;
import com.locolhive.chartout.helpers.ImageUtils;
import com.locolhive.chartout.helpers.Utils;
import java.util.List;

public class FavoritesAdapter extends BaseQuickAdapter<Post, BaseViewHolder> {

  public FavoritesAdapter(@Nullable List<Post> data) {
    super(R.layout.card_favorite, data);
  }

  @Override
  protected void convert(final BaseViewHolder helper, Post item) {

    CardFavoriteBinding binding = DataBindingUtil.bind(helper.itemView);
    assert binding != null;
    binding.setPost(item);
    if(item.getPrimaryImage()!=null){
      GlideApp.with(mContext)
          .load(ImageUtils.getUri(item.getPrimaryImage()))
          .placeholder(Utils.getProgressDrawable(mContext))
          .centerCrop()
          .into(binding.postImage);
    }
    binding.btnDelete.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        getData().remove(helper.getAdapterPosition());
        notifyItemRemoved(helper.getAdapterPosition());
      }
    });
  }

}
