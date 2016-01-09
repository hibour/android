package com.dsquare.hibour.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.activities.PostComments;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASHOK on 1/8/2016.
 */
public class AdapterPostComments extends RecyclerView.Adapter<AdapterPostComments.ViewHolder>{

    private List<String[]> listItems = new ArrayList<>();
    private Context context;
    private ViewHolder globalHolder;
    private ProgressDialog detailsDialog;

    public AdapterPostComments(Context context,List<String[]> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public AdapterPostComments.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_post_comments
                ,parent,false);
        final ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.userName.setText(listItems.get(position)[0]);
        holder.date.setText(listItems.get(position)[1]);
        holder.comment.setText(listItems.get(position)[2]);

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userName,date,comment;
        private ImageView userImage;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.adapter_comments_username);
            userImage = (ImageView) itemView.findViewById(R.id.adapter_comments);
            date = (TextView)itemView.findViewById(R.id.adapter_comments_date);
            comment = (TextView)itemView.findViewById(R.id.comments_comment);
        }
    }
}