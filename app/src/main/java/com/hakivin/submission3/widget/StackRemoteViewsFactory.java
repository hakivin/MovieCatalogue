package com.hakivin.submission3.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.hakivin.submission3.R;
import com.hakivin.submission3.db.WidgetHelper;
import com.hakivin.submission3.entity.Movie;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final List<Bitmap> mWidgetItems = new ArrayList<>();
    private final Context mContext;
    private WidgetHelper helper;

    StackRemoteViewsFactory(Context context) {
        mContext = context;
        helper = WidgetHelper.getInstance(mContext);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        helper.open();
        ArrayList<Movie> list = helper.getAllMovies();
        for (Movie movie : list){
            try {
                mWidgetItems.add(BitmapFactory.decodeStream(new java.net.URL(movie.getBackdrop()).openStream()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setImageViewBitmap(R.id.img_widget, mWidgetItems.get(i));

        Bundle extras = new Bundle();
        extras.putInt(FavouriteWidget.EXTRA_ITEM, i);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.img_widget, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
