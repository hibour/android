package com.dsquare.hibour.adapters;

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
 * Created by Dsquare Android on 2/3/2016.
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder>{

    private List<String> categories = new ArrayList<>();
    private Context context;

    public CategoriesAdapter(Context context,List<String> comments) {
        this.context = context;
        this.categories = comments;
    }

    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_categorys
                ,parent,false);
        final ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.categoryName.setText(categories.get(position));
        //holder.categoryName.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryName;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryName = (TextView) itemView.findViewById(R.id.adapter_categories_item_name);
        }
    }
}
