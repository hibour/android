package com.dsquare.hibour.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.database.table.NotificationTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aditya Ravikanti on 1/25/2016.
 */
public class NotificationsAdapter extends BaseAdapter {

    private Context context;
    private List<NotificationTable> gcmMessageList = new ArrayList<>();

    public NotificationsAdapter(Context context, List<NotificationTable> gcmMessageList) {
        this.context = context;
        this.gcmMessageList = gcmMessageList;
    }

    @Override
    public int getCount() {
        return gcmMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return gcmMessageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater  =  (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_notifications, parent, false);
            holder = new ViewHolder();
            holder.icon = (ImageView)convertView.findViewById(R.id.notif_icon);
            holder.tipContent = (TextView)convertView.findViewById(R.id.notif_text);
            holder.tipDate = (TextView)convertView.findViewById(R.id.notif_date);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Typeface tf = Typeface.createFromAsset(context.getAssets(),"fonts/pn_extrabold.otf");
        holder.tipContent.setText(gcmMessageList.get(position).message);
        holder.tipDate.setText(gcmMessageList.get(position).date);
        if (gcmMessageList.get(position).status.equals("unread")) {
            Log.d("status", "unread");
            holder.tipContent.setTypeface(tf);
            NotificationTable item = gcmMessageList.get(position);
            gcmMessageList.add(position, new NotificationTable(item.message, item.date, "read"));
            notifyDataSetChanged();

        }else{
            Log.d("status", "read");
            holder.tipContent.setTypeface(tf1);
            String[] d1 = new String[3];
            d1[0] = gcmMessageList.get(position).message;
            d1[1] = gcmMessageList.get(position).date;
            d1[2] = "unread";
            gcmMessageList.set(position, d1);
        }

        return convertView;
    }

    public class ViewHolder{
        ImageView icon;
        TextView tipContent;
        TextView tipDate;
    }
}
