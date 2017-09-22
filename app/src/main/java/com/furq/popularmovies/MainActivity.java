package com.furq.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.furq.popularmovies.Adapters.MoviesAdapter;
import com.furq.popularmovies.models.Movie;
import com.furq.popularmovies.models.MovieResponse;
import com.furq.popularmovies.utils.ApiClient;
import com.furq.popularmovies.utils.ApiInterface;

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
    private Toast mToast;
    private ArrayList<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        moviesGrid = (RecyclerView) findViewById(R.id.moviesGrid);
        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            getMoviesData(true);
        } else {
            movies = savedInstanceState.getParcelableArrayList("movies");
            populateGridView();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", movies);
        super.onSaveInstanceState(outState);

    }

    private void getMoviesData(boolean topRated) {

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
                    movies = response.body().getResults();
                    populateGridView();

                } else {
                    // call the refresh function
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
        MoviesAdapter recyclerAdapter = new MoviesAdapter(movies, R.layout.list_movie_item, MainActivity.this, MainActivity.this);
        gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
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
                Toast.makeText(this, "Popular movies ", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.action_top_movies:
                getMoviesData(true);
                Toast.makeText(this, "Top selected", Toast.LENGTH_SHORT)
                        .show();
                break;

            default:
                break;
        }

        return true;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Intent intent = new Intent();
    }
}
