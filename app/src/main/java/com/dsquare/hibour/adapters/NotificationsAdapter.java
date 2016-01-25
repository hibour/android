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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aditya Ravikanti on 1/25/2016.
 */
public class NotificationsAdapter extends BaseAdapter {

    private Context context;
    private List<String[]> gcmMessageList = new ArrayList<>();

    public NotificationsAdapter(Context context, List<String[]> gcmMessageList){
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
        Typeface tf1 = Typeface.createFromAsset(context.getAssets(),"fonts/pn_light.otf");
        holder.tipContent.setText(gcmMessageList.get(position)[0]);
        holder.tipDate.setText(gcmMessageList.get(position)[1]);
        if(gcmMessageList.get(position)[2].equals("unread")){
            Log.d("status", "unread");
            holder.tipContent.setTypeface(tf);
            String[] d1 = new String[3];
            d1[0] = gcmMessageList.get(position)[0];
            d1[1] = gcmMessageList.get(position)[1];
            d1[2] = "read";
            gcmMessageList.add(position,d1);
            notifyDataSetChanged();

        }else{
            Log.d("status", "read");
            holder.tipContent.setTypeface(tf1);
            String[] d1 = new String[3];
            d1[0] = gcmMessageList.get(position)[0];
            d1[1] = gcmMessageList.get(position)[1];
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