package com.dsquare.hibour.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dsquare.hibour.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dsquare Android on 2/2/2016.
 */
public class CategoriesRecycler extends RecyclerView.Adapter<CategoriesRecycler.ViewHolder> implements View.OnClickListener {

    private List<String[]> listItems = new ArrayList<>();
    private Context context;
    private String[] bgColors;
    public CategoriesRecycler(Context context,List<String[]> listItems) {
        this.context = context;
        this.listItems = listItems;
        bgColors = context.getApplicationContext().getResources().getStringArray(R.array.movie_serial_bg);
    }

    @Override
    public CategoriesRecycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_categorys
                ,parent,false);
        final ViewHolder holder = new ViewHolder(v);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.userName.setText(listItems.get(position)[0]);
        String color = bgColors[position % bgColors.length];
        holder.userName.setTextColor(Color.parseColor(color));
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public void onClick(View view) {


//        ImageView cancel = (ImageView) this.getActivity().findViewById(R.id.home_new_post);

//        ViewGroup.LayoutParams lp = postFragment.getLayoutParams();
//        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
//        postFragment.setGravity(Gravity.TOP);
//        postFragment.setLayoutParams(lp);
//
//        categoryList.setVisibility(View.GONE);
//        postWidget.setVisibility(View.VISIBLE);
//        relativeLayout.setVisibility(View.VISIBLE);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userName,profession;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.adapter_categories_item_name);
            LinearLayout postFragment = (LinearLayout) this.itemView.findViewById(R.id.post_fragment);
            LinearLayout postWidget = (LinearLayout) this.itemView.findViewById(R.id.post_widget);
            RelativeLayout categoryList = (RelativeLayout) this.itemView.findViewById(R.id.category_list);
           // LinearLayout relativeLayout = (LinearLayout) this.itemView.findViewById(R.id.post_liner_layout);
        }
    }
}
