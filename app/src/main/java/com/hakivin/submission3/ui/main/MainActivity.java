package com.hakivin.submission3.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.hakivin.submission3.BuildConfig;
import com.hakivin.submission3.R;
import com.hakivin.submission3.adapter.SectionsPagerAdapter;
import com.hakivin.submission3.viewmodel.DataViewModel;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private static boolean onFirst = false;

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
        context = this;
        if (savedInstanceState != null){
            isClicked = savedInstanceState.getBoolean(EXTRA_STATE);
        } else {
            isClicked = false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_STATE, isClicked);
    }

    private static boolean isClicked;

    public static boolean isIsClicked() {
        return isClicked;
    }

    private MenuItem favourite, home;

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