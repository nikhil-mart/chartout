package com.locolhive.chartout.chat;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.locolhive.chartout.chat.ChatListAdapter.PersonVH;
import com.locolhive.chartout.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class ChatListAdapter extends RecyclerView.Adapter<PersonVH> {

  private Context context;

  private ArrayList<ChatPeople> chats;

  public ChatListAdapter(Context context) {
    this.context = context;
    updateData();
  }

  public void updateData() {
//    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public PersonVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View newview = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
    return new PersonVH(newview);
  }

  @Override
  public void onBindViewHolder(@NonNull PersonVH holder, int position) {
//    chats.get(position)
    holder.setData(new ChatPeople(), position);
  }

  @Override
  public int getItemCount() {
    if (chats != null) {
      return chats.size();
    } else {
      return 4;
    }
  }

  class PersonVH extends RecyclerView.ViewHolder {

    CardView card;
    TextView hostName, expName, time;
    CircleImageView dot, host;

    public PersonVH(View itemView) {

      super(itemView);

      card = itemView.findViewById(R.id.card);
      dot = itemView.findViewById(R.id.dot);
      hostName = itemView.findViewById(R.id.hostName);
      expName = itemView.findViewById(R.id.expName);
      time = itemView.findViewById(R.id.time);
      host = itemView.findViewById(R.id.hostImage);
    }

    public void setData(ChatPeople person, final int position) {

      int i = new Random(Calendar.getInstance().getTimeInMillis()).nextInt(2);

      hostName.setText("Person #" + position);

      if (i == 0) {
        dot.setVisibility(View.VISIBLE);
      } else {
        dot.setVisibility(View.GONE);
      }

      card.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent i = new Intent(context, ActivityChat.class);
          i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
          context.startActivity(i);
        }
      });

    }

  }
}
