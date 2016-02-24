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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ASHOK on 1/8/2016.
 */
public class AdapterPostComments extends RecyclerView.Adapter<AdapterPostComments.ViewHolder>{

    private List<String[]> comments = new ArrayList<>();
    private Context context;
    private ViewHolder globalHolder;
    private ProgressDialog detailsDialog;

    public AdapterPostComments(Context context,List<String[]> comments) {
        this.context = context;
        this.comments = comments;
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
        holder.userName.setText(comments.get(position)[0]);
        holder.date.setText(comments.get(position)[1]);
        holder.comment.setText(comments.get(position)[2]);
        holder.date.setText(getTimeStamp(comments.get(position)[3]
            , comments.get(position)[1]));

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    /* get timestamp from feed*/
    private String getTimeStamp(String date, String time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String postedDate = date;//getDateFromString(date);
        String todayDate = getTodayDate();
        try {
            if ((formatter.parse(postedDate).compareTo(formatter.parse(todayDate))) < 0) {
                long secs = formatter.parse(todayDate).getTime()
                    - formatter.parse(postedDate).getTime();
                int diffInDays = (int) ((secs) / (1000 * 60 * 60 * 24));
                return diffInDays + " days ago";
            } else {
                String postTime = time;
                Calendar cal = Calendar.getInstance();
                Date date1 = cal.getTime();
                Date date2;
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)
                    , Integer.valueOf(postTime.substring(0, 2)), Integer.valueOf(postTime.substring(3, 5)));
                date2 = cal.getTime();
                long result = date1.getTime() - date2.getTime();
                int secs = (int) result / (1000);


                if (secs > 0) {
                    int mins = (int) secs / (60);
                    int hours = (int) mins / (60);
                    if (hours > 0) {
                        return hours + " hours ago";
                    } else if (mins > 0) {
                        return mins + " minutes ago";
                    } else {
                        return secs + " seconds ago";
                    }
                } else {
                    secs = secs * (-1);
                    int mins = (int) secs / (60);
                    int hours = (int) mins / (60);
                    if (hours > 0) {
                        return hours + " hours ago";
                    } else if (mins > 0) {
                        return mins + " minutes ago";
                    } else {
                        return secs + " seconds ago";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /* get today date*/
    private String getTodayDate() {
        try {
            Calendar currentDate = Calendar.getInstance(); //Get the current date
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //format it as per your requirement
            return formatter.format(currentDate.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userName, date, comment;
        private ImageView userImage;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.adapter_comments_username);
            userImage = (ImageView) itemView.findViewById(R.id.adapter_comments);
            date = (TextView) itemView.findViewById(R.id.adapter_comments_date);
            comment = (TextView) itemView.findViewById(R.id.comments_comment);
        }
    }
}
