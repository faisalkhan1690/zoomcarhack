package com.faisal.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by faisal khan on 9/13/2015.
 */
public class CarListAdapter extends BaseAdapter {

    private  LayoutInflater inflater;
    private  Context context;
    private List<CarModel> list;

    public CarListAdapter(List<CarModel> list, Context context){
        this.list=list;
        this.context=context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CarModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.listitem, null);
        TextView tvCarName = (TextView) convertView.findViewById(R.id.tv_car_name);
        TextView tvPerHour = (TextView) convertView.findViewById(R.id.tv_per_hour);
        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
        ImageView imgCar = (ImageView) convertView.findViewById(R.id.img_car);


        tvCarName.setText(list.get(position).getName());
        tvPerHour.setText(list.get(position).getHourly_rate()+" per hour");
        ratingBar.setNumStars(5);
        ratingBar.setRating(list.get(position).getRating());

        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.blank)
                .showImageOnLoading(R.drawable.blank).build();


        imageLoader.displayImage(list.get(position).getImage(), imgCar, options);

        return convertView;
    }
}
