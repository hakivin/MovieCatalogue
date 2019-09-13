package com.hakivin.submission3.helper;

import android.database.Cursor;

import com.hakivin.submission3.db.DatabaseContract;
import com.hakivin.submission3.entity.Movie;
import com.hakivin.submission3.entity.TVShow;

import java.util.ArrayList;

public class MappingHelper {
    public static ArrayList<Movie> mapCursorToMovieArrayList(Cursor cursor) {
        ArrayList<Movie> moviesList = new ArrayList<>();
        while (cursor.moveToNext()) {
//            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns._ID));
//            String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.TITLE));
//            String poster = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.POSTER));
//            String backdrop = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.BACKDROP));
//            String score = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.SCORE));
//            String releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.RELEASE_DATE));
//            String overview = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.OVERVIEW));
//            moviesList.add(new Movie(id, title, poster, backdrop, score, releaseDate, overview));
            moviesList.add(new Movie(cursor));
        }
        return moviesList;
    }
    public static ArrayList<TVShow> mapCursorToTVShowArrayList(Cursor cursor) {
        ArrayList<TVShow> moviesList = new ArrayList<>();
        while (cursor.moveToNext()) {
//            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns._ID));
//            String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns.TITLE));
//            String poster = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns.POSTER));
//            String backdrop = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns.BACKDROP));
//            String score = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns.SCORE));
//            String firstAirDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns.FIRST_AIR_DATE));
//            String overview = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVColumns.OVERVIEW));
//            moviesList.add(new TVShow(id, title, poster, backdrop, score, firstAirDate, overview));
            moviesList.add(new TVShow(cursor));
        }
        return moviesList;
    }
}
