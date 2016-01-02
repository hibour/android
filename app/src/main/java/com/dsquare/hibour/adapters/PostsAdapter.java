package com.dsquare.hibour.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.network.NetworkDetector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aditya Ravikanti on 1/2/2016.
 */
public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder>
        implements View.OnClickListener{

    private List<String[]> listItems = new ArrayList<>();
    private Context context;
    private ViewHolder globalHolder;
    private ProgressDialog detailsDialog;

    public PostsAdapter(Context context,List<String[]> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_posts,parent
                ,false);
        final ViewHolder holder = new ViewHolder(v);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.userName.setText(listItems.get(position)[0]);
        holder.date.setText(listItems.get(position)[1]);
        holder.description.setText(listItems.get(position)[2]);
        holder.categoryName.setText(listItems.get(position)[3]);
        holder.likes.setText(listItems.get(position)[4]);
        holder.comments.setText(listItems.get(position)[5]);

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


    @Override
    public void onClick(View v) {
        final ViewHolder viewHolder = (ViewHolder)v.getTag();
        final int position = viewHolder.getAdapterPosition();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userName,date,description,categoryName,likes
                ,comments;
        private ImageView userImage;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.post_user_name);
            userImage = (ImageView) itemView.findViewById(R.id.post_user_image);
            date = (TextView)itemView.findViewById(R.id.adapter_post_date);
            description = (TextView)itemView.findViewById(R.id.post_description_text);
            categoryName = (TextView)itemView.findViewById(R.id.post_category_name);
            likes = (TextView)itemView.findViewById(R.id.post_likes);
            comments = (TextView)itemView.findViewById(R.id.post_comments);
        }
    }
}
