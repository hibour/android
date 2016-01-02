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
            /*if(getItem(position).equals("Pill Remainder") ||
                    getItem(position).equals("About Only Health")){
                RelativeLayout bottomLine=(RelativeLayout)convertView.
                        findViewById(R.id.nav_drawer_bottom_line);
                bottomLine.setVisibility(View.GONE);
                LinearLayout topLayout=(LinearLayout)convertView.findViewById(R.id.nav_linear);
                ViewGroup.LayoutParams params=topLayout.getLayoutParams();
                int height= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80,
                        context.getResources().getDisplayMetrics());
                params.height=height;
                holder.notifIcon.setBackgroundResource(menuIcons.getResourceId(position, -1));
                holder.notifDesc.setText((String) getItem(position));
            }else if(getItem(position)=="Home"){

                LinearLayout topLayout=(LinearLayout)convertView.findViewById(R.id.nav_linear);
                ViewGroup.LayoutParams params=topLayout.getLayoutParams();
                int height= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,80,
                        context.getResources().getDisplayMetrics());
                params.height=height;
                RelativeLayout nav=(RelativeLayout)convertView.findViewById(R.id.nav_layout);
                int padding= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,80,
                        context.getResources().getDisplayMetrics());
                nav.setPadding(padding,0,0,0);
                holder.notifIcon.setBackgroundResource(menuIcons.getResourceId(position, -1));
                holder.notifDesc.setText((String) getItem(position));
            }else if(getItem(position).equals("Signout")){
                LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.nav_bottom_line);
                layout.setVisibility(View.GONE);
                holder.notifIcon.setBackgroundResource(menuIcons.getResourceId(position, -1));
                holder.notifDesc.setText((String) getItem(position));
            }else if(getItem(position).equals("Product Tour")){
                RelativeLayout.LayoutParams params =
                        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                int padding= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,16,
                        context.getResources().getDisplayMetrics());
                params.setMargins(padding,0,0,0);
                holder.notifIcon.setBackgroundResource(menuIcons.getResourceId(position, -1));
                holder.notifIcon.requestLayout();
                holder.notifIcon.setLayoutParams(params);
                holder.notifIcon.getLayoutParams().height = 30;
                holder.notifIcon.getLayoutParams().width=30;
                holder.notifDesc.setText((String) getItem(position));
            }else{*/
                holder.notifDesc.setText((String) getItem(position));
            //}

        }catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }

    class ViewHolder{
        TextView notifDesc;
    }
}
