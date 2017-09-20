package com.furq.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.furq.popularmovies.Constant;
import com.furq.popularmovies.R;
import com.furq.popularmovies.models.Movie;

import java.util.List;

/**
 * Created by furqan.khan on 9/20/17.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<Movie> moviesList;
    private int itemLayout;
    private Context context;

    public MoviesAdapter(List<Movie> moviesList, int itemLayout, Context context) {
        this.moviesList = moviesList;
        this.itemLayout = itemLayout;
        this.context = context;
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

            Glide.with(context)
                    .load(url).into(holder.movieThumbnail);

        } else {
            Glide.with(context).clear(holder.movieThumbnail);
//           holder.movieThumbnail.setImageDrawable(specialDrawable);
        }


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

//        private final ProgressBar progressBar;
        private ImageView movieThumbnail;


        public MovieViewHolder(View view) {
            super(view);
            movieThumbnail = (ImageView) view.findViewById(R.id.movieThumbnail);

        }


        public Object getImage() {
            return movieThumbnail;
        }
    }
}
