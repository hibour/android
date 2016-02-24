package com.dsquare.hibour.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.activities.PostComments;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.network.PostsClient;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Hibour;
import com.dsquare.hibour.utils.SlidingTabLayout;
import com.google.gson.Gson;

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
    private Gson gson;
    private Hibour application;
    private PostsClient postsClient;
    private ProgressDialog postsDialog;
    private NetworkDetector networkDetector;
    private List<String> tabsList = new ArrayList<>();
    private ViewPager pager;
    private SlidingTabLayout tabs;
    private RelativeLayout noFeedsLayout;
    private List<String> autocompleteList = new ArrayList<>();

    public PostsAdapter(Context context,List<String[]> listItems) {
        this.context = context;
        this.listItems = listItems;

    }

    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_posts,parent
                ,false);
        final ViewHolder holder = new ViewHolder(v);
        postsClient = new PostsClient(context);
        gson = new Gson();
        networkDetector = new NetworkDetector(context);
        application =  Hibour.getInstance(context);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.userName.setText(listItems.get(position)[0]);
        holder.date.setText(listItems.get(position)[1]);
        holder.description.setText(listItems.get(position)[2]);
        String categoryName = "";
        if(Constants.categoriesMap.containsKey(listItems.get(position)[3]))
            categoryName = Constants.categoriesMap.get(listItems.get(position)[3]);
        holder.categoryName.setText(categoryName);
        holder.likes.setText(listItems.get(position)[4]);
        holder.comments.setText(listItems.get(position)[5]);
        if(listItems.get(position)[7].equals("false")){
            holder.likesImage.setImageResource(R.mipmap.ic_likes_icon);
        }else {
            holder.likesImage.setImageResource(R.drawable.ic_like_red);

        }
        holder.shareImage.setOnClickListener(this);
        holder.shareImage.setTag(holder);

        holder.commentsLayout.setOnClickListener(this);
        holder.commentsLayout.setTag(holder);

        holder.likesImage.setOnClickListener(this);
        holder.likesImage.setTag(holder);
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
                openCommentsDialog(listItems.get(position)[6],listItems.get(position)[4]);
                break;
            case R.id.adapter_post_likes_image:
//                Fragment fragment = new Posts();
//                if (fragment instanceof Posts)
//                    ((Posts) fragment).getAllposts();
//                posts.getAllposts();
                if(listItems.get(position)[7].equals("false")){
                    viewHolder.likesImage.setImageResource(R.drawable.ic_like_red);
                    getLikesPost(listItems.get(position)[6]);
                   int value = Integer.parseInt(viewHolder.likes.getText().toString());
                    int value1 = value+1;
                    viewHolder.likes.setText(String.valueOf(value1));

                }else{
                    viewHolder.likesImage.setImageResource(R.mipmap.ic_likes_icon);
                    getLikesPost(listItems.get(position)[6]);
                    if(!viewHolder.likes.getText().toString().equals("0")){
                        int value = Integer.parseInt(viewHolder.likes.getText().toString());
                        int value1 = value-1;
                        viewHolder.likes.setText(String.valueOf(value1));
                    }
                }

//                getLikesPost(listItems.get(position)[6]);
                break;
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
    private void openCommentsDialog(String postId,String likes){
        Intent commentsIntent = new Intent(context, PostComments.class);
        commentsIntent.putExtra("postId",postId);
        commentsIntent.putExtra("likes",likes);
        context.startActivity(commentsIntent);
    }

    /* get all posts from server*/
    private void getLikesPost(String postid){
       /* if(networkDetector.isConnected()){
            postsDialog = ProgressDialog.show(context,"","Please wait...");
            postsClient.getLikesonPosts(application.getUserId(), postid, new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
//                    parsePostsDetails(jsonObject);
                    closePostsDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("posts error", error.toString());
                    closePostsDialog();
                }
            });
        }else{
            Toast.makeText(context, "Check network connection", Toast.LENGTH_LONG).show();
        }*/
    }

    /* close posts dialog*/
    private void closePostsDialog(){
        if(postsDialog!=null){
            if(postsDialog.isShowing()){
                postsDialog.dismiss();
                postsDialog=null;
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userName, date, description, categoryName, likes, comments;
        private ImageView userImage, shareImage, likesImage, likesImage1;
        private RelativeLayout commentsLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.post_user_name);
            userImage = (ImageView) itemView.findViewById(R.id.post_user_image);
            date = (TextView) itemView.findViewById(R.id.adapter_post_date);
            description = (TextView) itemView.findViewById(R.id.post_description_text);
            categoryName = (TextView) itemView.findViewById(R.id.post_category_name);
            likes = (TextView) itemView.findViewById(R.id.post_likes);
            comments = (TextView) itemView.findViewById(R.id.post_comments);
            shareImage = (ImageView) itemView.findViewById(R.id.post_share_image);
            likesImage = (ImageView) itemView.findViewById(R.id.adapter_post_likes_image);
            commentsLayout = (RelativeLayout) itemView.findViewById(R.id.post_comments_layout);
        }
    }
}
