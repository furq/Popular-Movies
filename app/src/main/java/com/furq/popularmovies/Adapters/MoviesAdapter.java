package com.furq.popularmovies.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.furq.popularmovies.Constant;
import com.furq.popularmovies.R;
import com.furq.popularmovies.models.Movie;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by furqan.khan on 9/20/17.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private static final String TAG = MoviesAdapter.class.getSimpleName();

    private List<Movie> moviesList;
    private int itemLayout;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public MoviesAdapter(List<Movie> moviesList, int itemLayout, ListItemClickListener mOnClickListener) {
        this.moviesList = moviesList;
        this.itemLayout = itemLayout;
        this.mOnClickListener = mOnClickListener;
    }

    @Override
    public MoviesAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.MovieViewHolder holder, int position) {

        if (!moviesList.get(position).getPosterPath().equals(null)) {
            String url = Constant.IMAGE_BASE_URL + moviesList.get(position).getPosterPath();

            Glide.with(holder.itemView)
                    .load(url).into(holder.movieThumbnail);

        } else {
            Glide.with(holder.itemView).clear(holder.movieThumbnail);
        }
        holder.movieTitle.setText(moviesList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.movieThumbnail)
        ImageView movieThumbnail;
        @Bind(R.id.movieTitle)
        TextView movieTitle;

        public MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
