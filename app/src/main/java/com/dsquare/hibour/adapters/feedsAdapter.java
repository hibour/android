package com.dsquare.hibour.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.pojos.message.UserMessage;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Hibour;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ASHOK on 1/31/2016.
 */
public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.ViewHolder> implements View.OnClickListener {

    private List<String[]> listItems = new ArrayList<>();

    private int size = 0;
    public FeedsAdapter(int size) {
        Log.d("feds", "yes");
        this.size = size;
    }

    @Override
    public FeedsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_feeds
                ,parent,false);
        final ViewHolder holder = new ViewHolder(v);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return size;
    }

    @Override
    public void onClick(View view) {
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userName,profession;
        private ImageView userImage;

        public ViewHolder(View itemView) {
            super(itemView);

        }
    }
}
