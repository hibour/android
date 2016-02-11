package com.dsquare.hibour.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dsquare.hibour.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASHOK on 1/8/2016.
 */
public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> implements View.OnClickListener {

    private List<String[]> listItems = new ArrayList<>();
    private Context context;
    private ServicesListInterface servicesListInterface;

    public void setServicesListInterface(ServicesListInterface servicesListInterface) {
        this.servicesListInterface = servicesListInterface;
    }

    public interface ServicesListInterface {

        void itemClicked(int id);

    }

    public ServicesAdapter(Context context, List<String[]> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public ServicesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_services
                ,parent,false);
        final ViewHolder holder = new ViewHolder(v);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.channelName.setText(listItems.get(position)[0]);
        holder.channelDescriptionLine1.setText(listItems.get(position)[1]);
        holder.channelDescriptionLine2.setText(listItems.get(position)[2]);
        holder.channelDescriptionLine1.setVisibility(View.VISIBLE);
        holder.channelDescriptionLine2.setVisibility(View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public void onClick(View view) {
//        Intent groupPostsIntent = new Intent(context, Chat.class);
//        context.startActivity(groupPostsIntent);
        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        final int id = viewHolder.getAdapterPosition();

        if(servicesListInterface != null) {
            servicesListInterface.itemClicked(id);
        } else {
            Toast.makeText(view.getContext(), "no click listener", Toast.LENGTH_SHORT).show();

        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView channelName, channelDescriptionLine1, channelDescriptionLine2;
        private ImageView channelImage;

        public ViewHolder(View itemView) {
            super(itemView);
            channelName = (TextView) itemView.findViewById(R.id.adapter_channels_name);
            channelImage = (ImageView) itemView.findViewById(R.id.adapter_channels_image);
            channelDescriptionLine1 = (TextView)itemView.findViewById(R.id.adapter_channels_description_line_1);
            channelDescriptionLine2 = (TextView) itemView.findViewById(R.id.adapter_channels_user_profession_line_2);

        }
    }


}
