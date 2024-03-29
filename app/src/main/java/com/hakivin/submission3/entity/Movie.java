package com.hakivin.submission3.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.hakivin.submission3.db.DatabaseContract;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie implements Parcelable {
    private int id;
    private String title;
    private String poster;
    private String backdrop;
    private String score;
    private String releaseDate;
    private String overview;

    public Movie(JSONObject object) {
        String baseUrl = "https://image.tmdb.org/t/p/";
        String imgSize = "w500/";
        try {
            this.id = object.getInt("id");
            this.title = object.getString("title");
            this.poster = baseUrl + imgSize + object.getString("poster_path");
            this.backdrop = baseUrl + imgSize + object.getString("backdrop_path");
            this.score = String.valueOf(object.getDouble("vote_average"));
            this.releaseDate = object.getString("release_date");
            this.overview = object.getString("overview");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Movie() {

    }

    public Movie(int id, String title, String poster, String backdrop, String score, String releaseDate, String overview) {
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.backdrop = backdrop;
        this.score = score;
        this.releaseDate = releaseDate;
        this.overview = overview;
    }

    public Movie(Cursor cursor){
        this.id = DatabaseContract.getColumnInt(cursor, DatabaseContract.MovieColumns._ID);
        this.title = DatabaseContract.getColumnString(cursor, DatabaseContract.MovieColumns.TITLE);
        this.poster = DatabaseContract.getColumnString(cursor, DatabaseContract.MovieColumns.POSTER);
        this.backdrop = DatabaseContract.getColumnString(cursor, DatabaseContract.MovieColumns.BACKDROP);
        this.score = DatabaseContract.getColumnString(cursor, DatabaseContract.MovieColumns.SCORE);
        this.releaseDate = DatabaseContract.getColumnString(cursor, DatabaseContract.MovieColumns.RELEASE_DATE);
        this.overview = DatabaseContract.getColumnString(cursor, DatabaseContract.MovieColumns.OVERVIEW);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.poster);
        dest.writeString(this.backdrop);
        dest.writeString(this.score);
        dest.writeString(this.releaseDate);
        dest.writeString(this.overview);
    }

    private Movie(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.poster = in.readString();
        this.backdrop = in.readString();
        this.score = in.readString();
        this.releaseDate = in.readString();
        this.overview = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}