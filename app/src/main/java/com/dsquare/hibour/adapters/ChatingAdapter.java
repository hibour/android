package com.dsquare.hibour.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.pojos.message.UserMessage;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Hibour;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Aditya Ravikanti on 1/8/2016.
 */
public class ChatingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private List<UserMessage> userMessageList = new ArrayList<>();
  private Context context;
  private ViewHolder globalHolder;
  private ProgressDialog detailsDialog;
  private Hibour application;

  public ChatingAdapter(Context context, List<UserMessage> userMessageList) {
    application = Hibour.getInstance(context);
    this.context = context;
    this.userMessageList = userMessageList;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v;
    if (viewType == Constants.MESSAGE_LEFT)
      v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_left, parent, false);
    else
      v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_right, parent, false);
    ViewHolder holder = new ViewHolder(v);
    return holder;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
    ViewHolder holder = (ViewHolder) viewHolder;
    UserMessage item = userMessageList.get(position);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(item.time);
    String time = (calendar.get(Calendar.HOUR) < 10 ? "0" + calendar.get(Calendar.HOUR) : calendar.get(Calendar.HOUR))
        + ":" + (calendar.get(Calendar.MINUTE) < 10 ? "0" + calendar.get(Calendar.MINUTE) : calendar.get(Calendar.MINUTE)) +
        (calendar.get(Calendar.AM_PM) == 0 ? " AM" : " PM");
    holder.message.setText(item.message);
    holder.time.setText(time);
  }

  @Override
  public int getItemCount() {
    return userMessageList.size();
  }

  @Override
  public int getItemViewType(int position) {
    if (application.getUserId().equalsIgnoreCase(userMessageList.get(position).fromUserID))
      return Constants.MESSAGE_RIGHT;
    else
      return Constants.MESSAGE_LEFT;
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    private TextView message, time;

    public ViewHolder(View itemView) {
      super(itemView);
      message = (TextView) itemView.findViewById(R.id.message);
      time = (TextView) itemView.findViewById(R.id.time);
    }
  }
}
