package com.hakivin.submission3.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hakivin.submission3.entity.Movie;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;

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
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
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
        args.put(_ID, movie.getId());
        args.put(DatabaseContract.MovieColumns.TITLE, movie.getTitle());
        args.put(DatabaseContract.MovieColumns.POSTER, movie.getPoster());
        args.put(DatabaseContract.MovieColumns.BACKDROP, movie.getBackdrop());
        args.put(DatabaseContract.MovieColumns.SCORE, movie.getScore());
        args.put(DatabaseContract.MovieColumns.RELEASE_DATE, movie.getReleaseDate());
        args.put(DatabaseContract.MovieColumns.OVERVIEW, movie.getOverview());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public int deleteMovie(int id){
        return database.delete(DATABASE_TABLE, _ID + " = '" + id + "'", null);
    }

    public void open() throws SQLException{
        database = databaseHelper.getWritableDatabase();
    }

    public void close(){
        databaseHelper.close();

        if (database.isOpen())
            database.close();
    }

    Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE, null
                , _ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    Cursor queryProvider() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , _ID + " ASC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, _ID + " = ?", new String[]{id});
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }
}
