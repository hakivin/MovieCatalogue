package com.hakivin.submission3.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hakivin.submission3.R;
import com.hakivin.submission3.adapter.MovieAdapter;
import com.hakivin.submission3.adapter.TVShowAdapter;
import com.hakivin.submission3.entity.Movie;
import com.hakivin.submission3.entity.TVShow;
import com.hakivin.submission3.viewmodel.DataViewModel;

import java.util.ArrayList;

public class MovieFragment extends Fragment {

    private MovieAdapter adapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    public static MovieFragment newInstance() {
        return new MovieFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.movie_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DataViewModel mViewModel = ViewModelProviders.of(this).get(DataViewModel.class);
        mViewModel.getMovies().observe(this, getMovie);
        mViewModel.setMovies();
        showLoading(true);
        // TODO: Use the ViewModel
    }

    private Observer<ArrayList<Movie>> getMovie = new Observer<ArrayList<Movie>>() {
        @Override
        public void onChanged(@Nullable ArrayList<Movie> movies) {
            if (movies != null)
                adapter.setList(movies);
            showLoading(false);
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new MovieAdapter();
        adapter.notifyDataSetChanged();
        recyclerView = view.findViewById(R.id.rv_movie);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        progressBar = view.findViewById(R.id.progress_bar_movie);
        clicked = false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    private boolean clicked;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.favourites){
            //Toast.makeText(getContext(), "Berhasil terpencet", Toast.LENGTH_LONG).show();
            if (!clicked){
                showLoading(true);
                TVShowAdapter adapter = new TVShowAdapter();
                adapter.notifyDataSetChanged();
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
                showLoading(false);
                clicked = true;
                Log.d(MovieFragment.class.getSimpleName(), clicked + " value at clicked");
            } else {
                showLoading(true);
                MovieAdapter adapter = new MovieAdapter();
                adapter.notifyDataSetChanged();
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
                showLoading(false);
                clicked = false;
                Log.d(MovieFragment.class.getSimpleName(), clicked + " value at clicked");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

}
