package com.hakivin.submission3.ui.main;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.hakivin.submission3.R;
import com.hakivin.submission3.db.MovieHelper;
import com.hakivin.submission3.db.TVShowHelper;
import com.hakivin.submission3.entity.Movie;
import com.hakivin.submission3.entity.TVShow;

import java.util.ArrayList;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_TV = "extra_tv";
    private TextView tvRelease, tvRating, tvOverview;
    private ImageView imgPoster, imgBackdrop;
    private ProgressBar progressBar;
    private MovieHelper movieHelper;
    private TVShowHelper tvShowHelper;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        final Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        final TVShow tvShow = getIntent().getParcelableExtra(EXTRA_TV);
        openHelper(movie, tvShow);
        tvRelease = findViewById(R.id.tv_release_detail);
        tvRating = findViewById(R.id.tv_rating_detail);
        tvOverview = findViewById(R.id.tv_overview_detail);
        imgPoster = findViewById(R.id.img_poster_detail);
        imgBackdrop = findViewById(R.id.img_backdrop_detail);
        progressBar = findViewById(R.id.loading);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        showData(movie, tvShow);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvShow == null){
                    if (isFavoured(movie)){
                        movieHelper.deleteMovie(movie.getId());
                        fab.setImageDrawable(getDrawable(R.drawable.ic_favorite_black_24dp));
                        Toast.makeText(getApplicationContext(), getString(R.string.removed), Toast.LENGTH_LONG).show();

                    } else {
                        movieHelper.insertMovie(movie);
                        fab.setImageDrawable(getDrawable(R.drawable.ic_favorite_red_24dp));
                        Toast.makeText(getApplicationContext(), getString(R.string.added), Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (isFavoured(tvShow)){
                        tvShowHelper.deleteTVShow(tvShow.getId());
                        fab.setImageDrawable(getDrawable(R.drawable.ic_favorite_black_24dp));
                        Toast.makeText(getApplicationContext(), getString(R.string.removed), Toast.LENGTH_LONG).show();

                    } else {
                        tvShowHelper.insertTVShow(tvShow);
                        fab.setImageDrawable(getDrawable(R.drawable.ic_favorite_red_24dp));
                        Toast.makeText(getApplicationContext(), getString(R.string.added), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void openHelper(Movie movie, TVShow tvShow){
        if (tvShow == null){
            movieHelper = MovieHelper.getInstance(getApplicationContext());
            movieHelper.open();
            if (isFavoured(movie)){
                fab.setImageDrawable(getDrawable(R.drawable.ic_favorite_red_24dp));
            }
        } else {
            tvShowHelper = TVShowHelper.getInstance(getApplicationContext());
            tvShowHelper.open();
            if (isFavoured(tvShow)){
                fab.setImageDrawable(getDrawable(R.drawable.ic_favorite_red_24dp));
            }
        }
    }

    private boolean isFavoured(Movie movie){
        ArrayList<Movie> list = movieHelper.getAllMovies();
        for (Movie m : list){
            if (m.getId() == movie.getId())
                return true;
        }
        return false;
    }

    private boolean isFavoured(TVShow tvShow){
        ArrayList<TVShow> list = tvShowHelper.getAllTVShows();
        for (TVShow n : list){
            if (n.getId() == tvShow.getId())
                return true;
        }
        return false;
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
            Glide.with(this).load(movie.getPoster())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            showLoading(false);
                            Toast.makeText(getApplicationContext(), R.string.check_internet, Toast.LENGTH_LONG).show();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            showLoading(false);
                            return false;
                        }
                    }).apply(new RequestOptions()).into(imgPoster);
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
                            Toast.makeText(getApplicationContext(), getString(R.string.check_internet), Toast.LENGTH_LONG).show();
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
        String month = dates[1];
        switch(month){
            case "01":
                month = getString(R.string.jan);
                break;
            case "02":
                month = getString(R.string.feb);
                break;
            case "03":
                month = getString(R.string.mar);
                break;
            case "04":
                month = getString(R.string.apr);
                break;
            case "05":
                month = getString(R.string.may);
                break;
            case "06":
                month = getString(R.string.jun);
                break;
            case "07":
                month = getString(R.string.jul);
                break;
            case "08":
                month = getString(R.string.aug);
                break;
            case "09":
                month = getString(R.string.sep);
                break;
            case "10":
                month = getString(R.string.oct);
                break;
            case "11":
                month = getString(R.string.nov);
                break;
            case "12":
                month = getString(R.string.dec);
                break;
        }
        return month + " " + day + ", " + year;
    }
}
