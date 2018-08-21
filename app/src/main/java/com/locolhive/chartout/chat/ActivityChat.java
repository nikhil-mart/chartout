package com.locolhive.chartout.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.locolhive.chartout.R;
import com.locolhive.chartout.classes.ObjectId;
import com.locolhive.chartout.home.GlobalData;
import com.locolhive.chartout.interfaces.OnResultListener;
import com.locolhive.chartout.profile.UserProfile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class ActivityChat extends AppCompatActivity {

  Context context;
  RecyclerView msgRecycler;
  UserProfile user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);

    context = ActivityChat.this;

    msgRecycler = findViewById(R.id.reyclerview_message_list);

    ((GlobalData) getApplication()).getUser(context, new OnResultListener<UserProfile>() {
      @Override
      public void OnResult(UserProfile result) {
        user = result;
        Random random = new Random(Calendar.getInstance().getTimeInMillis());

        ChatAdapter adapter = new ChatAdapter(context, user, getMessages(random.nextInt(30) + 10));
        msgRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true));
        msgRecycler.setAdapter(adapter);

        final EditText text = findViewById(R.id.edittext_chatbox);
        Button send = findViewById(R.id.button_chatbox_send);

        send.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            String trim = text.getText().toString().trim();
            if (!trim.equals("")) {
              text.setText("");
              ((ChatAdapter) msgRecycler.getAdapter()).addMsg(trim);
              msgRecycler.scrollToPosition(0);
            }
          }
        });
      }
    });


  }

  private ArrayList<Msg> getMessages(int num) {

    ArrayList<Msg> messageList = new ArrayList<>();
    String[] stringArray = context.getResources().getStringArray(R.array.msgArr);
    Random random = new Random(Calendar.getInstance().getTimeInMillis());
    ObjectId id = user.get_id();
    for (int i = 0; i < num; i++) {
      if (random.nextBoolean()) {
        Log.i("getMsg", "getMessages: me");
        Msg msg = new Msg(
            stringArray[random.nextInt(stringArray.length)],
            "Me", user.get_id());
        messageList.add(msg);
      } else {
        Log.i("getMsg", "getMessages: oth");
        ObjectId id1 = new ObjectId();
        id1.setMachineIdentifier(id.getMachineIdentifier() + 2);
        id1.setTime(id.getTime());
        id1.setDate(id.getDate());
        id1.setCounter(id.getCounter() + 4);
        id1.setProcessIdentifier(id.getProcessIdentifier() + 32);
        Msg msg2 = new Msg(
            stringArray[random.nextInt(stringArray.length)],
            "Amith", id1);
        messageList.add(msg2);
      }
    }
    return messageList;
  }

}
