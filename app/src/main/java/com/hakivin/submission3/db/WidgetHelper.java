package com.hakivin.submission3.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hakivin.submission3.entity.Movie;
import com.hakivin.submission3.entity.TVShow;

import java.util.ArrayList;

public class WidgetHelper {
    private static final String MOVIE_TABLE = DatabaseContract.TABLE_MOVIE;
    private static final String TV_TABLE = DatabaseContract.TABLE_TV;

    private static DatabaseHelper databaseHelper;
    private static WidgetHelper INSTANCE;

    private static SQLiteDatabase database;

    private WidgetHelper(Context context){
        databaseHelper = new DatabaseHelper(context);
    }

    public static WidgetHelper getInstance(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new WidgetHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public ArrayList<Movie> getAllMovies(){
        ArrayList<Movie> arrayList = new ArrayList<>();
        Cursor cursor = database.query(MOVIE_TABLE, null, null, null, null, null, null, null);
        Movie movie;
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns._ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.TITLE)));
                movie.setPoster(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.POSTER)));
                movie.setBackdrop(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.BACKDROP)));
                movie.setScore(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.SCORE)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.RELEASE_DATE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.OVERVIEW)));
                arrayList.add(movie);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public ArrayList<TVShow> getAllTVShows(){
        ArrayList<TVShow> arrayList = new ArrayList<>();
        Cursor cursor = database.query(TV_TABLE, null, null, null, null, null, null, null);
        cursor.moveToFirst();
        TVShow tvShow;
        if (cursor.getCount() > 0){
            do {
                tvShow = new TVShow();
                tvShow.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns._ID)));
                tvShow.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns.TITLE)));
                tvShow.setPoster(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns.POSTER)));
                tvShow.setBackdrop(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns.BACKDROP)));
                tvShow.setScore(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns.SCORE)));
                tvShow.setFirstAirDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns.FIRST_AIR_DATE)));
                tvShow.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns.OVERVIEW)));
                arrayList.add(tvShow);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
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
