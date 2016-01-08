package com.dsquare.hibour.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dsquare.hibour.R;

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
       // holder.itemView.setSel ected(selectedPos == position);
        holder.prefName.setText(listItems.get(position)[0]);
        if(listItems.get(position)[3].equals("true")){
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.brand));
        }else{
            holder.layout.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


    @Override
    public void onClick(View v) {
        final ViewHolder viewHolder = (ViewHolder)v.getTag();
        final int position = viewHolder.getAdapterPosition();
        if(listItems.get(position)[3].equals("false")){
            String[] data = new String[4];
            data[0] = listItems.get(position)[0];
            data[1] = listItems.get(position)[1];
            data[2] = listItems.get(position)[2];
            data[3] = "true";
            listItems.set(position,data);
        }else{
            String[] data = new String[4];
            data[0] = listItems.get(position)[0];
            data[1] = listItems.get(position)[1];
            data[2] = listItems.get(position)[2];
            data[3] = "false";
            listItems.set(position,data);
        }
        notifyItemChanged(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView prefName;
        private ImageView prefImage;
        private RelativeLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            prefName = (TextView) itemView.findViewById(R.id.pref_name);
            layout = (RelativeLayout)itemView.findViewById(R.id.pref_layout);
        }
    }
}
