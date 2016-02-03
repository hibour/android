package com.dsquare.hibour.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.activities.PostComments;
import com.dsquare.hibour.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ASHOK on 1/31/2016.
 */
public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.ViewHolder> implements View.OnClickListener {

    private List<String[]> listItems = new ArrayList<>();
    private Context context;
    public FeedsAdapter(Context context, List<String[]> listItems) {
        this.context = context;
        this.listItems = listItems;
        Log.d("s",listItems.size()+"");
    }

    @Override
    public FeedsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_feeds
                ,parent,false);
        final ViewHolder holder = new ViewHolder(v);
       // holder.itemView.setOnClickListener(this);
        //holder.itemView.setTag(holder);
        holder.likesLayout.setOnClickListener(this);
        holder.likesLayout.setTag(holder);

        holder.commentsLayout.setOnClickListener(this);
        holder.commentsLayout.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.message.setText(listItems.get(position)[2]);
        String categoryString = "";
        if(Constants.categoriesMap.containsKey(listItems.get(position)[3]))
            categoryString = Constants.categoriesMap.get(listItems.get(position)[3]);
        holder.categoryName.setText(categoryString);
        holder.likes.setText(listItems.get(position)[4]);
        holder.comments.setText(listItems.get(position)[5]);
        if(listItems.get(position)[8].length()>10){
            Log.d("image",listItems.get(position)[8]);
            try {
                holder.feedImage.setImageBitmap(base64ToBitmap(listItems.get(position)[8]));
            } catch (Exception e) {
                e.printStackTrace();
                holder.feedImage.setVisibility(View.GONE);
            }
        }else{
            holder.feedImage.setVisibility(View.GONE);
        }
        Log.d("user liked",listItems.get(position)[7]);
        if(listItems.get(position)[7].equals("true")){
            Bitmap likesIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_like_red);
            holder.likesImage.setImageBitmap(likesIcon);
        }else{
            Bitmap likesIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_likes_icon);
            holder.likesImage.setImageBitmap(likesIcon);
        }
        holder.timeStamp.setText(getTimeStamp(listItems.get(position)[1],""));

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public void onClick(View view) {
        final ViewHolder viewHolder = (ViewHolder)view.getTag();
        final int position = viewHolder.getAdapterPosition();
        switch (view.getId()){
            case R.id.feeds_comments_layout:
                openCommentsDialog(listItems.get(position)[6],listItems.get(position)[4]);
                break;
            case R.id.feeds_likes_layout:
                changeLikesCount(position);
                break;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView timeStamp,message,categoryName,likes,comments;
        private ImageView userImage,shareImage,likesImage,feedImage;
        private LinearLayout commentsLayout,likesLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            timeStamp = (TextView)itemView.findViewById(R.id.feeds_timestamp);
            message = (TextView)itemView.findViewById(R.id.feeds_message);
            categoryName = (TextView)itemView.findViewById(R.id.feeds_category_name);
            likes = (TextView)itemView.findViewById(R.id.feeds_likes_text);
            comments = (TextView)itemView.findViewById(R.id.feeds_comments_text);
            userImage = (ImageView)itemView.findViewById(R.id.feeds_user_image);
            likesImage = (ImageView)itemView.findViewById(R.id.feeds_likes_image);
            feedImage = (ImageView)itemView.findViewById(R.id.feeds_image);
            commentsLayout = (LinearLayout)itemView.findViewById(R.id.feeds_comments_layout);
            likesLayout = (LinearLayout)itemView.findViewById(R.id.feeds_likes_layout);
        }
    }
    /* change likes count*/
    private void changeLikesCount(int position){
        String[] data = new String[9];
        data[0] = listItems.get(position)[0];
        data[1] = listItems.get(position)[1];
        data[2] = listItems.get(position)[2];
        data[3] = listItems.get(position)[3];
        data[4] = "";
        data[5] = listItems.get(position)[5];
        data[6] = listItems.get(position)[6];
        data[7] = "";
        if(listItems.get(position)[7].equals("true")){
            data[7] = "false";
            int likesCount = Integer.valueOf(listItems.get(position)[4])-1;
            data[4] = likesCount+"";
        }else{
            data[7] = "true";
            int likesCount = Integer.valueOf(listItems.get(position)[4])+1;
            data[4] = likesCount+"";
        }
        data[8] = listItems.get(position)[8];
        listItems.set(position,data);
        notifyItemChanged(position);
    }
    /* convert base64 string to image*/
    private Bitmap base64ToBitmap(String myImageData){
        byte[] imageAsBytes = Base64.decode(myImageData.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }
    /* get timestamp from feed*/
    private String getTimeStamp(String date,String time){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String postedDate = date;//getDateFromString(date);
        String todayDate = getTodayDate();
        try {
            if((formatter.parse(postedDate).compareTo(formatter.parse(todayDate)))<0){
                long secs = formatter.parse(todayDate).getTime()
                        -formatter.parse(postedDate).getTime();
                int diffInDays = (int) ((secs) / (1000 * 60 * 60 * 24));
                return diffInDays+" days ago";
            }else{
                String postTime = time;
                Calendar cal = Calendar.getInstance();
                Date date1 = cal.getTime();
                Date date2;
                cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE)
                        ,Integer.valueOf(postTime.substring(0,2)),Integer.valueOf(postTime.substring(3,5)));
                date2 = cal.getTime();
                long result = date1.getTime()-date2.getTime();
                int secs = (int)result/(1000);
                int mins = (int) secs/(60);
                int hours = (int)mins/(60);
                if(hours>0){
                    return hours+" hours ago";
                }else if(mins>0){
                    return mins+" minutes ago";
                }else{
                    return secs+" seconds ago";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /* get today date*/
    private String getTodayDate(){
        try{
            Calendar currentDate = Calendar.getInstance(); //Get the current date
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd"); //format it as per your requirement
            return formatter.format(currentDate.getTime());
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
    /* get date from string*/
    public String getDateFromString(String days){
        Calendar currentDate = Calendar.getInstance(); //Get the current date
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd"); //format it as per your requirement
        currentDate.add(Calendar.DATE, Integer.parseInt(days));
        return formatter.format(currentDate.getTime());
    }

    /* open post comments*/
    private void openCommentsDialog(String postId,String likes){
        Intent commentsIntent = new Intent(context, PostComments.class);
        commentsIntent.putExtra("postId",postId);
        commentsIntent.putExtra("likes",likes);
        context.startActivity(commentsIntent);
    }
}
