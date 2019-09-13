package com.hakivin.favouriteapplication;

import android.database.Cursor;

import com.hakivin.favouriteapplication.entity.FavouriteData;

import java.util.ArrayList;

class MappingHelper {
    static ArrayList<FavouriteData> mapCursorToMovieArrayList(Cursor cursor) {
        ArrayList<FavouriteData> moviesList = new ArrayList<>();
        while (cursor.moveToNext()) {
            moviesList.add(new FavouriteData(cursor));
        }
        return moviesList;
    }
}
