package com.dsquare.hibour.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dsquare.hibour.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aditya Ravikanti on 1/8/2016.
 */
public class ChatingAdapter extends RecyclerView.Adapter<ChatingAdapter.ViewHolder>
        {

    private List<String[]> listItems = new ArrayList<>();
    private Context context;
    private ViewHolder globalHolder;
    private ProgressDialog detailsDialog;

    public ChatingAdapter(Context context, List<String[]> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public ChatingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chating,parent
                ,false);
        final ViewHolder holder = new ViewHolder(v);
        holder.itemView.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.sentmsg.setText(listItems.get(position)[0]);
        holder.sentdate.setText(listItems.get(position)[1]);
        holder.recivemsg.setText(listItems.get(position)[2]);
        holder.recivedate.setText(listItems.get(position)[3]);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView sentmsg,sentdate,recivemsg,recivedate;

        public ViewHolder(View itemView) {
            super(itemView);
            sentmsg = (TextView) itemView.findViewById(R.id.sent_text);
            sentdate = (TextView)itemView.findViewById(R.id.sent_date);
            recivemsg = (TextView)itemView.findViewById(R.id.recived_text);
            recivedate = (TextView)itemView.findViewById(R.id.recived_date);
        }
    }
}
