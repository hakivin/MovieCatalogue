package com.hakivin.submission3.ui.main;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.hakivin.submission3.R;
import com.hakivin.submission3.model.Movie;
import com.hakivin.submission3.model.TVShow;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_TV = "extra_tv";
    private TextView tvRelease, tvRating, tvOverview;
    private ImageView imgPoster, imgBackdrop;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        TVShow tvShow = getIntent().getParcelableExtra(EXTRA_TV);
        tvRelease = findViewById(R.id.tv_release_detail);
        tvRating = findViewById(R.id.tv_rating_detail);
        tvOverview = findViewById(R.id.tv_overview_detail);
        imgPoster = findViewById(R.id.img_poster_detail);
        imgBackdrop = findViewById(R.id.img_backdrop_detail);
        progressBar = findViewById(R.id.loading);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        showData(movie, tvShow);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showData(Movie movie, TVShow tvShow) {
        if (tvShow == null){
            tvRelease.setText(convertDate(movie.getReleaseDate()));
            tvRating.setText(movie.getScore());
            if (!movie.getOverview().equals("")){
                tvOverview.setText(movie.getOverview());
            }
            else {
                tvOverview.setText(getString(R.string.empty));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tvOverview.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            }
            Glide.with(this).load(movie.getPoster()).apply(new RequestOptions()).into(imgPoster);
            Glide.with(this).load(movie.getBackdrop()).apply(new RequestOptions()).into(imgBackdrop);
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(movie.getTitle());
        }
        else {
            tvRelease.setText(convertDate(tvShow.getFirstAirDate()));
            tvRating.setText(tvShow.getScore());
            if (!tvShow.getOverview().equals("")){
                tvOverview.setText(tvShow.getOverview());
            }
            else {
                tvOverview.setText(getString(R.string.empty));
            }
            Glide.with(this).load(tvShow.getPoster())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            showLoading(false);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            showLoading(false);
                            return false;
                        }
                    })
                    .apply(new RequestOptions()).into(imgPoster);
            Glide.with(this).load(tvShow.getBackdrop()).apply(new RequestOptions()).into(imgBackdrop);
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(tvShow.getTitle());
        }
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private String convertDate(String date){
        String[] dates = date.split("-");
        String year = dates[0];
        String day = dates[2];
        String month = null;
        switch(dates[1]){
            case "01":
                month = getString(R.string.jan);
            case "02":
                month = getString(R.string.feb);
            case "03":
                month = getString(R.string.mar);
            case "04":
                month = getString(R.string.apr);
            case "05":
                month = getString(R.string.may);
            case "06":
                month = getString(R.string.jun);
            case "07":
                month = getString(R.string.jul);
            case "08":
                month = getString(R.string.aug);
            case "09":
                month = getString(R.string.sep);
            case "10":
                month = getString(R.string.oct);
            case "11":
                month = getString(R.string.nov);
            case "12":
                month = getString(R.string.dec);
        }
        return month + " " + day + ", " + year;
    }
}
