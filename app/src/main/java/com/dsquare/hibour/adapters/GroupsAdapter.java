package com.dsquare.hibour.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.activities.GroupPosts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dsquare Android on 1/9/2016.
 */
public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder>
        implements View.OnClickListener{

    private List<String[]> listItems = new ArrayList<>();
    private Context context;
    private ViewHolder globalHolder;
    private ProgressDialog detailsDialog;

    public GroupsAdapter(Context context,List<String[]> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public GroupsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_groups
                ,parent,false);
        final ViewHolder holder = new ViewHolder(v);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.groupName.setText(listItems.get(position)[0]);
        holder.groupMembers.setText(listItems.get(position)[1]);
        holder.groupDesc.setText(listItems.get(position)[2]);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView groupName,groupMembers,groupDesc;
        private ImageView userImage;

        public ViewHolder(View itemView) {
            super(itemView);
            groupName = (TextView) itemView.findViewById(R.id.adapter_group_name);
            groupMembers = (TextView)itemView.findViewById(R.id.adapter_group_members);
            groupDesc = (TextView)itemView.findViewById(R.id.adapter_group_description);

        }
    }

    @Override
    public void onClick(View view) {
        Intent groupPostsIntent = new Intent(context, GroupPosts.class);
        context.startActivity(groupPostsIntent);
    }
}
