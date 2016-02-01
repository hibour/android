package com.dsquare.hibour.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dsquare.hibour.R;

/**
 * Created by Aditya Ravikanti on 12/31/2015.
 */
public class NavigationDrawerAdapter extends BaseAdapter {

    private Context context;
    private String[] drawerItems;
    private String[] drawerImages;
    private TypedArray menuIcons;
    public NavigationDrawerAdapter(){

    }

    public NavigationDrawerAdapter(Context context){
        this.context = context;
        Resources resources = context.getResources();
        drawerItems = resources.getStringArray(R.array.drawerItems);
        drawerImages = resources.getStringArray(R.array.drawerImages);
    }

    @Override
    public int getCount() {
        return drawerItems.length;
    }

    @Override
    public Object getItem(int position) {
        return drawerItems[position];
    }

    public Object getDrawerIcon(int position) {
        return drawerImages[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.nav_drawer_row, parent, false);
            holder = new ViewHolder();
            holder.notifIcon = (ImageView)convertView.findViewById(R.id.nav_item_icon);
            holder.notifDesc = (TextView)convertView.findViewById(R.id.nav_item_text);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        try{
            if(getItem(position).equals("GROUPS")){
                RelativeLayout layout = (RelativeLayout)convertView.findViewById(R.id.nav_bottom_layout);
                layout.setVisibility(View.VISIBLE);
                RelativeLayout l = (RelativeLayout)convertView.findViewById(R.id.nav_drawer_bottom_line1);
                l.setVisibility(View.VISIBLE);
                RelativeLayout navBottomLine = (RelativeLayout)convertView.findViewById(R.id.nav_drawer_bottom_line);
                navBottomLine.setVisibility(View.GONE);
            }else if(getItem(position).equals("HOME")){
                RelativeLayout topLayout=(RelativeLayout)convertView.findViewById(R.id.nav_drawer_row_layout);
                topLayout.setVisibility(View.VISIBLE);
            }else if(getItem(position).equals("LOGOUT")){
                RelativeLayout topLayout=(RelativeLayout)convertView.findViewById(R.id.nav_drawer_row_layout);
                topLayout.setVisibility(View.VISIBLE);
                RelativeLayout navBottomLine = (RelativeLayout)convertView.findViewById(R.id.nav_drawer_bottom_line);
                navBottomLine.setVisibility(View.GONE);
                RelativeLayout bottomLayout = (RelativeLayout)convertView.findViewById(R.id.nav_logout_layout);
                bottomLayout.setVisibility(View.VISIBLE);
            } else if(getItem(position).equals("SETTINGS")){
                RelativeLayout navBottomLine = (RelativeLayout)convertView.findViewById(R.id.nav_drawer_bottom_line);
                navBottomLine.setVisibility(View.GONE);
            }
            String drawerIcon = (String)getDrawerIcon(position);
            if(!drawerIcon.isEmpty()) {
                holder.notifIcon.setImageResource(context.getResources().getIdentifier(drawerIcon.substring(0, drawerIcon.length()), "drawable", context.getPackageName()));
            }

            holder.notifDesc.setText((String)getItem(position));
        }catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }

    class ViewHolder{
        TextView notifDesc;
        ImageView notifIcon;
    }
}
