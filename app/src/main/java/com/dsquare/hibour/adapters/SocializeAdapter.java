package com.dsquare.hibour.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
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
    private int numColumns;
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
        int listViewItemType = getItemViewType(position);
        Log.d("size", listViewItemType + "");
        boolean isLast = (position % listViewItemType) == 0;
        Log.d("islast", isLast + "");

        holder.prefCount.setVisibility(View.VISIBLE);
        Drawable bgShape = holder.prefLinearLayout.getBackground().mutate();
        Log.d("name",listItems.get(position)[1]);
        switch (listItems.get(position)[1]){
            case "Fitness":
                holder.topLayout.setBackgroundColor(context.getResources().getColor(R.color.col_fitness));
                break;
            case "Movies":
                holder.topLayout.setBackgroundColor(context.getResources().getColor(R.color.col_movies));
                break;
            case "Sports":
                holder.topLayout.setBackgroundColor(context.getResources().getColor(R.color.col_sports));
                Log.d("sports", "yes");
              //  bgShape.getPaint().setColor(context.getResources().getColor(R.color.col_sports));
                break;
            case "Fashion":
                if (bgShape instanceof ShapeDrawable) {
                    Log.d("drawable","shape");
                    ((ShapeDrawable)bgShape).getPaint().setColor(context.getResources().getColor(R.color.col_fashion));
                } else if (bgShape instanceof GradientDrawable) {
                    Log.d("drawable","gradient");
                    ((GradientDrawable)bgShape).setColor(Color.BLACK);
                }
               // bgShape.getPaint().setColor(context.getResources().getColor(R.color.col_fashion));
                break;
            case "Music":
                if (bgShape instanceof ShapeDrawable) {
                    ((ShapeDrawable)bgShape).getPaint().setColor(context.getResources().getColor(R.color.col_music));
                } else if (bgShape instanceof GradientDrawable) {
                    ((GradientDrawable)bgShape).setColor(0xff999999);
                    bgShape.invalidateSelf();
                }
               // bgShape.getPaint().setColor(context.getResources().getColor(R.color.col_music));
                break;
            case "Outdoor":
                if (bgShape instanceof ShapeDrawable) {
                    ((ShapeDrawable)bgShape).getPaint().setColor(context.getResources().getColor(R.color.col_outdoor));
                } else if (bgShape instanceof GradientDrawable) {
                    ((GradientDrawable)bgShape).setColor(0xff999999);
                }
               // bgShape.getPaint().setColor(context.getResources().getColor(R.color.col_outdoor));
                break;
            case "Books":
                if (bgShape instanceof ShapeDrawable) {
                    ((ShapeDrawable)bgShape).getPaint().setColor(context.getResources().getColor(R.color.col_books));
                } else if (bgShape instanceof GradientDrawable) {
                    ((GradientDrawable)bgShape).setColor(0xff999999);
                }
               // bgShape.getPaint().setColor(context.getResources().getColor(R.color.col_books));
                break;
            case "Parties":
                if (bgShape instanceof ShapeDrawable) {
                    ((ShapeDrawable)bgShape).getPaint().setColor(context.getResources().getColor(R.color.col_parties));
                } else if (bgShape instanceof GradientDrawable) {
                    ((GradientDrawable)bgShape).setColor(0xff999999);
                }
                //bgShape.getPaint().setColor(context.getResources().getColor(R.color.col_parties));
                break;
            case "Dance":
                if (bgShape instanceof ShapeDrawable) {
                    ((ShapeDrawable)bgShape).getPaint().setColor(context.getResources().getColor(R.color.col_dance));
                } else if (bgShape instanceof GradientDrawable) {
                    ((GradientDrawable)bgShape).setColor(0xff999999);
                }
               // bgShape.getPaint().setColor(context.getResources().getColor(R.color.col_dance));
                break;
            case "Food":
                if (bgShape instanceof ShapeDrawable) {
                    ((ShapeDrawable)bgShape).getPaint().setColor(context.getResources().getColor(R.color.col_food));
                } else if (bgShape instanceof GradientDrawable) {
                    ((GradientDrawable)bgShape).setColor(0xff999999);
                }
              //  bgShape.getPaint().setColor(context.getResources().getColor(R.color.col_food));
                break;
        }
        if(listItems.get(position)[4].equals("false")){
            try {
                imageLoader.get(listItems.get(position)[2], ImageLoader.getImageListener(holder.prefImage
                        , R.mipmap.ic_launcher, R.mipmap.ic_launcher));
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.prefName.setTextColor(context.getResources().getColor(R.color.brand));
            holder.prefCount.setTextColor(context.getResources().getColor(R.color.brand));
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
            holder.prefCount.setTextColor(context.getResources().getColor(R.color.white));
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
        holder.prefCount.setText(listItems.get(position)[5]);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


    @Override
    public void onClick(View v) {

        final ViewHolder viewHolder = (ViewHolder)v.getTag();
        final int position = viewHolder.getAdapterPosition();
        openPrefsActivity(listItems.get(position)[0], listItems.get(position)[1]);
    }

    /* open home activity*/
    private void openPrefsActivity(String id, String name) {
        Intent prefIntent = new Intent(context, com.dsquare.hibour.activities.PreferencesViews.class);
        prefIntent.putExtra("frmAdapter", true);
        prefIntent.putExtra("prefId", id);
        prefIntent.putExtra("prefName", name);
        context.startActivity(prefIntent);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView prefImage;
        private TextView prefName, prefCount;
        private RelativeLayout layout;
        private LinearLayout prefLinearLayout;
        private RelativeLayout topLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            prefName = (TextView) itemView.findViewById(R.id.pref_name);
            prefImage = (ImageView) itemView.findViewById(R.id.pref_icon);
            layout = (RelativeLayout)itemView.findViewById(R.id.pref_layout);
            prefLinearLayout = (LinearLayout)itemView.findViewById(R.id.pref_linear_layout);
            prefCount = (TextView)itemView.findViewById(R.id.pref_count);
            topLayout = (RelativeLayout)itemView.findViewById(R.id.pref_layout);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        int mod = position % 3;
          if(position == 0 || position == 1 || position == 2)
            return 3;
          else if(mod == 0 || mod == 1 || mod == 2)
            return 3;
          else if(mod == 0 || mod == 1)
            return 2;
          else if(mod == 0);
            return 1;
   }
}
