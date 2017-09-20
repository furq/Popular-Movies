package com.furq.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.furq.popularmovies.Adapters.MoviesAdapter;
import com.furq.popularmovies.models.Movie;
import com.furq.popularmovies.models.MovieResponse;
import com.furq.popularmovies.utils.ApiClient;
import com.furq.popularmovies.utils.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by furqan.khan on 9/15/17.
 */

public class MainActivity extends AppCompatActivity {

    private RecyclerView moviesGrid;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        moviesGrid = (RecyclerView) findViewById(R.id.moviesGrid);
        callTopRatedMovies();
    }

    private void callTopRatedMovies() {

        ApiInterface apiService =
                ApiClient.getClient(Constant.BASE_URL).create(ApiInterface.class);
        Call<MovieResponse> call = apiService.getTopRatedMovies(Constant.API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    List<Movie> movies = response.body().getResults();

                    MoviesAdapter recyclerAdapter = new MoviesAdapter(movies, R.layout.list_movie_item, MainActivity.this);
                    gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
                    moviesGrid.setHasFixedSize(true);
                    moviesGrid.setLayoutManager(gridLayoutManager);
                    moviesGrid.setAdapter(recyclerAdapter);
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
}
