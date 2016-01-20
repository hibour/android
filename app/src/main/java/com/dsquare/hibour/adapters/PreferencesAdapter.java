package com.dsquare.hibour.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.pojos.preference.Datum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dsquare Android on 1/8/2016.
 */
public class PreferencesAdapter extends RecyclerView.Adapter<PreferencesAdapter.ViewHolder>
        implements View.OnClickListener{

    private List<String[]> listItems = new ArrayList<>();
    private Context context;
    private ViewHolder globalHolder;
    private ProgressDialog detailsDialog;
    private int selectedPos = 0;
    private List<Datum> data;

    public PreferencesAdapter(Context context,List<String[]> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public PreferencesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        if(listItems.get(position)[4].equals("false")){
            String name = listItems.get(position)[2];
            int id = context.getResources().getIdentifier(name, "mipmap", context.getPackageName());
            Drawable drawable = context.getResources().getDrawable(id);
            holder.prefImage.setImageDrawable(drawable);
        }else{
            String name = listItems.get(position)[3];
            int id = context.getResources().getIdentifier(name, "mipmap", context.getPackageName());
            Drawable drawable = context.getResources().getDrawable(id);
            holder.prefImage.setImageDrawable(drawable);
            holder.prefName.setTextColor(context.getResources().getColor(R.color.white));
           // holder.prefLinearLayout.setBackgroundDrawable(context.getResources()
             //       .getDrawable(R.drawable.social_prefs_selected_state));
           // GradientDrawable bgShape = (GradientDrawable)holder.prefLinearLayout.getBackground();
          //  bgShape.setColor(Color.BLACK);
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk <16) {
                holder.prefLinearLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.social_prefs_selected_state));
            } else {
                holder.prefLinearLayout.setBackground(context.getResources().getDrawable(R.drawable.social_prefs_selected_state));
            }
        }
        holder.prefName.setText(listItems.get(position)[0]);
       /* if(listItems.get(position)[3].equals("true")){
            holder.prefLinearLayout.setBackgroundResource(R.drawable.social_prefs_selected_state);
            holder.prefName.setTextColor(context.getResources().getColor(R.color.white));
           // holder.prefImage.setColorFilter(context.getResources().getColor(R.color.white));
        }else{
            holder.prefLinearLayout.setBackgroundResource(R.drawable.social_prefs_unselected_state);
        }*/
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
        }else{
            String[] data = new String[5];
            data[0] = listItems.get(position)[0];
            data[1] = listItems.get(position)[1];
            data[2] = listItems.get(position)[2];
            data[3] = listItems.get(position)[3];
            data[4] = "false";
            listItems.set(position,data);
        }
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
