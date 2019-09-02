package com.hakivin.submission3.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import android.widget.Toast;

import com.hakivin.submission3.ui.main.MainActivity;
import com.hakivin.submission3.entity.Movie;
import com.hakivin.submission3.entity.TVShow;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class DataViewModel extends ViewModel {
    private static final String API = MainActivity.getAPIKey();

    private MutableLiveData<ArrayList<Movie>> listMovies = new MutableLiveData<>();
    private MutableLiveData<ArrayList<TVShow>> listTV = new MutableLiveData<>();

    public LiveData<ArrayList<Movie>> getMovies(){
        return listMovies;
    }
    public LiveData<ArrayList<TVShow>> getTVShow(){ return listTV; }

    public void setMovies(){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> list = new ArrayList<>();
        Locale locale = MainActivity.getContext().getResources().getConfiguration().locale;
        String lang = locale.toLanguageTag();
        String url = "https://api.themoviedb.org/3/movie/upcoming?api_key="+API+"&language="+lang;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray res = responseObject.getJSONArray("results");
                    for (int i = 0; i < res.length(); i++){
                        Movie movie = new Movie(res.getJSONObject(i));
                        if (!movie.getBackdrop().equals("null") && !movie.getScore().equals("0.0")) //karena ada movie yang backdropnya null
                            list.add(movie);
                    }
                    listMovies.postValue(list);
                } catch (Exception e){
                    Log.d("Exception", e.getMessage());
                    Toast.makeText(MainActivity.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    listMovies.postValue(list);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
                Toast.makeText(MainActivity.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                listMovies.postValue(list);
            }
        });
    }

    public void setTVShow(){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TVShow> list = new ArrayList<>();
        Locale locale = MainActivity.getContext().getResources().getConfiguration().locale;
        String lang = locale.toLanguageTag();
        String url = "https://api.themoviedb.org/3/tv/on_the_air?api_key="+API+"&language="+lang;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray res = responseObject.getJSONArray("results");
                    for(int i = 0; i < res.length(); i++){
                        TVShow tvShow = new TVShow(res.getJSONObject(i));
                        if (!tvShow.getBackdrop().equals("null") && !tvShow.getScore().equals("0.0"))
                            list.add(tvShow);
                    }
                    listTV.postValue(list);
                } catch (Exception e){
                    Log.d("Exception", e.getMessage());
                    Toast.makeText(MainActivity.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    listTV.postValue(list);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
                Toast.makeText(MainActivity.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                listTV.postValue(list);
            }
        });
    }
}
