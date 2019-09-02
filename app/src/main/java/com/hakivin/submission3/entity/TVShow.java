package com.hakivin.submission3.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class TVShow implements Parcelable {

    private int id;
    private String title, poster, backdrop, score, firstAirDate, overview;

    public TVShow(JSONObject object) {
        String baseUrl = "https://image.tmdb.org/t/p/";
        String imgSize = "w500/";
        try {
            this.id = object.getInt("id");
            this.title = object.getString("original_name");
            this.poster = baseUrl+imgSize+object.getString("poster_path");
            this.backdrop = baseUrl+imgSize+object.getString("backdrop_path");
            this.score = String.valueOf(object.getDouble("vote_average"));
            this.firstAirDate = object.getString("first_air_date");
            this.overview = object.getString("overview");
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
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
        dest.writeString(this.firstAirDate);
        dest.writeString(this.overview);
    }

    protected TVShow(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.poster = in.readString();
        this.backdrop = in.readString();
        this.score = in.readString();
        this.firstAirDate = in.readString();
        this.overview = in.readString();
    }

    public static final Parcelable.Creator<TVShow> CREATOR = new Parcelable.Creator<TVShow>() {
        @Override
        public TVShow createFromParcel(Parcel source) {
            return new TVShow(source);
        }

        @Override
        public TVShow[] newArray(int size) {
            return new TVShow[size];
        }
    };
}
