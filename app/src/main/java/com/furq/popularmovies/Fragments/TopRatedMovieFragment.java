package com.furq.popularmovies.Fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.furq.popularmovies.Adapters.MoviesAdapter;
import com.furq.popularmovies.Constant;
import com.furq.popularmovies.DetailActivity;
import com.furq.popularmovies.R;
import com.furq.popularmovies.models.Movie;
import com.furq.popularmovies.models.MovieResponse;
import com.furq.popularmovies.utils.ApiClient;
import com.furq.popularmovies.utils.ApiInterface;
import com.furq.popularmovies.utils.Network;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


/**
 * Created by furqan.khan on 30/09/2017.
 */

public class TopRatedMovieFragment extends Fragment implements MoviesAdapter.ListItemClickListener {

    @Bind(R.id.moviesGrid)
    RecyclerView moviesGrid;
    private GridLayoutManager gridLayoutManager;
    private ArrayList<Movie> movies;
    MoviesAdapter recyclerAdapter;

    Parcelable state;

    public TopRatedMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager = new GridLayoutManager(getContext(), 4);
        } else {
            gridLayoutManager = new GridLayoutManager(getContext(), 2);
        }

        if (Network.isConnected(getContext())) {
            if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
                getMoviesData();
            } else {
                movies = savedInstanceState.getParcelableArrayList("movies");
                state = savedInstanceState.getParcelable("state");
                populateGridView();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top_movie, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // In order to prevent saving null when internet is not connected
        if (movies != null) {
            outState.putParcelableArrayList("movies", movies);
            outState.putParcelable("state", moviesGrid.getLayoutManager().onSaveInstanceState());
        }
    }

    private void getMoviesData() {
        if (!Network.isConnected(getActivity())) {
            Toast.makeText(getContext(), "Please check your internet connectivity", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiInterface apiService =
                ApiClient.getClient(Constant.BASE_URL).create(ApiInterface.class);
        Call<MovieResponse> call;

        call = apiService.getTopRatedMovies(Constant.API_KEY);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
//                    refresh.setVisibility(View.GONE);
                    movies = response.body().getResults();
                    populateGridView();

                } else {
                    // call the refresh function
                    Toast.makeText(getActivity(), "Something went wrong, Please try again", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    void populateGridView() {
        if (recyclerAdapter == null) {
            recyclerAdapter = new MoviesAdapter(movies, R.layout.list_movie_item, this);
        }
        moviesGrid.setHasFixedSize(true);
        moviesGrid.setLayoutManager(gridLayoutManager);
        moviesGrid.setAdapter(recyclerAdapter);
        if (state != null) {
            moviesGrid.getLayoutManager().onRestoreInstanceState(state);
        }

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("movies", movies.get(clickedItemIndex));
        startActivity(intent);
    }
}
