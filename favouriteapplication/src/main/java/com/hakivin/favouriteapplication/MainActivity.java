package com.hakivin.favouriteapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;

import com.hakivin.favouriteapplication.adapter.FavouriteAdapter;
import com.hakivin.favouriteapplication.db.DatabaseContract;
import com.hakivin.favouriteapplication.entity.FavouriteData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoadCallbacks{
    private FavouriteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.rv_fav);
        adapter = new FavouriteAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver observer = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(DatabaseContract.CONTENT_URI_MOVIE, true, observer);
        new getData(this, this).execute();
    }

    @Override
    public void postExecute(Cursor cursor) {
        ArrayList<FavouriteData> list = MappingHelper.mapCursorToMovieArrayList(cursor);
        if (list.size() > 0){
            adapter.setList(list);
        } else {
            adapter.setList(new ArrayList<FavouriteData>());
        }
    }

    static class DataObserver extends ContentObserver {
        final Context context;
        DataObserver(Handler handler, Context context){
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new getData(context, (MainActivity) context).execute();
        }
    }

    private static class getData extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadCallbacks> weakCallback;

        private getData(Context context, LoadCallbacks callbacks) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callbacks);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return weakContext.get().getContentResolver().query(DatabaseContract.CONTENT_URI_MOVIE, null, null, null, null);
        }
        @Override
        protected void onPostExecute(Cursor data) {
            super.onPostExecute(data);
            weakCallback.get().postExecute(data);
        }
    }
}
