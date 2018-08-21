package com.locolhive.chartout.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.locolhive.chartout.R;
import com.locolhive.chartout.classes.AccOptions;
import com.locolhive.chartout.helpers.GlideApp;
import java.util.ArrayList;

public class AccOptionsAdapter extends RecyclerView.Adapter<AccOptionsAdapter.AccOptionsVH> {

  Context context;
  ArrayList<AccOptions> list;
  ArrayList<View.OnClickListener> listeners;

  public AccOptionsAdapter(ArrayList<AccOptions> list, ArrayList<View.OnClickListener> listeners,
      Context context) {
    this.context = context;
    this.list = list;
    this.listeners = listeners;
  }

  @NonNull
  @Override
  public AccOptionsAdapter.AccOptionsVH onCreateViewHolder(@NonNull ViewGroup parent,
      int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.card_manage_home, parent, false);
    return new AccOptionsVH(view);
  }

  @Override
  public void onBindViewHolder(@NonNull AccOptionsAdapter.AccOptionsVH holder, int position) {
    holder.setData(list.get(position));
    holder.setListeners(position);
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  class AccOptionsVH extends RecyclerView.ViewHolder {

    ConstraintLayout card;
    ImageView icon;
    TextView title;

    public AccOptionsVH(View itemView) {
      super(itemView);
      card = itemView.findViewById(R.id.card);
      icon = itemView.findViewById(R.id.card_Icon);
      title = itemView.findViewById(R.id.card_Title);
    }

    void setData(AccOptions option) {
      if (option.icon != null) {
        GlideApp.with(context).load(option.icon).into(icon);
      }
      title.setText(option.title);
    }

    void setListeners(int position) {
      card.setOnClickListener(listeners.get(position));
    }

  }
}
