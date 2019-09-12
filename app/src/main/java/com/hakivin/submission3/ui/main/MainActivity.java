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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hakivin.submission3.BuildConfig;
import com.hakivin.submission3.R;
import com.hakivin.submission3.adapter.SectionsPagerAdapter;
import com.hakivin.submission3.entity.Reminder;
import com.hakivin.submission3.notification.AlarmReceiver;
import com.hakivin.submission3.viewmodel.DataViewModel;

public class MainActivity extends AppCompatActivity implements ReminderFragment.OnCompleteListener {
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
            case R.id.reminder:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ReminderFragment reminderFragment = ReminderFragment.newInstance();
                ft.addToBackStack(null);
                reminderFragment.show(ft, ReminderFragment.class.getSimpleName());
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
        if (alarmReceiver.isAlarmNotSet(this, AlarmReceiver.ID_DAILY))
            alarmReceiver.setRepeatingAlarm(this, "07:00", AlarmReceiver.ID_DAILY);
    }

    private void setReleaseTodayReminder(){
        if (alarmReceiver.isAlarmNotSet(this, AlarmReceiver.ID_RELEASE_TODAY)){
            alarmReceiver.setRepeatingAlarm(getApplicationContext(), "08:00", AlarmReceiver.ID_RELEASE_TODAY);
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

    @Override
    public void onComplete(Reminder reminder) {
        if (reminder.getDaily())
            setDailyReminder();
        else
            alarmReceiver.cancelAlarm(this, AlarmReceiver.ID_DAILY);
        if (reminder.getRelease())
            setReleaseTodayReminder();
        else
            alarmReceiver.cancelAlarm(this, AlarmReceiver.ID_RELEASE_TODAY);
    }
}