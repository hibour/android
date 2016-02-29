package com.dsquare.hibour.adapters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.activities.Chat;
import com.dsquare.hibour.database.DatabaseHandler;
import com.dsquare.hibour.dialogs.UserChatOptionDialoge;
import com.dsquare.hibour.pojos.message.UserMessage;
import com.dsquare.hibour.pojos.user.UserDetail;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Hibour;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PreviousChatListAdapter extends RecyclerView.Adapter<PreviousChatListAdapter.ViewHolder> {


  private List<UserDetail> listItems = new ArrayList<>();
  private ViewHolder globalHolder;
  private ProgressDialog detailsDialog;
  private AppCompatActivity context;
  private DatabaseHandler dbHandler;
  private Hibour application;

  public PreviousChatListAdapter(AppCompatActivity context) {
    this.context = context;
    dbHandler = new DatabaseHandler(context);
    application = Hibour.getInstance(context);
  }

  public List<UserDetail> getUserList() {
    return listItems;
  }

  @Override
  public PreviousChatListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_previous_chat
        , parent, false);
    final ViewHolder holder = new ViewHolder(v);
    holder.itemView.setTag(holder);
    return holder;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {
    final UserDetail item = listItems.get(position);
    holder.userName.setText(listItems.get(position).Username);
//    Log.d("username",listItems.get(position).Username);
    UserMessage lastMessage = dbHandler.getRecentUserMessage(item.id, application.getUserId());
    holder.lastMessage.setText(lastMessage.message);

    Calendar messageTime = lastMessage.getTime();
    holder.time.setText((messageTime.get(Calendar.HOUR) < 10 ? "0" + messageTime.get(Calendar.HOUR) : messageTime.get(Calendar.HOUR))
        + ":" + (messageTime.get(Calendar.MINUTE) < 10 ? "0" + messageTime.get(Calendar.MINUTE) : messageTime.get(Calendar.MINUTE))
        + " " + (messageTime.get(Calendar.AM_PM) == 0 ? " AM" : " PM"));

    holder.entireView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Bundle data = new Bundle();
        data.putString(Constants.KEYWORD_USER_ID, item.id);
        Intent groupPostsIntent = new Intent(context, Chat.class);
        groupPostsIntent.putExtras(data);
        context.startActivity(groupPostsIntent);
      }
    });
    holder.entireView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        UserChatOptionDialoge userChatOptionDialoge = new UserChatOptionDialoge() {
          @Override
          public void deleteChat() {
            dbHandler.deleteUserMessage(item.id, application.getUserId());
            listItems.remove(position);
            notifyDataSetChanged();
          }
        };
        userChatOptionDialoge.show(context.getSupportFragmentManager(), "Chat Option Dialoge");
        return false;
      }
    });
  }

  @Override
  public int getItemCount() {
    return listItems.size();
  }

  public void removeItem(int adapterPosition) {
    listItems.remove(adapterPosition);
    notifyDataSetChanged();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    private TextView userName, lastMessage, time;
    private ImageView userImage;
    private View entireView;

    public ViewHolder(View itemView) {
      super(itemView);
      userName = (TextView) itemView.findViewById(R.id.adapter_neighbours_username);
      userImage = (ImageView) itemView.findViewById(R.id.adapter_neighbours_image);
      lastMessage = (TextView) itemView.findViewById(R.id.last_msg);
      time = (TextView) itemView.findViewById(R.id.last_msg_time);
      entireView = itemView.findViewById(R.id.entire_view);
    }
  }
}
