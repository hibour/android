package com.dsquare.hibour.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsquare.hibour.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dsquare Android on 1/28/2016.
 */
public class AdapterPostLikes extends RecyclerView.Adapter<AdapterPostLikes.ViewHolder> implements View.OnClickListener {

    private List<String[]> listItems = new ArrayList<>();
    private Context context;

    public AdapterPostLikes(Context context,List<String[]> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public AdapterPostLikes.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_post_likes
                ,parent,false);
        final ViewHolder holder = new ViewHolder(v);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.userName.setText(listItems.get(position)[1]);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public void onClick(View view) {
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userName,profession;
        private ImageView userImage;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.adapter_likes_username);
            userImage = (ImageView) itemView.findViewById(R.id.adapter_likes_image);

        }
    }
}
