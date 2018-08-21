package com.locolhive.chartout.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.locolhive.chartout.R;
import java.util.ArrayList;
import java.util.Date;

public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.SlotVH> {

  private Context context;
  private int num;

  private ArrayList<Integer> list;

  public SlotAdapter(Context context) {
    list = new ArrayList<>();
    this.context = context;
    this.num = 2;
  }

  @NonNull
  @Override
  public SlotVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view;

//    if (viewType == 0) {
//      view = LayoutInflater.from(context).inflate(R.layout.card_add_slot, parent, false);
//    } else {
    view = LayoutInflater.from(context).inflate(R.layout.card_plus, parent, false);
//    }

    return new SlotVH(view);
  }

  @Override
  public int getItemViewType(int position) {
    if (position == num - 1) {
      return 1;
    } else {
      return 0;
    }
  }

  @Override
  public void onBindViewHolder(@NonNull SlotVH holder, int position) {
    holder.setData(position);
  }

  @Override
  public int getItemCount() {
    return num;
  }

  class SlotVH extends RecyclerView.ViewHolder {

    View itemView;
    Date s;

    SlotVH(View itemView) {
      super(itemView);
      this.itemView = itemView;
      s = null;
    }

    void setPlusListener() {
      itemView.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//                    if(list.size()==num-1) {
          num++;
          notifyItemChanged(num - 2);
          notifyItemInserted(num - 1);
//                    }
        }
      });
    }

    public void setData(int position) {
      if (position == num - 1) {
        setPlusListener();
      } else {
        TextView tv = itemView.findViewById(R.id.tv5);
//        tv.setText(context.getString(R.string.slot_n, position + 1));
//        setETListeners();
      }
    }
  }
}
