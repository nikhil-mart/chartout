package com.locolhive.chartout.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.locolhive.chartout.R;
import java.util.ArrayList;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.OptionsVH> {

  private Context context;
  private ArrayList<String> list;
  private ArrayList<View.OnClickListener> listeners;

  public OptionsAdapter(ArrayList<String> list, ArrayList<View.OnClickListener> listeners,
      Context context) {
    this.context = context;
    this.list = list;
    this.listeners = listeners;
  }

  @NonNull
  @Override
  public OptionsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.card_options, parent, false);
    return new OptionsVH(view);
  }

  @Override
  public void onBindViewHolder(@NonNull OptionsVH holder, int position) {
    holder.setData(list.get(position));
    holder.setListeners(position);
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  class OptionsVH extends RecyclerView.ViewHolder {

    LinearLayout card;
    TextView title;

    public OptionsVH(View itemView) {
      super(itemView);
      card = itemView.findViewById(R.id.card);
      title = itemView.findViewById(R.id.card_Title);
    }

    void setData(String s) {
      title.setText(s);
    }

    void setListeners(int position) {
      card.setOnClickListener(listeners.get(position));
    }

  }
}
