package com.hakivin.submission3.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hakivin.submission3.R;
import com.hakivin.submission3.adapter.TVShowAdapter;
import com.hakivin.submission3.entity.TVShow;
import com.hakivin.submission3.viewmodel.DataViewModel;

import java.util.ArrayList;

public class TVFragment extends Fragment {

    private TVShowAdapter adapter;
    private ProgressBar progressBar;

    public static TVFragment newInstance() {
        return new TVFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tv_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DataViewModel mViewModel = ViewModelProviders.of(this).get(DataViewModel.class);
        mViewModel.getTVShow().observe(this, getTV);
        mViewModel.setTVShow();
        showLoading(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new TVShowAdapter();
        adapter.notifyDataSetChanged();

        RecyclerView recyclerView = view.findViewById(R.id.rv_tv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        progressBar = view.findViewById(R.id.progress_bar_tv);
    }

    private Observer<ArrayList<TVShow>> getTV = new Observer<ArrayList<TVShow>>() {
        @Override
        public void onChanged(@Nullable ArrayList<TVShow> tvShows) {
            if (tvShows != null)
                adapter.setList(tvShows);
            showLoading(false);
        }
    };

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
