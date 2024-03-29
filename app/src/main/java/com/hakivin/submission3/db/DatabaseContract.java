package com.hakivin.submission3.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    static final String AUTHORITY = "com.hakivin.submission3";
    private static final String SCHEME = "content";

    static String TABLE_MOVIE = "movie";
    static String TABLE_TV = "tvshow";

    public static final class MovieColumns implements BaseColumns{
        public static String TITLE = "title";
        public static String POSTER = "poster";
        public static String BACKDROP = "backdrop";
        public static String SCORE = "score";
        public static String RELEASE_DATE = "release_date";
        public static String OVERVIEW = "overview";
    }

    public static final class TVColumns implements BaseColumns{
        public static String TITLE = "title";
        public static String POSTER = "poster";
        public static String BACKDROP = "backdrop";
        public static String SCORE = "score";
        public static String FIRST_AIR_DATE = "first_air_date";
        public static String OVERVIEW = "overview";
    }

    public static final Uri CONTENT_URI_MOVIE = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_MOVIE)
            .build();

    public static final Uri CONTENT_URI_TV = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_TV)
            .build();

    public static String getColumnString(Cursor cursor, String columnName){
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName){
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static long getColumnLong(Cursor cursor, String columnName){
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

}
