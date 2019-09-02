package com.hakivin.submission3.db;

import android.provider.BaseColumns;

public class DatabaseContract {
    static String TABLE_MOVIE = "movie";
    static String TABLE_TV = "tvshow";

    static final class MovieColumns implements BaseColumns{

        static String TITLE = "title";
        static String POSTER = "poster";
        static String BACKDROP = "backdrop";
        static String SCORE = "score";
        static String RELEASE_DATE = "release_date";
        static String OVERVIEW = "overview";

    }

    static final class TVColumns implements BaseColumns{
        static String TITLE = "title";
        static String POSTER = "poster";
        static String BACKDROP = "backdrop";
        static String SCORE = "score";
        static String FIRST_AIR_DATE = "first_air_date";
        static String OVERVIEW = "overview";
    }
}
