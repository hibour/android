package com.dsquare.hibour.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
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
import com.dsquare.hibour.network.HibourConnector;
import com.dsquare.hibour.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dsquare Android on 1/8/2016.
 */
public class PreferencesAdapter extends RecyclerView.Adapter<PreferencesAdapter.ViewHolder>
        implements View.OnClickListener{

    private List<String[]> listItems = new ArrayList<>();
    private Context context;
    private ImageLoader imageLoader;
    public PreferencesAdapter(Context context,List<String[]> listItems) {
        this.context = context;
        this.listItems = listItems;
        imageLoader = HibourConnector.getInstance(context).getImageLoader();
    }

    @Override
    public PreferencesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_prefernces_views,parent
                ,false);
        final ViewHolder holder = new ViewHolder(v);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(holder);
        return holder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d("size", listItems.get(position)[0]);
        ShapeDrawable shape = new ShapeDrawable(new OvalShape());
        shape.getPaint().setColor(Color.WHITE);
//        shape.setColorFilter(R.color.white, PorterDuff.Mode.ADD);
        shape.setIntrinsicHeight(10);
        shape.setIntrinsicWidth(10);
        if(listItems.get(position)[4].equals("false")){
            try {
                imageLoader.get(listItems.get(position)[2], ImageLoader.getImageListener(holder.prefImage
                        , R.mipmap.ic_launcher, R.mipmap.ic_launcher));
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.prefName.setTextColor(context.getResources().getColor(R.color.brand));
            final int sdk1 = android.os.Build.VERSION.SDK_INT;

            if(sdk1 <16) {
                holder.prefLinearLayout.setBackgroundDrawable(shape);
            } else {
                holder.prefLinearLayout.setBackground(shape);
            }
        }else{
            try {
                imageLoader.get(listItems.get(position)[3], ImageLoader.getImageListener(holder.prefImage
                        , R.mipmap.ic_launcher, R.mipmap.ic_launcher));
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.prefName.setTextColor(context.getResources().getColor(R.color.white));
            final int sdk2 = android.os.Build.VERSION.SDK_INT;
            switch (listItems.get(position)[1]){
                case "Fitness":
                    shape.getPaint().setColor(context.getResources().getColor(R.color.col_fitness));
                    break;
                case "Movies":
                    shape.getPaint().setColor(context.getResources().getColor(R.color.col_movies));
                    break;
                case "Sports":
                    shape.getPaint().setColor(context.getResources().getColor(R.color.col_sports));
                    break;
                case "Fashion":
                    shape.getPaint().setColor(context.getResources().getColor(R.color.col_fashion));
                    break;
                case "Music":
                    shape.getPaint().setColor(context.getResources().getColor(R.color.col_music));
                    break;
                case "Outdoor":
                    shape.getPaint().setColor(context.getResources().getColor(R.color.col_outdoor));
                    break;
                case "Books":
                    shape.getPaint().setColor(context.getResources().getColor(R.color.col_books));
                    break;
                case "Parties":
                    shape.getPaint().setColor(context.getResources().getColor(R.color.col_parties));
                    break;
                case "Dance":
                    shape.getPaint().setColor(context.getResources().getColor(R.color.col_dance));
                    break;
                case "Food":
                    shape.getPaint().setColor(context.getResources().getColor(R.color.col_food));
                    break;
            }
            if(sdk2 <16) {
                holder.prefLinearLayout.setBackgroundDrawable(shape);
            } else {
                holder.prefLinearLayout.setBackground(shape);
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
            String[] data = new String[5];
            data[0] = listItems.get(position)[0];
            data[1] = listItems.get(position)[1];
            data[2] = listItems.get(position)[2];
            data[3] = listItems.get(position)[3];
            data[4] = "true";
            listItems.set(position,data);
            Constants.prefernceMap.put(listItems.get(position)[0],listItems.get(position)[1]);
        }else{
            String[] data = new String[5];
            data[0] = listItems.get(position)[0];
            data[1] = listItems.get(position)[1];
            data[2] = listItems.get(position)[2];
            data[3] = listItems.get(position)[3];
            data[4] = "false";
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
