package com.dsquare.hibour.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.dsquare.hibour.R;
import com.dsquare.hibour.activities.PreferencesViews;
import com.dsquare.hibour.network.HibourConnector;
import com.dsquare.hibour.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dsquare Android on 1/25/2016.
 */
public class SocializeAdapter extends RecyclerView.Adapter<SocializeAdapter.ViewHolder>
        implements View.OnClickListener{

    private List<String[]> listItems = new ArrayList<>();
    private Context context;
    private ImageLoader imageLoader;
    private String userprefer;
    public SocializeAdapter(Context context,List<String[]> listItems) {
        this.context = context;
        this.listItems = listItems;
        imageLoader = HibourConnector.getInstance(context).getImageLoader();
    }

    @Override
    public SocializeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_social_prefs,parent
                ,false);
        final ViewHolder holder = new ViewHolder(v);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d("size", listItems.get(position)[0]);
        for(String pref: Constants.prefernceMap.keySet()){
            userprefer = userprefer+","+pref;
        }
//        userprefer = userprefer.substring(1,userprefer.length());
        if(listItems.get(position)[4].equals("false")){
            try {
                imageLoader.get(listItems.get(position)[2], ImageLoader.getImageListener(holder.prefImage
                        , R.mipmap.ic_launcher, R.mipmap.ic_launcher));
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.prefName.setTextColor(context.getResources().getColor(R.color.brand));
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk <16) {
                holder.prefLinearLayout.setBackgroundDrawable(context.getResources()
                        .getDrawable(R.drawable.social_prefs_unselected_state));
            } else {
                holder.prefLinearLayout.setBackground(context.getResources()
                        .getDrawable(R.drawable.social_prefs_unselected_state));
            }
        }else{
            try {
                imageLoader.get(listItems.get(position)[3], ImageLoader.getImageListener(holder.prefImage
                        , R.mipmap.ic_launcher, R.mipmap.ic_launcher));
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.prefName.setTextColor(context.getResources().getColor(R.color.white));
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk <16) {
                holder.prefLinearLayout.setBackgroundDrawable(context.getResources()
                        .getDrawable(R.drawable.social_prefs_selected_state));
            } else {
                holder.prefLinearLayout.setBackground(context.getResources()
                        .getDrawable(R.drawable.social_prefs_selected_state));
            }
        }
        holder.prefName.setText(listItems.get(position)[1]);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


    @Override
    public void onClick(View v) {

        final ViewHolder viewHolder = (ViewHolder)v.getTag();
        final int position = viewHolder.getAdapterPosition();
        if(listItems.get(position)[4].equals("false")){
            String[] data = new String[6];
            data[0] = listItems.get(position)[0];
            data[1] = listItems.get(position)[1];
            data[2] = listItems.get(position)[2];
            data[3] = listItems.get(position)[3];
            data[4] = "true";
            data[5] = listItems.get(position)[5];
            listItems.set(position,data);
            Constants.prefernceMap.put(listItems.get(position)[0],listItems.get(position)[1]);

        }else{
            Intent intent = new Intent(context, PreferencesViews.class);
            context.startActivity(intent);
            String[] data = new String[6];
            data[0] = listItems.get(position)[0];
            data[1] = listItems.get(position)[1];
            data[2] = listItems.get(position)[2];
            data[3] = listItems.get(position)[3];
            data[4] = "false";
            data[5] = listItems.get(position)[5];
            listItems.set(position,data);
            Constants.prefernceMap.remove(listItems.get(position)[0]);
        }
        //notifyDataSetChanged();
        notifyItemChanged(position);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView prefName;
        public ImageView prefImage;
        private RelativeLayout layout;
        private LinearLayout prefLinearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            prefName = (TextView) itemView.findViewById(R.id.pref_name);
            prefImage = (ImageView) itemView.findViewById(R.id.pref_icon);
            layout = (RelativeLayout)itemView.findViewById(R.id.pref_layout);
            prefLinearLayout = (LinearLayout)itemView.findViewById(R.id.pref_linear_layout);
        }
    }
}
