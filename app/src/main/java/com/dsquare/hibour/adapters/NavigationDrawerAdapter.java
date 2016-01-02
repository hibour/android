package com.dsquare.hibour.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dsquare.hibour.R;

/**
 * Created by Aditya Ravikanti on 12/31/2015.
 */
public class NavigationDrawerAdapter extends BaseAdapter {

    private Context context;
    private String[] drawerItems;
    private TypedArray menuIcons;

    public NavigationDrawerAdapter(){

    }

    public NavigationDrawerAdapter(Context context){
        this.context = context;
        Resources resources = context.getResources();
        drawerItems = resources.getStringArray(R.array.drawerItems);
    }

    @Override
    public int getCount() {
        return drawerItems.length;
    }

    @Override
    public Object getItem(int position) {
        return drawerItems[position];
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
            holder.notifDesc.setText((String)getItem(position));
        }catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }

    class ViewHolder{
        TextView notifDesc;
    }
}
