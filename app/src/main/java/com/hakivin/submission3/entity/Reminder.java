package com.hakivin.submission3.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Reminder implements Parcelable {
    private boolean daily;
    private boolean release;

    public boolean getDaily() {
        return daily;
    }

    public void setDaily(boolean daily) {
        this.daily = daily;
    }

    public boolean getRelease() {
        return release;
    }

    public void setRelease(boolean release) {
        this.release = release;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.daily ? (byte) 1 : (byte) 0);
        dest.writeByte(this.release ? (byte) 1 : (byte) 0);
    }

    public Reminder() {
    }

    protected Reminder(Parcel in) {
        this.daily = in.readByte() != 0;
        this.release = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Reminder> CREATOR = new Parcelable.Creator<Reminder>() {
        @Override
        public Reminder createFromParcel(Parcel source) {
            return new Reminder(source);
        }

        @Override
        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };
}
