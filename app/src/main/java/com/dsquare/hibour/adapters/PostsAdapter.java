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

        holder.shareImage.setOnClickListener(this);
        holder.shareImage.setTag(holder);

        holder.commentsLayout.setOnClickListener(this);
        holder.commentsLayout.setTag(holder);

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


    @Override
    public void onClick(View v) {
        final ViewHolder viewHolder = (ViewHolder)v.getTag();
        final int position = viewHolder.getAdapterPosition();
        switch (v.getId()){
            case R.id.post_share_image:
                sharePost(listItems.get(position)[2]);
                break;
            case R.id.post_comments_layout:
                openCommentsDialog();
                break;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userName,date,description,categoryName,likes
                ,comments;
        private ImageView userImage,shareImage;
        private RelativeLayout commentsLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.post_user_name);
            userImage = (ImageView) itemView.findViewById(R.id.post_user_image);
            date = (TextView)itemView.findViewById(R.id.adapter_post_date);
            description = (TextView)itemView.findViewById(R.id.post_description_text);
            categoryName = (TextView)itemView.findViewById(R.id.post_category_name);
            likes = (TextView)itemView.findViewById(R.id.post_likes);
            comments = (TextView)itemView.findViewById(R.id.post_comments);
            shareImage = (ImageView)itemView.findViewById(R.id.post_share_image);
            commentsLayout = (RelativeLayout)itemView.findViewById(R.id.post_comments_layout);
        }
    }

    /*share post*/
    private void sharePost(String postMessage){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }
    /* open post comments*/
    private void openCommentsDialog(){
        Intent commentsIntent = new Intent(context, PostComments.class);
        context.startActivity(commentsIntent);
    }
}
