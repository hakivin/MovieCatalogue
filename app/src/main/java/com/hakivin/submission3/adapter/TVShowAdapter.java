package com.hakivin.submission3.adapter;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hakivin.submission3.R;
import com.hakivin.submission3.entity.TVShow;
import com.hakivin.submission3.ui.main.DetailActivity;

import java.util.ArrayList;

public class TVShowAdapter extends RecyclerView.Adapter<TVShowAdapter.TVShowViewHolder> {
    private ArrayList<TVShow> list = new ArrayList<>();

    public void setList(ArrayList<TVShow> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public TVShowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_tvshow, viewGroup, false);
        return new TVShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TVShowViewHolder holder, int position) {
        final TVShow tvShow = this.list.get(position);
        holder.bind(tvShow);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_TV, tvShow);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    class TVShowViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgBackdrop;
        private TextView tvTitle, tvScore;
        TVShowViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBackdrop = itemView.findViewById(R.id.img_cardview_tv);
            tvTitle = itemView.findViewById(R.id.tv_tv_title);
            tvScore = itemView.findViewById(R.id.tv_tv_score);
        }
        void bind(TVShow tvShow){
            Glide.with(itemView.getContext()).load(tvShow.getBackdrop()).apply(new RequestOptions()).into(imgBackdrop);
            tvTitle.setText(tvShow.getTitle());
            tvScore.setText(tvShow.getScore());
        }
    }
}
