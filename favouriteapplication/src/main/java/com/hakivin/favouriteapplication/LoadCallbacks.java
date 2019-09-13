package com.hakivin.favouriteapplication;

import android.database.Cursor;

public interface LoadCallbacks {
    void postExecute(Cursor cursor);
}
