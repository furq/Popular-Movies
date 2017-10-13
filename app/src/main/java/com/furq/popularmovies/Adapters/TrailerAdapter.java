package com.furq.popularmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.furq.popularmovies.R;
import com.furq.popularmovies.models.Trailer;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by furqan.khan on 9/20/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private List<Trailer> trailersList;
    private int itemLayout;
    Context context;

    public TrailerAdapter(List<Trailer> trailersList, int itemLayout, Context context) {
        this.trailersList = trailersList;
        this.context = context;
        this.itemLayout = itemLayout;
    }

    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrailerAdapter.TrailerViewHolder holder, final int position) {

        final String id = trailersList.get(position).getKey();
        String thumbnailURL = "http://img.youtube.com/vi/".concat(id).concat("/hqdefault.jpg");

        Glide.with(holder.itemView)
                .load(thumbnailURL)
                .into(holder.movieThumbnail);

        holder.movieThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.youtube.com/watch?v=".concat(trailersList.get(position).getKey());
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailersList.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.trailerImage)
        ImageView movieThumbnail;

        public TrailerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
