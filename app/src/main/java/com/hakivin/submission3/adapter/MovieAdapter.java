package com.hakivin.submission3.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hakivin.submission3.ui.main.DetailActivity;
import com.hakivin.submission3.ui.main.MainActivity;
import com.hakivin.submission3.R;
import com.hakivin.submission3.entity.Movie;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private ArrayList<Movie> list = new ArrayList<>();

    public void setList(ArrayList<Movie> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_movie, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, int position) {
        final Movie movie = this.list.get(position);
        holder.bind(movie);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_MOVIE, movie);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgBackdrop;
        private TextView tvTitle, tvScore;
        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBackdrop = itemView.findViewById(R.id.img_cardview_movie);
            tvTitle = itemView.findViewById(R.id.tv_movie_title);
            tvScore = itemView.findViewById(R.id.tv_movie_score);
        }

        void bind(Movie movie){
            Glide.with(itemView.getContext()).load(movie.getBackdrop()).apply(new RequestOptions()).into(imgBackdrop);
            tvTitle.setText(movie.getTitle());
            tvScore.setText(movie.getScore());
        }
    }
}
