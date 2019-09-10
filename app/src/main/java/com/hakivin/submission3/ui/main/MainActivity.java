package com.hakivin.submission3.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hakivin.submission3.BuildConfig;
import com.hakivin.submission3.R;
import com.hakivin.submission3.adapter.SectionsPagerAdapter;
import com.hakivin.submission3.notification.AlarmReceiver;
import com.hakivin.submission3.viewmodel.DataViewModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private static boolean onFirst = false;
    private AlarmReceiver alarmReceiver;

    public static Context getContext(){
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        alarmReceiver = new AlarmReceiver();
        setDailyReminder();
        setReleaseTodayReminder();
        context = this;
        if (savedInstanceState != null){
            isClicked = savedInstanceState.getBoolean(EXTRA_STATE);
        } else {
            isClicked = false;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_STATE, isClicked);
    }

    private static boolean isClicked;

    public static boolean isIsClicked() {
        return isClicked;
    }

    private MenuItem favourite;
    private MenuItem home;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        favourite = menu.findItem(R.id.favourites);
        home = menu.findItem(R.id.home);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DataViewModel movieDataViewModel = MovieFragment.getmViewModel();
        DataViewModel TVDataViewModel = TVFragment.getmViewModel();
        switch (item.getItemId()) {
            case R.id.setting:
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                onFirst = true;
                startActivity(mIntent);
                break;
            case R.id.favourites:
                isClicked = true;
                this.invalidateOptionsMenu();
                movieDataViewModel.setMoviesFromDB(getContext());
                TVDataViewModel.setTVShowsFromDB(getContext());
                break;
            case R.id.home:
                isClicked = false;
                this.invalidateOptionsMenu();
                movieDataViewModel.setMovies();
                TVDataViewModel.setTVShows();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        if (onFirst){
            onFirst = false;
            restart();
        }

        super.onResume();
    }

    private void restart(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setDailyReminder(){
        if (!alarmReceiver.isAlarmSet(this, AlarmReceiver.ID_DAILY))
            alarmReceiver.setRepeatingAlarm(this, "07:00", getString(R.string.app_name), getString(R.string.daily_reminder), AlarmReceiver.ID_DAILY);
    }

    private void setReleaseTodayReminder(){
        if (!alarmReceiver.isAlarmSet(this, AlarmReceiver.ID_RELEASE_TODAY)){
            Date date = new Date();
            String fDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
            System.out.println("Date = " + fDate);
            AsyncHttpClient client = new AsyncHttpClient();
            final String url = BuildConfig.DISCOVER_MOVIE_URL+getAPIKey()+"&primary_release_date.gte="+fDate
                    +"&primary_release_date.lte="+fDate+"&with_original_language=en";
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String title = null;
                        String result = new String(responseBody);
                        JSONObject responseObject = new JSONObject(result);
                        JSONArray array = responseObject.getJSONArray("results");
                        if (!array.isNull(0)){
                            JSONObject movie = array.getJSONObject(0);
                            title = movie.getString("title");
                            alarmReceiver.setRepeatingAlarm(getApplicationContext(), "08:00", title, title+getString(R.string.release_today_reminder), AlarmReceiver.ID_RELEASE_TODAY);
                        }
                        System.out.println("title = "+title);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    error.printStackTrace();
                }
            });
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isClicked) {
            favourite.setVisible(false);
            home.setVisible(true);
        } else {
            favourite.setVisible(true);
            home.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public static String getAPIKey(){
        return BuildConfig.TMDB_API_KEY;
    }

}