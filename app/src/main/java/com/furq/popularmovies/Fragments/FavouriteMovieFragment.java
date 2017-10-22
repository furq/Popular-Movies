package com.furq.popularmovies.Fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.furq.popularmovies.Adapters.MoviesAdapter;
import com.furq.popularmovies.Db.MovieContract;
import com.furq.popularmovies.DetailActivity;
import com.furq.popularmovies.R;
import com.furq.popularmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by furqan.khan on 30/09/2017.
 */

public class FavouriteMovieFragment extends Fragment implements MoviesAdapter.ListItemClickListener {

    @Bind(R.id.moviesGrid)
    RecyclerView moviesGrid;
    private GridLayoutManager gridLayoutManager;
    private ArrayList<Movie> movies;



    public FavouriteMovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top_movie, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        movies = new ArrayList<>();
        getFavouriteFromDb();
        populateGridView(movies);
    }

    void getFavouriteFromDb() {

        Cursor cursor = getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.MOVIE_ID,
                        MovieContract.MovieEntry.MOVIE_TITLE,
                        MovieContract.MovieEntry.MOVIE_RATING,
                        MovieContract.MovieEntry.MOVIE_CONTENT,
                        MovieContract.MovieEntry.MOVIE_RELEASE_DATE,
                        MovieContract.MovieEntry.MOVIE_POSTER_PATH,
                        MovieContract.MovieEntry.MOVIE_BACKDROP_PATH,
                },
                null,
                null,
                null);

        if (cursor != null && cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                int idColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_ID);
                int titleColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_TITLE);
                int voteColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_RATING);
                int overviewColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_CONTENT);
                int releaseDateColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_RELEASE_DATE);
                int backdropColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_BACKDROP_PATH);
                int posterColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_POSTER_PATH);

                int id = cursor.getInt(idColumnIndex);
                String title = cursor.getString(titleColumnIndex);
                String voteAverage = cursor.getString(voteColumnIndex);
                String overview = cursor.getString(overviewColumnIndex);
                String releaseDate = cursor.getString(releaseDateColumnIndex);
                String backdropPath = cursor.getString(backdropColumnIndex);
                String posterPath = cursor.getString(posterColumnIndex);

                double vote_average = Double.valueOf(voteAverage);
                /*the poster will be set by the adapter, so pass null*/
                movies.add(new Movie(id, title, overview, releaseDate, vote_average, backdropPath, posterPath));
            }
            cursor.close();
        }
    }

    void populateGridView(List movies) {
        MoviesAdapter recyclerAdapter = new MoviesAdapter(movies, R.layout.list_movie_item, this);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager = new GridLayoutManager(getContext(), 4);
        } else {
            gridLayoutManager = new GridLayoutManager(getContext(), 2);
        }
        moviesGrid.setHasFixedSize(true);
        moviesGrid.setLayoutManager(gridLayoutManager);
        moviesGrid.setAdapter(recyclerAdapter);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("movies", movies.get(clickedItemIndex));
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
