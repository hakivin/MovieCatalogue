package com.hakivin.favouriteapplication.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.hakivin.favouriteapplication.db.DatabaseContract;

public class FavouriteData implements Parcelable {
    private int id;
    private String title;
    private String poster;
    private String backdrop;
    private String score;
    private String releaseDate;
    private String overview;

    public FavouriteData() {

    }

    public FavouriteData(Cursor cursor){
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

    private FavouriteData(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.poster = in.readString();
        this.backdrop = in.readString();
        this.score = in.readString();
        this.releaseDate = in.readString();
        this.overview = in.readString();
    }

    public static final Parcelable.Creator<FavouriteData> CREATOR = new Parcelable.Creator<FavouriteData>() {
        @Override
        public FavouriteData createFromParcel(Parcel source) {
            return new FavouriteData(source);
        }

        @Override
        public FavouriteData[] newArray(int size) {
            return new FavouriteData[size];
        }
    };
}