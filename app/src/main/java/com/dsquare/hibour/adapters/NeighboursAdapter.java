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
public class NeighboursAdapter extends RecyclerView.Adapter<NeighboursAdapter.ViewHolder> {
    private List<UserDetail> listItems = new ArrayList<>();
    private ViewHolder globalHolder;
    private ProgressDialog detailsDialog;
    private Context context;
    private int layout;

    public NeighboursAdapter(Context context, List<UserDetail> listItems, int layout) {
        this.context = context;
        this.listItems = listItems;
        this.layout = layout;
    }

    @Override
    public NeighboursAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(this.layout
                , parent, false);
        final ViewHolder holder = new ViewHolder(v);
        holder.itemView.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final UserDetail item = listItems.get(position);
        String displayName = "";
        if(item.Username != null && item.Username.length() <= 0) {
            displayName = item.Username;
        } else {
            displayName = item.Email;
        }
        holder.userName.setText(displayName);
        if(holder.profession != null) {
            holder.profession.setText(item.Address);
        }
        holder.entireView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString(Constants.KEYWORD_USER_ID, item.id);
                data.putString(Constants.KEYWORD_USER_NAME, item.Username);
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

    public void removeItem(int position) {
        this.listItems.remove(position);
        this.notifyItemRemoved(position);
        //TODO API CALL
    }
}
