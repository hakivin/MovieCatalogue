package com.hakivin.submission3.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hakivin.submission3.entity.TVShow;

import java.util.ArrayList;

public class TVShowHelper {
    private static final String DATABASE_TABLE = DatabaseContract.TABLE_TV;
    private static DatabaseHelper databaseHelper;
    private static TVShowHelper INSTANCE;

    private static SQLiteDatabase database;

    private TVShowHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static TVShowHelper getInstance(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new TVShowHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public ArrayList<TVShow> getAllTVShows(){
        ArrayList<TVShow> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null, null, null, null, null, null, null);
        cursor.moveToFirst();
        TVShow tvShow;
        if (cursor.getCount() > 0){
            do {
                tvShow = new TVShow();
                tvShow.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns._ID)));
                tvShow.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.TITLE)));
                tvShow.setPoster(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.POSTER)));
                tvShow.setBackdrop(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.BACKDROP)));
                tvShow.setScore(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.SCORE)));
                tvShow.setFirstAirDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.RELEASE_DATE)));
                tvShow.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.OVERVIEW)));
                arrayList.add(tvShow);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insertMovie(TVShow tvShow){
        ContentValues args = new ContentValues();
        args.put(DatabaseContract.TVColumns._ID, tvShow.getId());
        args.put(DatabaseContract.TVColumns.TITLE, tvShow.getTitle());
        args.put(DatabaseContract.TVColumns.POSTER, tvShow.getPoster());
        args.put(DatabaseContract.TVColumns.BACKDROP, tvShow.getBackdrop());
        args.put(DatabaseContract.TVColumns.SCORE, tvShow.getScore());
        args.put(DatabaseContract.TVColumns.FIRST_AIR_DATE, tvShow.getFirstAirDate());
        args.put(DatabaseContract.TVColumns.OVERVIEW, tvShow.getOverview());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public int deleteMovie(int id){
        return database.delete(DATABASE_TABLE, DatabaseContract.TVColumns._ID + " = '" + id + "'", null);
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close(){
        databaseHelper.close();

        if (database.isOpen())
            database.close();
    }
}
