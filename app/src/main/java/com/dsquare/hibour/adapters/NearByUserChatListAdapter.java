package com.dsquare.hibour.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.activities.Chat;
import com.dsquare.hibour.pojos.user.UserDetail;
import com.dsquare.hibour.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASHOK on 1/8/2016.
 */
public class NearByUserChatListAdapter extends RecyclerView.Adapter<NearByUserChatListAdapter.ViewHolder> {


  private List<UserDetail> listItems = new ArrayList<>();
  private ViewHolder globalHolder;
  private ProgressDialog detailsDialog;
  private Context context;

  public NearByUserChatListAdapter(Context context) {
    this.context = context;
  }

  public List<UserDetail> getUserList() {
    return listItems;
  }

  @Override
  public NearByUserChatListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_neighbours
        , parent, false);
    final ViewHolder holder = new ViewHolder(v);
    holder.itemView.setTag(holder);
    return holder;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final UserDetail item = listItems.get(position);
    holder.userName.setText(listItems.get(position).Username);
//    Log.d("username",listItems.get(position).Username);
    holder.profession.setText(listItems.get(position).Address);
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
    private TextView userName, profession;
    private ImageView userImage;
    private View entireView;

    public ViewHolder(View itemView) {
      super(itemView);
      userName = (TextView) itemView.findViewById(R.id.adapter_neighbours_username);
      userImage = (ImageView) itemView.findViewById(R.id.adapter_neighbours_image);
      profession = (TextView) itemView.findViewById(R.id.adapter_neighbours_user_profession);
      entireView = itemView.findViewById(R.id.entire_view);
    }
  }
}
