package com.hakivin.favouriteapplication.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hakivin.favouriteapplication.R;
import com.hakivin.favouriteapplication.entity.FavouriteData;

import java.util.ArrayList;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {
    private final ArrayList<FavouriteData> list = new ArrayList<>();

    public void setList(ArrayList<FavouriteData> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favourite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavouriteData favouriteData = list.get(position);
        holder.tvFavourite.setText(favouriteData.getTitle());
        Log.d(FavouriteAdapter.class.getSimpleName(), "onBindViewHolder: " + favouriteData.getBackdrop());
        Glide.with(holder.itemView.getContext()).load(favouriteData.getBackdrop()).apply(new RequestOptions()).into(holder.imgFavourite);
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFavourite;
        ImageView imgFavourite;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFavourite = itemView.findViewById(R.id.tv_favourite);
            imgFavourite = itemView.findViewById(R.id.img_favourite);
        }
    }
}
