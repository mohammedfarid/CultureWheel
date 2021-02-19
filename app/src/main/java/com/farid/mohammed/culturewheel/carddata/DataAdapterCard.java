package com.farid.mohammed.culturewheel.carddata;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.farid.mohammed.culturewheel.R;

import java.util.ArrayList;


/**
 * Created by Mohammed on 28/10/2016.
 */

public class DataAdapterCard extends RecyclerView.Adapter<DataAdapterCard.ViewHolder> {
    Context mContext;
    ArrayList<Categories> categories;

    public DataAdapterCard(Context mContext, ArrayList<Categories> categories) {
        this.mContext = mContext;
        this.categories = categories;
    }

    @Override
    public DataAdapterCard.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DataAdapterCard.ViewHolder viewHolder, int position) {
        Categories categories = this.categories.get(position);
        viewHolder.tv_days.setText(categories.getTvDay());
        viewHolder.tv_month.setText(categories.getTvMonth());
        // loading album cover using Glide library
        Glide.with(mContext).load(categories.getThumbnailsEvent()).into(viewHolder.thumbnail);
        viewHolder.thumbnail.setBackgroundResource(categories.getThumbnailsEvent());
        Glide.with(mContext).load(categories.getOverFlow()).into(viewHolder.overFlow);
        viewHolder.overFlow.setBackgroundResource(categories.getOverFlow());

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnail, overFlow;
        private TextView tv_days, tv_month;

        public ViewHolder(View view) {
            super(view);
            tv_days = (TextView) view.findViewById(R.id.day_tv);
            tv_month = (TextView) view.findViewById(R.id.month_tv);
            overFlow = (ImageView) view.findViewById(R.id.overflow);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }

}
