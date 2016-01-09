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
 * Created by Dsquare Android on 1/9/2016.
 */
public class GroupPostsAdapter extends RecyclerView.Adapter<GroupPostsAdapter.ViewHolder>
        implements View.OnClickListener{

    private List<String[]> listItems = new ArrayList<>();
    private Context context;
    private ViewHolder globalHolder;
    private ProgressDialog detailsDialog;

    public GroupPostsAdapter(Context context,List<String[]> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public GroupPostsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_group_posts,parent
                ,false);
        final ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.userName.setText(listItems.get(position)[0]);
        holder.date.setText(listItems.get(position)[1]);
        holder.description.setText(listItems.get(position)[2]);
        holder.likes.setText(listItems.get(position)[3]);
        holder.comments.setText(listItems.get(position)[4]);

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
            case R.id.adapter_group_posts_comments_layout:
                openCommentsDialog();
                break;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userName,date,description,likes,comments;
        private ImageView userImage;
        private RelativeLayout commentsLayout,likesLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.group_post_user_name);
            userImage = (ImageView) itemView.findViewById(R.id.group_post_user_image);
            date = (TextView)itemView.findViewById(R.id.adapter_group_post_date);
            description = (TextView)itemView.findViewById(R.id.adapter_group_post_description_text);
            likes = (TextView)itemView.findViewById(R.id.post_likes);
            comments = (TextView)itemView.findViewById(R.id.post_comments);
            commentsLayout = (RelativeLayout)itemView.findViewById(R.id.adapter_group_posts_comments_layout);
        }
    }

    /* open post comments*/
    private void openCommentsDialog(){
        Intent commentsIntent = new Intent(context, PostComments.class);
        context.startActivity(commentsIntent);
    }
}
