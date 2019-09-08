package com.hakivin.submission3.ui.main;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hakivin.submission3.R;
import com.hakivin.submission3.adapter.MovieAdapter;
import com.hakivin.submission3.entity.Movie;
import com.hakivin.submission3.viewmodel.DataViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class MovieFragment extends Fragment {

    private MovieAdapter adapter;
    private ProgressBar progressBar;
    private static DataViewModel mViewModel;

    public static MovieFragment newInstance() {
        return new MovieFragment();
    }

    static DataViewModel getmViewModel(){
        return mViewModel;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.movie_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DataViewModel.class);
        mViewModel.getMovies().observe(this, getMovie);
        if (savedInstanceState == null) {
            mViewModel.setMovies();
        }
        showLoading(true);
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
        RecyclerView recyclerView = view.findViewById(R.id.rv_movie);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        progressBar = view.findViewById(R.id.progress_bar_movie);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem search = menu.findItem(R.id.search_bar);
        SearchManager searchManager = (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null){
            final SearchView searchView = (SearchView) (menu.findItem(R.id.search_bar)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.insert));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    showLoading(true);
                    mViewModel.setMoviesFromSearch(query);
                    searchView.clearFocus();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    mViewModel.setEmptyMovie();
                    return true;
                }
            });
        }
        search.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                showLoading(true);
                mViewModel.setMovies();
                return true;
            }
        });
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
        if (!MainActivity.isIsClicked())
            mViewModel.setMovies();
    }

    @Override
    public void onResume() {
        if (MainActivity.isIsClicked())
            mViewModel.setMoviesFromDB(getContext());
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home)
            showLoading(true);
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
