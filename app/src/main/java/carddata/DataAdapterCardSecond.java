package carddata;

import android.content.Context;
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

import com.farid.mohammed.culturewheel.R;

import java.util.ArrayList;


/**
 * Created by Mohammed on 28/10/2016.
 */

public class DataAdapterCardSecond extends RecyclerView.Adapter<DataAdapterCardSecond.ViewHolder> {
    private Context mContext;
    private ArrayList<CategorySecond> categories;
    public DataAdapterCardSecond(Context mContext, ArrayList<CategorySecond> categories){
        this.mContext=mContext;
        this.categories = categories;
    }
    @Override
    public DataAdapterCardSecond.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row_second, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DataAdapterCardSecond.ViewHolder viewHolder, int position) {
        CategorySecond category = categories.get(position);
        //viewHolder.tv_days.setText(category.getTvDay());
        //viewHolder.tv_month.setText(category.getTvMonth());
        viewHolder.tv_title.setText(category.getTitleEvent());
        viewHolder.tv_date.setText(category.getDateEvent());
        viewHolder.tv_cat.setText(category.getCategoryName());
        // loading album cover using Glide library
        //Glide.with(mContext).load(category.getThumbnailsEvent()).into(viewHolder.thumbnail);
        //viewHolder.thumbnail.setBackgroundResource(category.getThumbnailsEvent());
        viewHolder.thumbnail.setImageBitmap(category.getThumbnailsEventBitmap());

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_title;
        private TextView tv_cat;
        private TextView tv_date;
        private ImageView thumbnail,overflow;
        private TextView tv_days,tv_month;
        public ViewHolder(View view) {
            super(view);
            tv_days = (TextView) view.findViewById(R.id.day_tv);
            tv_month = (TextView) view.findViewById(R.id.month_tv);
            tv_title = (TextView)view.findViewById(R.id.title);
            tv_cat = (TextView)view.findViewById(R.id.cat);
            tv_date =(TextView)view.findViewById(R.id.date);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


}
