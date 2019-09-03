package com.hakivin.submission3.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.hakivin.submission3.entity.Movie;
import com.hakivin.submission3.ui.main.MainActivity;

import java.util.ArrayList;

public class MovieHelper {
    private static final String DATABASE_TABLE = DatabaseContract.TABLE_MOVIE;
    private static DatabaseHelper databaseHelper;
    private static MovieHelper INSTANCE;

    private static SQLiteDatabase database;

    private MovieHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static MovieHelper getInstance(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new MovieHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public ArrayList<Movie> getAllMovies(){
        ArrayList<Movie> arrayList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + DATABASE_TABLE + ";", null);
        Movie movie;
        Log.d(MovieHelper.class.getSimpleName(), String.valueOf(cursor.getCount()));
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

    public long insertMovie(Movie movie){
        ContentValues args = new ContentValues();
        args.put(DatabaseContract.MovieColumns._ID, movie.getId());
        args.put(DatabaseContract.MovieColumns.TITLE, movie.getTitle());
        args.put(DatabaseContract.MovieColumns.POSTER, movie.getPoster());
        args.put(DatabaseContract.MovieColumns.BACKDROP, movie.getBackdrop());
        args.put(DatabaseContract.MovieColumns.SCORE, movie.getScore());
        args.put(DatabaseContract.MovieColumns.RELEASE_DATE, movie.getReleaseDate());
        args.put(DatabaseContract.MovieColumns.OVERVIEW, movie.getOverview());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public int deleteMovie(int id){
        return database.delete(DATABASE_TABLE, DatabaseContract.MovieColumns._ID + " = '" + id + "'", null);
    }

    public void open() throws SQLException{
        database = databaseHelper.getWritableDatabase();
    }

    public void close(){
        databaseHelper.close();

        if (database.isOpen())
            database.close();
    }
}
