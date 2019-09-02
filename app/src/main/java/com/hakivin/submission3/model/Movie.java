package com.hakivin.submission3.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hakivin.submission3.ui.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie implements Parcelable {
    private String title;
    private String poster;
    private String backdrop;
    private String score;
    private String releaseDate;
    private String overview;

    public Movie(JSONObject object){
        String baseUrl = "https://image.tmdb.org/t/p/";
        String imgSize = "w500/";

        try {
            this.title = object.getString("title");
            this.poster = baseUrl+imgSize+object.getString("poster_path");
            this.backdrop = baseUrl+imgSize+object.getString("backdrop_path");
            this.score = String.valueOf(object.getDouble("vote_average"));
            this.releaseDate = object.getString("release_date");
            this.overview = object.getString("overview");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        dest.writeString(this.title);
        dest.writeString(this.poster);
        dest.writeString(this.backdrop);
        dest.writeString(this.score);
        dest.writeString(this.releaseDate);
        dest.writeString(this.overview);
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
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
