package com.dsquare.hibour.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.activities.Chat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASHOK on 1/8/2016.
 */
public class NeighboursAdapter extends RecyclerView.Adapter<NeighboursAdapter.ViewHolder> implements View.OnClickListener {

    private List<String[]> listItems = new ArrayList<>();
    private Context context;
    private ViewHolder globalHolder;
    private ProgressDialog detailsDialog;

    public NeighboursAdapter(Context context,List<String[]> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public NeighboursAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_neighbours
                ,parent,false);
        final ViewHolder holder = new ViewHolder(v);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.userName.setText(listItems.get(position)[0]);
        holder.profession.setText(listItems.get(position)[1]);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public void onClick(View view) {
        Intent groupPostsIntent = new Intent(context, Chat.class);
        context.startActivity(groupPostsIntent);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userName,profession;
        private ImageView userImage;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.adapter_neighbours_username);
            userImage = (ImageView) itemView.findViewById(R.id.adapter_neighbours_image);
            profession = (TextView)itemView.findViewById(R.id.adapter_neighbours_user_profession);

        }
    }
}
