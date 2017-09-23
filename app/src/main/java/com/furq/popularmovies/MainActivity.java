package com.furq.popularmovies;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.furq.popularmovies.Adapters.MoviesAdapter;
import com.furq.popularmovies.models.Movie;
import com.furq.popularmovies.models.MovieResponse;
import com.furq.popularmovies.utils.ApiClient;
import com.furq.popularmovies.utils.ApiInterface;
import com.furq.popularmovies.utils.Network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by furqan.khan on 9/15/17.
 */

public class MainActivity extends AppCompatActivity implements MoviesAdapter.ListItemClickListener {

    private RecyclerView moviesGrid;
    private GridLayoutManager gridLayoutManager;
    private ArrayList<Movie> movies;
    private Button refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        refresh = (Button) findViewById(R.id.refresh);
        moviesGrid = (RecyclerView) findViewById(R.id.moviesGrid);

        if (Network.isConnected(this)) {
            if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
                getMoviesData(true);
            } else {
                movies = savedInstanceState.getParcelableArrayList("movies");
                populateGridView();
            }
        } else {
            Toast.makeText(MainActivity.this, "Please check your internet connectivity", Toast.LENGTH_SHORT).show();
            refresh.setVisibility(View.VISIBLE);
        }

        refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getMoviesData(true);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // In order to prevent saving null when internet is not connected
        if (movies != null) {
            outState.putParcelableArrayList("movies", movies);
        }
        super.onSaveInstanceState(outState);
    }

    private void getMoviesData(boolean topRated) {
        if (!Network.isConnected(MainActivity.this)) {
            Toast.makeText(MainActivity.this, "Please check your internet connectivity", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiInterface apiService =
                ApiClient.getClient(Constant.BASE_URL).create(ApiInterface.class);
        Call<MovieResponse> call;
        if (topRated) {
            call = apiService.getTopRatedMovies(Constant.API_KEY);
        } else {
            call = apiService.getPopularMovies(Constant.API_KEY);
        }
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    refresh.setVisibility(View.GONE);
                    movies = response.body().getResults();
                    populateGridView();

                } else {
                    // call the refresh function
                    Toast.makeText(MainActivity.this, "Something went wrong, Please try again", Toast.LENGTH_SHORT).show();
                    refresh.setVisibility(View.VISIBLE);
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
        MoviesAdapter recyclerAdapter = new MoviesAdapter(movies, R.layout.list_movie_item, MainActivity.this);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager = new GridLayoutManager(MainActivity.this, 4);
        } else {
            gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        }
        moviesGrid.setHasFixedSize(true);
        moviesGrid.setLayoutManager(gridLayoutManager);
        moviesGrid.setAdapter(recyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_popular:
                getMoviesData(false);

                break;
            case R.id.action_top_movies:
                getMoviesData(true);
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("movies", movies.get(clickedItemIndex));
        startActivity(intent);
    }
}
