package com.hakivin.favouriteapplication.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    private static final String AUTHORITY = "com.hakivin.submission3";
    private static final String SCHEME = "content";

    private static String TABLE_MOVIE = "movie";

    public static final class MovieColumns implements BaseColumns{
        public static String TITLE = "title";
        public static String POSTER = "poster";
        public static String BACKDROP = "backdrop";
        public static String SCORE = "score";
        public static String RELEASE_DATE = "release_date";
        public static String OVERVIEW = "overview";
    }

    public static final Uri CONTENT_URI_MOVIE = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_MOVIE)
            .build();

    public static String getColumnString(Cursor cursor, String columnName){
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName){
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }
}

