package carddata;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import com.farid.mohammed.culturewheel.R;


/**
 * Created by Mohammed on 28/10/2016.
 */

public class DataAdapterCard extends RecyclerView.Adapter<DataAdapterCard.ViewHolder> {
    private Context mContext;
    private ArrayList<Category> categories;
    public DataAdapterCard(Context mContext,ArrayList<Category> categories){
        this.mContext=mContext;
        this.categories = categories;
    }
    @Override
    public DataAdapterCard.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DataAdapterCard.ViewHolder viewHolder, int position) {
        Category category = categories.get(position);
        viewHolder.tv_days.setText(category.getTvDay());
        viewHolder.tv_month.setText(category.getTvMonth());
        // loading album cover using Glide library
        Glide.with(mContext).load(category.getThumbnailsEvent()).into(viewHolder.thumbnail);
        viewHolder.thumbnail.setBackgroundResource(category.getThumbnailsEvent());
        Glide.with(mContext).load(category.getOverFlow()).into(viewHolder.overFlow);
        viewHolder.overFlow.setBackgroundResource(category.getOverFlow());

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView thumbnail,overFlow;
        private TextView tv_days,tv_month;
        public ViewHolder(View view) {
            super(view);
            tv_days = (TextView) view.findViewById(R.id.day_tv);
            tv_month = (TextView) view.findViewById(R.id.month_tv);
            overFlow = (ImageView) view.findViewById(R.id.overflow);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }

}
