package com.furq.popularmovies.Fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.furq.popularmovies.Adapters.MoviesAdapter;

import com.furq.popularmovies.DetailActivity;
import com.furq.popularmovies.R;
import com.furq.popularmovies.models.Movie;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;


/**
 * Created by furqan.khan on 30/09/2017.
 */

public class FavouriteMovieFragment extends Fragment implements MoviesAdapter.ListItemClickListener {

    @Bind(R.id.moviesGrid)
    RecyclerView moviesGrid;
    private GridLayoutManager gridLayoutManager;
    private ArrayList<Movie> movies;
    Realm realm;


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
        realm = Realm.getDefaultInstance();

    }

    @Override
    public void onResume() {
        super.onResume();
        RealmResults<Movie> realmResults = realm.where(Movie.class).findAll();

        List<Movie> result = realm.copyFromRealm(realmResults);

        movies = new ArrayList<Movie>(result);

        Log.d("Size", String.valueOf(realmResults.size()));

        populateGridView(movies);
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
        realm.close();
    }
}
