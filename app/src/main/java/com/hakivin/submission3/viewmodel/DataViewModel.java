package com.hakivin.submission3.viewmodel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hakivin.submission3.BuildConfig;
import com.hakivin.submission3.R;
import com.hakivin.submission3.db.MovieHelper;
import com.hakivin.submission3.db.TVShowHelper;
import com.hakivin.submission3.entity.Movie;
import com.hakivin.submission3.entity.TVShow;
import com.hakivin.submission3.ui.main.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class DataViewModel extends ViewModel {
    private static final String API = BuildConfig.TMDB_API_KEY;
    private static final String NULL_CONDITION = BuildConfig.IMG_URL_NULL;

    private MutableLiveData<ArrayList<Movie>> listMovies = new MutableLiveData<>();
    private MutableLiveData<ArrayList<TVShow>> listTV = new MutableLiveData<>();

    public LiveData<ArrayList<Movie>> getMovies(){
        return listMovies;
    }
    public LiveData<ArrayList<TVShow>> getTVShows(){ return listTV; }

    public void setMovies(){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> list = new ArrayList<>();
        Locale locale = MainActivity.getContext().getResources().getConfiguration().locale;
        String lang = locale.toLanguageTag();
        String url = BuildConfig.MOVIE_URL+API+"&language="+lang;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray res = responseObject.getJSONArray("results");
                    for (int i = 0; i < res.length(); i++){
                        Movie movie = new Movie(res.getJSONObject(i));
                        if (!movie.getBackdrop().equals(NULL_CONDITION) && !movie.getScore().equals("0.0")) //karena ada movie yang backdropnya null
                            list.add(movie);
                    }
                    listMovies.postValue(list);
                } catch (Exception e){
                    Log.d("Exception", Objects.requireNonNull(e.getMessage()));
                    Toast.makeText(MainActivity.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    listMovies.postValue(list);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", Objects.requireNonNull(error.getMessage()));
                Toast.makeText(MainActivity.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                listMovies.postValue(list);
            }
        });
    }

    public void setMoviesFromSearch(String query){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> list = new ArrayList<>();
        Locale locale = MainActivity.getContext().getResources().getConfiguration().locale;
        String lang = locale.toLanguageTag();
        String url = BuildConfig.MOVIE_SEARCH_URL+API+"&language="+lang+"&query="+query;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray res = responseObject.getJSONArray("results");
                    for (int i = 0; i < res.length(); i++){
                        Movie movie = new Movie(res.getJSONObject(i));
                        if (!movie.getBackdrop().equals(NULL_CONDITION)) //karena ada movie yang backdropnya null
                            list.add(movie);
                    }
                    listMovies.postValue(list);
                } catch (Exception e) {
                    Log.d("Exception", Objects.requireNonNull(e.getMessage()));
                    Toast.makeText(MainActivity.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    listMovies.postValue(list);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", Objects.requireNonNull(error.getMessage()));
                Toast.makeText(MainActivity.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                listMovies.postValue(list);
            }
        });
    }

    public void setEmptyMovie(){
        ArrayList<Movie> list = new ArrayList<>();
        listMovies.postValue(list);
    }

    public void setMoviesFromDB(Context context){
        MovieHelper helper = MovieHelper.getInstance(context);
        helper.open();
        ArrayList<Movie> list = helper.getAllMovies();
        listMovies.postValue(list);
        if (list.isEmpty())
            Toast.makeText(context, context.getString(R.string.empty_list), Toast.LENGTH_LONG).show();
    }

    public void setTVShows(){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TVShow> list = new ArrayList<>();
        Locale locale = MainActivity.getContext().getResources().getConfiguration().locale;
        String lang = locale.toLanguageTag();
        String url = BuildConfig.TV_URL+API+"&language="+lang;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray res = responseObject.getJSONArray("results");
                    for(int i = 0; i < res.length(); i++){
                        TVShow tvShow = new TVShow(res.getJSONObject(i));
                        if (!tvShow.getBackdrop().equals(NULL_CONDITION))
                            list.add(tvShow);
                    }
                    listTV.postValue(list);
                } catch (Exception e){
                    Log.d("Exception", Objects.requireNonNull(e.getMessage()));
                    Toast.makeText(MainActivity.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    listTV.postValue(list);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", Objects.requireNonNull(error.getMessage()));
                Toast.makeText(MainActivity.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                listTV.postValue(list);
            }
        });
    }

    public void setTVShowsFromSearch(String query){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TVShow> list = new ArrayList<>();
        Locale locale = MainActivity.getContext().getResources().getConfiguration().locale;
        String lang = locale.toLanguageTag();
        String url = BuildConfig.TV_SEARCH_URL+API+"&language="+lang+"&query="+query;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray res = responseObject.getJSONArray("results");
                    for(int i = 0; i < res.length(); i++){
                        TVShow tvShow = new TVShow(res.getJSONObject(i));
                        if (!tvShow.getBackdrop().equals(NULL_CONDITION))
                            list.add(tvShow);
                    }
                    listTV.postValue(list);
                } catch (Exception e){
                    Log.d("Exception", Objects.requireNonNull(e.getMessage()));
                    Toast.makeText(MainActivity.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    listTV.postValue(list);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", Objects.requireNonNull(error.getMessage()));
                Toast.makeText(MainActivity.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                listTV.postValue(list);
            }
        });
    }

    public void setEmptyTVShow(){
        ArrayList<TVShow> list = new ArrayList<>();
        listTV.postValue(list);
    }

    public void setTVShowsFromDB(Context context){
        TVShowHelper helper = TVShowHelper.getInstance(context);
        helper.open();
        ArrayList<TVShow> list = helper.getAllTVShows();
        listTV.postValue(list);
        if (list.isEmpty())
            Toast.makeText(context, context.getString(R.string.empty_list), Toast.LENGTH_LONG).show();
    }
}
